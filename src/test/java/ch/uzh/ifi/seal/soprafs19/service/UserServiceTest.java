package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.entity.UserLogin;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class UserServiceTest {


    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    @Test
    public void createUser() {
        userRepository.deleteAll();
        Assert.assertNull(userRepository.findByUsername("testUsername"));

        User testUser = new User();
        testUser.setPassword("testPassword");
        testUser.setUsername("testUsername");

        User createdUser = userService.createUser(testUser);

        Assert.assertNotNull(createdUser.getToken());
        Assert.assertEquals(createdUser, userRepository.findByToken(createdUser.getToken()));
    }

    @Test
    public void verifyUser() {
        User testUser2 = new User();
        testUser2.setPassword("testPassword");
        testUser2.setUsername("testUsername");
        User createdUser2 = userService.createUser(testUser2);
        UserLogin testLogin = new UserLogin();
        testLogin.setPassword(testUser2.getPassword());
        testLogin.setUsername(testUser2.getUsername());
        Assert.assertEquals(createdUser2.getToken(), userService.verifyUser(testLogin).getToken());
    }

    @Test
    public void getUser() {
        User testUser3 = new User();
        testUser3.setPassword("testPassword3");
        testUser3.setUsername("testUsername3");
        User createdUser3 = userService.createUser(testUser3);
        long id = createdUser3.getId();
        Assert.assertEquals(createdUser3.getToken(), userRepository.findById(id).getToken());
    }
}
