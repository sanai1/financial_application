package com.example.financial_application.users;

public class Goal {
    private String name_goal;
    private Double summa_goal;
    private Double start_goal;
    private Double percent;
    private Double inflation;
    private Integer goal_id;

    public Goal(String name_goal, Double summa_goal, Double start_goal, Double percent, Double inflation, Integer goal_id) {
        this.name_goal = name_goal;
        this.summa_goal = summa_goal;
        this.start_goal = start_goal;
        this.percent = percent;
        this.inflation = inflation;
        this.goal_id = goal_id;
    }

    public String getName_goal() {
        return name_goal;
    }

    public void setName_goal(String name_goal) {
        this.name_goal = name_goal;
    }

    public Double getSumma_goal() {
        return summa_goal;
    }

    public void setSumma_goal(Double summa_goal) {
        this.summa_goal = summa_goal;
    }

    public Double getStart_goal() {
        return start_goal;
    }

    public void setStart_goal(Double start_goal) {
        this.start_goal = start_goal;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    public Double getInflation() {
        return inflation;
    }

    public void setInflation(Double inflation) {
        this.inflation = inflation;
    }

    public Integer getGoal_id() {
        return goal_id;
    }

    public void setGoal_id(Integer goal_id) {
        this.goal_id = goal_id;
    }
}
