package com.android45.orderdrinks.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.android45.orderdrinks.R;
import com.android45.orderdrinks.activity.ViewAllActivity;
import com.android45.orderdrinks.models.HomeCategory;
import com.bumptech.glide.Glide;

import java.util.List;

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder> {
    private Context context;
    private List<HomeCategory> categoryList;

    public HomeCategoryAdapter(Context context, List<HomeCategory> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ic_item_list_product,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(categoryList.get(position).getImg_url()).into(holder.img);
        holder.name.setText(categoryList.get(position).getName());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewAllActivity.class);
            intent.putExtra("type",categoryList.get(position).getType());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imageSp);
            name = itemView.findViewById(R.id.tvName);
        }
    }
}
