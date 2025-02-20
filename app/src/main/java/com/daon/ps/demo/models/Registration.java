package com.daon.ps.demo.models;

public class Registration {

    public Registration(String href, Application application) {
        this.href = href;
        this.application = application;
    }

    public Registration(String registrationId, User user, Application application){
        this.registrationId = registrationId;
        this.user = user;
        this.application = application;
    }

    private String registrationId;
    private User user;
    private String href;
    private Application application;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }
}
