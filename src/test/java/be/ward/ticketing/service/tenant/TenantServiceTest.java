package be.ward.ticketing.service.tenant;

import org.camunda.bpm.engine.ProcessEngine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TenantServiceTest {

    @Mock
    private ProcessEngine processEngine;

    @InjectMocks
    private TenantService tenantService;

    @Test
    public void addTenantTest() {
    }

    @Test
    public void createTenantTest() {
    }

    @Test
    public void deleteTenantTest() {
    }

    @Test
    public void startProcessEnginesTest() {
    }

    @Test
    public void startProcessEngineTest() {
    }

    @Test
    public void stopProcessEnginesTest() {
    }

    @Test
    public void stopProcessEngineTest() {
    }

    @Test
    public void deployProcessToEngineTest() {
    }
}