package be.ward.ticketing.service;

import be.ward.ticketing.data.tenant.TenantDao;
import be.ward.ticketing.entities.tenants.Tenant;
import be.ward.ticketing.util.TicketingException;
import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.application.ProcessApplicationReference;
import org.camunda.bpm.container.RuntimeContainerDelegate;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.authorization.Authorization;
import org.camunda.bpm.engine.authorization.Groups;
import org.camunda.bpm.engine.authorization.Resource;
import org.camunda.bpm.engine.authorization.Resources;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.ProcessEngineImpl;
import org.camunda.bpm.engine.impl.application.ProcessApplicationManager;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.camunda.bpm.engine.impl.persistence.StrongUuidGenerator;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.camunda.bpm.engine.authorization.Authorization.ANY;
import static org.camunda.bpm.engine.authorization.Authorization.AUTH_TYPE_GRANT;
import static org.camunda.bpm.engine.authorization.Permissions.ALL;

@Service
public class TenantService {

    private final static Logger LOGGER = LoggerFactory.getLogger(TenantService.class);
    @Autowired
    ProcessEngine camunda;
    @Autowired
    private TenantDao tenantDao;

    public Tenant addTenant(Tenant tenant) {
        try {
            camunda.getIdentityService().newTenant(tenant.getId());
        } catch (IllegalArgumentException e) {
            LOGGER.debug("Tenant already exists in camunda db");
        }
        if (getTenant(tenant.getId()) == null) {
            return tenantDao.save(tenant);
        } else {
            throw new TicketingException("Tenant already exists with id in Tenant db");
        }
    }

    public String deleteTenant(String tenantId) {
        try {
            if (getProcessEngine(tenantId) != null) {
                stopProcessEngine(tenantId);
            }
            tenantDao.delete(tenantId);
            camunda.getIdentityService().deleteTenant(tenantId);
            return "Tenant with id [" + tenantId + "] has been deleted.";
        } catch (TicketingException e) {
            return "Can't delete tenant";
        }
    }

    public List<Tenant> getAllTenants() {
        List<Tenant> tenants = new ArrayList<>();
        for (Tenant tenant : tenantDao.findAll()) {
            tenants.add(tenant);
        }
        return tenants;
    }

    public Tenant getTenant(String id) {
        return tenantDao.findOne(id);
    }

    public List<String> startProcessEngines() {
        List<String> processEngines = new ArrayList<>();

        for (Tenant tenant : getAllTenants()) {
            String processEngineComment = startProcessEngine(tenant.getId());
            processEngines.add(processEngineComment);
        }

        return processEngines;
    }

    public String startProcessEngine(String engineId) {
        try {
            if (getTenant(engineId) == null) {
                addTenant(new Tenant(engineId, engineId));
            }
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

            return "Process engine with id [" + engineId + "] has started.";
        } catch (TicketingException e) {
            return "Couldn't start process engine";
        }
    }

    public void stopProcessEngines() {
        for (Tenant tenant : getAllTenants()) {
            stopProcessEngine(tenant.getId());
        }
    }

    public String stopProcessEngine(String engineId) {
        try {
            RuntimeContainerDelegate runtimeContainerDelegate = RuntimeContainerDelegate.INSTANCE.get();
            ProcessEngine processEngine = getProcessEngine(engineId);
            processEngine.close();
            runtimeContainerDelegate.unregisterProcessEngine(processEngine);
            return "Process engine with id [" + engineId + "] has been stopped.";
        } catch (TicketingException e) {
            return "Couldn't stop process engine";
        }
    }

    public void createNewDefaultUserForEngine(String engineId, String username, String password) {
        ProcessEngine engine = getProcessEngine(engineId);

        // and add default user to Camunda to be ready-to-go
        if (engine.getIdentityService().createUserQuery().userId(username).count() == 0) {
            User user = engine.getIdentityService().newUser(username);
            user.setPassword(password);
            engine.getIdentityService().saveUser(user);

            Group group = engine.getIdentityService().newGroup(Groups.CAMUNDA_ADMIN);
            group.setName("Administrators");
            group.setType(Groups.GROUP_TYPE_SYSTEM);
            engine.getIdentityService().saveGroup(group);

            for (Resource resource : Resources.values()) {
                Authorization auth = engine.getAuthorizationService().createNewAuthorization(AUTH_TYPE_GRANT);
                auth.setGroupId(Groups.CAMUNDA_ADMIN);
                auth.addPermission(ALL);
                auth.setResourceId(ANY);
                auth.setResource(resource);
                engine.getAuthorizationService().saveAuthorization(auth);
            }

            engine.getIdentityService().createMembership(username, Groups.CAMUNDA_ADMIN);
        }
    }

    public String deployProcessToEngine(String processKey, String engineId) {
        ProcessEngine defaultProcessEngine = BpmPlatform.getDefaultProcessEngine();
        ProcessEngineConfigurationImpl defaultProcessEngineConfiguration = ((ProcessEngineImpl) defaultProcessEngine).getProcessEngineConfiguration();
        ProcessApplicationManager defaultProcessApplicationManager = defaultProcessEngineConfiguration.getProcessApplicationManager();

        DeploymentBuilder deploymentBuilder = getProcessEngine(engineId).getRepositoryService().createDeployment();
        List<ProcessDefinition> processDefinitions = defaultProcessEngine.getRepositoryService().createProcessDefinitionQuery().list();
        ProcessDefinition processDefinition = null;
        String deploymentId = "";

        for (ProcessDefinition definition : processDefinitions) {
            if ((definition.getKey()).equals(processKey)) {
                processDefinition = definition;
                deploymentId = definition.getDeploymentId();
            }
        }

        deploymentBuilder.addInputStream(//
                processDefinition.getResourceName(),
                defaultProcessEngine
                        .getRepositoryService()
                        .getResourceAsStream(deploymentId, processDefinition.getResourceName()));

        //deployment
        Deployment deployment = deploymentBuilder.enableDuplicateFiltering().deploy();

        //registration
        String newDeploymentId = deployment.getId();

        ProcessApplicationReference processApplication = defaultProcessApplicationManager.getProcessApplicationForDeployment(deploymentId);
        if (processApplication != null) {
            getProcessEngine(engineId).getManagementService().registerProcessApplication(newDeploymentId, processApplication);
        }


        return "Process [" + processKey + "] successfully deployed to engine [" + engineId + "].";
    }

    public String deployProcessesToEngine(String processKey, String engineId) {
        try {
            ProcessEngine defaultProcessEngine = BpmPlatform.getDefaultProcessEngine();
            ProcessEngineConfigurationImpl defaultProcessEngineConfiguration = ((ProcessEngineImpl) defaultProcessEngine).getProcessEngineConfiguration();
            ProcessApplicationManager defaultProcessApplicationManager = defaultProcessEngineConfiguration.getProcessApplicationManager();

            Map<String, DeploymentBuilder> deployments = new HashMap<String, DeploymentBuilder>();

            List<ProcessDefinition> processDefinitions = defaultProcessEngine.getRepositoryService().createProcessDefinitionQuery().list();
            for (ProcessDefinition processDefinition : processDefinitions) {
                String deploymentId = processDefinition.getDeploymentId();

                if (!deployments.containsKey(deploymentId)) {
                    deployments.put(deploymentId, getProcessEngine(engineId).getRepositoryService().createDeployment());
                }

                deployments.get(deploymentId).addInputStream(//
                        processDefinition.getResourceName(), //
                        defaultProcessEngine.getRepositoryService().getResourceAsStream(deploymentId, processDefinition.getResourceName()));
            }

            for (Map.Entry<String, DeploymentBuilder> entry : deployments.entrySet()) {
                // do the deployment
                Deployment deployment = entry.getValue().enableDuplicateFiltering().deploy();

                // and registration
                String defaultExistingDeploymentId = entry.getKey();
                String newDeploymentId = deployment.getId();

                ProcessApplicationReference processApplication = defaultProcessApplicationManager.getProcessApplicationForDeployment(defaultExistingDeploymentId);
                if (processApplication != null) {
                    getProcessEngine(engineId).getManagementService().registerProcessApplication(newDeploymentId, processApplication);
                } else {
                    // no Process Application is deployed - ignore
                }

                // TODO: Resume old versions!
            }

            return "Process [" + processKey + "] deployed to engine [" + engineId + "].";
        } catch (TicketingException e) {
            return "Process [" + processKey + "] not deployed.";
        }
    }

    private ProcessEngine getProcessEngine(String engineId) {
        return RuntimeContainerDelegate.INSTANCE.get().getProcessEngineService().getProcessEngine(engineId);
    }

}
