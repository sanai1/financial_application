package com.example.financial_application;


import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.example.financial_application.databinding.ActivityMainBinding;
import com.example.financial_application.databinding.AddCategoryBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;
import java.util.UUID;


public class MainActivity extends AppCompatActivity implements CategoryDialog.DialogListenerAdd {
    ActivityMainBinding binding_activity_main;
    AddCategoryBinding binding_add_category;
    protected DBHelper dbHelper;
    protected SQLiteDatabase database;
    protected boolean expense_main = true;
    protected CategoryDialog dialog_category;
    protected Calendar calendar = Calendar.getInstance();
    protected String[] mas_name_category_expense = new String[50];
    protected String[] mas_name_category_income = new String[50];
    public static int count_category = 0;

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
        setInitialDate();
        dialog_category = new CategoryDialog();
        dialog_category.setMyDialogListener(this);

        get_mas_expense();
    }

    private void get_mas_expense() {
        database = dbHelper.getWritableDatabase();

        String command_expense = "select category_t_c from category where expense=1";
        String command_income = "select category_t_c from category where expense=0";
        Cursor cursor_expense = database.rawQuery(command_expense, null);
        Cursor cursor_income = database.rawQuery(command_income, null);

        String[] mas_test_expense = new String[50];
        String[] mas_test_income = new String[50];

        int ind_name_category_in_cursor = 0;
        while (cursor_expense.moveToNext())
            mas_test_expense[ind_name_category_in_cursor++] = cursor_expense.getString(0);
        cursor_expense.close();

        ind_name_category_in_cursor = 0;
        while (cursor_income.moveToNext())
            mas_test_income[ind_name_category_in_cursor++] = cursor_income.getString(0);
        cursor_income.close();

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

        count_category = len_mas_expense + len_mas_income;

        ArrayAdapter<String> adapter;
        if (binding_activity_main.buttonExpense.isActivated()) {
            adapter = new ArrayAdapter(this, R.layout.spinner_item, mas_name_category_income);
        } else {
            adapter = new ArrayAdapter(this, R.layout.spinner_item, mas_name_category_expense);
        }

        adapter.setDropDownViewResource(R.layout.spinner_item);
        binding_activity_main.spinner.setAdapter(adapter);
    }

    public void income(View view) {
        binding_activity_main.buttonIncome.setEnabled(false);
        binding_activity_main.buttonExpense.setEnabled(true);
        binding_activity_main.checkBoxBidPurchase.setChecked(false);
        binding_activity_main.checkBoxBidPurchase.setText("разовый доход");
        expense_main = false;

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, mas_name_category_income);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        binding_activity_main.spinner.setAdapter(adapter);

        Toast.makeText(this, "Доход", Toast.LENGTH_SHORT).show();
    }

    public void expense(View view) {
        binding_activity_main.buttonIncome.setEnabled(true);
        binding_activity_main.checkBoxBidPurchase.setEnabled(true);
        binding_activity_main.checkBoxBidPurchase.setChecked(false);
        binding_activity_main.checkBoxBidPurchase.setText("крупная покупка");
        binding_activity_main.buttonExpense.setEnabled(false);
        expense_main = true;

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, mas_name_category_expense);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        binding_activity_main.spinner.setAdapter(adapter);

        Toast.makeText(this, "Расход", Toast.LENGTH_SHORT).show();
    }

    public void add_category_in_mainactivity(View view) {
        dialog_category.show(getSupportFragmentManager(), "dialogCategory");
    }
    @Override
    public void onDialogClickListener(String name_category, int expense) {
        binding_add_category = AddCategoryBinding.inflate(getLayoutInflater());
        database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        UUID uuid = UUID.randomUUID();
        System.out.println(uuid);
        contentValues.put(DBHelper.COLUMN_CATEGORY_ID, String.valueOf(uuid));
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
        Toast.makeText(this, "Категория добавлена", Toast.LENGTH_SHORT).show();
    }

    public void update_data(View view) {
        new DatePickerDialog(MainActivity.this, onDateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    private void setInitialDate() {
        binding_activity_main.textViewDate.setText(DateUtils.formatDateTime(this,
                calendar.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
    }
    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDate();
        }
    };

    public void menu(View view) {
        binding_activity_main.drawerLayoutId.openDrawer(GravityCompat.START);
        Toast.makeText(this, "Меню", Toast.LENGTH_SHORT).show();
    }

    public void save_expense(View view) {
        if (binding_activity_main.editTextNumberSum.getText().length() != 0) {
            database = dbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            String command_get_category = "select " + DBHelper.COLUMN_CATEGORY_ID + " from " + DBHelper.TABLE_CATEGORY +
                    " where " + DBHelper.COLUMN_CATEGORY_T_C + " = '" + binding_activity_main.spinner.getSelectedItem().toString() + "'";
            Cursor cursorUid = database.rawQuery(command_get_category, null);
            cursorUid.moveToNext();
            contentValues.put(DBHelper.COLUMN_CATEGORY_UID, cursorUid.getString(0));
            cursorUid.close();

            if (expense_main) {
                contentValues.put(DBHelper.COLUMN_IS_EXPENSE, 1);
            } else {
                contentValues.put(DBHelper.COLUMN_IS_EXPENSE, 0);
            }
            if (binding_activity_main.checkBoxBidPurchase.isChecked()) {
                contentValues.put(DBHelper.COLUMN_IS_BIG_PURCHASE, 1);
            } else {
                contentValues.put(DBHelper.COLUMN_IS_BIG_PURCHASE, 0);
            }

            double sum = Double.parseDouble(binding_activity_main.editTextNumberSum.getText().toString());
            contentValues.put(DBHelper.COLUMN_SUMMA, sum);

            String year = String.valueOf(calendar.get(Calendar.YEAR));
            String month = String.valueOf(calendar.get(Calendar.MONTH));
            String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            if (month.length() == 1) {
                month = "0" + month;
            }
            if (day.length() == 1) {
                day = "0" + day;
            }
            contentValues.put(DBHelper.COLUMN_ADD_DATA, day + "." + month + "." + year);
            database.insert(DBHelper.TABLE_HISTORY, null, contentValues);

            binding_activity_main.editTextNumberSum.setText("");
            binding_activity_main.checkBoxBidPurchase.setChecked(false);
            Toast.makeText(this, "Запись сохранена", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Проверьте введеные данные", Toast.LENGTH_SHORT).show();
        }
    }

}