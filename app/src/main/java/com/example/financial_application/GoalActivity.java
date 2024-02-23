package com.example.financial_application;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.example.financial_application.databinding.ActivityGoalBinding;
import com.example.financial_application.databinding.ActivityGoalStartBinding;
import com.google.android.material.navigation.NavigationView;

public class GoalActivity extends AppCompatActivity {
    protected ActivityGoalStartBinding binding_activity_goal_start;
    protected ActivityGoalBinding binding_activity_goal;
    protected DBHelper dbHelper;
    protected SQLiteDatabase database;
    protected boolean start_activity = true;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding_activity_goal_start = ActivityGoalStartBinding.inflate(getLayoutInflater());
        binding_activity_goal = ActivityGoalBinding.inflate(getLayoutInflater());

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        String command_test = "select name_goal from goal";
        Cursor cursor = database.rawQuery(command_test, null);
        start_activity = !cursor.moveToNext();

        if (start_activity) {
            setContentView(R.layout.activity_goal_start);
            setContentView(binding_activity_goal_start.getRoot());

            create_menu_start();
        } else {
            create_goal();
        }

        binding_activity_goal.includeMenuGoal.buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding_activity_goal.goalDrawerLayout.openDrawer(GravityCompat.START);
                print();
            }
        });
        binding_activity_goal_start.includeMenu.buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding_activity_goal_start.goalDrawerLayout.openDrawer(GravityCompat.START);
                print();
            }
        });

    }

    public void calculation(View view) {
        String command = "select  t.*," +
                "t.dohod - t.rash as delta " +
                "from ( " +
                "SELECT " +
                "sum((case when is_big_purchase=1 and is_expense = 1 then summa else 0 end)) as rash_kp, " +
                "sum((case when is_big_purchase=0 and is_expense = 1 then summa else 0 end)) as rash, " +
                "sum((case when is_expense = 0 then summa else 0 end)) as dohod, " +
                "substr(created_date ,4,7) as my " +
                "from history as h " +
                "group by substr(created_date ,4,7)" +
                ") t order by my";
        Cursor cursor = database.rawQuery(command, null);

        cursor.close();
    }

    private void print() {
        Toast.makeText(this, "Меню", Toast.LENGTH_SHORT).show();
    }

    private void create_menu_start() {
        binding_activity_goal_start.goalNavigationMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_main) {
                    binding_activity_goal_start.goalDrawerLayout.close();
                    Toast.makeText(GoalActivity.this, "Главная", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(GoalActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_goal) {
                    binding_activity_goal_start.goalDrawerLayout.close();
                    Toast.makeText(GoalActivity.this, "Цель", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_history) {
                    binding_activity_goal_start.goalDrawerLayout.close();
                    Toast.makeText(GoalActivity.this, "История", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(GoalActivity.this, HistoryActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_report) {
                    binding_activity_goal_start.goalDrawerLayout.close();
                    Toast.makeText(GoalActivity.this, "Отчет", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(GoalActivity.this, ReportActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_categories) {
                    binding_activity_goal_start.goalDrawerLayout.close();
                    Toast.makeText(GoalActivity.this, "Категории", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(GoalActivity.this, CategoryActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }

    private void create_goal() {
        setContentView(R.layout.activity_goal);
        setContentView(binding_activity_goal.getRoot());

        String command_info = "select * from goal";
        Cursor cursor_info = database.rawQuery(command_info, null);

        cursor_info.moveToNext();

        binding_activity_goal.textViewNameGoal.setText("Название цели: " + cursor_info.getString(0));
        binding_activity_goal.textViewSumGoal.setText("Сумма цели : " + cursor_info.getInt(1));

        binding_activity_goal.goalNavigationMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_main) {
                    binding_activity_goal.goalDrawerLayout.close();
                    Toast.makeText(GoalActivity.this, "Главная", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(GoalActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_goal) {
                    binding_activity_goal.goalDrawerLayout.close();
                    Toast.makeText(GoalActivity.this, "Цель", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_history) {
                    binding_activity_goal.goalDrawerLayout.close();
                    Toast.makeText(GoalActivity.this, "История", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(GoalActivity.this, HistoryActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_report) {
                    binding_activity_goal.goalDrawerLayout.close();
                    Toast.makeText(GoalActivity.this, "Отчет", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(GoalActivity.this, ReportActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_categories) {
                    binding_activity_goal.goalDrawerLayout.close();
                    Toast.makeText(GoalActivity.this, "Категории", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(GoalActivity.this, CategoryActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }

    public void save_goal(View view) {
        boolean is_exception = false;
        Integer number_sum = null, number_start_sum = null, number_percent = null, number_inflation;
        try {
            number_sum = Integer.valueOf(String.valueOf(binding_activity_goal_start.editTextNumberSum.getText()));
            number_start_sum = Integer.valueOf(String.valueOf(binding_activity_goal_start.editTextNumberStartSum.getText()));
            number_percent = Integer.valueOf(String.valueOf(binding_activity_goal_start.editTextNumberPercent.getText()));
        } catch (Exception ise) {
            Toast.makeText(this, "Проверьте введенные данные", Toast.LENGTH_SHORT).show();
            is_exception = true;
        }

        String name_goal = binding_activity_goal_start.editTextTextName.getText().toString();
        if (!is_exception && name_goal.length() != 0) {
            try {
                number_inflation = Integer.valueOf(String.valueOf(binding_activity_goal_start.editTextNumberInflation.getText()));
            } catch (Exception ise) {
                number_inflation = 5;
            }

            SQLiteDatabase database = dbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put(DBHelper.COLUMN_NAME, name_goal);
            contentValues.put(DBHelper.COLUMN_SUMMA, number_sum);
            contentValues.put(DBHelper.COLUMN_START_CAPITAL, number_start_sum);
            contentValues.put(DBHelper.COLUMN_PERCENT, number_percent);
            contentValues.put(DBHelper.COLUMN_INFLATION, number_inflation);

            database.insert(DBHelper.TABLE_GOAL, null, contentValues);

            Toast.makeText(this, "Цель добавлена", Toast.LENGTH_SHORT).show();

            create_goal();
        }
    }
}
