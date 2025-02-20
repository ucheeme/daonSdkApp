package com.daon.ps.demo.models;

public class CreateAuthenticationResponse {

    private String id;
    private String fidoAuthenticationRequest;
    private Error error;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFidoAuthenticationRequest() {
        return fidoAuthenticationRequest;
    }

    public void setFidoAuthenticationRequest(String fidoAuthenticationRequest) {
        this.fidoAuthenticationRequest = fidoAuthenticationRequest;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
