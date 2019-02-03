/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

@SuppressWarnings({"WeakerAccess"})
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class HttpError {
    public final int status;
    public final String code;
    public final String message;
    public final String debug;

    public HttpError(MyException ex) {
        status = ex.getErrorCode().getValue();
        code = ex.getErrorCode() == null ? "" : ex.getErrorCode().name();
        message = ex.getMessage();
        debug = ex.getCause() == null ? "" : "Caused by " + ex.getCause().getMessage();
    }
}
