package be.ward.ticketing;

import be.ward.ticketing.camundarest.CamundaRestApi;
import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.container.RuntimeContainerDelegate;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.authorization.Authorization;
import org.camunda.bpm.engine.authorization.Groups;
import org.camunda.bpm.engine.authorization.Resource;
import org.camunda.bpm.engine.authorization.Resources;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.spring.ProcessEngineFactoryBean;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

import static org.camunda.bpm.engine.authorization.Authorization.ANY;
import static org.camunda.bpm.engine.authorization.Authorization.AUTH_TYPE_GRANT;
import static org.camunda.bpm.engine.authorization.Permissions.ALL;

@EnableProcessApplication
@SpringBootApplication
public class TicketingApplication {

    static ProcessEngine processEngine;

    public static void main(String... args) {
        SpringApplication.run(TicketingApplication.class, args);

        initCamundaRestApi();

        processEngine = BpmPlatform.getDefaultProcessEngine();
        ProcessEngine engine = BpmPlatform.getDefaultProcessEngine();
        //createDefaultUser(engine);
        getTenants();
        //createEngines();
        //deployProcesses();
    }

    private static void createEngines() {
        ProcessEngines.init();

        for (final String tenant : getTenants()) {
            ProcessEngineFactoryBean factoryBean = new ProcessEngineFactoryBean();
            SpringProcessEngineConfiguration configuration = processEngineConfiguration(dataSource(tenant));
            configuration.setProcessEngineName(tenant);
            factoryBean.setProcessEngineConfiguration(configuration);
            try {
                RuntimeContainerDelegate.INSTANCE.get().registerProcessEngine(factoryBean.getObject());
            } catch (final Exception e) {
                // TODO: proper error handling
                e.printStackTrace();
            }
        }
    }

    private static void initCamundaRestApi() {
        CamundaRestApi camundaRestApi = new CamundaRestApi();
        camundaRestApi.getClasses();
    }

    // should be retrieved from tenant-service
    public static List<String> getTenants() {
        // TODO: retrieve data sources for all tenants from tenant-service

        List<String> result = new ArrayList<>();
        result.add("tenant1");
        result.add("tenant2");
        return result;
    }

    private static void deployProcesses() {
        ProcessEngine processEngine = BpmPlatform.getProcessEngineService().getProcessEngine("tenant1");
        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.createDeployment()
                .tenantId("tenant1")
                .addClasspathResource("processes/tenant1/ticket.bpmn")
                .deploy();

    }

    private static SpringProcessEngineConfiguration processEngineConfiguration(final DataSource dataSource) {
        SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();
        config.setDataSource(dataSource);
        config.setTransactionManager(transactionManager(dataSource));
        config.setDatabaseSchemaUpdate("true");
        config.setHistory("audit");
        config.setJobExecutorActivate(true);
        return config;
    }

    private static PlatformTransactionManager transactionManager(final DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    // should be retrieved from tenant-service
    private static DataSource dataSource(final String db) {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
        dataSource.setUsername("ticketmaster");
        dataSource.setPassword("mJ2CEk9EA7rDrNp0");
        dataSource.setUrl("jdbc:mysql://localhost:3306/db_ticketingsystem_" + db + "?useSSL=false");
        return dataSource;
    }

    public static void createDefaultUser(ProcessEngine engine) {
        // and add default user to Camunda to be ready-to-go
        if (engine.getIdentityService().createUserQuery().userId("demo").count() == 0) {
            User user = engine.getIdentityService().newUser("demo");
            user.setFirstName("Demo");
            user.setLastName("Demo");
            user.setPassword("demo");
            user.setEmail("demo@camunda.org");
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

            engine.getIdentityService().createMembership("demo", Groups.CAMUNDA_ADMIN);
        }
    }

    @PostConstruct
    public void startProcess() {
        createEngines();
    }
}