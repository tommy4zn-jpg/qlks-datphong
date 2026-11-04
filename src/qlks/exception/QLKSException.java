package qlks.exception;

public class QLKSException extends Exception {
    public QLKSException(String message) {
        super(message);
    }

    public QLKSException(String message, Throwable cause) {
        super(message, cause);
    }
}
