package be.ward.ticketing.controller.tenant;

import be.ward.ticketing.entities.tenants.Tenant;
import be.ward.ticketing.service.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    @PostMapping("/addtenant")
    private Tenant addTenant(@RequestParam String tenantId, @RequestParam String tenantName) {
        return tenantService.addTenant(new Tenant(tenantId, tenantName));
    }

    @PostMapping("/deletetenant")
    private String deleteTenant(@RequestParam String tenantId) {
        return tenantService.deleteTenant(tenantId);
    }

    @PostMapping("/startengine")
    private String startEngine(@RequestParam String engineId) {
        return tenantService.startProcessEngine(engineId);
    }

    @PostMapping("/stopengine")
    private String stopEngine(@RequestParam String engineId) {
        return tenantService.stopProcessEngine(engineId);
    }

    @PostMapping("/deployprocess")
    private String deployProcessToEngine(@RequestParam String processKey, @RequestParam String engineId) {
        return tenantService.deployProcessToEngine(processKey, engineId);
    }
}
