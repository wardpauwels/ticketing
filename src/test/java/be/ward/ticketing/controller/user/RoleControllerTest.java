package be.ward.ticketing.controller.user;

import be.ward.ticketing.entities.user.Role;
import be.ward.ticketing.service.user.RoleService;
import jersey.repackaged.com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class RoleControllerTest {

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    @Test
    public void findAllRolesTest() {
        Role roleAdmin = new Role();
        roleAdmin.setRole("Administrator");
        Role roleMods = new Role();
        roleMods.setRole("Moderator");

        when(roleService.findAllRoles()).thenReturn(Lists.newArrayList(roleAdmin, roleMods));

        List<Role> roles = roleController.findAllRoles();

        assertEquals(2, roles.size());
        assertEquals(roleAdmin.getRole(), roles.get(0).getRole());
        assertEquals(roleMods.getRole(), roles.get(1).getRole());

        verify(roleService, times(1)).findAllRoles();
        verifyNoMoreInteractions(roleService);
    }
}