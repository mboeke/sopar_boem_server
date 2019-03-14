package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.controller.UserController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;

import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import org.springframework.web.server.ResponseStatusException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    //in here test the REST api tests that need to be performed according to the table

    @Autowired
    private MockMvc mockmvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserController userController;

    //add a new user
    public void post_new_user_for_registration() throws Exception {
        //this.mockmvc.perform(post("/users/register").contentType(MediaType.APPLICATION_JSON).content());

    }
    //adding new user failed because username already exists

    //login user successful
    @Test
    public void post_user_for_login() throws Exception{
        User test1 = new User();
        test1.setUsername("first test user");
        test1.setPassword("first_pw");
        userService.createUser(test1);
        this.mockmvc.perform(post("/users/login").contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"first test user\", \"password\": \"first_pw\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.token", notNullValue()))
                .andExpect(jsonPath("$.id", notNullValue())); //jsonPath accessing json
    }

    //login user failed
    @Test
    public void already_existing_username_error(){

    }

    @Test
    public void incorrect_password_error(){

    }

    @Test
    public void get_all_users_valid() throws Exception{

    }

    @Test
    public void get_all_users_invalid() throws Exception{

    }

    //display user profile with userId by GETting user object
    //displaying user profile failed because userId was not found


    //update user profile with new values for username and birthday_date
    //updating user profile failed because userId of new values for username and birthday_date were not found

    //exceptions rather to be tested in controllertest:

}
