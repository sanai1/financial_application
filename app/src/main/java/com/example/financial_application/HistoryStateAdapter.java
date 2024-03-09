package com.example.financial_application;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryStateAdapter extends RecyclerView.Adapter<HistoryStateAdapter.ViewHolder> {
    private final List<HistoryState> historyStateList;

    public HistoryStateAdapter(List<HistoryState> historyStateList) {
        this.historyStateList = historyStateList;
    }

    @Override
    public HistoryStateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.update(historyStateList.get(position));
    }

    @Override
    public int getItemCount() {
        return historyStateList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewDate, textViewNameCategory, textViewSumma;

        ViewHolder(View view) {
            super(view);

            textViewDate = view.findViewById(R.id.textViewDateHistory);
            textViewNameCategory = view.findViewById(R.id.textViewNameCategoryHistory);
            textViewSumma = view.findViewById(R.id.textViewSumCategoryHistory);

        }

        public void update(HistoryState historyState) {
            textViewDate.setText(historyState.getDate());
            textViewNameCategory.setText(historyState.getName());
            textViewSumma.setText(historyState.getSumma());
            if (!historyState.isIs_expense()) {
                textViewSumma.setTextColor(Color.rgb(0, 100, 0));
            }
        }
    }
}
