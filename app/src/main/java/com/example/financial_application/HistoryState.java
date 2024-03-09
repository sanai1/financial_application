package com.example.financial_application;

public class HistoryState {
    private String date;
    private String name;
    private String summa;
    private boolean is_expense;
    private boolean is_big_purchase;

    public HistoryState(String date, String name, String summa, int is_expense, int is_big_purchase) {
        this.date = date;
        this.name = name;
        this.summa = summa;
        if (is_expense == 1) {
            this.is_expense = true;
        } else {
            this.is_expense = false;
        }
        if (is_big_purchase == 1) {
            this.is_big_purchase = true;
        } else {
            this.is_big_purchase = false;
        }
    }

    public boolean isIs_big_purchase() {
        return is_big_purchase;
    }

    public void setIs_big_purchase(boolean is_big_purchase) {
        this.is_big_purchase = is_big_purchase;
    }

    public boolean isIs_expense() {
        return is_expense;
    }

    public void setIs_expense(boolean is_expense) {
        this.is_expense = is_expense;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSumma(String summa) {
        this.summa = summa;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getSumma() {
        return summa;
    }
}
