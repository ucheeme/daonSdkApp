package com.daon.ps.demo.models;

public class UpdateRegistrationChallengeResponse {
    private String id;
    private String fidoRegistrationRequest;
    private String fidoRegistrationResponse;
    private int fidoResponseCode;
    private String fidoResponseMsg;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFidoRegistrationRequest() {
        return fidoRegistrationRequest;
    }

    public void setFidoRegistrationRequest(String fidoRegistrationRequest) {
        this.fidoRegistrationRequest = fidoRegistrationRequest;
    }

    public String getFidoRegistrationResponse() {
        return fidoRegistrationResponse;
    }

    public void setFidoRegistrationResponse(String fidoRegistrationResponse) {
        this.fidoRegistrationResponse = fidoRegistrationResponse;
    }

    public int getFidoResponseCode() {
        return fidoResponseCode;
    }

    public void setFidoResponseCode(int fidoResponseCode) {
        this.fidoResponseCode = fidoResponseCode;
    }

    public String getFidoResponseMsg() {
        return fidoResponseMsg;
    }

    public void setFidoResponseMsg(String fidoResponseMsg) {
        this.fidoResponseMsg = fidoResponseMsg;
    }
}
