package com.example.financial_application.dialog_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.DialogFragment;

import com.example.financial_application.R;

public class CategoryUpdateDialog extends DialogFragment implements View.OnClickListener {
    private DialogListenerUpdate dialogListenerUpdate;
    private String uid;
    private int expense;
    private String name_category;
    private EditText editText;
    private RadioButton radioButtonExpense;
    private RadioButton radioButtonIncome;


    public CategoryUpdateDialog(String uid, int is_expense, String name_category) {
        this.uid = uid;
        this.expense = is_expense;
        this.name_category = name_category;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_category, null);

        view.findViewById(R.id.buttonAddAddCategory).setOnClickListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        editText = getDialog().findViewById(R.id.editTextTextNameCategoryAddCategory);
        radioButtonExpense = getDialog().findViewById(R.id.radioButtonExpense);
        radioButtonIncome = getDialog().findViewById(R.id.radioButtonIncome);
        Button btn = getDialog().findViewById(R.id.buttonAddAddCategory);

        btn.setText("изменить");
        editText.setText(name_category);
        if (expense == 1) {
            radioButtonExpense.setChecked(true);
            radioButtonIncome.setChecked(false);
        } else {
            radioButtonExpense.setChecked(false);
            radioButtonIncome.setChecked(true);
        }
        radioButtonIncome.setEnabled(false);
        radioButtonExpense.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        RadioGroup radioGroup = getDialog().findViewById(R.id.radioGroup);
        if (radioGroup.getCheckedRadioButtonId() != -1 && !editText.getText().toString().isEmpty()) {
            RadioButton radioButton_expense = getDialog().findViewById(R.id.radioButtonExpense);
            if (dialogListenerUpdate != null) {
                name_category = editText.getText().toString();
                expense = (radioButton_expense.isChecked()) ? 1 : 0;
                dialogListenerUpdate.onDialogClickListenerUpdate(uid, name_category, expense);
            }
            dismiss();
        }
    }

    public interface DialogListenerUpdate {
        void onDialogClickListenerUpdate(String uid, String name, int expense);
    }

    public void setMyDialogListenerUpdate(DialogListenerUpdate listener) {
        dialogListenerUpdate = listener;
    }
}
