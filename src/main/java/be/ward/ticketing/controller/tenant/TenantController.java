package be.ward.ticketing.controller.tenant;

import be.ward.ticketing.service.TenantService;
import be.ward.ticketing.util.StringResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    @PostMapping(value = "/addtenant")
    private StringResponse addTenant(@RequestParam String tenantId, @RequestParam String tenantName) {
        return new StringResponse(tenantService.addTenant(tenantId, tenantName));
    }

    @PostMapping(value = "/deletetenant")
    private StringResponse deleteTenant(@RequestParam String tenantId) {
        return new StringResponse(tenantService.deleteTenant(tenantId));
    }

    @PostMapping(value = "/startengine")
    private StringResponse startEngine(@RequestParam String engineId) {
        return new StringResponse(tenantService.startProcessEngine(engineId));
    }

    @PostMapping(value = "/startengines")
    private StringResponse startEngines() {
        return new StringResponse(tenantService.startProcessEngines());
    }

    @PostMapping(value = "/stopengine")
    private StringResponse stopEngine(@RequestParam String engineId) {
        return new StringResponse(tenantService.stopProcessEngine(engineId));
    }

    @PostMapping(value = "/stopengines")
    private StringResponse stopEngines() {
        return new StringResponse(tenantService.stopProcessEngines());
    }

    @PostMapping(value = "/deployprocess")
    private StringResponse deployProcessToEngine(@RequestParam String processKey, @RequestParam String engineId) {
        return new StringResponse(tenantService.deployProcessToEngine(processKey, engineId));
    }
}
