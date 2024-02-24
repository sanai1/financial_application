package com.example.financial_application;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.example.financial_application.databinding.UpdateDateBinding;

import java.text.SimpleDateFormat;

public class UpdateDataDialog extends DialogFragment implements View.OnClickListener{
    // TODO: реализовать нажатие на календарь (нажатие не работает)
    UpdateDateBinding binding_update_date;
    private DialogListenerData dialogListenerData;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.update_date, null);
        binding_update_date = UpdateDateBinding.inflate(getLayoutInflater());

//        view.findViewById(R.id.buttonSaveDate).setOnClickListener(this);
//        view.findViewById(R.id.calendarView).setOnClickListener(this);
        binding_update_date.buttonSaveDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("--------------------------------");
            }
        });


        return view;
    }

    @Override
    public void onClick(View v) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        long date = binding_update_date.calendarView.getDate();
        String str = format.format(date);

        dialogListenerData.onDialogDataClick(str);
        dismiss();
    }

    public interface DialogListenerData {
        void onDialogDataClick (String text);
    }

    public void setMyDialogDataListener(CategoryDialog.DialogListenerAdd listener) {
        dialogListenerData = (DialogListenerData) listener;
    }
}
