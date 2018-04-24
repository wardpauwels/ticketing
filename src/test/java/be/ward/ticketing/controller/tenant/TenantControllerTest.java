package be.ward.ticketing.controller.tenant;

import be.ward.ticketing.service.tenant.TenantService;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TenantControllerTest {

    @Mock
    private TenantService tenantService;

    @InjectMocks
    private TenantController tenantController;

    @Test
    public void addTenantTest() {
        when(tenantService.addTenant("tenantId", "tenantName")).thenReturn("Tenant has been added to db and engine has started.");

        String answerFromAddingTenant = tenantController.addTenant("tenantId", "tenantName");
        JSONObject jsonAnswer = new JSONObject(answerFromAddingTenant);
        assertEquals("Tenant has been added to db and engine has started.", jsonAnswer.getString("result"));
    }

    @Test
    public void deleteTenantTest() {
        when(tenantService.deleteTenant("tenantId")).thenReturn("Tenant with id [tenantId] has been deleted.");

        String answerFromDeletingTenant = tenantController.deleteTenant("tenantId");
        JSONObject jsonAnswer = new JSONObject(answerFromDeletingTenant);
        assertEquals("Tenant with id [tenantId] has been deleted.", jsonAnswer.getString("result"));
    }
}