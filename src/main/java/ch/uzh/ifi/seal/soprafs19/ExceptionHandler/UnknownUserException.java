package ch.uzh.ifi.seal.soprafs19.ExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UnknownUserException extends ResponseStatusException {

    public UnknownUserException(String errorMessage){
        super(HttpStatus.NOT_FOUND, errorMessage);
    }
}
