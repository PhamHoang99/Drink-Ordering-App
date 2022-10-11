package com.android45.orderdrinks.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android45.orderdrinks.R;
import com.android45.orderdrinks.adapter.ProductsAdapter;
import com.android45.orderdrinks.models.Products;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class ViewAllActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView rec_products;
    ProductsAdapter productsAdapter;
    List<Products> productsList;

    ProgressBar progressBar;
    ImageView imgBack;
    TextView nameSp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);
        String type = getIntent().getStringExtra("type");
        rec_products = findViewById(R.id.rec_view_all);
        imgBack = findViewById(R.id.imageBack);
        nameSp = findViewById(R.id.textViewSp);

        imgBack.setOnClickListener(v -> {
            finish();
        });

        progressBar = findViewById(R.id.progress_bar_view_all);
        progressBar.setVisibility(View.VISIBLE);

        rec_products.setVisibility(View.GONE);
        rec_products.setLayoutManager(new GridLayoutManager(this,2));
        productsList = new ArrayList<>();
        productsAdapter = new ProductsAdapter(getApplicationContext(),productsList);
        rec_products.setAdapter(productsAdapter);

        if (type != null && type.equalsIgnoreCase("shop")){
            db.collection("HomeCategory")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Products products = document.toObject(Products.class);
                                    productsList.add(products);
                                    productsAdapter.notifyDataSetChanged();
                                    progressBar.setVisibility(View.GONE);
                                    rec_products.setVisibility(View.VISIBLE);
                                }
                            } else {
                                Toasty.error(getApplicationContext(),"Lỗi"+task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
        //geting coffee
        if (type != null && type.equalsIgnoreCase("coffee")){
            db.collection("AllProduct").whereEqualTo("type","coffee").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot documentSnapshot: task.getResult().getDocuments()){
                        Products products = documentSnapshot.toObject(Products.class);
                        productsList.add(products);
                        productsAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        rec_products.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
        //geting trasua
        if (type != null && type.equalsIgnoreCase("trasua")){
            db.collection("AllProduct").whereEqualTo("type","trasua").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot documentSnapshot: task.getResult().getDocuments()){
                        Products products = documentSnapshot.toObject(Products.class);
                        productsList.add(products);
                        productsAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        rec_products.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
        //geting giaikhat
        if (type != null && type.equalsIgnoreCase("giaikhat")){
            db.collection("AllProduct").whereEqualTo("type","giaikhat").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot documentSnapshot: task.getResult().getDocuments()){
                        Products products = documentSnapshot.toObject(Products.class);
                        productsList.add(products);
                        productsAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        rec_products.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
        //geting gia vi
        if (type != null && type.equalsIgnoreCase("spice")){
            db.collection("AllProduct").whereEqualTo("type","spice").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot documentSnapshot: task.getResult().getDocuments()){
                        Products products = documentSnapshot.toObject(Products.class);
                        productsList.add(products);
                        productsAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        rec_products.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
        //get hải sản
        if (type != null && type.equalsIgnoreCase("seafood")){
            db.collection("AllProduct").whereEqualTo("type","seafood").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot documentSnapshot: task.getResult().getDocuments()){
                        Products products = documentSnapshot.toObject(Products.class);
                        productsList.add(products);
                        productsAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        rec_products.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
        //get thit
        if (type != null && type.equalsIgnoreCase("meat")){
            db.collection("AllProduct").whereEqualTo("type","meat").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot documentSnapshot: task.getResult().getDocuments()){
                        Products products = documentSnapshot.toObject(Products.class);
                        productsList.add(products);
                        productsAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        rec_products.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }
}