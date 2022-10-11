package com.android45.orderdrinks.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android45.orderdrinks.OnClickItemFavorite;
import com.android45.orderdrinks.OnClickUnFavorite;
import com.android45.orderdrinks.R;
import com.android45.orderdrinks.activity.DetailedActivity;
import com.android45.orderdrinks.models.MyFavoriteModel;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class MyFavoriteAdapter extends RecyclerView.Adapter<MyFavoriteAdapter.ViewHolder> {
    Context context;
    List<MyFavoriteModel> favoriteModels;

    private OnClickItemFavorite onClickItemInterface;

    FirebaseFirestore firestore;
    FirebaseAuth auth;

    public MyFavoriteAdapter(Context context, List<MyFavoriteModel> favoriteModels, OnClickItemFavorite onClickItemInterface) {
        this.context = context;
        this.favoriteModels = favoriteModels;
        this.onClickItemInterface = onClickItemInterface;
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.my_favorite_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyFavoriteModel model = favoriteModels.get(position);

        Glide.with(context).load(model.getProductImg()).into(holder.img);
        holder.tvName.setText(model.getProductName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.tvPrice.setText("Giá: " + decimalFormat.format(model.getProductPrice()) + "VNĐ");
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailedActivity.class);
            intent.putExtra("detail",favoriteModels.get(position));
            context.startActivity(intent);
        });


        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickItemInterface.onClickItemVav(model);
            }
        });

        holder.setUnFavorite(new OnClickUnFavorite() {
            @Override
            public void onClickUnFav(View view, int pos) {
                firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                        .collection("Favorite")
                        .document(model.getDocumentID())
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    favoriteModels.remove(model);
                                    notifyDataSetChanged();
                                    Toasty.success(context,"Bạn đã bỏ yêu thích", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView img, favorite;
        TextView tvName, tvPrice;
        RelativeLayout layout;
        OnClickUnFavorite unFavorite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.vOnClickFavorite);

            img = itemView.findViewById(R.id.imgMyFavoriteHerbal);
            favorite = itemView.findViewById(R.id.btnFavorite);
            tvName = itemView.findViewById(R.id.tvFavoriteProductName);
            tvPrice = itemView.findViewById(R.id.tvFavoriteProductCost);

            favorite.setOnClickListener(this);
        }

        public void setUnFavorite(OnClickUnFavorite unFavorite) {
            this.unFavorite = unFavorite;
        }

        @Override
        public void onClick(View v) {
            unFavorite.onClickUnFav(v, getAdapterPosition());
        }
    }
}
