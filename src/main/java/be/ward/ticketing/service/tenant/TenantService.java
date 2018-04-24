package be.ward.ticketing.service.tenant;

import be.ward.ticketing.exception.NoEngineFoundException;
import be.ward.ticketing.exception.NoProcessDefinitionFoundException;
import be.ward.ticketing.exception.ProcessEngineException;
import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.application.ProcessApplicationReference;
import org.camunda.bpm.container.RuntimeContainerDelegate;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.identity.Tenant;
import org.camunda.bpm.engine.impl.ProcessEngineImpl;
import org.camunda.bpm.engine.impl.application.ProcessApplicationManager;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.camunda.bpm.engine.impl.persistence.StrongUuidGenerator;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TenantService {

    private final ProcessEngine processEngine;

    @Value("${spring.datasource.username}")
    private String databaseUsername;

    @Value("${spring.datasource.password}")
    private String databasePassword;

    @Value("${spring.datasource.driver-class-name}")
    private String databaseDriverClassName;

    @Autowired
    public TenantService(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    public String addTenant(String tenantId, String tenantName) {
        if (getTenant(tenantId) == null) {
            Tenant tenant = createTenant(tenantId, tenantName);
            processEngine.getIdentityService().saveTenant(tenant);
            startProcessEngine(tenant.getId());
            return "Tenant has been added to db and engine has started.";
        } else {
            return "Tenant already exists in db.";
        }
    }

    private Tenant createTenant(String tenantId, String tenantName) {
        Tenant tenant = processEngine.getIdentityService().newTenant(tenantId);
        tenant.setName(tenantName);
        return tenant;
    }

    public String deleteTenant(String tenantId) {
        if (!getProcessEngine(tenantId).isPresent()) {
            processEngine.getIdentityService().deleteTenant(tenantId);
            return "Tenant with id [" + tenantId + "] has been deleted.";
        } else {
            return "Engine for tenant with id [" + tenantId + "] still running";
        }
    }

    private List<Tenant> getAllTenants() {
        return processEngine.getIdentityService().createTenantQuery().orderByTenantId().asc().list();
    }

    private Tenant getTenant(String tenantId) {
        return processEngine.getIdentityService().createTenantQuery().tenantId(tenantId).singleResult();
    }

    private Tenant getTenantForEngine(String tenantId, ProcessEngine engine) {
        return engine.getIdentityService().createTenantQuery().tenantId(tenantId).singleResult();
    }

    public String startProcessEngines() {
        getAllTenants()
                .stream()
                .filter(tenant -> !getProcessEngine(tenant.getId()).isPresent())
                .forEach(tenant -> startProcessEngine(tenant.getId()));

        return "All services have been started.";
    }

    public String startProcessEngine(String engineId) {
        ProcessEngineConfigurationImpl configuration = newProcessConfiguration(engineId);

        ProcessEngine processEngine = configuration.buildProcessEngine();
        try {
            // start the process engine inside the container.
            RuntimeContainerDelegate.INSTANCE.get().registerProcessEngine(processEngine);
        } catch (org.camunda.bpm.engine.ProcessEngineException e) {
            throw new ProcessEngineException();
        }

        if (getTenantForEngine(engineId, processEngine) == null) {
            processEngine.getIdentityService().saveTenant(createTenant(engineId, engineId));
        }
        return "Process engine with id [" + engineId + "] has started.";
    }

    private ProcessEngineConfigurationImpl newProcessConfiguration(String engineId) {
        ProcessEngineConfigurationImpl config = new StandaloneProcessEngineConfiguration();

        config.setDatabaseSchemaUpdate(StandaloneProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        config.setHistory(StandaloneProcessEngineConfiguration.HISTORY_AUDIT);
        config.setIdGenerator(new StrongUuidGenerator());
        config.setJdbcDriver(databaseDriverClassName);
        config.setJdbcUrl("jdbc:mysql://localhost:3306/db_ticketingsystem_" + engineId + "?useSSL=false&createDatabaseIfNotExist=true");
        config.setJdbcUsername(databaseUsername);
        config.setJdbcPassword(databasePassword);
        config.setJobExecutorDeploymentAware(true);
        config.setProcessEngineName(engineId);

        return config;
    }

    public String stopProcessEngines() {
        getAllProcessEngines()
                .stream()
                .filter(processEngine -> !processEngine.getName().equals("default"))
                .forEach(processEngine -> stopProcessEngine(processEngine.getName()));

        return "All engines have been stopped.";
    }

    public String stopProcessEngine(String engineId) {
        if (!getProcessEngine(engineId).isPresent()) throw new NoEngineFoundException();
        ProcessEngine processEngine = getProcessEngine(engineId).get();
        RuntimeContainerDelegate runtimeContainerDelegate = RuntimeContainerDelegate.INSTANCE.get();

        processEngine.close();
        runtimeContainerDelegate.unregisterProcessEngine(processEngine);

        return "Process engine with id [" + engineId + "] has been stopped.";
    }

    public String deployProcessToEngine(String processKey, String engineId) {
        ProcessEngine defaultProcessEngine = BpmPlatform.getDefaultProcessEngine();
        ProcessEngineConfigurationImpl defaultProcessEngineConfiguration = ((ProcessEngineImpl) defaultProcessEngine).getProcessEngineConfiguration();
        ProcessApplicationManager defaultProcessApplicationManager = defaultProcessEngineConfiguration.getProcessApplicationManager();

        List<ProcessDefinition> processDefinitions = defaultProcessEngine.getRepositoryService().createProcessDefinitionQuery().list();
        Optional<ProcessEngine> processEngineOptional = getProcessEngine(engineId);

        // if no process engine is found, deployment has failed
        if (!processEngineOptional.isPresent()) throw new NoEngineFoundException();

        DeploymentBuilder deploymentBuilder = processEngineOptional.get().getRepositoryService().createDeployment();
        Optional<ProcessDefinition> processDefinitionOptional = processDefinitions
                .stream()
                .filter(definition -> definition.getKey().equals(processKey))
                .findFirst();

        // if no process definition is found, deployment failed
        if (!processDefinitionOptional.isPresent()) throw new NoProcessDefinitionFoundException();

        ProcessDefinition processDefinition = processDefinitionOptional.get();
        deploymentBuilder.addInputStream(
                processDefinition.getResourceName(),
                defaultProcessEngine
                        .getRepositoryService()
                        .getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getResourceName()));

        deploymentBuilder.tenantId(engineId);

        // deployment of process
        Deployment deployment = deployProcess(deploymentBuilder);

        // registration of process
        if (registerProcess(defaultProcessApplicationManager, processDefinition, processEngineOptional.get(), deployment)) {
            return "Process [" + processKey + "] successfull deployed to engine [" + engineId + "].";
        }
        return null;
    }

    private List<ProcessEngine> getAllProcessEngines() {
        return RuntimeContainerDelegate.INSTANCE.get().getProcessEngineService().getProcessEngines();
    }

    private Optional<ProcessEngine> getProcessEngine(String engineId) {
        ProcessEngine processEngine = RuntimeContainerDelegate.INSTANCE.get().getProcessEngineService().getProcessEngine(engineId);
        if (processEngine != null) {
            return Optional.of(processEngine);
        } else {
            //no process engine running with engineId
            return Optional.empty();
        }
    }

    private Deployment deployProcess(DeploymentBuilder deploymentBuilder) {
        return deploymentBuilder.enableDuplicateFiltering(false).deploy();
    }

    private Boolean registerProcess(ProcessApplicationManager processApplicationManager, ProcessDefinition processDefinition, ProcessEngine processEngine, Deployment deployment) {
        ProcessApplicationReference processApplication = processApplicationManager.getProcessApplicationForDeployment(processDefinition.getDeploymentId());
        if (processApplication != null) {
            processEngine.getManagementService().registerProcessApplication(deployment.getId(), processApplication);
            return true;
        } else {
            return false;
        }
    }
}