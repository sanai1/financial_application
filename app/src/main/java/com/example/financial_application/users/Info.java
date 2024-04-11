package com.example.financial_application.users;

public class Info {
    private String uid;
    private Integer subscription;

    public Info(String uid, Integer subscription) {
        this.uid = uid;
        this.subscription = subscription;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getSubscription() {
        return subscription;
    }

    public void setSubscription(Integer subscription) {
        this.subscription = subscription;
    }
}
