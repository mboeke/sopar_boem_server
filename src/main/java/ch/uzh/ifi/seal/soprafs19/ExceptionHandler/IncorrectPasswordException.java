package ch.uzh.ifi.seal.soprafs19.ExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class IncorrectPasswordException extends ResponseStatusException {

    public IncorrectPasswordException(String errorMessage){
        super(HttpStatus.NOT_ACCEPTABLE, errorMessage);
    }
}
