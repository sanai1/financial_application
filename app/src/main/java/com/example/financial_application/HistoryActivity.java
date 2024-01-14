package com.example.financial_application;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.example.financial_application.databinding.ActivityHistoryBinding;
import com.google.android.material.navigation.NavigationView;

public class HistoryActivity extends AppCompatActivity {
    protected ActivityHistoryBinding binding_activity_history;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        binding_activity_history = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding_activity_history.getRoot());

        binding_activity_history.historyNavigationMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_main) {
                    binding_activity_history.drawerLayoutId.close();
                    Toast.makeText(HistoryActivity.this, "Главная", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_goal){
                    binding_activity_history.drawerLayoutId.close();
                    Toast.makeText(HistoryActivity.this, "Цель", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HistoryActivity.this, GoalActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_history) {
                    binding_activity_history.drawerLayoutId.close();
                    Toast.makeText(HistoryActivity.this, "История", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_report) {
                    binding_activity_history.drawerLayoutId.close();
                    Toast.makeText(HistoryActivity.this, "Отчет", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HistoryActivity.this, ReportActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_categories) {
                    binding_activity_history.drawerLayoutId.close();
                    Toast.makeText(HistoryActivity.this, "Категории", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HistoryActivity.this, CategoryActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }

    public void onClickHistory(View view) {
        System.out.println("История");
    }

    public void menu(View view) {
        binding_activity_history.drawerLayoutId.openDrawer(GravityCompat.START);
        Toast.makeText(this, "Меню", Toast.LENGTH_SHORT).show();
    }
}
