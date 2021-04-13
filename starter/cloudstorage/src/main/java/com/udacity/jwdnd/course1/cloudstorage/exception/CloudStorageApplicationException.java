package com.udacity.jwdnd.course1.cloudstorage.exception;

import lombok.Getter;

@Getter
public class CloudStorageApplicationException extends Throwable {
    private final String message;
    public CloudStorageApplicationException(String message){
        super(message);
        this.message = message;
    }
}
