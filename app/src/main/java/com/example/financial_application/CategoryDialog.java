package com.example.financial_application;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.example.financial_application.databinding.AddCategoryBinding;


public class CategoryDialog extends DialogFragment implements View.OnClickListener {
    DBHelper dbHelper;
    AddCategoryBinding binding_add_category;
    int expense = 0;
    String name_category = "name_category";


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_category, null);
        binding_add_category = AddCategoryBinding.inflate(getLayoutInflater());

        view.findViewById(R.id.buttonAddAddCategory).setOnClickListener(this);
        view.findViewById(R.id.editTextTextNameCategoryAddCategory).setOnClickListener(this);
        view.findViewById(R.id.radioButtonExpense).setOnClickListener(this);
        view.findViewById(R.id.radioButtonIncome).setOnClickListener(this);
        return view;
    }
    void add_database() {

    }

    @Override
    public void onClick(View v) {
//        SQLiteDatabase database = dbHelper.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
        if (v.getId() == R.id.radioButtonExpense) {
            expense = 1;
        } else if (v.getId() == R.id.radioButtonIncome) {
            expense = 2;
        } else if (v.getId() == R.id.editTextTextNameCategoryAddCategory) {
            EditText editText = (EditText) getDialog().findViewById(R.id.editTextTextNameCategoryAddCategory);
            name_category = String.valueOf(editText.getText());
        } else {
            if (expense != 0 && name_category != "name_category") {
                add_database();
                dismiss();
            }
        }

    }
}
