//package be.ward.ticketing.tenant;
//
//import org.camunda.bpm.BpmPlatform;
//import org.camunda.bpm.engine.DecisionService;
//import org.camunda.bpm.engine.ProcessEngine;
//import org.camunda.bpm.engine.ProcessEngineException;
//import org.camunda.bpm.engine.RuntimeService;
//import org.camunda.bpm.engine.cdi.impl.ProcessEngineServicesProducer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//
//import java.util.Random;
//
//@Configuration
//@ThreadScoped
//@AllArgsConstructor
//public class TenantAwareProcessEngineServicesProducer extends ProcessEngineServicesProducer {
//    @Override
//    @ThreadScoped
//    @Bean
//    // The ProcessEngine bean should either:
//    // 1. have a thread-scope (be careful) so if it is used as a dependency in any spring component/service it would be resolved based on tenantId from current thread-local
//    // 2. or it should never be injected/autowired but only resolved by a call to a Custom CamundaProcessEngineFactory where an instance would also be returned based on tenantId from current thread-local
//    // Here you can see implementation of option 1.
//    public ProcessEngine processEngine() {
//        String processEngineName = getEngine();
//        if (processEngineName != null) {
//            ProcessEngine processEngine =
//                    BpmPlatform.getProcessEngineService().getProcessEngine(processEngineName);
//            if (processEngine != null) {
//                return processEngine;
//            } else {
//                throw new ProcessEngineException("No process engine found for tenant id '" + processEngineName + "'.");
//            }
//        } else {
//            throw new ProcessEngineException("No tenant id specified. A process engine can only be retrieved based on a tenant.");
//        }
//    }
//
//    private String getEngine() {
//        // TODO: TenantContext.getCurrentTenant()
//        return getTenants().get(new Random().nextInt(getTenants().size()));
//    }
//
//    @Override
//    @ThreadScoped
//    @Primary
//    @Bean
//    public RuntimeService runtimeService() {
//        return processEngine().getRuntimeService();
//    }
//
//    @Override
//    @ThreadScoped
//    @Primary
//    @Bean
//    public DecisionService decisionService() {
//        return processEngine().getDecisionService();
//    }
//}