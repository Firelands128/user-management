/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value()),
    FORBIDDEN(HttpStatus.FORBIDDEN.value()),
    NOT_FOUND(HttpStatus.NOT_FOUND.value()),
    CONFLICT(HttpStatus.CONFLICT.value()),
    UNPROCESSABLE_ENTITY(HttpStatus.UNPROCESSABLE_ENTITY.value()),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value());

    private final int value;

    ErrorCode(int value) {
        this.value = value;
    }

    public static ErrorCode getErrorCode(int value) {
        for (ErrorCode errorCode : values()) {
            if (errorCode.value == value) {
                return errorCode;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    String getMessage() {
        switch (this) {
            case BAD_REQUEST:
                return "Bad Request.";
            case FORBIDDEN:
                return "Forbidden.";
            case NOT_FOUND:
                return "Not Found.";
            case CONFLICT:
                return "Conflict.";
            case UNPROCESSABLE_ENTITY:
                return "Unprocessable entity";
            case INTERNAL_SERVER_ERROR:
                return "Internal Server Error";
        }
        return null;
    }
}