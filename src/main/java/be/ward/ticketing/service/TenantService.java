package be.ward.ticketing.service;

import be.ward.ticketing.exception.NoEngineFoundException;
import be.ward.ticketing.exception.NoProcessDefinitionFoundException;
import be.ward.ticketing.exception.ProcessEngineException;
import be.ward.ticketing.exception.TicketingException;
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

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
public class TenantService {

    @Autowired
    private ProcessEngine processEngine;

    @Value("${spring.datasource.username}")
    private String databaseUsername;

    @Value("${spring.datasource.password}")
    private String databasePassword;

    @Value("${spring.datasource.driver-class-name}")
    private String databaseDriverClassName;

    @PostConstruct
    public void startProcesses() {
        startProcessEngines();
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
        try {
            ProcessEngineConfigurationImpl configuration = new StandaloneProcessEngineConfiguration();

            configuration.setIdGenerator(new StrongUuidGenerator());
            configuration.setProcessEngineName(engineId);
            configuration.setJdbcDriver(databaseDriverClassName);
            configuration.setJdbcUrl("jdbc:mysql://localhost:3306/db_ticketingsystem_" + engineId + "?useSSL=false&createDatabaseIfNotExist=true");
            configuration.setJdbcUsername(databaseUsername);
            configuration.setJdbcPassword(databasePassword);
            configuration.setDatabaseSchemaUpdate(StandaloneProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
            configuration.setJobExecutorDeploymentAware(true);
            configuration.setHistory(StandaloneProcessEngineConfiguration.HISTORY_AUDIT);

            ProcessEngine processEngine = configuration.buildProcessEngine();

            // start the process engine inside the container.
            RuntimeContainerDelegate.INSTANCE.get().registerProcessEngine(processEngine);

            if (getTenantForEngine(engineId, processEngine) == null) {
                processEngine.getIdentityService().saveTenant(createTenant(engineId, engineId));
            }

            return "Process engine with id [" + engineId + "] has started.";
        } catch (ProcessEngineException e) {
            throw new ProcessEngineException("Couldn't start process engine");
        }
    }

    public String stopProcessEngines() {
        getAllProcessEngines()
                .stream()
                .filter(processEngine -> !processEngine.getName().equals("default"))
                .forEach(processEngine -> stopProcessEngine(processEngine.getName()));

        return "All engines have been stopped.";
    }

    public String stopProcessEngine(String engineId) {
        try {
            if (!getProcessEngine(engineId).isPresent()) throw new NoEngineFoundException();
            ProcessEngine processEngine = getProcessEngine(engineId).get();
            RuntimeContainerDelegate runtimeContainerDelegate = RuntimeContainerDelegate.INSTANCE.get();

            processEngine.close();
            runtimeContainerDelegate.unregisterProcessEngine(processEngine);

            return "Process engine with id [" + engineId + "] has been stopped.";
        } catch (TicketingException e) {
            return "Couldn't stop process engine. \n e";
        }
    }

    public String deployProcessToEngine(String processKey, String engineId) {
        ProcessEngine defaultProcessEngine = BpmPlatform.getDefaultProcessEngine();
        ProcessEngineConfigurationImpl defaultProcessEngineConfiguration = ((ProcessEngineImpl) defaultProcessEngine).getProcessEngineConfiguration();
        ProcessApplicationManager defaultProcessApplicationManager = defaultProcessEngineConfiguration.getProcessApplicationManager();

        List<ProcessDefinition> processDefinitions = defaultProcessEngine.getRepositoryService().createProcessDefinitionQuery().list();
        Optional<ProcessEngine> processEngineOptional = getProcessEngine(engineId);

        // if no process engine is found deployment has failed
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

        //deployment
        Deployment deployment = deploymentBuilder.enableDuplicateFiltering(false).deploy();

        //registration
        ProcessApplicationReference processApplication = defaultProcessApplicationManager.getProcessApplicationForDeployment(processDefinition.getDeploymentId());
        if (processApplication != null) {
            processEngineOptional
                    .map(processEngine -> {
                        processEngine.getManagementService().registerProcessApplication(deployment.getId(), processApplication);
                        return "Process [" + processKey + "] successfull deployed to engine [" + engineId + "].";
                    });
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
}