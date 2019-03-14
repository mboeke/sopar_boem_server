package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.ExceptionHandler.IncorrectPasswordException;
import ch.uzh.ifi.seal.soprafs19.ExceptionHandler.UnauthorizedException;
import ch.uzh.ifi.seal.soprafs19.ExceptionHandler.UnknownUserException;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService service;

    UserController(UserService service) {
        this.service = service;
    }

    UserRepository userRepository;

    @GetMapping("/users")
    Iterable<User> all( //add authorization token in Frontend --> DONE
            @RequestHeader(value = "Authorization",defaultValue = "") String token
    ) {
        if(service.authorized_user(token)){
            return service.getUsers();
        }else{
            throw new UnauthorizedException("User is not authorized to get all users");
        }

    }

    //Login
    @PostMapping("/users/login")
    User loginUser(@RequestBody User userToAuthenticate) throws UnknownUserException, IncorrectPasswordException{
        return this.service.loginUser(userToAuthenticate);
    }

    //Registration
    @PostMapping("/users/register")
    User createUser(@RequestBody User newUser) {
        return this.service.createUser(newUser);
    }

    //Getting user data for user details page
    @GetMapping("/users/{id}") //add authorization token in Frontend --> DONE
    User returnUserData(@PathVariable long id, @RequestHeader(value = "Authorization",defaultValue = "") String token) throws UnknownUserException {
        if(service.authorized_user(token)){
            return this.service.findUserById(id);
        }else{
            throw new UnauthorizedException("User is not authorized to get user data");
        }
    }

    //Update user_data according to input on edit page
    @PutMapping("/users/{id}/edit") //add authorization token in Frontend
    Boolean updateUserData(@RequestBody User newUser, @RequestHeader(value = "Authorization",defaultValue = "") String token) throws UnknownUserException{
        if(service.authorized_user(token)){
            return this.service.updateUserData(newUser);
        }else{
            throw new UnauthorizedException("User is not authorized to update users");
        }

    }
}
