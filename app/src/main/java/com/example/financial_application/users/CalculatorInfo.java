package com.example.financial_application.users;

public class CalculatorInfo {
    private String id;
    private String date_calculation;
    private Integer temp_goal;
    private String date_finish;
    private String percent_date;

    public CalculatorInfo(String id, String date_calculation, Integer temp_goal, String date_finish, String percent_date) {
        this.id = id;
        this.date_calculation = date_calculation;
        this.temp_goal = temp_goal;
        this.date_finish = date_finish;
        this.percent_date = percent_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate_calculation() {
        return date_calculation;
    }

    public void setDate_calculation(String date_calculation) {
        this.date_calculation = date_calculation;
    }

    public Integer getTemp_goal() {
        return temp_goal;
    }

    public void setTemp_goal(Integer temp_goal) {
        this.temp_goal = temp_goal;
    }

    public String getDate_finish() {
        return date_finish;
    }

    public void setDate_finish(String date_finish) {
        this.date_finish = date_finish;
    }

    public String getPercent_date() {
        return percent_date;
    }

    public void setPercent_date(String percent_date) {
        this.percent_date = percent_date;
    }
}
