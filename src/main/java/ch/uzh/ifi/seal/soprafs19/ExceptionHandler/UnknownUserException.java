package ch.uzh.ifi.seal.soprafs19.ExceptionHandler;

public class UnknownUserException extends RuntimeException{

    public UnknownUserException(String errorMessage){
        super(errorMessage);
    }

}
