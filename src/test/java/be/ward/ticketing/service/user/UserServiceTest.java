package be.ward.ticketing.service.user;

import be.ward.ticketing.data.user.UserRepository;
import be.ward.ticketing.entities.user.User;
import jersey.repackaged.com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserService userService;

    private User userWard;
    private User userBert;

    @Before
    public void setUp() {
        userWard = new User("ward", "pauwels");
        userBert = new User("bert", "hackerman");
    }

    @Test
    @Ignore
    public void createUserTest() {
        when(userRepository.save(userWard)).thenReturn(userWard);
        when(encoder.encode(userWard.getPassword())).thenReturn("pauwels");

        User newUser = userService.createUser(userWard.getUsername(), userWard.getPassword());

        assertNotNull(newUser);
    }

    @Test
    public void findAllUsersTest() {
        when(userRepository.findAll()).thenReturn(Lists.newArrayList(userWard, userBert));
        List<User> users = Lists.newArrayList(userService.findAllUsers());

        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals(userWard.getUsername(), users.get(0).getUsername());
        assertEquals(userBert.getUsername(), users.get(1).getUsername());
        verify(userRepository, times(1)).findAll();
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void findUserWithUsernameTest() {
        when(userRepository.findUserByUsername("bert")).thenReturn(userBert);
        User user = userService.findUserWithUsername("bert");

        assertNotNull(user);
        assertEquals(userBert.getUsername(), user.getUsername());
        assertEquals(userBert.getPassword(), user.getPassword());
        verify(userRepository, times(1)).findUserByUsername("bert");
        verifyNoMoreInteractions(userRepository);
    }
}