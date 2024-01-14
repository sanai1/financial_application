package com.example.financial_application;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.example.financial_application.databinding.ActivityCategoryBinding;
import com.google.android.material.navigation.NavigationView;

public class CategoryActivity extends AppCompatActivity {
    protected ActivityCategoryBinding binding_activity_category;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        binding_activity_category = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding_activity_category.getRoot());

        binding_activity_category.categoryNavigationMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_main) {
                    binding_activity_category.drawerLayoutId.close();
                    Toast.makeText(CategoryActivity.this, "Главная", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CategoryActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_goal){
                    binding_activity_category.drawerLayoutId.close();
                    Toast.makeText(CategoryActivity.this, "Цель", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CategoryActivity.this, GoalActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_history) {
                    binding_activity_category.drawerLayoutId.close();
                    Toast.makeText(CategoryActivity.this, "История", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CategoryActivity.this, HistoryActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_report) {
                    binding_activity_category.drawerLayoutId.close();
                    Toast.makeText(CategoryActivity.this, "Отчет", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CategoryActivity.this, ReportActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_categories) {
                    binding_activity_category.drawerLayoutId.close();
                    Toast.makeText(CategoryActivity.this, "Категории", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    public void onClickCategory(View view) {
        System.out.println("Категории");
    }

    public void menu(View view) {
        binding_activity_category.drawerLayoutId.openDrawer(GravityCompat.START);
        Toast.makeText(this, "Меню", Toast.LENGTH_SHORT).show();
    }
}
