package com.example.financial_application;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import com.example.financial_application.databinding.ActivityMainBinding;
import com.example.financial_application.databinding.AddCategoryBinding;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends FragmentActivity implements CategoryDialog.DialogListenerAdd{
    ActivityMainBinding binding_activity_main;
    AddCategoryBinding binding_add_category;
    DBHelper dbHelper;
    boolean expense = true;
    CategoryDialog dialog_category;
    String [] mas_test = {"one", "two", "three"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout_id);

        NavigationView navigationView = findViewById(R.id.navigator_view_id);


        binding_activity_main = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding_activity_main.getRoot());

        dbHelper = new DBHelper(this);

        dialog_category = new CategoryDialog();
        dialog_category.setMyDialogListener(this);

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, mas_test);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
    }

    public void income(View view) {
        binding_activity_main.buttonIncome.setEnabled(false);
        binding_activity_main.checkBoxBidPurchase.setEnabled(false);
        binding_activity_main.buttonExpense.setEnabled(true);
        expense = true;
    }

    public void expense(View view) {
        binding_activity_main.buttonIncome.setEnabled(true);
        binding_activity_main.checkBoxBidPurchase.setEnabled(true);
        binding_activity_main.buttonExpense.setEnabled(false);
        expense = false;
    }

    public void add_category_in_mainactivity(View view) {
        System.out.println("no");
        dialog_category.show(getSupportFragmentManager(), "dialogCategory");
        System.out.println("yes");
    }
    @Override
    public void onDialogClickListener(String name_categoty, int expense) {
        binding_add_category = AddCategoryBinding.inflate(getLayoutInflater());
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelper.COLUMN_EXPENSE, expense);
        contentValues.put(DBHelper.COLUMN_CATEGORY_T_C, name_categoty);

        database.insert(DBHelper.TABLE_CATEGORY, null, contentValues);
        System.out.println(true);

        binding_add_category.editTextTextNameCategoryAddCategory.setText("Название категории");
        binding_add_category.radioButtonExpense.setChecked(false);
        binding_add_category.buttonAddAddCategory.setEnabled(false);

        System.out.println(false);
    }



    public void menu(View view) {

    }

    public void save_expense(View view) {
        if (binding_activity_main.editTextNumberSum.getText().length() != 0 && binding_activity_main.editTextDate.getText().length() != 0) {
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            if (expense) {
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