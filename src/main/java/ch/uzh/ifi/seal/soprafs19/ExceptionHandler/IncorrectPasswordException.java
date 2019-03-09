package ch.uzh.ifi.seal.soprafs19.ExceptionHandler;

public class IncorrectPasswordException extends RuntimeException{

    public IncorrectPasswordException(String errorMessage){
        super(errorMessage);
    }
}
