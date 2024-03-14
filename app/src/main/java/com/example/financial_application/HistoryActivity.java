package com.example.financial_application;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financial_application.databinding.ActivityHistoryBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    protected ActivityHistoryBinding binding_activity_history;
    private List<HistoryState> historyStateList;
    private HistoryStateAdapter stateAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private DBHelper dbHelper;
    private SQLiteDatabase database;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        binding_activity_history = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding_activity_history.getRoot());

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        binding_activity_history.historyNavigationMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_main) {
                    binding_activity_history.drawerLayoutId.close();
                    Toast.makeText(HistoryActivity.this, "Главная", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_goal){
                    binding_activity_history.drawerLayoutId.close();
                    Toast.makeText(HistoryActivity.this, "Цель", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HistoryActivity.this, GoalActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_history) {
                    binding_activity_history.drawerLayoutId.close();
                    Toast.makeText(HistoryActivity.this, "История", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_report) {
                    binding_activity_history.drawerLayoutId.close();
                    Toast.makeText(HistoryActivity.this, "Отчет", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HistoryActivity.this, ReportActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_categories) {
                    binding_activity_history.drawerLayoutId.close();
                    Toast.makeText(HistoryActivity.this, "Категории", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HistoryActivity.this, CategoryActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_feedback) {
                    binding_activity_history.drawerLayoutId.close();
                    Toast.makeText(HistoryActivity.this, "Обратная связь", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(HistoryActivity.this, FeedbackActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        historyStateList = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                update_view();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void update_view() {
        String command = "select " + DBHelper.COLUMN_IS_EXPENSE + ", " + DBHelper.COLUMN_IS_BIG_PURCHASE + ", " +
                DBHelper.COLUMN_SUMMA + ", " + DBHelper.COLUMN_ADD_DATA + ", " + DBHelper.COLUMN_CATEGORY_UID + " from " + DBHelper.TABLE_HISTORY;
        // TODO: сделать сортировку записей истории (от самой последней к самой старой записи)
        Cursor cursor = database.rawQuery(command, null);

        HashMap<String, String> hashMapCategory = new HashMap<>();
        String command_get_category = "select * from " + DBHelper.TABLE_CATEGORY;
        Cursor cursor_get_category = database.rawQuery(command_get_category, null);

        while (cursor_get_category.moveToNext()) {
            hashMapCategory.put(cursor_get_category.getString(0), cursor_get_category.getString(1));
        }
        cursor_get_category.close();

        String name;
        while (cursor.moveToNext()) {
            int[] color = {76, 175, 80};
            name = hashMapCategory.get(cursor.getString(4));

            if (cursor.getInt(0) == 1) {
                historyStateList.add(new HistoryState(cursor.getString(3), name, cursor.getString(2), cursor.getInt(0), cursor.getInt(1)));
            } else {
                historyStateList.add(new HistoryState(cursor.getString(3), name, cursor.getString(2), cursor.getInt(0), cursor.getInt(1), color));
            }
        }
        cursor.close();
        Collections.reverse(historyStateList);
        // TODO: при наличии 1 категории, она не удаляется при переключении расход/доход
        if (historyStateList.size() > 0) {
            stateAdapter = new HistoryStateAdapter(historyStateList);
            binding_activity_history.historyList.setAdapter(stateAdapter);
            binding_activity_history.historyList.setLayoutManager(layoutManager);
        }

    }

    public void menu(View view) {
        binding_activity_history.drawerLayoutId.openDrawer(GravityCompat.START);
        Toast.makeText(this, "Меню", Toast.LENGTH_SHORT).show();
    }
}
