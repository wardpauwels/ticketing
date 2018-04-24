package be.ward.ticketing.controller.user;

import be.ward.ticketing.entities.user.User;
import be.ward.ticketing.service.user.UserService;
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
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    public void getAllUsersTest() {
        User userWard = new User("ward", "password");
        User userBert = new User("bert", "hackerman");
        when(userService.findAllUsers()).thenReturn(Lists.newArrayList(userWard, userBert));

        List<User> users = userController.getAllUsers();

        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals(userBert.getUsername(), users.get(1).getUsername());
        assertEquals(userWard.getPassword(), users.get(0).getPassword());
    }

    @Test
    public void getUserByUsernameTest() {
        User userWard = new User("ward", "password");
        when(userService.findUserWithUsername("ward")).thenReturn(userWard);

        User askedUser = userController.getUserByUsername("ward");

        assertNotNull(askedUser);
        assertEquals(userWard.getUsername(), askedUser.getUsername());
    }

    @Test
    public void makeNewUserTest() {
        User userBert = new User("bert", "hackerman");
        when(userService.createUser("bert", "hackerman")).thenReturn(userBert);

        User user = userController.makeNewUser("bert", "hackerman");

        assertNotNull(user);
        assertEquals(userBert.getUsername(), user.getUsername());
        assertEquals(userBert.getPassword(), user.getPassword());
    }
}