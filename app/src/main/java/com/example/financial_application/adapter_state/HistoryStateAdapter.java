package com.example.financial_application.adapter_state;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.financial_application.R;

import java.util.List;

public class HistoryStateAdapter extends RecyclerView.Adapter<HistoryStateAdapter.ViewHolder> {
    public interface OnStateClickListenerHistory{
        void onStateClickHistory(HistoryState historyState, int position);
    }
    private final List<HistoryState> historyStateList;
    private final LayoutInflater inflater;
    private final OnStateClickListenerHistory onClickListenerHistory;

    public HistoryStateAdapter(List<HistoryState> historyStateList, Context context, OnStateClickListenerHistory onStateClickListenerHistory) {
        this.historyStateList = historyStateList;
        this.inflater = LayoutInflater.from(context);
        this.onClickListenerHistory = onStateClickListenerHistory;
    }

    @Override
    public HistoryStateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.update(historyStateList.get(position));
        holder.click(position);
    }

    @Override
    public int getItemCount() {
        return historyStateList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewDate, textViewNameCategory, textViewSumma;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.textViewDateHistory);
            textViewNameCategory = itemView.findViewById(R.id.textViewNameCategoryHistory);
            textViewSumma = itemView.findViewById(R.id.textViewSumCategoryHistory);
        }

        public void click(int position) {
            textViewNameCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListenerHistory.onStateClickHistory(historyStateList.get(position), position);
                }
            });
        }

        public void update(HistoryState historyState) {
            textViewDate.setText(historyState.getDate());
            textViewSumma.setText(historyState.getSumma());
            if (historyState.get_is_expense() == 0) {
                if (historyState.get_is_big_purchase() == 1) {
                    textViewNameCategory.setText(historyState.getName() + "**");
                }
                else {
                    textViewNameCategory.setText(historyState.getName());
                }
            } else if (historyState.get_is_big_purchase() == 1) {
                textViewNameCategory.setText(historyState.getName() + "*");
            } else {
                textViewNameCategory.setText(historyState.getName());
            }
            int[] color = historyState.getColor();
            textViewSumma.setTextColor(Color.rgb(color[0], color[1], color[2]));
        }
    }
}
