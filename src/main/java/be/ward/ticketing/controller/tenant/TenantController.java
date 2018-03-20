package be.ward.ticketing.controller.tenant;

import be.ward.ticketing.service.TenantService;
import org.json.JSONObject;
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
    private String addTenant(@RequestParam String tenantId, @RequestParam String tenantName) {
        return new JSONObject()
                .put("result", tenantService.addTenant(tenantId, tenantName))
                .toString();
    }

    @PostMapping(value = "/deletetenant")
    private String deleteTenant(@RequestParam String tenantId) {
        return new JSONObject()
                .put("result", tenantService.deleteTenant(tenantId))
                .toString();
    }

    @PostMapping(value = "/startengine")
    private String startEngine(@RequestParam String engineId) {
        return new JSONObject()
                .put("result", tenantService.startProcessEngine(engineId))
                .toString();
    }

    @PostMapping(value = "/startengines")
    private String startEngines() {
        return new JSONObject()
                .put("result", tenantService.startProcessEngines())
                .toString();
    }

    @PostMapping(value = "/stopengine")
    private String stopEngine(@RequestParam String engineId) {
        return new JSONObject()
                .put("result", tenantService.stopProcessEngine(engineId))
                .toString();
    }

    @PostMapping(value = "/stopengines")
    private String stopEngines() {
        return new JSONObject()
                .put("result", tenantService.stopProcessEngines())
                .toString();
    }

    @PostMapping(value = "/deployprocess")
    private String deployProcessToEngine(@RequestParam String processKey, @RequestParam String engineId) {
        return new JSONObject()
                .put("result", tenantService.deployProcessToEngine(processKey, engineId))
                .toString();
    }
}