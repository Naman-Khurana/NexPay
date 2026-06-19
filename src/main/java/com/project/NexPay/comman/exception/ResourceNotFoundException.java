package com.project.NexPay.comman.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException{

    String resourceName;
    String identifier;

    public ResourceNotFoundException(String message, String resourceName, String identifier) {
        super(message + " not found");
        this.resourceName = resourceName;
        this.identifier = identifier;
    }
}
