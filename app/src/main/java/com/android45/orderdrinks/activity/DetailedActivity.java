package com.android45.orderdrinks.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android45.orderdrinks.ChangeFavorite;
import com.android45.orderdrinks.ChangeListFavoriteEvent;
import com.android45.orderdrinks.R;
import com.android45.orderdrinks.models.MyFavoriteModel;
import com.android45.orderdrinks.models.Products;
import com.android45.orderdrinks.models.UserModel;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class DetailedActivity extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth auth;

    MyFavoriteModel model = new MyFavoriteModel();
    public static int totalQuantity = 1;
    public static int totalPrice = 0;
    boolean checkFav = false;
    Toolbar toolbar;
    ImageView detailImg,addItem, removeItem,back,favorite;
    TextView price, detail,name,quantity;
    Button addToCart;

    public static Products products = null;
    UserModel userModel = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        detailImg = findViewById(R.id.imgDetail);
        addItem = findViewById(R.id.imgTang);
        removeItem = findViewById(R.id.imgGiam);
        back = findViewById(R.id.imageback);
        price = findViewById(R.id.tvPriceProduct);
        detail = findViewById(R.id.tvDescription);
        addToCart = findViewById(R.id.btnAddCart);
        favorite = findViewById(R.id.imageFavourite);
//        toolbar = findViewById(R.id.tbar_detail);
        name = findViewById(R.id.tvNameProduct);
        quantity = findViewById(R.id.tvQuantity);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        back.setOnClickListener(v -> {
            finish();
        });

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        final Object object = getIntent().getSerializableExtra("detail");
        if (object instanceof Products){
            products = (Products) object;
        }

        if (products != null){
            Glide.with(getApplicationContext()).load(products.getImg_url()).into(detailImg);
            detail.setText(products.getDescription());
            name.setText(products.getName());
            price.setText("Giá: " + products.getPrice());
            totalPrice = products.getPrice() * totalQuantity;
        }

        addItem.setOnClickListener(v -> {
            if (totalQuantity < 10){
                totalQuantity++;
                quantity.setText(String.valueOf(totalQuantity));
                totalPrice = products.getPrice() * totalQuantity;
            }
        });
        removeItem.setOnClickListener(v -> {
            if (totalQuantity > 1){
                totalQuantity--;
                quantity.setText(String.valueOf(totalQuantity));
                totalPrice = products.getPrice() * totalQuantity;
            }
        });

        addToCart.setOnClickListener(v -> {
            AddToCart();
        });
        favorite.setOnClickListener(v -> {
            if (checkFav) {
                db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                        .collection("Favorite")
                        .document(model.getDocumentID())
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                checkFav = false;
                                favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                                org.greenrobot.eventbus.EventBus.getDefault().postSticky(new ChangeListFavoriteEvent());
                            }
                        });

            } else {
                checkFav = true;
                favorite.setImageResource(R.drawable.ic_baseline_favorite_24);
                final HashMap<String, Object> cartMap = new HashMap<>();

                cartMap.put("productImg", products.getImg_url());
                cartMap.put("productName", products.getName());
                cartMap.put("productPrice", products.getPrice());

                db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                        .collection("Favorite").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(DetailedActivity.this, "Đã thêm vào sản phẩm yêu thích", Toast.LENGTH_SHORT).show();
                        org.greenrobot.eventbus.EventBus.getDefault().postSticky(new ChangeFavorite());
                        org.greenrobot.eventbus.EventBus.getDefault().postSticky(new ChangeListFavoriteEvent());
                    }
                });
            }
        });
    }
    private void AddToCart() {
        String saveCurrentDate, saveCurrentTime;
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        final HashMap<String,Object> cartMap = new HashMap<>();

        cartMap.put("productName",products.getName());
        cartMap.put("productImg",products.getImg_url());
        cartMap.put("productPrice",products.getPrice());
        cartMap.put("currentDate",saveCurrentDate);
        cartMap.put("currentTime",saveCurrentTime);
        cartMap.put("totalQuantity",quantity.getText().toString());
        cartMap.put("totalPrice",totalPrice);

        db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Toasty.success(DetailedActivity.this,"Thêm thành công", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    private void checkFavorite(){
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference yourCollRef = rootRef.collection("CurrentUser").document(auth.getCurrentUser().getUid()).collection("Favorite");
        Query query = yourCollRef.whereEqualTo("productName", products.getName());
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        model.setDocumentID(document.getId());
                        checkFav = true;
                        favorite.setImageResource(R.drawable.ic_baseline_favorite_24);
                    }
                } else {
                    checkFav = false;
                    favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                }
            }
        });

    }
    @org.greenrobot.eventbus.Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void eventChangeFavorite(ChangeFavorite event) {
        if (event != null) {
            checkFavorite();
        }
    }
}