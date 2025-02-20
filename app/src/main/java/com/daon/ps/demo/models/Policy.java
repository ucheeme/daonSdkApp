package com.daon.ps.demo.models;

public class Policy {

    public Policy(String href, Application application) {
        this.href = href;
        this.application = application;
    }

    public Policy(String href) {
        this.href = href;
    }

    public Policy(Application application, String policyId) {
        this.application = application;
        this.policyId = policyId;
    }

    private String href;
    private Application application;
    private String policyId;

    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }

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
}
