package com.daon.ps.demo.models;

public class CreateUserAndRegChallengeResponse {

    private String href;
    private String id;
    private String fidoRegistrationRequest;
    private String status;
    private Registration registration;


    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Registration getRegistration() {
        return registration;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

}
