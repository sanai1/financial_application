package com.example.financial_application;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    int expense = -1;
    String name_category = "name_category";
    private DialogListenerAdd dListenerAdd;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_category, null);
        //binding_add_category = AddCategoryBinding.inflate(getLayoutInflater());

        view.findViewById(R.id.buttonAddAddCategory).setOnClickListener(this);
        view.findViewById(R.id.editTextTextNameCategoryAddCategory).setOnClickListener(this);
        view.findViewById(R.id.radioButtonExpense).setOnClickListener(this);
        view.findViewById(R.id.radioButtonIncome).setOnClickListener(this);
        return view;
    }
    void add_database() {

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelper.COLUMN_CATEGORY_T_C, name_category);
        contentValues.put(DBHelper.COLUMN_EXPENSE, expense);

        Cursor cursor = database.rawQuery("select category_t_c from category;", null);
        while (cursor.moveToNext()) {
            String str_test = cursor.getString(0);
            System.out.println(str_test);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.radioButtonExpense) {
            expense = 1;
        } else if (v.getId() == R.id.radioButtonIncome) {
            expense = 0;
        } else if (v.getId() == R.id.editTextTextNameCategoryAddCategory) {
            EditText editText = (EditText) getDialog().findViewById(R.id.editTextTextNameCategoryAddCategory);
            name_category = String.valueOf(editText.getText());
        } else {
            if (expense != -1 && name_category != "name_category") {
                if (dListenerAdd != null) {
                    dListenerAdd.onDialogClickListener(name_category, expense);
                }
                //onDetach();
                //onDestroyView();
                //onPause();
                dismiss();

            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }
//    @Override
//    public void onDismiss(DialogInterface dialogInterface) {
//        super.onDismiss(dialogInterface);
//    }

    public interface DialogListenerAdd {
        void onDialogClickListener (String name_categoty, int expense);
    }

    public void setMyDialogListener(DialogListenerAdd listener) {
        dListenerAdd = listener;
    }
}
