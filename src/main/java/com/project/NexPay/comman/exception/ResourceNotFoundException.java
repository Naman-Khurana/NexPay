package com.project.NexPay.comman.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException{

    String resourceName;
    Object identifier;

    public ResourceNotFoundException(String resourceName, Object identifier) {
        super(resourceName  + " not found: " + identifier);
        this.resourceName = resourceName;
        this.identifier = identifier;
    }
}
