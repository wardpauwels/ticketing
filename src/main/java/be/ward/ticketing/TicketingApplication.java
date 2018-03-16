package be.ward.ticketing;

import be.ward.ticketing.camundarest.CamundaRestApi;
import be.ward.ticketing.service.TenantService;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@EnableProcessApplication
@SpringBootApplication
public class TicketingApplication {

    public static boolean experimental = false;

    @Autowired
    private TenantService tenantService;

    public static void main(String... args) {
        SpringApplication.run(TicketingApplication.class, args);

        initCamundaRestApi();
    }

    @PostConstruct
    public void startProcesses() {
        tenantService.startProcessEngines();
    }

    private static void initCamundaRestApi() {
        CamundaRestApi camundaRestApi = new CamundaRestApi();
        camundaRestApi.getClasses();
    }
}