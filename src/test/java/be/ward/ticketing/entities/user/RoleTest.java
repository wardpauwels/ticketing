package be.ward.ticketing.entities.user;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;

@SpringBootTest
public class RoleTest {

    @Test
    public void createAndReadRoleTest() {
        Role role = new Role();

        role.setRole("Administrator");

        assertEquals(0, role.getId());
        assertEquals("Administrator", role.getRole());
    }

}