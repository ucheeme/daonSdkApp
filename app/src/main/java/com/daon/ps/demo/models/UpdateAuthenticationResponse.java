package com.daon.ps.demo.models;

public class UpdateAuthenticationResponse {

    private String fidoAuthenticationResponse;
    private int fidoResponseCode;
    private String id;
    private String fidoResponseMsg;

    public UpdateAuthenticationResponse(String fidoAuthenticationResponse, int fidoResponseCode, String fidoResponseMsg) {
        this.fidoAuthenticationResponse = fidoAuthenticationResponse;
        this.fidoResponseCode = fidoResponseCode;
        this.fidoResponseMsg = fidoResponseMsg;
    }

    public UpdateAuthenticationResponse(String fidoAuthenticationResponse, String authRequestId) {
        this.fidoAuthenticationResponse = fidoAuthenticationResponse;
        this.id = authRequestId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFidoAuthenticationResponse() {
        return fidoAuthenticationResponse;
    }

    public void setFidoAuthenticationResponse(String fidoAuthenticationResponse) {
        this.fidoAuthenticationResponse = fidoAuthenticationResponse;
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
