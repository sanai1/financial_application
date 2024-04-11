package com.example.financial_application.users;

public class Category {
    private String category_id;
    private String category_name;
    private Integer expense;

    public Category(String category_id, String category_name, Integer expense) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.expense = expense;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public Integer getExpense() {
        return expense;
    }

    public void setExpense(Integer expense) {
        this.expense = expense;
    }
}