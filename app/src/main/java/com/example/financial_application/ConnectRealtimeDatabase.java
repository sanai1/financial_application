package com.example.financial_application;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.financial_application.users.CalculatorInfo;
import com.example.financial_application.users.Capital;
import com.example.financial_application.users.Category;
import com.example.financial_application.users.Goal;
import com.example.financial_application.users.History;
import com.example.financial_application.users.Info;
import com.example.financial_application.users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ConnectRealtimeDatabase {
    private static ConnectRealtimeDatabase INSTANCE;
    private Context context;
    private DatabaseReference root;

    private ConnectRealtimeDatabase(Context context) {
        this.context = context;
        this.root = FirebaseDatabase.getInstance().getReference().getRoot();
    }

    public synchronized static ConnectRealtimeDatabase getInstance(Context context) {
        if (INSTANCE == null) INSTANCE = new ConnectRealtimeDatabase(context);
        return INSTANCE;
    }


    public void saveUser(User user, Info info, Goal goal) {
        root.child(user.getUid()).child("info").setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isComplete())
                    System.out.println("Ошибка при добавлении Info");
            }
        });
        root.child(user.getUid()).child("category").setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isComplete())
                    System.out.println("Ошибка при добавлении Category");
            }
        });
        root.child(user.getUid()).child("capital").setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isComplete())
                    System.out.println("Ошибка при добавлении Capital");
            }
        });
        root.child(user.getUid()).child("goal").setValue(goal).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isComplete())
                    System.out.println("Ошибка при добавлении Goal");
            }
        });
        root.child(user.getUid()).child("history").setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isComplete())
                    System.out.println("Ошибка при добавлении History");
            }
        });
        root.child(user.getUid()).child("calculation_info").setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isComplete())
                    System.out.println("Ошибка при добавлении CalculationInfo");
            }
        });

    }

    public void saveCategory(String uid, Category category) {
        root.child(uid).child("category").child(category.getCategory_id()).setValue(category).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isComplete())
                    System.out.println("Ошибка при добавлении Category");
            }
        });
    }

    public void saveHistory(String uid, History history) {
        root.child(uid).child("history").child(history.getId()).setValue(history).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isComplete())
                    System.out.println("Ошибка при добавлении History");
            }
        });
    }

    public void saveGoal(String uid, Goal goal) {
        root.child(uid).child("goal").setValue(goal).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isComplete())
                    System.out.println("Ошибка при добавлении Goal");
            }
        });
    }

    public void saveCapital(String uid, Capital capital) {
        root.child(uid).child("capital").child(capital.getCapital_id()).setValue(capital).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isComplete())
                    System.out.println("Ошибка при добавлении Capital");
            }
        });
    }

    public void updateCapital(String uid, Double start_sum) {
        // TODO: протестировать на изменении цели
        root.child(uid).child("capital").child("1").child("capital_sum").setValue(start_sum)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isComplete())
                            System.out.println("Ошибка при обновлении Capital");
                    }
                });
    }

    public void saveCalculatorInfo(String uid, CalculatorInfo calculatorInfo) {
        root.child(uid).child("calculator_info").child(calculatorInfo.getId()).setValue(calculatorInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isComplete())
                    System.out.println("Ошибка при добавлении CalculatorInfo");
            }
        });
    }

    public void checkUser(User user, Info info, Goal goal) {
        Query query = root.equalTo(user.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {
                    System.out.println("User found");
                } else {
                    System.out.println("User not found");
                    saveUser(user, info, goal);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
