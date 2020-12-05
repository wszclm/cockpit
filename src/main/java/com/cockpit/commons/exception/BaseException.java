package com.cockpit.commons.exception;

public class BaseException extends RuntimeException {

    public int status = 400;
    public String code;
    public String reason;
    public String message;
    public String referenceError;

    public BaseException(int httpCode, String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
        this.status = httpCode;
    }

    public BaseException(String message) {
        super(message);
        this.message = message;
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public BaseException(int httpCode, String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
        this.status = httpCode;
    }

    public BaseException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    public BaseException(Throwable cause, int status, String code, String reason, String message, String referenceError) {
        super(message, cause);
        this.status = status;
        this.code = code;
        this.reason = reason;
        this.message = message;
        this.referenceError = referenceError;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReferenceError() {
        return this.referenceError;
    }

    public void setReferenceError(String referenceError) {
        this.referenceError = referenceError;
    }

}
