package be.ward.ticketing.service.user;

import be.ward.ticketing.data.user.RoleRepository;
import be.ward.ticketing.entities.user.Role;
import jersey.repackaged.com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @Test
    public void findAllRolesTest() {
        Role adminRole = new Role();
        adminRole.setRole("Administrator");
        Role modRole = new Role();
        modRole.setRole("Moderator");
        when(roleRepository.findAll()).thenReturn(Lists.newArrayList(adminRole, modRole));

        List<Role> roles = Lists.newArrayList(roleService.findAllRoles());

        assertNotNull(roles);
        assertEquals(adminRole.getRole(), roles.get(0).getRole());
        assertEquals(modRole.getRole(), roles.get(1).getRole());
        verify(roleRepository, times(1)).findAll();
        verifyNoMoreInteractions(roleRepository);
    }
}