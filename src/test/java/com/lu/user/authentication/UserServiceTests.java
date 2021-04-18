package com.lu.user.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lu.user.authentication.exceptions.UserAuthenticationException;
import com.lu.user.authentication.model.User;
import com.lu.user.authentication.model.UserState;
import com.lu.user.authentication.repository.AddressRepository;
import com.lu.user.authentication.repository.UserRepository;
import com.lu.user.authentication.service.UserServiceImpl;
import org.hamcrest.core.StringStartsWith;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;

import static com.lu.user.authentication.model.UserState.ACTIVE;
import static com.lu.user.authentication.model.UserState.LOCKED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserServiceImpl userService;

    @MockBean
    private AddressRepository addressRepository;

    @MockBean
    private UserRepository userRepository;

//    @Test
//    void getUserNameAndPasswordTest() throws Exception {
//        when(userRepository.findByUsername(any())).thenReturn(mockUser());
//
//        Pair<String, String> usernameAndPassword = userService.getUserNameAndPassword("Basic bG9yZW5hLmRhbmNpdUB5YWhvby5jb206dGVzdDEyMzQ=");
//        assertNotNull(usernameAndPassword.getLeft());
//        assertNotNull(usernameAndPassword.getRight());
//        assertThat("lorena.danciu@yahoo.com").isEqualTo(usernameAndPassword.getLeft());
//        assertThat("test1234").isEqualTo(usernameAndPassword.getRight());
//    }

    @Test
    void loginTest_BadCredentials() throws Exception {
        when(userRepository.findByUsername(any())).thenReturn(mockUser());
        try {
            userService.login(mockUser(), "test12345");
        }
        catch (UserAuthenticationException ex) {
            assertThat(ex.getMessage()).startsWith("Authentication failed. Bad credentials.");
        }
    }

    @Test
    void loginTest() throws Exception {
        when(userRepository.findByUsername(any())).thenReturn(mockUser());
        try {
            userService.login(mockUser(), "test1234");
            assert(true);
        } catch (Exception ex) {
           assert(false);
        }
    }

    @Test
    void saveLoginSuccessTest() throws Exception {
        when(userRepository.findByUsername(any())).thenReturn(mockUser());
        ArgumentCaptor<User> argUser = ArgumentCaptor.forClass(User.class);

        userService.saveLoginSuccess("lorena.danciu@yahoo.com");
        Mockito.verify(userRepository).save(argUser.capture());

        User user = argUser.getValue();
        assertNotNull(user.getFailedLogins());
        assertNotNull(user.getUpdateDate());
        assertNotNull(user.getLastLogin());
        assertNotNull(user.getState());
        assertThat(2).isEqualTo(user.getFailedLogins());
        assertThat(ACTIVE).isEqualTo(user.getState());
        assertThat(user.getUpdateDate().getHour()).isEqualTo(OffsetDateTime.now().getHour());
        assertThat(user.getLastLogin().getHour()).isEqualTo(OffsetDateTime.now().getHour());
    }

    @Test
    void saveLoginFailedTest() throws Exception {
        when(userRepository.findByUsername(any())).thenReturn(mockUser());
        ArgumentCaptor<User> argUser = ArgumentCaptor.forClass(User.class);

        userService.saveLoginFailed("lorena.danciu@yahoo.com");
        Mockito.verify(userRepository).save(argUser.capture());

        User user = argUser.getValue();
        assertNotNull(user.getFailedLogins());
        assertNotNull(user.getUpdateDate());
        assertNotNull(user.getState());
        assertThat(3).isEqualTo(user.getFailedLogins());
        assertThat(LOCKED).isEqualTo(user.getState());
        assertThat(user.getUpdateDate().getHour()).isEqualTo(OffsetDateTime.now().getHour());
    }

    private User mockUser() {
        User user = new User();
        user.setUsername("lorena.danciu@yahoo.com");
        user.setPassword("$2a$10$zalfUjwGQMIjIytT89W4EuU1OJA76Iq0bF6F4Hwz/BoLCB2afYtnS");
        user.setState(UserState.ACTIVE);
        user.setFailedLogins(2);

        return user;
    }
}
