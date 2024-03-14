package com.example.financial_application;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.financial_application.databinding.ActivityFeedbackBinding;

public class FeedbackActivity extends AppCompatActivity {
    private ActivityFeedbackBinding binding_activity_feedback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding_activity_feedback = ActivityFeedbackBinding.inflate(getLayoutInflater());
        setContentView(binding_activity_feedback.getRoot());

    }
}
