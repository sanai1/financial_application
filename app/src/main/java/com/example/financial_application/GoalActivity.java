package com.example.financial_application;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.financial_application.databinding.ActivityGoalBinding;

public class GoalActivity extends AppCompatActivity {
    protected ActivityGoalBinding binding_activity_goal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        binding_activity_goal = ActivityGoalBinding.inflate(getLayoutInflater());
        setContentView(binding_activity_goal.getRoot());
    }
    public void onClickButton(View view) {
        System.out.println("------------------------------------------");
    }
}
