package com.example.financial_application.authorization;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.financial_application.databinding.AutGoogleBinding;

public class AutGoogleActivity extends AppCompatActivity {
    private AutGoogleBinding binding_aut_google;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding_aut_google = AutGoogleBinding.inflate(getLayoutInflater());
        setContentView(binding_aut_google.getRoot());
    }
}