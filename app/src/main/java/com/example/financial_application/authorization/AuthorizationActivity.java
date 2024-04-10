package com.example.financial_application.authorization;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.financial_application.activity.MainActivity;
import com.example.financial_application.databinding.SelectionAuthorizationBinding;
import com.google.firebase.auth.FirebaseAuth;

public class AuthorizationActivity extends AppCompatActivity {
    private SelectionAuthorizationBinding binding_selection;
    private FirebaseAuth firebaseAuth;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding_selection = SelectionAuthorizationBinding.inflate(getLayoutInflater());
        setContentView(binding_selection.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
    }

    protected void onStart() {
        super.onStart();

        if (firebaseAuth.getCurrentUser() != null) {
            Intent intent = new Intent(AuthorizationActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void btn_aut_email(View view) {
        Intent intent = new Intent(AuthorizationActivity.this, AutEmailActivity.class);
        startActivity(intent);
    }

    public void btn_aut_google(View view) {
        Intent intent = new Intent(AuthorizationActivity.this, AutGoogleActivity.class);
        startActivity(intent);
    }

    public void btn_aut_phone(View view) {
        Toast.makeText(this, "Попробуйте другой способ", Toast.LENGTH_SHORT).show();
    }
}
