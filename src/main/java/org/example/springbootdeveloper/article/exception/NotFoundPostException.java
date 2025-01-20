package org.example.springbootdeveloper.article.exception;

public class NotFoundPostException extends RuntimeException {
    public NotFoundPostException() {
        super("해당하는 글이 존재하지 않습니다.");
    }

    public NotFoundPostException(String message) {
        super(message);
    }
}
