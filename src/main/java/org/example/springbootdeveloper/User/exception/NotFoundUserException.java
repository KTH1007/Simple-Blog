package org.example.springbootdeveloper.User.exception;

public class NotFoundUserException extends RuntimeException {
    public NotFoundUserException() {
        super("해당하는 사용자가 존재하지 않습니다.");
    }

    public NotFoundUserException(String message) {
        super(message);
    }
}
