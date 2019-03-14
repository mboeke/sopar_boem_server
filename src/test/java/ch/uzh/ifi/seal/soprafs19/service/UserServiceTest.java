package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.List;


/**
 * Test class for the UserResource REST resource.
 *
 * @see UserService
 */
@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
public class UserServiceTest {

    //test in here Unit tests, meaning to test every function/method or class that is used within the Backend

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private User test_user_1;
    private User test_user_2;

    @Before
    public void setup_test_users(){
        this.test_user_1 = new User();
        this.test_user_1.setUsername("first test user");
        this.test_user_1.setPassword("first_pw");

        this.test_user_2 = new User();
        this.test_user_2.setUsername("second test user");
        this.test_user_2.setPassword("second_pw");
    }

    //add a new user --> think what shall a new user include: shall exist, have a username, password, status, token and shall be returned after creation
    @Test
    public void createUser() {
        //assure users don't exist yet
        Assert.assertNull(userRepository.findByUsername(this.test_user_1.getUsername()));
        Assert.assertNull(userRepository.findByUsername(this.test_user_2.getUsername()));

        //create user entity as would register() with a username and password
        User test1 = userService.createUser(this.test_user_1);

        //new test1 user has a username, password, id, token and status
        Assert.assertNotNull("Username is null",test1.getUsername());
        Assert.assertNotNull("PW is null",test1.getPassword());
        Assert.assertNotNull("ID is null", test1.getId());
        Assert.assertNotNull("Token is null", test1.getToken());
        Assert.assertNotNull("Status not online", test1.getStatus());
        Assert.assertNotNull("Creation date is null", test1.getCreation_date());
        Assert.assertNull("Birthday date has value", test1.getBirthday_date());

        //check if user entity correctly safed to userRepository
        Assert.assertEquals(test1, userRepository.findByUsername(test1.getUsername()));
        Assert.assertEquals(test1, userRepository.findByToken(test1.getToken()));

        //delete test users
        userService.deleteUser(test1.getId());
    }

    @Test
    public void getUserData(){
        Assert.assertNull(userRepository.findByUsername(this.test_user_1.getUsername()));

        //create multiple users that shall be fetched
        User test1 = userService.createUser(this.test_user_1);

        Assert.assertNotNull("UserId is null",userService.getUserById(test1.getId()));
        Assert.assertEquals(test1, userService.getUserById(test1.getId()));

        userService.deleteUser(test1.getId());
    }

    @Test
    public void get_all_test_users(){
        Assert.assertNull(userRepository.findByUsername(this.test_user_1.getUsername()));
        Assert.assertNull(userRepository.findByUsername(this.test_user_2.getUsername()));

        //create test users
        User test1 = userService.createUser(this.test_user_1);
        User test2 = userService.createUser(this.test_user_2);

        Iterable<User> test_list = userService.getUsers();
        Iterable<User> repo_test_list = userRepository.findAll();

        //assure that both lists contain the same users which implies they have been created correctly within userRepository & getUsers() returns correctly list of users
        Assert.assertEquals(test_list, repo_test_list);

        userService.deleteUser(test1.getId());
        userService.deleteUser(test2.getId());
    }

    @Test
    public void generate_token_for_user(){
        //create test user
        User test1 = userService.createUser(this.test_user_1);

        //generate token for test1
        User temp_user = userRepository.findByUsername(test1.getUsername());
        JSONObject json = new JSONObject();
        json.put("user_id", temp_user.getId().toString());
        String token = Base64.getEncoder().encodeToString(json.toString().getBytes());

        //check if token generated in test equals token generated by generateToken()
        Assert.assertEquals(test1.getToken(), token);

        //delete user
        userService.deleteUser(test1.getId());
    }

    @Test
    public void authenticate_users_for_login(){
        //create test user
        User test1 = userService.createUser(this.test_user_1);

        Assert.assertNotNull("Username is null", test1.getUsername());
        Assert.assertNotNull("Password is null", test1.getPassword());

        //check if username exists and password equal
        Assert.assertTrue(userRepository.existsByUsername(test1.getUsername()));
        Assert.assertEquals(userRepository.findByUsername(test1.getUsername()).getPassword(),test1.getPassword());

        //delete user
        userService.deleteUser(test1.getId());
    }

    //update user profile with new values for username and birthday_date
    //updating user profile failed because userId of new values for username and birthday_date were not found
    @Test
    public void updateUserData() {
        //create test user
        User test1 = userService.createUser(this.test_user_1);

        //check state before updating user profile
        Assert.assertNotNull(test1.getUsername());
        Assert.assertEquals("first test user", test1.getUsername());
        Assert.assertNull(test1.getBirthday_date());
        Assert.assertEquals("first_pw", test1.getPassword());

        //update user profile
        User temp_user = userService.getUserById(test1.getId());
        temp_user.setUsername("HUHU");
        temp_user.setBirthday_date("2019-01-01");
        userRepository.save(temp_user);
        Assert.assertTrue("Not correctly updated", userService.updateUserData(temp_user));

        //check state after updating user profile
        Assert.assertNotNull(test1.getUsername());
        Assert.assertEquals(temp_user.getUsername(), userService.findUserById(test1.getId()).getUsername());
        Assert.assertEquals(temp_user.getBirthday_date(), userService.findUserById(test1.getId()).getBirthday_date());

        //delete user
        userService.deleteUser(test1.getId());
    }

    @Test
    public void deleting_a_user(){
        //create test user
        User test1 = userService.createUser(this.test_user_1);

        Assert.assertNotNull("Username is null",test1.getUsername());

        userService.deleteUser(test1.getId());
        System.out.println(test1.getUsername());
        Assert.assertFalse(userRepository.existsById(test1.getId()));
    }
}
