package be.ward.ticketing.controller.tenant;

import be.ward.ticketing.service.tenant.TenantService;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EngineControllerTest {
    @Mock
    private TenantService tenantService;

    @InjectMocks
    private EngineController engineController;

    @Test
    public void startEngineTest() {
        when(tenantService.startProcessEngine("engineId")).thenReturn("Process engine with id [engineId] has started.");

        String answerFromStartingProcessEngine = engineController.startEngine("engineId");
        JSONObject jsonAnswer = new JSONObject(answerFromStartingProcessEngine);
        assertEquals("Process engine with id [engineId] has started.", jsonAnswer.getString("result"));
    }

    @Test
    public void startEnginesTest() {
        when(tenantService.startProcessEngines()).thenReturn("All services have been started.");

        String answerFromStartingAllProcessEngines = engineController.startEngines();
        JSONObject jsonAnswer = new JSONObject(answerFromStartingAllProcessEngines);
        assertEquals("All services have been started.", jsonAnswer.getString("result"));
    }

    @Test
    public void stopEngineTest() {
        when(tenantService.stopProcessEngine("engineId")).thenReturn("Process engine with id [engineId] has been stopped.");

        String answerFromStoppingProcessEngine = engineController.stopEngine("engineId");
        JSONObject jsonAnswer = new JSONObject(answerFromStoppingProcessEngine);
        assertEquals("Process engine with id [engineId] has been stopped.", jsonAnswer.getString("result"));
    }

    @Test
    public void stopEnginesTest() {
        when(tenantService.stopProcessEngines()).thenReturn("All engines have been stopped.");

        String answerFromStoppingAllProcessEngines = engineController.stopEngines();
        JSONObject jsonAnswer = new JSONObject(answerFromStoppingAllProcessEngines);
        assertEquals("All engines have been stopped.", jsonAnswer.getString("result"));
    }

    @Test
    public void deployProcessToEngineTest() {
        when(tenantService.deployProcessToEngine("processKey", "engineId")).thenReturn("Process [processKey] successfull deployed to engine [engineId].");

        String answerFromDeployingProcessToEngine = engineController.deployProcessToEngine("processKey", "engineId");
        JSONObject jsonAnswer = new JSONObject(answerFromDeployingProcessToEngine);
        assertEquals("Process [processKey] successfull deployed to engine [engineId].", jsonAnswer.getString("result"));
    }
}