package com.example.financial_application;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import com.example.financial_application.databinding.ActivityMainBinding;
import com.example.financial_application.databinding.AddCategoryBinding;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends FragmentActivity {
    ActivityMainBinding binding_activity_main;
    AddCategoryBinding binding_add_category;
    DBHelper dbHelper;
    boolean expense = true;
    DialogFragment dialog_category;

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
//        CategoryDialog dialog_category = new CategoryDialog();
//        dialog_category.show(getSupportFragmentManager(), "dialogCategory");

//        dialog_category = new Dialog(MainActivity.this);
//        dialog_category.setContentView(R.layout.add_category);
//        dialog_category.show();

        dialog_category.show(getSupportFragmentManager(), "dialogCategory");



//        btn_income.setOnClickListener(listener_one);


//        onClick(btn_income);
//        btn_income.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println(true);
//                btn_income.setEnabled(false);
//                System.out.println(false);
//            }
//        });


    }



    public void menu(View view) {
    }

    public void save_expense(View view) {
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

        double sum = Double.parseDouble(binding_activity_main.editTextTextSum.getText().toString());
        String add_data = binding_activity_main.editTextDate.getText().toString();
        contentValues.put(DBHelper.COLUMN_SUM, sum);
        contentValues.put(DBHelper.COLUMN_ADD_DATA, add_data);

        database.insert(DBHelper.TABLE_HISTORY, null, contentValues);

    }


}