package com.example.financial_application.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.example.financial_application.R;
import com.example.financial_application.authorization.AuthorizationActivity;
import com.example.financial_application.databinding.ActivityFeedbackBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class FeedbackActivity extends AppCompatActivity {
    private ActivityFeedbackBinding binding_activity_feedback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding_activity_feedback = ActivityFeedbackBinding.inflate(getLayoutInflater());
        setContentView(binding_activity_feedback.getRoot());

        binding_activity_feedback.includeMenu.textViewInfo.setText("Обратная связь");

        binding_activity_feedback.reportNavigationMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_main) {
                    binding_activity_feedback.drawerLayout.close();
                    Intent intent = new Intent(FeedbackActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_goal){
                    binding_activity_feedback.drawerLayout.close();
                    Intent intent = new Intent(FeedbackActivity.this, GoalActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_history) {
                    binding_activity_feedback.drawerLayout.close();
                    Intent intent = new Intent(FeedbackActivity.this, HistoryActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_report) {
                    binding_activity_feedback.drawerLayout.close();
                    Intent intent = new Intent(FeedbackActivity.this, ReportActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_categories) {
                    binding_activity_feedback.drawerLayout.close();
                    Intent intent = new Intent(FeedbackActivity.this, CategoryActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_feedback) {
                    binding_activity_feedback.drawerLayout.close();
                } else if (id == R.id.nav_exit) {
                    binding_activity_feedback.drawerLayout.close();
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(FeedbackActivity.this, AuthorizationActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            }
        });
    }

    public void menu(View view) {
        binding_activity_feedback.drawerLayout.openDrawer(GravityCompat.START);
    }

    public void btn_telegram(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/f_a_support_bot"));
        startActivity(intent);
    }
}
