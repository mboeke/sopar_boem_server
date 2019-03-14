package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.ExceptionHandler.IncorrectPasswordException;
import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ch.uzh.ifi.seal.soprafs19.ExceptionHandler.UsernameException;
import ch.uzh.ifi.seal.soprafs19.ExceptionHandler.UnknownUserException;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Base64;
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

    //get the user's data, create a Base64 encoded token including the user's id and save it to the user's entity
    private String generateToken(User newUser) throws JSONException{
        User temp_user = userRepository.findByUsername(newUser.getUsername());
        String user_id = temp_user.getId().toString();

        //creating JSON representing new token format which then can be encoded --> DOUBLE CHECK IF ALSO WORKS WITH STRINGS ONLY
        JSONObject json = new JSONObject();
        json.put("user_id", user_id);

        //creating and encoding token
        return Base64.getEncoder().encodeToString(json.toString().getBytes());
    }

    //verify that user is authorized to login and authenticate user respectively
    //throw Exception for a) unknown user & b) incorrect password
    public User loginUser(User userToAuthenticate) throws UnknownUserException, IncorrectPasswordException{
        String username = userToAuthenticate.getUsername();
        String password = userToAuthenticate.getPassword();

        //checking if username exists
        if(userRepository.existsByUsername(username)){
            //checking if correct password of username trying to log in
            if(userRepository.findByUsername(username).getPassword().equals(password)){
                //generating encoded token for user trying to login
                userToAuthenticate.setToken(generateToken(userToAuthenticate));
                return userToAuthenticate;
            }else { //throw error as password incorrect
                throw new IncorrectPasswordException("Password incorrect, please double check");
            }
        }else{ //throw error if username doesn't exist
            throw new UnknownUserException("Unknown username, please correct or register");
        }
    }

    //setup a new User entity for the user newUser
    public User createUser(User newUser) {
        if(userRepository.findByUsername(newUser.getUsername()) != null){
            throw new UsernameException("The username is already taken please choose another one");
        }
        newUser.setToken("dummy_token"); //not really needed after registration;

        newUser.setStatus(UserStatus.ONLINE);
        userRepository.save(newUser); //userRepository creates User entity for the first time

        newUser.setToken(generateToken(newUser));
        userRepository.save(newUser); //userRepository saves the new entity items to existing user

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

    //update an existing user entity with provided data
    public Boolean updateUserData (User currentUser){ //throws Exception if user not existing in database

        if(userRepository.existsById(currentUser.getId())){
            User temp_user = getUserById(currentUser.getId());

            temp_user.setUsername(currentUser.getUsername());
            temp_user.setBirthday_date(currentUser.getBirthday_date());
            userRepository.save(temp_user);

            return true;
        }else{
            throw new UnknownUserException("This user doesn't exist and can therefore not be updated");
        }
    }

    //delete an existing user
    public Boolean deleteUser (long id){
        User user_to_be_deleted = userRepository.findById(id);
        if(user_to_be_deleted != null){
            userRepository.delete(user_to_be_deleted);
            return userRepository.existsById(id);
        }else{
            throw new UnknownUserException("This user doesn't exist and can therefore not be deleted");
        }

    }

    public User getUserById(long id) { return userRepository.findById(id);}

    public Boolean authorized_user(String token){
        return userRepository.existsByToken(token);
    }
}
