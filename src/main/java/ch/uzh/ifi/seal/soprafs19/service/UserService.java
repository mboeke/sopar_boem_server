package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.ExceptionHandler.IncorrectPasswordException;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ch.uzh.ifi.seal.soprafs19.ExceptionHandler.UsernameException;
import ch.uzh.ifi.seal.soprafs19.ExceptionHandler.UnknownUserException;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.UUID;

@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Iterable<User> getUsers() {
        return this.userRepository.findAll();
    }

    public User loginUser(User userToAuthenticate) throws UnknownUserException, IncorrectPasswordException{ //throw Exception for a) unknown user & b) incorrect password
        String username = userToAuthenticate.getUsername();
        String password = userToAuthenticate.getPassword();

        //checking if username exists
        if(userRepository.existsByUsername(username)){
            if(userRepository.findByUsername(username).getPassword().equals(password)){
                userToAuthenticate.setToken(UUID.randomUUID().toString());
                return userToAuthenticate;
            }else { //throw error as password incorrect
                throw new IncorrectPasswordException("Password incorrect, please double check");
            }
        }else{ //throw error if username doesn't exist
            throw new UnknownUserException("Unknown username, please correct or register");
        }
    }

    public User createUser(User newUser) {
        if(userRepository.findByUsername(newUser.getUsername()) != null){
            throw new UsernameException("The username is already taken please choose another one");
        }
        newUser.setToken(UUID.randomUUID().toString()); //not really needed after registration;
        newUser.setStatus(UserStatus.ONLINE);
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User findUserById (long id){
        //check if user id exists if not throw error
        if(userRepository.existsById(id)){
            return this.userRepository.findById(id);
        }else{
            throw new UnknownUserException("This user doesn't exist in our database");
        }
    }

    public Boolean existUser(long id){
        return userRepository.existsById(id);
    }

    public User existUsername(String username){
        return userRepository.findByUsername(username);
    }
}
