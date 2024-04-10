package com.example.financial_application.authorization;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.financial_application.R;
import com.example.financial_application.activity.MainActivity;
import com.example.financial_application.databinding.AutEmailBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AutEmailActivity extends AppCompatActivity {
    private AutEmailBinding binding_start;
    private FirebaseAuth firebaseAuth;
    private static final String TAG = "AutEmailActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding_start = AutEmailBinding.inflate(getLayoutInflater());
        setContentView(binding_start.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();

    }

    protected void onStart() {
        super.onStart();

        if (firebaseAuth.getCurrentUser() != null)
            goToMainActivity();
    }

    public void not_registered_yet(View view) {
        int visible = binding_start.editTextTextPasswordDouble.getVisibility();
        if (visible == View.VISIBLE) {
            binding_start.authorization.setText(R.string.Authorization);
            binding_start.editTextTextPassword.setHint(R.string.enter_the_password);
            binding_start.editTextTextPasswordDouble.setVisibility(View.INVISIBLE);
            binding_start.logIn.setText("войти");
            binding_start.textViewRegistration.setText(R.string.registration);
        } else {
            binding_start.authorization.setText("Регистрация");
            binding_start.editTextTextPassword.setHint("Придумайте пароль");
            binding_start.editTextTextPasswordDouble.setVisibility(View.VISIBLE);
            binding_start.logIn.setText(R.string.start_registration);
            binding_start.textViewRegistration.setText(R.string.entrance);
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
        Pair<String, String> info = getUserInfo();
        if (info == null) return;

        System.out.println("log_in");
        // TODO: реализовать логику входа в учетную запись, если такая существует, если пароль подходит

        firebaseAuth.signInWithEmailAndPassword(info.first, info.second)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Пользователь авторизован
                            Log.d(TAG, "signInWithEmail:success");
                            goToMainActivity();
                        } else {
                            // Произошла ошибка при авторизации пользователя
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(AutEmailActivity.this, "Проверьте введенные данные",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void registration() {
        Pair<String, String> info = getUserInfo();
        if (info == null) return;
        String passwordDouble = binding_start.editTextTextPasswordDouble.getText().toString();

        if (passwordDouble.isEmpty()) {
            Toast.makeText(this, R.string.check_info, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!info.second.equals(passwordDouble)) {
            Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
            return;
        }

        System.out.println("registration");
        // TODO: реализовать логику создания учетной записи
        System.out.println(info.first + " --> " + info.second);

        firebaseAuth.createUserWithEmailAndPassword(info.first, info.second)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Пользователь успешно создан
                            System.out.println("=========================================");
                            Log.d(TAG, "createUserWithEmail:success");
                            System.out.println("----------------------------------------------------");
                            goToMainActivity();
                        } else {
                            // Произошла ошибка при создании пользователя
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(AutEmailActivity.this, "Проверьте введенные данные",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private Pair<String, String> getUserInfo() {
        String login = binding_start.editTextTextEmailAddress.getText().toString();
        String password = binding_start.editTextTextPassword.getText().toString();
        if (login.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, R.string.check_info, Toast.LENGTH_SHORT).show();
            return null;
        }
        return Pair.create(login, password);
    }

    private void goToMainActivity() {
        Intent intent = new Intent(AutEmailActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
