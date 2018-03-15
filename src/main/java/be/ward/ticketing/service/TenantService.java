package be.ward.ticketing.service;

import be.ward.ticketing.util.TicketingException;
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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TenantService {

    @Autowired
    ProcessEngine camunda;

    public String addTenant(String tenantId, String tenantName) {
        if (getTenant(tenantId) == null) {
            camunda.getIdentityService().saveTenant(createTenant(tenantId, tenantName));
            return "Tenant has been added to db.";
        } else {
            return "Tenant already exists in db.";
        }
    }

    private Tenant createTenant(String tenantId, String tenantName) {
        Tenant tenant = camunda.getIdentityService().newTenant(tenantId);
        tenant.setName(tenantName);
        return tenant;
    }

    public String deleteTenant(String tenantId) {
        if (getProcessEngine(tenantId) == null) {
            camunda.getIdentityService().deleteTenant(tenantId);
            return "Tenant with id [" + tenantId + "] has been deleted.";
        } else {
            return "Engine for tenant with id [" + tenantId + "] still running";
        }
    }

    private List<Tenant> getAllTenants() {
        return camunda.getIdentityService().createTenantQuery().orderByTenantId().asc().list();
    }

    private Tenant getTenant(String tenantId) {
        return camunda.getIdentityService().createTenantQuery().tenantId(tenantId).singleResult();
    }

    private Tenant getTenantForEngine(String tenantId, ProcessEngine engine) {
        return engine.getIdentityService().createTenantQuery().tenantId(tenantId).singleResult();
    }

    public String startProcessEngines() {
        for (Tenant tenant : getAllTenants()) {
            if (getProcessEngine(tenant.getId()) == null) {
                startProcessEngine(tenant.getId());
            }
        }

        return "All services have been started.";
    }

    public String startProcessEngine(String engineId) {
        try {
            ProcessEngineConfigurationImpl configuration = new StandaloneProcessEngineConfiguration();
            configuration.setIdGenerator(new StrongUuidGenerator());

            configuration.setProcessEngineName(engineId);

            configuration.setJdbcDriver("com.mysql.jdbc.Driver");
            configuration.setJdbcUrl("jdbc:mysql://localhost:3306/db_ticketingsystem_" + engineId + "?useSSL=false&createDatabaseIfNotExist=true");
            configuration.setJdbcUsername("ticketmaster");
            configuration.setJdbcPassword("mJ2CEk9EA7rDrNp0");
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
        } catch (TicketingException e) {
            return "Couldn't start process engine";
        }
    }

    public String stopProcessEngines() {
        for (ProcessEngine processEngine : getAllProcessEngines()) {
            if (!processEngine.getName().equals("default")) {
                stopProcessEngine(processEngine.getName());
            }
        }
        return "All engines have been stopped.";
    }

    public String stopProcessEngine(String engineId) {
        try {
            RuntimeContainerDelegate runtimeContainerDelegate = RuntimeContainerDelegate.INSTANCE.get();
            ProcessEngine processEngine = getProcessEngine(engineId);
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

        DeploymentBuilder deploymentBuilder = getProcessEngine(engineId).getRepositoryService().createDeployment();
        List<ProcessDefinition> processDefinitions = defaultProcessEngine.getRepositoryService().createProcessDefinitionQuery().list();
        ProcessDefinition processDefinition = null;
        String deploymentId = null;

        for (ProcessDefinition definition : processDefinitions) {
            if ((definition.getKey()).equals(processKey)) {
                processDefinition = definition;
                deploymentId = definition.getDeploymentId();
            }
        }

        deploymentBuilder.addInputStream(
                processDefinition.getResourceName(),
                defaultProcessEngine
                        .getRepositoryService()
                        .getResourceAsStream(deploymentId, processDefinition.getResourceName()));

        //deployment
        Deployment deployment = deploymentBuilder.enableDuplicateFiltering(false).deploy();

        //registration
        ProcessApplicationReference processApplication = defaultProcessApplicationManager.getProcessApplicationForDeployment(deploymentId);
        if (processApplication != null) {
            getProcessEngine(engineId).getManagementService().registerProcessApplication(deployment.getId(), processApplication);
        }

        return "Process [" + processKey + "] successfully deployed to engine [" + engineId + "].";
    }

    private List<ProcessEngine> getAllProcessEngines() {
        return RuntimeContainerDelegate.INSTANCE.get().getProcessEngineService().getProcessEngines();
    }

    public ProcessEngine getProcessEngine(String engineId) {
        return RuntimeContainerDelegate.INSTANCE.get().getProcessEngineService().getProcessEngine(engineId);
    }

}
