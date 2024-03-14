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

import com.example.financial_application.databinding.ActivityCategoryBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity { //implements CategoryDialog.DialogListenerAdd {
    protected ActivityCategoryBinding binding_activity_category;
    private List<CategoryState> categoryStates;
    private CategoryStateAdapter categoryStateAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SQLiteDatabase database;
    private DBHelper dbHelper;
//    private CategoryDialog categoryDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding_activity_category = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding_activity_category.getRoot());

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

//        categoryDialog = new CategoryDialog();
//        categoryDialog.setMyDialogListener(this);

        binding_activity_category.categoryNavigationMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_main) {
                    binding_activity_category.drawerLayoutId.close();
                    Toast.makeText(CategoryActivity.this, "Главная", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CategoryActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_goal){
                    binding_activity_category.drawerLayoutId.close();
                    Toast.makeText(CategoryActivity.this, "Цель", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CategoryActivity.this, GoalActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_history) {
                    binding_activity_category.drawerLayoutId.close();
                    Toast.makeText(CategoryActivity.this, "История", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CategoryActivity.this, HistoryActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_report) {
                    binding_activity_category.drawerLayoutId.close();
                    Toast.makeText(CategoryActivity.this, "Отчет", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CategoryActivity.this, ReportActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_categories) {
                    binding_activity_category.drawerLayoutId.close();
                    Toast.makeText(CategoryActivity.this, "Категории", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_feedback) {
                    binding_activity_category.drawerLayoutId.close();
                    Toast.makeText(CategoryActivity.this, "Обратная связь", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CategoryActivity.this, FeedbackActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        layoutManager = new LinearLayoutManager(this);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                update_view(true);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void update_view(boolean expense) {
        categoryStates = new ArrayList<>();
        String command;
        if (expense) {
            command = "select " + DBHelper.COLUMN_CATEGORY_T_C + " from " + DBHelper.TABLE_CATEGORY + " where " + DBHelper.COLUMN_EXPENSE + " = 1";
        } else {
            command = "select " + DBHelper.COLUMN_CATEGORY_T_C + " from " + DBHelper.TABLE_CATEGORY + " where " + DBHelper.COLUMN_EXPENSE + " = 0";
        }
        Cursor cursor = database.rawQuery(command, null);
        while (cursor.moveToNext()) {
            categoryStates.add(new CategoryState(cursor.getString(0)));
        }
        cursor.close();

        if (categoryStates.size() > 0) {
            categoryStateAdapter = new CategoryStateAdapter(categoryStates);
            binding_activity_category.listCategory.setAdapter(categoryStateAdapter);
            binding_activity_category.listCategory.setLayoutManager(layoutManager);
        }
    }

    public void menu(View view) {
        binding_activity_category.drawerLayoutId.openDrawer(GravityCompat.START);
        Toast.makeText(this, "Меню", Toast.LENGTH_SHORT).show();
    }

    public void btn_expense_category(View view) {
        binding_activity_category.buttonExpenseCategory.setEnabled(false);
        binding_activity_category.buttonIncomeCategory.setEnabled(true);
        update_view(true);
    }

    public void btn_income_category(View view) {
        binding_activity_category.buttonExpenseCategory.setEnabled(true);
        binding_activity_category.buttonIncomeCategory.setEnabled(false);
        update_view(false);
    }

//    @Override
//    public void onDialogClickListener(String name_categoty, int expense) {
//        System.out.println("работает");
//    }
}
