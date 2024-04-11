package com.example.financial_application.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.example.financial_application.R;
import com.example.financial_application.authorization.AuthorizationActivity;
import com.example.financial_application.databinding.ActivityReportBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class ReportActivity extends AppCompatActivity {
    ActivityReportBinding binding_activity_report;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        binding_activity_report = ActivityReportBinding.inflate(getLayoutInflater());
        setContentView(binding_activity_report.getRoot());

        binding_activity_report.includeMenu.textViewInfo.setText("Отчет");

        binding_activity_report.reportNavigationMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_main) {
                    binding_activity_report.drawerLayoutId.close();
                    Intent intent = new Intent(ReportActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_goal){
                    binding_activity_report.drawerLayoutId.close();
                    Intent intent = new Intent(ReportActivity.this, GoalActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_history) {
                    binding_activity_report.drawerLayoutId.close();
                    Intent intent = new Intent(ReportActivity.this, HistoryActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_report) {
                    binding_activity_report.drawerLayoutId.close();
                } else if (id == R.id.nav_categories) {
                    binding_activity_report.drawerLayoutId.close();
                    Intent intent = new Intent(ReportActivity.this, CategoryActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_feedback) {
                    binding_activity_report.drawerLayoutId.close();
                    Intent intent = new Intent(ReportActivity.this, FeedbackActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_exit) {
                    binding_activity_report.drawerLayoutId.close();
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(ReportActivity.this, AuthorizationActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });
    }

    public void menu(View view) {
        binding_activity_report.drawerLayoutId.openDrawer(GravityCompat.START);
    }
}
