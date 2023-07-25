package uz.community.javacommunity.common.exception;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException() {
        super("Incorrect username or password");
    }
}

