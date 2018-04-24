package be.ward.ticketing.entities.user;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@SpringBootTest
public class UserTest {

    @Test
    public void createAndReadUserTest() {
        Role role = new Role();
        User user = new User("pietje", "puk");

        user.setRole(role);

        assertNull(user.getId());
        assertEquals("pietje", user.getUsername());
        assertEquals("puk", user.getPassword());
        assertEquals(role, user.getRole());

        user.setUsername("ward");
        user.setPassword("pauwels");

        assertEquals("ward", user.getUsername());
        assertEquals("pauwels", user.getPassword());
    }

}