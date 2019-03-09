package ch.uzh.ifi.seal.soprafs19.ExceptionHandler;

public class UsernameException extends RuntimeException{

    public UsernameException(String errorMessage){
        super(errorMessage);
    }
}

