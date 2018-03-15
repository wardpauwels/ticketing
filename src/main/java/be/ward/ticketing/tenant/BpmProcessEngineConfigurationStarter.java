//package be.ward.ticketing.tenant;
//
//import org.camunda.bpm.container.RuntimeContainerDelegate;
//import org.camunda.bpm.engine.spring.ProcessEngineFactoryBean;
//import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.ApplicationListener;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.jdbc.datasource.SimpleDriverDataSource;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import javax.sql.DataSource;
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//public class BpmProcessEngineConfigurationStarter implements
//        ApplicationListener<ApplicationReadyEvent> {
//    // On application start-up a rest-call should be performed to Tenant- Service to get all relevant camunda- db configurations
//    // And an instance of ProcessEngine should be created and registered in context for each configuration.
//
//    @Override
//    public void onApplicationEvent(final ApplicationReadyEvent event) {
//        for (final String tenant : getTenants()) {
//            ProcessEngineFactoryBean factoryBean = new
//                    ProcessEngineFactoryBean();
//            SpringProcessEngineConfiguration configuration =
//                    processEngineConfiguration(dataSource(tenant));
//            configuration.setProcessEngineName(tenant);
//            factoryBean.setProcessEngineConfiguration(configuration);
//            try {
//                RuntimeContainerDelegate.INSTANCE.get().registerProcessEngine(factoryBean.
//                        getObject());
//            } catch (final Exception e) {
//                // TODO: proper error handling
//                e.printStackTrace();
//            }
//        }
//    }
//
//    // should be retrieved from tenant-service
//    public static List<String> getTenants() {
//        // TODO: retrieve data sources for all tenant from tenant-service
//        List<String> result = new ArrayList<>();
//        result.add("tenant1");
//        result.add("tenant2");
//        return result;
//    }
//
//    // should be retrieved from tenant-service
//    private DataSource dataSource(final String db) {
//        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
//        dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
//        dataSource.setUsername("ticketmaster");
//        dataSource.setPassword("mJ2CEk9EA7rDrNp0");
//        dataSource.setUrl("jdbc:mysql://localhost:3306/db_ticketingsystem_" + db + "?useSSL=false");
//        return dataSource;
//    }
//
//    private PlatformTransactionManager transactionManager(final DataSource
//                                                                  dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }
//
//    private SpringProcessEngineConfiguration
//    processEngineConfiguration(final DataSource dataSource) {
//        SpringProcessEngineConfiguration config = new
//                SpringProcessEngineConfiguration();
//        config.setDataSource(dataSource);
//        config.setTransactionManager(transactionManager(dataSource));
//        config.setDatabaseSchemaUpdate("true");
//        config.setHistory("audit");
//        config.setJobExecutorActivate(true);
//        return config;
//    }
//}