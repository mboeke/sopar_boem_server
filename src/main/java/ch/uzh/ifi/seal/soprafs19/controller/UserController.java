package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.ExceptionHandler.IncorrectPasswordException;
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

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users")
    Iterable<User> all() {
        return service.getUsers();
    }

    //Login
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping("/users/login")
    User loginUser(@RequestBody User userToAuthenticate) throws UnknownUserException, IncorrectPasswordException{
        return this.service.loginUser(userToAuthenticate);
    }

    //Registration
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/users/register")
    User createUser(@RequestBody User newUser) {
        return this.service.createUser(newUser);
    }

    //Getting user data for user details page
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/{id}") //getting value from url
    User returnUserData(@PathVariable(value="id") long id) throws UnknownUserException {
        return this.service.findUserById(id);
    }

    //Update user_data according to input on edit page
    //@ResponseStatus(HttpStatus.NO_CONTENT) --> causing error response which is being catch by frontend function update_users_data()
    //@ResponseStatus(HttpStatus.NOT_FOUND)
    @PutMapping("/users/{id}/edit")
    Boolean updateUserData(@RequestBody User newUser) throws UnknownUserException{
        return this.service.updateUserData(newUser);
    }
}
