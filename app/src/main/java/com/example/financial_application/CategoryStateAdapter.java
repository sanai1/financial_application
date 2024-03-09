package com.example.financial_application;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoryStateAdapter extends RecyclerView.Adapter<CategoryStateAdapter.ViewHolder> {
    private final List<CategoryState> categoryStates;

    public CategoryStateAdapter(List<CategoryState> categoryStates) {
        this.categoryStates = categoryStates;
    }

    @Override
    public CategoryStateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.update(categoryStates.get(position));
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
//            textViewNameCategory.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    System.out.println("нажатие на категорию");
//                }
//            });
        }
        public void update(CategoryState categoryState) {
            textViewNameCategory.setText(categoryState.getName());
        }
    }
}
