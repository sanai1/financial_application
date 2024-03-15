package com.example.financial_application.adapter_state;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.financial_application.R;

import java.util.List;

public class CategoryStateAdapter extends RecyclerView.Adapter<CategoryStateAdapter.ViewHolder> {
    public interface OnStateClickListenerCategory {
        void onStateClickCategory(String tag, CategoryState categoryState, int position);
    }
    private final List<CategoryState> categoryStates;
    private final OnStateClickListenerCategory onStateClickListenerCategory;

    public CategoryStateAdapter(List<CategoryState> categoryStates, OnStateClickListenerCategory onStateClickListenerCategory) {
        this.categoryStates = categoryStates;
        this.onStateClickListenerCategory = onStateClickListenerCategory;
    }

    @Override
    public CategoryStateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.update(categoryStates.get(position));
        holder.click(position);
    }

    @Override
    public int getItemCount() {
        return categoryStates.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewNameCategory;

        ViewHolder(View view) {
            super(view);
            textViewNameCategory = view.findViewById(R.id.textViewNameCategory);
        }

        public void click(int position) {
            textViewNameCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onStateClickListenerCategory.onStateClickCategory(categoryStates.get(position).getName(), categoryStates.get(position), position);
                }
            });
        }

        public void update(CategoryState categoryState) {
            textViewNameCategory.setText(categoryState.getName());
        }
    }
}
