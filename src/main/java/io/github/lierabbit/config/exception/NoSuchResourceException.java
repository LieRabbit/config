package io.github.lierabbit.config.exception;

/**
 * 找不到资源异常
 *
 * @author xyy
 * @since 2019-03-30 10:07
 */
public class NoSuchResourceException extends RuntimeException {
    public NoSuchResourceException() {
    }

    public NoSuchResourceException(String message) {
        super(message);
    }

    public NoSuchResourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchResourceException(Throwable cause) {
        super(cause);
    }

    public NoSuchResourceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
