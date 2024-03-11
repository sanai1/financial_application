package com.example.financial_application;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.DialogFragment;

import com.example.financial_application.databinding.AddCategoryBinding;


public class CategoryDialog extends DialogFragment implements View.OnClickListener {
    AddCategoryBinding binding_add_category;
    int expense = -1;
    String name_category = "name_category";
    private DialogListenerAdd dListenerAdd;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_category, null);
        binding_add_category = AddCategoryBinding.inflate(getLayoutInflater());

        view.findViewById(R.id.buttonAddAddCategory).setOnClickListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EditText editText = (EditText) getDialog().findViewById(R.id.editTextTextNameCategoryAddCategory);
        editText.setText("");
        RadioGroup radioGroup = (RadioGroup) getDialog().findViewById(R.id.radioGroup);
        radioGroup.clearCheck();
    }

    @Override
    public void onClick(View v) {
        EditText editText = (EditText) getDialog().findViewById(R.id.editTextTextNameCategoryAddCategory);
        RadioGroup radioGroup = (RadioGroup) getDialog().findViewById(R.id.radioGroup);
        if (radioGroup.getCheckedRadioButtonId() != -1 && !editText.getText().toString().isEmpty()) {
            RadioButton radioButton_expense = (RadioButton) getDialog().findViewById(R.id.radioButtonExpense);
            if (dListenerAdd != null) {
                name_category = editText.getText().toString();
                expense = (radioButton_expense.isChecked()) ? 1 : 0;
                dListenerAdd.onDialogClickListener(name_category, expense);
            }
            dismiss();
        }
    }

    public interface DialogListenerAdd {
        void onDialogClickListener (String name_category, int expense);
    }

    public void setMyDialogListener(DialogListenerAdd listener) {
        dListenerAdd = listener;
    }
}
