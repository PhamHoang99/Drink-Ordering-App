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
import com.android45.orderdrinks.activity.DetailedActivity;
import com.android45.orderdrinks.models.Products;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {
    private Context context;
    private List<Products> khuyendungList;

    public ProductsAdapter(Context context, List<Products> khuyendungList) {
        this.context = context;
        this.khuyendungList = khuyendungList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ic_list_item_popular,parent,false));
    }
    public void searchSp(String str){
        str = str.toLowerCase();
        int k = 0;
        for (int i = 0; i < khuyendungList.size(); i++){
            Products products = khuyendungList.get(i);
            String tenSP = products.getName().toLowerCase();
            if(tenSP.indexOf(str) >= 0 ){
                khuyendungList.set(i, khuyendungList.get(k));
                khuyendungList.set(k,products);
                k++;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(khuyendungList.get(position).getImg_url()).into(holder.img);
        holder.name.setText(khuyendungList.get(position).getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.price.setText(decimalFormat.format(khuyendungList.get(position).getPrice())  + " VND");
        holder.detail.setText(khuyendungList.get(position).getDescription());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailedActivity.class);
            intent.putExtra("detail",khuyendungList.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return khuyendungList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name, price,detail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            name = itemView.findViewById(R.id.nameProduct);
            price = itemView.findViewById(R.id.price);
            detail = itemView.findViewById(R.id.detail);
        }
    }
}
