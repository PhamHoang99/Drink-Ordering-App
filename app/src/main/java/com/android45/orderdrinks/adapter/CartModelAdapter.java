package com.android45.orderdrinks.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android45.orderdrinks.R;
import com.android45.orderdrinks.activity.DetailedActivity;
import com.android45.orderdrinks.models.CartModel;
import com.bumptech.glide.Glide;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class CartModelAdapter extends RecyclerView.Adapter<CartModelAdapter.ViewHolder> {
    private Context context;
    List<CartModel> listCart;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    int totalPrice = 0;
    FirebaseFirestore db;
    FirebaseAuth auth;

    public CartModelAdapter(Context context, List<CartModel> listCart) {
        this.context = context;
        this.listCart = listCart;
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_cart,parent,false)) ;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(listCart.get(position).getProductImg()).into(holder.img);
        CartModel cartModel = listCart.get(position);
        holder.name.setText(listCart.get(position).getProductName());
//        holder.date.setText("Ngày: " + listCart.get(position).getCurrentDate());
//        holder.time.setText("Giờ: " + listCart.get(position).getCurrentTime());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
//        holder.price.setText("Giá: " + String.valueOf(decimalFormat.format(listCart.get(position).getProductPrice()))) ;
        holder.totalPrice.setText(String.valueOf(listCart.get(position).getTotalPrice()));
        holder.quantity.setText(listCart.get(position).getTotalQuantity());
        holder.add.setOnClickListener(v -> {
            if (DetailedActivity.totalQuantity < 10){
                DetailedActivity.totalQuantity++;
                holder.quantity.setText(String.valueOf(DetailedActivity.totalQuantity));
                totalPrice = DetailedActivity.products.getPrice() * DetailedActivity.totalQuantity;
            }
        });

        holder.remove.setOnClickListener(v -> {
            if (DetailedActivity.totalQuantity > 1 ){
                DetailedActivity.totalQuantity--;
                holder.quantity.setText(String.valueOf(DetailedActivity.totalQuantity));
                totalPrice = DetailedActivity.products.getPrice() * DetailedActivity.totalQuantity;
            }
        });

        holder.deleteItem.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Thông báo...")
                    .setMessage("Bạn có chắc chắn muốn xóa không!")
                    .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                                    .collection("AddToCart")
                                    .document(listCart.get(position).getDocumentID())
                                    .delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                listCart.remove(cartModel);
                                                notifyDataSetChanged();
                                                Toasty.success(context,"Xóa thành công",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    })
                    .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();

        });

        totalPrice = totalPrice + listCart.get(position).getTotalPrice();
        Intent intent = new Intent("MyTotalAmount");
        intent.putExtra("totalAmount",totalPrice);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @Override
    public int getItemCount() {
        return listCart.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img, deleteItem,add,remove;
        TextView name,date,time,price,quantity,totalPrice,quanty;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_cart);
            name = itemView.findViewById(R.id.name_product_cart);
//            date = itemView.findViewById(R.id.date_product_cart);
//            time = itemView.findViewById(R.id.time_product_cart);
            quantity = itemView.findViewById(R.id.quantity);
            totalPrice = itemView.findViewById(R.id.totalPrice_product_cart);
            deleteItem = itemView.findViewById(R.id.delete_item);
            remove = itemView.findViewById(R.id.imgRemove);
            add = itemView.findViewById(R.id.imgAdd);
        }
    }
}
