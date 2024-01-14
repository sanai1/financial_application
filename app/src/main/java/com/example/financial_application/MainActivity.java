package com.example.financial_application;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.financial_application.databinding.ActivityMainBinding;
import com.example.financial_application.databinding.AddCategoryBinding;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements CategoryDialog.DialogListenerAdd{
    ActivityMainBinding binding_activity_main;
    AddCategoryBinding binding_add_category;
    protected DBHelper dbHelper;
    protected boolean expense_main = true;
    protected CategoryDialog dialog_category;
    protected String[] mas_name_category_expense = new String[50];
    protected String[] mas_name_category_income = new String[50];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding_activity_main = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding_activity_main.getRoot());
        binding_activity_main.navigatorViewId.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_main) {
                    binding_activity_main.drawerLayoutId.close();
                    Toast.makeText(MainActivity.this, "Главная", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_goal){
                    binding_activity_main.drawerLayoutId.close();
                    Toast.makeText(MainActivity.this, "Цель", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, GoalActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_history) {
                    binding_activity_main.drawerLayoutId.close();
                    Toast.makeText(MainActivity.this, "История", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_report) {
                    binding_activity_main.drawerLayoutId.close();
                    Toast.makeText(MainActivity.this, "Отчет", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, ReportActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_categories) {
                    binding_activity_main.drawerLayoutId.close();
                    Toast.makeText(MainActivity.this, "Категории", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });

        dbHelper = new DBHelper(this);

        dialog_category = new CategoryDialog();
        dialog_category.setMyDialogListener(this);

        // TODO: найти и исправить ошибку
        get_mas_expense();
    }

    void get_mas_expense() {
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        String command_expense = "select category_t_c from category where expense=1";
        String command_income = "select category_t_c from category where expense=0";
        Cursor cursor_expense = database.rawQuery(command_expense, null);
        Cursor cursor_income = database.rawQuery(command_income, null);

        String[] mas_test_expense = new String[50];
        String[] mas_test_income = new String[50];

        int ind_name_category_in_cursor = 0;
        while (cursor_expense.moveToNext())
            mas_test_expense[ind_name_category_in_cursor++] = cursor_expense.getString(0);

        ind_name_category_in_cursor = 0;
        while (cursor_income.moveToNext())
            mas_test_income[ind_name_category_in_cursor++] = cursor_income.getString(0);

        int len_mas_expense = 0, len_mas_income = 0;
        for (int i = 0; mas_test_expense[i] != null; i++)
            len_mas_expense++;
        for (int i = 0; mas_test_income[i] != null; i++)
            len_mas_income++;
        mas_name_category_expense = new String[len_mas_expense];
        mas_name_category_income = new String[len_mas_income];
        for (int i = 0; i < mas_name_category_expense.length; i++)
            mas_name_category_expense[i] = mas_test_expense[i];
        for (int i = 0; i < mas_name_category_income.length; i++)
            mas_name_category_income[i] = mas_test_income[i];

        //mas_name_category_expense[0] = "expense"; // временная мера до заполнения БД реальными данными

        ArrayAdapter<String> adapter;
        if (binding_activity_main.buttonExpense.isActivated()) {
            adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, mas_name_category_income);
        } else {
            adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, mas_name_category_expense);
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        binding_activity_main.spinner.setAdapter(adapter);
    }

    public void income(View view) {
        binding_activity_main.buttonIncome.setEnabled(false);
        binding_activity_main.buttonExpense.setEnabled(true);
        binding_activity_main.checkBoxBidPurchase.setChecked(false);
        binding_activity_main.checkBoxBidPurchase.setEnabled(false);
        expense_main = false;

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, mas_name_category_income);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        binding_activity_main.spinner.setAdapter(adapter);
    }

    public void expense(View view) {
        binding_activity_main.buttonIncome.setEnabled(true);
        binding_activity_main.checkBoxBidPurchase.setEnabled(true);
        binding_activity_main.buttonExpense.setEnabled(false);
        expense_main = true;

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, mas_name_category_expense);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        binding_activity_main.spinner.setAdapter(adapter);
    }

    public void add_category_in_mainactivity(View view) {
        dialog_category.show(getSupportFragmentManager(), "dialogCategory");
    }
    @Override
    public void onDialogClickListener(String name_category, int expense) {
        binding_add_category = AddCategoryBinding.inflate(getLayoutInflater());
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelper.COLUMN_EXPENSE, expense);
        contentValues.put(DBHelper.COLUMN_CATEGORY_T_C, name_category);

        database.insert(DBHelper.TABLE_CATEGORY, null, contentValues);

        if (expense == 1) {
            String[] mas_test_expense = new String[mas_name_category_expense.length + 1];
            for (int i = 0; i < mas_name_category_expense.length; i++)
                mas_test_expense[i] = mas_name_category_expense[i];
            mas_test_expense[mas_test_expense.length - 1] = name_category;
            mas_name_category_expense = mas_test_expense;
            if (expense_main) {
                ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, mas_name_category_expense);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                binding_activity_main.spinner.setAdapter(adapter);
            }
        } else {
            String[] mas_test_income = new String[mas_name_category_income.length + 1];
            for (int i = 0; i < mas_name_category_income.length; i++)
                mas_test_income[i] = mas_name_category_income[i];
            mas_test_income[mas_test_income.length - 1] = name_category;
            mas_name_category_income = mas_test_income;
            if (!expense_main) {
                ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, mas_name_category_income);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                binding_activity_main.spinner.setAdapter(adapter);
            }
        }
    }

    public void menu(View view) {

    }

    public void save_expense(View view) {
        if (binding_activity_main.editTextNumberSum.getText().length() != 0 && binding_activity_main.editTextDate.getText().length() != 0) {
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            if (expense_main) {
                contentValues.put(DBHelper.COLUMN_EXPENDITURE, 1);
                if (binding_activity_main.checkBoxBidPurchase.isChecked()) {
                    contentValues.put(DBHelper.COLUMN_BIG_PURCHASE, 1);
                } else {
                    contentValues.put(DBHelper.COLUMN_BIG_PURCHASE, 0);
                }
            } else {
                contentValues.put(DBHelper.COLUMN_EXPENDITURE, 0);
            }

            double sum = Double.parseDouble(binding_activity_main.editTextNumberSum.getText().toString());
            String add_data = binding_activity_main.editTextDate.getText().toString();
            contentValues.put(DBHelper.COLUMN_SUM, sum);
            contentValues.put(DBHelper.COLUMN_ADD_DATA, add_data);

            database.insert(DBHelper.TABLE_HISTORY, null, contentValues);
        } else {
            System.out.println(true);
        }
    }

}