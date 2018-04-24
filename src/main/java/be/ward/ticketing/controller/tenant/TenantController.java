package be.ward.ticketing.controller.tenant;

import be.ward.ticketing.service.tenant.TenantService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/tenants")
public class TenantController {

    private final TenantService tenantService;

    @Autowired
    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @PostMapping
    public String addTenant(@RequestParam String tenantId, @RequestParam String tenantName) {
        return new JSONObject()
                .put("result", tenantService.addTenant(tenantId, tenantName))
                .toString();
    }

    @DeleteMapping("/{tenantId}")
    public String deleteTenant(@PathVariable(value = "tenantId") String tenantId) {
        return new JSONObject()
                .put("result", tenantService.deleteTenant(tenantId))
                .toString();
    }
}