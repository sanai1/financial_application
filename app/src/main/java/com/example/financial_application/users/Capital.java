package com.example.financial_application.users;

public class Capital {
    private String month;
    private Double capital_sum;
    private String capital_id;

    public Capital(String month, Double capital_sum, String capital_id) {
        this.month = month;
        this.capital_sum = capital_sum;
        this.capital_id = capital_id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Double getCapital_sum() {
        return capital_sum;
    }

    public void setCapital_sum(Double capital_sum) {
        this.capital_sum = capital_sum;
    }

    public String getCapital_id() {
        return capital_id;
    }

    public void setCapital_id(String capital_id) {
        this.capital_id = capital_id;
    }
}