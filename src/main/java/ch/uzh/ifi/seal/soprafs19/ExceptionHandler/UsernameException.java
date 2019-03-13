package ch.uzh.ifi.seal.soprafs19.ExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UsernameException extends ResponseStatusException {

    public UsernameException(String errorMessage){
        super(HttpStatus.CONFLICT, errorMessage);
    }
}

