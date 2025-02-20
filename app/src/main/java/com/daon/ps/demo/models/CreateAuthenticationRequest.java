package com.daon.ps.demo.models;

public class CreateAuthenticationRequest {
    private Application application;
    private Policy policy;
    private User user;
    private String description;
    private String type;

    public CreateAuthenticationRequest(Application application, Policy policy, User user, String description, String type) {
        this.application = application;
        this.policy = policy;
        this.user = user;
        this.description = description;
        this.type = type;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
