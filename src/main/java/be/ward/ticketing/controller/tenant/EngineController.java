package be.ward.ticketing.controller.tenant;

import be.ward.ticketing.service.tenant.TenantService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/engines")
public class EngineController {

    private final TenantService tenantService;

    @Autowired
    public EngineController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @PostMapping(value = "/start")
    public String startEngine(@RequestParam String engineId) {
        return new JSONObject()
                .put("result", tenantService.startProcessEngine(engineId))
                .toString();
    }

    @PostMapping(value = "/startall")
    public String startEngines() {
        return new JSONObject()
                .put("result", tenantService.startProcessEngines())
                .toString();
    }

    @PostMapping(value = "/stop")
    public String stopEngine(@RequestParam String engineId) {
        return new JSONObject()
                .put("result", tenantService.stopProcessEngine(engineId))
                .toString();
    }

    @PostMapping(value = "/stopall")
    public String stopEngines() {
        return new JSONObject()
                .put("result", tenantService.stopProcessEngines())
                .toString();
    }

    @PostMapping(value = "/deployprocess")
    public String deployProcessToEngine(@RequestParam String processKey, @RequestParam String engineId) {
        return new JSONObject()
                .put("result", tenantService.deployProcessToEngine(processKey, engineId))
                .toString();
    }
}
