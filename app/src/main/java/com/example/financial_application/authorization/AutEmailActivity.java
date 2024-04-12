package com.example.financial_application.authorization;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.financial_application.ConnectRealtimeDatabase;
import com.example.financial_application.R;
import com.example.financial_application.activity.MainActivity;
import com.example.financial_application.databinding.AutEmailBinding;
import com.example.financial_application.users.Goal;
import com.example.financial_application.users.Info;
import com.example.financial_application.users.User;
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
            goToMainActivity(false, firebaseAuth.getCurrentUser().getUid());
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

        firebaseAuth.signInWithEmailAndPassword(info.first, info.second)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Пользователь авторизован
                            Log.d(TAG, "signInWithEmail:success");
                            goToMainActivity(false, firebaseAuth.getCurrentUser().getUid());
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
            Toast.makeText(this, "После пароль не может быть пустым", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!info.second.equals(passwordDouble)) {
            Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(info.first, info.second)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Пользователь успешно создан
                            Log.d(TAG, "createUserWithEmail:success");
                            goToMainActivity(true, firebaseAuth.getCurrentUser().getUid());
                        } else {
                            // Произошла ошибка при создании пользователя
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(AutEmailActivity.this, "Похоже вы уже зарегистрированны\nпопробуйте войти",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private Pair<String, String> getUserInfo() {
        String login = binding_start.editTextTextEmailAddress.getText().toString();
        String password = binding_start.editTextTextPassword.getText().toString();
        if (login.isEmpty()) {
            Toast.makeText(this, "Поле email не может быть пустым", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "Поле пароль не может быть пустым", Toast.LENGTH_SHORT).show();
            return null;
        }
        return Pair.create(login, password);
    }

    private void goToMainActivity(boolean is_reg, String uid) {
        if (is_reg) {
            User user = new User(uid);
            Info info = new Info(uid, 0);
            Goal goal = new Goal("", 0.0, 0.0, 0.0, 0.0, 1);

            ConnectRealtimeDatabase.getInstance(this).saveUser(user, info, goal);
        }
        Intent intent = new Intent(AutEmailActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
