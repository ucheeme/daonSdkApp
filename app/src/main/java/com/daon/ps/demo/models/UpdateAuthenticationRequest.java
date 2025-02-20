package com.daon.ps.demo.models;

public class UpdateAuthenticationRequest {

    private String fidoAuthenticationResponse;

    public UpdateAuthenticationRequest(String fidoAuthenticationResponse) {
        this.fidoAuthenticationResponse = fidoAuthenticationResponse;
    }

    public String getFidoAuthenticationResponse() {
        return fidoAuthenticationResponse;
    }

    public void setFidoAuthenticationResponse(String fidoAuthenticationResponse) {
        this.fidoAuthenticationResponse = fidoAuthenticationResponse;
    }
}
