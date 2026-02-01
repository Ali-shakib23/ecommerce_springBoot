package com.example.demo.exceptions;

public class ResourceNotFoundException extends RuntimeException{

    String resourceName;
    String field;
    String filename;
    Long fieldId;

    public ResourceNotFoundException(String resourceName, String field, String filename) {
        super(String.format("%s not found with %s : %s", resourceName , field , filename));
        this.resourceName = resourceName;
        this.field = field;
        this.filename = filename;
    }

    public ResourceNotFoundException( String resourceName, String field, Long fieldId) {
        super(String.format("%s not found with %s : %s", resourceName , field , fieldId));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldId = fieldId;
    }

    public ResourceNotFoundException() {

    }
}
