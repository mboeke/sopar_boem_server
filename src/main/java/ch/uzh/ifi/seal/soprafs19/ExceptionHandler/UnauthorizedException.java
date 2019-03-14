package ch.uzh.ifi.seal.soprafs19.ExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UnauthorizedException extends ResponseStatusException {

    public UnauthorizedException(String errorMessage){
        super(HttpStatus.UNAUTHORIZED, errorMessage);
    }
}
