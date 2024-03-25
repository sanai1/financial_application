package com.example.financial_application.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.financial_application.R;
import com.example.financial_application.databinding.ActivityStartAutBinding;

public class StartAutActivity extends AppCompatActivity {
    protected ActivityStartAutBinding binding_start;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding_start = ActivityStartAutBinding.inflate(getLayoutInflater());
        setContentView(binding_start.getRoot());
    }

    public void not_registered_yet(View view) {
        int visible = binding_start.editTextTextPasswordDouble.getVisibility();
        if (visible == View.VISIBLE) {
            binding_start.authorization.setText("Авторизация");
            binding_start.editTextTextPassword.setHint("Введите пароль");
            binding_start.editTextTextPasswordDouble.setVisibility(View.INVISIBLE);
            binding_start.logIn.setText("войти");
            binding_start.textViewRegistration.setText(R.string.registration);
        } else {
            binding_start.authorization.setText("Регистрация");
            binding_start.editTextTextPassword.setHint("Придумайте пароль");
            binding_start.editTextTextPasswordDouble.setVisibility(View.VISIBLE);
            binding_start.logIn.setText(R.string.start_registration);
            binding_start.textViewRegistration.setText("вход");
        }
    }

    public void log_reg(View view) {
        int visibility = binding_start.editTextTextPasswordDouble.getVisibility();
        if (visibility == View.VISIBLE) {
            registration();
        } else {
            log_in();
        }
    }

    private void log_in() {
        String login, password;
        login = binding_start.editTextTextEmailAddress.getText().toString();
        password = binding_start.editTextTextPassword.getText().toString();
        if (!login.isEmpty() && !password.isEmpty()) {
            System.out.println("log_in");
            // TODO: реализовать логику входа в учетную запись, если такая существует, если пароль подходит
        } else {
            Toast.makeText(this, R.string.check_info, Toast.LENGTH_SHORT).show();
        }
    }
    private void registration() {
        String login, password, passwordDouble;
        login = binding_start.editTextTextEmailAddress.getText().toString();
        password = binding_start.editTextTextPassword.getText().toString();
        passwordDouble = binding_start.editTextTextPasswordDouble.getText().toString();
        if (!login.isEmpty() && !password.isEmpty() && !passwordDouble.isEmpty()) {
            if (password.equals(passwordDouble)) {
                System.out.println("registration");
                // TODO: реализовать логику создания цчетной записи
            } else {
                Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.check_info, Toast.LENGTH_SHORT).show();
        }
    }
}
