package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
public class UserControllerTest {

    //in here test the REST api tests that need to be performed according to the table

    //add a new user
    //adding new user failed because username already exists


    //display user profile with userId by GETting user object
    //displaying user profile failed because userId was not found


    //update user profile with new values for username and birthday_date
    //updating user profile failed because userId of new values for username and birthday_date were not found

    //exceptions rather to be tested in controllertest:

    @Test
    public void already_existing_username_error(){

    }

    @Test
    public void incorrect_password_error(){

    }

}
