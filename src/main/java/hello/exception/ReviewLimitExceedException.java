package hello.exception;

public class ReviewLimitExceedException extends RuntimeException {
    public ReviewLimitExceedException(String message) {
        super(message);
    }
}