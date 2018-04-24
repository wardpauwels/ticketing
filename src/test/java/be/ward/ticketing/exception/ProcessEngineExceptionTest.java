package be.ward.ticketing.exception;

import be.ward.ticketing.service.tenant.TenantService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ProcessEngineExceptionTest {

    @Autowired
    private TenantService tenantService;

    @Test(expected = ProcessEngineException.class)
//    @Ignore
    public void processEngineExceptionTest() {
        tenantService.startProcessEngine("myLittleEngine");
        tenantService.startProcessEngine("myLittleEngine");
    }
}