package com.example.financial_application;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.financial_application.databinding.ActivityGoalBinding;
import com.google.android.material.navigation.NavigationView;

public class GoalActivity extends AppCompatActivity {
    protected ActivityGoalBinding binding_activity_goal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        binding_activity_goal = ActivityGoalBinding.inflate(getLayoutInflater());
        setContentView(binding_activity_goal.getRoot());

        binding_activity_goal.goalNavigationMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_main) {
                    binding_activity_goal.goalDrawerLayout.close();
                    Toast.makeText(GoalActivity.this, "Главная", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(GoalActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_goal) {
                    binding_activity_goal.goalDrawerLayout.close();
                    Toast.makeText(GoalActivity.this, "Цель", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_history) {
                    binding_activity_goal.goalDrawerLayout.close();
                    Toast.makeText(GoalActivity.this, "История", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(GoalActivity.this, HistoryActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_report) {
                    binding_activity_goal.goalDrawerLayout.close();
                    Toast.makeText(GoalActivity.this, "Отчет", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(GoalActivity.this, ReportActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_categories) {
                    binding_activity_goal.goalDrawerLayout.close();
                    Toast.makeText(GoalActivity.this, "Категории", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(GoalActivity.this, CategoryActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });

    }
    public void onClickButton(View view) {
        System.out.println("Цель");
    }
}
