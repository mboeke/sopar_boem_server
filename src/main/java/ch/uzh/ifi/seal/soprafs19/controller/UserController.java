package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.ExceptionHandler.IncorrectPasswordException;
import ch.uzh.ifi.seal.soprafs19.ExceptionHandler.UnknownUserException;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService service;

    UserController(UserService service) {
        this.service = service;
    }

    UserRepository userRepository;

    @GetMapping("/users")
    Iterable<User> all() {
        return service.getUsers();
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
    @GetMapping("/users/{id}") //getting value from url
    User returnUserData(@PathVariable(value="id") long id) throws UnknownUserException {
        return this.service.findUserById(id);
    }

    //Update user_data according to input on edit page
    @PutMapping("/users/{id}/edit")
    Boolean updateUserData(@RequestBody User newUser) throws UnknownUserException{
        return this.service.updateUserData(newUser);
    }
}
