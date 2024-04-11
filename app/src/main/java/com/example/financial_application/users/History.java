package com.example.financial_application.users;

public class History {
    private String id;
    private Integer is_big_purchase;
    private Double summa;
    private String add_date;
    private String category_id;
    private String comment;

    public History(String id, Integer is_big_purchase, Double summa, String add_date, String category_id, String comment) {
        this.id = id;
        this.is_big_purchase = is_big_purchase;
        this.summa = summa;
        this.add_date = add_date;
        this.category_id = category_id;
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getIs_big_purchase() {
        return is_big_purchase;
    }

    public void setIs_big_purchase(Integer is_big_purchase) {
        this.is_big_purchase = is_big_purchase;
    }

    public Double getSumma() {
        return summa;
    }

    public void setSumma(Double summa) {
        this.summa = summa;
    }

    public String getAdd_date() {
        return add_date;
    }

    public void setAdd_date(String add_date) {
        this.add_date = add_date;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}