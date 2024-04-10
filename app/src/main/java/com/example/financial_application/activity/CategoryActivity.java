package com.example.financial_application.activity;

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
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.financial_application.dialog_fragment.CategoryUpdateDialog;
import com.example.financial_application.dialog_fragment.CategoryDialog;
import com.example.financial_application.adapter_state.CategoryState;
import com.example.financial_application.adapter_state.CategoryStateAdapter;
import com.example.financial_application.DBHelper;
import com.example.financial_application.R;
import com.example.financial_application.databinding.ActivityCategoryBinding;
import com.example.financial_application.databinding.AddCategoryBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CategoryActivity extends AppCompatActivity implements CategoryDialog.DialogListenerAdd, CategoryUpdateDialog.DialogListenerUpdate {
    protected ActivityCategoryBinding binding_activity_category;
    private AddCategoryBinding binding_add_category;
    private List<CategoryState> categoryStates;
    private CategoryStateAdapter categoryStateAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private CategoryDialog categoryAddDialog;
    private CategoryUpdateDialog categoryUpdateDialog;
    private boolean expenseButton = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding_activity_category = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding_activity_category.getRoot());

        binding_activity_category.includeMenu.textViewInfo.setText("Категории");

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        categoryAddDialog = new CategoryDialog();
        categoryAddDialog.setMyDialogListener(this);

        binding_activity_category.categoryNavigationMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_main) {
                    binding_activity_category.drawerLayoutId.close();
//                    Toast.makeText(CategoryActivity.this, "Главная", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CategoryActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_goal){
                    binding_activity_category.drawerLayoutId.close();
//                    Toast.makeText(CategoryActivity.this, "Цель", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CategoryActivity.this, GoalActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_history) {
                    binding_activity_category.drawerLayoutId.close();
//                    Toast.makeText(CategoryActivity.this, "История", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CategoryActivity.this, HistoryActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_report) {
                    binding_activity_category.drawerLayoutId.close();
//                    Toast.makeText(CategoryActivity.this, "Отчет", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CategoryActivity.this, ReportActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_categories) {
                    binding_activity_category.drawerLayoutId.close();
//                    Toast.makeText(CategoryActivity.this, "Категории", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_feedback) {
                    binding_activity_category.drawerLayoutId.close();
//                    Toast.makeText(CategoryActivity.this, "Обратная связь", Toast.LENGTH_SHORT).show();
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
                update_view(expenseButton);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void update_view(boolean expense_boolean) {
        // TODO:поменять тип параметра
        int expense;
        if (expense_boolean) {
            expense = 1;
        } else {
            expense = 0;
        }
        CategoryStateAdapter.OnStateClickListenerCategory onStateClickListenerCategory = new CategoryStateAdapter.OnStateClickListenerCategory() {
            @Override
            public void onStateClickCategory(String tag, CategoryState categoryState, int position) {
                categoryUpdateDialog = new CategoryUpdateDialog(categoryState.getUid(), categoryState.getIs_expense(), categoryState.getName());
                categoryUpdateDialog.setMyDialogListenerUpdate(CategoryActivity.this);
                categoryUpdateDialog.show(getSupportFragmentManager(), tag);
            }
        };
        categoryStates = new ArrayList<>();
        String command = "select * from " + DBHelper.TABLE_CATEGORY;
        Cursor cursor = database.rawQuery(command, null);
        while (cursor.moveToNext()) {
            if (expense == cursor.getInt(2)) {
                categoryStates.add(new CategoryState(cursor.getString(0), cursor.getString(1), cursor.getInt(2)));
            }
        }
        cursor.close();

        if (categoryStates.size() > 0) {
            categoryStateAdapter = new CategoryStateAdapter(categoryStates, onStateClickListenerCategory);
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
        binding_activity_category.buttonExpenseCategory.setBackgroundColor(ContextCompat.getColor(this, R.color.light_sky_blue));
        binding_activity_category.buttonExpenseCategory.setTextColor(ContextCompat.getColor(this, R.color.gray));
        binding_activity_category.buttonIncomeCategory.setBackgroundColor(ContextCompat.getColor(this, R.color.blue));
        binding_activity_category.buttonIncomeCategory.setTextColor(ContextCompat.getColor(this, R.color.white));
        expenseButton = true;
        update_view(expenseButton);
    }

    public void btn_income_category(View view) {
        binding_activity_category.buttonExpenseCategory.setEnabled(true);
        binding_activity_category.buttonIncomeCategory.setEnabled(false);
        binding_activity_category.buttonExpenseCategory.setBackgroundColor(ContextCompat.getColor(this, R.color.blue));
        binding_activity_category.buttonExpenseCategory.setTextColor(ContextCompat.getColor(this, R.color.white));
        binding_activity_category.buttonIncomeCategory.setBackgroundColor(ContextCompat.getColor(this, R.color.light_sky_blue));
        binding_activity_category.buttonIncomeCategory.setTextColor(ContextCompat.getColor(this, R.color.gray));
        expenseButton = false;
        update_view(expenseButton);
    }

    public void add_category_in_category(View view) {
        categoryAddDialog.show(getSupportFragmentManager(), "addDialogCategory");
    }

    @Override
    public void onDialogClickListener(String name_category, int expense) {
        binding_add_category = AddCategoryBinding.inflate(getLayoutInflater());
        database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        UUID uuid = UUID.randomUUID();
        contentValues.put(DBHelper.COLUMN_CATEGORY_ID, String.valueOf(uuid));
        contentValues.put(DBHelper.COLUMN_EXPENSE, expense);
        contentValues.put(DBHelper.COLUMN_CATEGORY_T_C, name_category);

        database.insert(DBHelper.TABLE_CATEGORY, null, contentValues);

        Toast.makeText(this, "Категория добавлена", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDialogClickListenerUpdate(String uid, String name, int expense) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COLUMN_CATEGORY_T_C, name);
        contentValues.put(DBHelper.COLUMN_EXPENSE, expense);

        database.update(DBHelper.TABLE_CATEGORY, contentValues, DBHelper.COLUMN_CATEGORY_ID + " = '" + uid + "'", null);

        update_view(expenseButton);

        Toast.makeText(this, "Категория обнавлена", Toast.LENGTH_SHORT).show();
    }
}
