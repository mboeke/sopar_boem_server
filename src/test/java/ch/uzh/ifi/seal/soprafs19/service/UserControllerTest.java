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

    @Test
    public void post_user_for_login() throws Exception{
        User test1 = new User();
        test1.setUsername("first test");
        test1.setPassword("first_pw");
        userService.createUser(test1);
        this.mockmvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"first test\", \"password\": \"first_pw\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.token", notNullValue()))
                .andExpect(jsonPath("$.id", notNullValue())); //jsonPath accessing json
        userService.deleteUser(test1.getId());
    }

    @Test
    public void post_register_User() throws Exception {
        this.mockmvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"first test user\", \"password\": \"first_pw\"}"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.token", notNullValue()));

    }

    @Test
    public void get_all_users_invalid() throws Exception{
        this.mockmvc.perform(get("/users")
                .header("Authorization", "wrong_token"))
                .andExpect(status().isUnauthorized())
                .andExpect(status().is(401));
    }

    @Test
    public void get_all_users_valid() throws Exception{
        User test1 = new User();
        test1.setUsername("test1");
        test1.setPassword("test_password");
        userService.createUser(test1);
        userService.loginUser(test1);
        this.mockmvc.perform(get("/users")
                .header("Authorization", userService.findUserById(test1.getId()).getToken()))
                .andExpect(status().is(200))
                .andExpect(content().contentType("application/json;charset=UTF-8"));
        userService.deleteUser(test1.getId());
    }

    @Test
    public void get_specific_user_valid() throws Exception{
        User test1 = new User();
        test1.setUsername("test1");
        test1.setPassword("test_password");
        userService.createUser(test1);
        userService.loginUser(test1);
        this.mockmvc.perform(get("/users/"+test1.getId())
                .header("Authorization", userService.findUserById(test1.getId()).getToken()))
                .andExpect(status().isOk())
                .andExpect(status().is(200))
                .andExpect(content().contentType("application/json;charset=UTF-8"));
        userService.deleteUser(test1.getId());
    }

    @Test
    public void get_specific_user_invalid() throws Exception{
        this.mockmvc.perform(get("/users/")
                .header("Authorization", "wrong_token"))
                .andExpect(status().isUnauthorized())
                .andExpect(status().is(401));

    }

    @Test
    public void put_update_user_valid() throws Exception{
        User test1 = new User();
        test1.setUsername("test1");
        test1.setPassword("test_password");
        userService.createUser(test1);
        this.mockmvc.perform(put("/users/"+test1.getId()+"/edit")
                .header("Authorization", userService.findUserById(test1.getId()).getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": \""+test1.getId()+"\", \"username\": \"new username\", \"birthday_date\": \"2019-02-14\"}"))
                .andExpect(status().isOk())
                .andExpect(status().is(200))
                .andExpect(content().contentType("application/json;charset=UTF-8"));
        userService.deleteUser(test1.getId());
    }
}
