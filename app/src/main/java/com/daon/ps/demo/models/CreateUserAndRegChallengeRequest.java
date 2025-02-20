package com.daon.ps.demo.models;

public class CreateUserAndRegChallengeRequest {

    private String serverData;
    private Registration registration;
    private Policy policy;

    public CreateUserAndRegChallengeRequest(String serverData, Registration registration, Policy policy) {
        this.serverData = serverData;
        this.registration = registration;
        this.policy = policy;
    }

    public String getServerData() {
        return serverData;
    }

    public void setServerData(String serverData) {
        this.serverData = serverData;
    }

    public Registration getRegistration() {
        return registration;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }
}
