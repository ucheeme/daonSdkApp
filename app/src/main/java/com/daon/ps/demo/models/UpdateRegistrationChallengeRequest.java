package com.daon.ps.demo.models;

public class UpdateRegistrationChallengeRequest {
    private String fidoRegistrationResponse;

    public UpdateRegistrationChallengeRequest(String fidoRegistrationResponse) {
        this.fidoRegistrationResponse = fidoRegistrationResponse;
    }

    public String getFidoRegistrationResponse() {
        return fidoRegistrationResponse;
    }

    public void setFidoRegistrationResponse(String fidoRegistrationResponse) {
        this.fidoRegistrationResponse = fidoRegistrationResponse;
    }
}
