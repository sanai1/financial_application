package com.example.financial_application.adapter_state;

public class HistoryState {
    private String date;
    private String name;
    private String summa;
    private int is_expense;
    private int is_big_purchase;
    private String comment;
    private int[] color = {244, 67, 54};

    public HistoryState(String date, String name, String summa, int is_expense, int is_big_purchase, String comment) {
        this.date = date;
        if (name.length() > 10) {
            this.name = name.substring(0, 9) + "...";
        } else {
            this.name = name;
        }
        this.summa = summa;
        this.is_expense = is_expense;
        this.is_big_purchase = is_big_purchase;
        this.comment = comment;
    }

    public HistoryState(String date, String name, String summa, int is_expense, int is_big_purchase, String comment, int[] color) {
        this.date = date;
        if (name.length() > 10) {
            this.name = name.substring(0, 9) + "...";
        } else {
            this.name = name;
        }
        this.summa = summa;
        this.is_expense = is_expense;
        this.is_big_purchase = is_big_purchase;
        this.comment = comment;
        this.color = color;
    }

    public String getComment() {
        return comment;
    }

    public int[] getColor() {
        return color;
    }

    public int get_is_big_purchase() {
        return is_big_purchase;
    }

    public void set_is_big_purchase(int is_big_purchase) {
        this.is_big_purchase = is_big_purchase;
    }

    public int get_is_expense() {
        return is_expense;
    }

    public void set_is_expense(int is_expense) {
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
