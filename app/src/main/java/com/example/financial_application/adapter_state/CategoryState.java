package com.example.financial_application.adapter_state;

public class CategoryState {
    private String uid;
    private String name;
    private int is_expense;

    public CategoryState(String uid, String name, int is_expense) {
        this.uid = uid;
        this.name = name;
        this.is_expense = is_expense;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getIs_expense() {
        return is_expense;
    }

    public void setIs_expense(int is_expense) {
        this.is_expense = is_expense;
    }

    public String getUid() {
        return uid;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
