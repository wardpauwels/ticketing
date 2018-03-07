package be.ward.ticketing;

import be.ward.ticketing.camundarest.CamundaRestApi;
import org.camunda.bpm.BpmPlatform;
import org.camunda.bpm.container.RuntimeContainerDelegate;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.identity.Tenant;
import org.camunda.bpm.engine.identity.TenantQuery;
import org.camunda.bpm.engine.impl.identity.Authentication;
import org.camunda.bpm.engine.spring.ProcessEngineFactoryBean;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@EnableProcessApplication
@SpringBootApplication
public class TicketingApplication {

    static ProcessEngine processEngine;


    public static void main(String... args) {
        SpringApplication.run(TicketingApplication.class, args);

        initCamundaRestApi();

        processEngine = BpmPlatform.getDefaultProcessEngine();
        createEngines();
        deployProcesses();
    }

    private static void initCamundaRestApi() {
        CamundaRestApi camundaRestApi = new CamundaRestApi();
        camundaRestApi.getClasses();
    }

    private static void createEngines() {
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

    // should be retrieved from tenant-service
    public static List<String> getTenants() {
        IdentityService identityService = processEngine.getIdentityService();
        Authentication currentAuthentication = identityService.getCurrentAuthentication();

        // TODO: retrieve data sources for all tenants from tenant-service
        List<String> result = new ArrayList<>();
        result.add("tenant1");
        result.add("tenant2");
        return result;
    }

//    public List<TenantDto> queryTenants(UriInfo uriInfo, Integer firstResult, Integer maxResults) {
//        TenantQueryDto queryDto = new TenantQueryDto(getObjectMapper(), uriInfo.getQueryParameters());
//
//        TenantQuery query = queryDto.toQuery(getProcessEngine());
//
//        List<Tenant> tenants;
//        if (firstResult != null || maxResults != null) {
//            tenants = executePaginatedQuery(query, firstResult, maxResults);
//        } else {
//            tenants = query.list();
//        }
//
//        return TenantDto.fromTenantList(tenants );
//    }

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

    private static void deployProcesses() {
        ProcessEngine processEngine = BpmPlatform.getProcessEngineService().getProcessEngine("tenant1");
        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.createDeployment()
                .tenantId("tenant1")
                .addClasspathResource("processes/ticket.bpmn")
                .deploy();

    }

    protected List<Tenant> executePaginatedQuery(TenantQuery query, Integer firstResult, Integer maxResults) {
        if (firstResult == null) {
            firstResult = 0;
        }
        if (maxResults == null) {
            maxResults = Integer.MAX_VALUE;
        }
        return query.listPage(firstResult, maxResults);
    }
}