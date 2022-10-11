package com.android45.orderdrinks.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android45.orderdrinks.R;
import com.android45.orderdrinks.adapter.OrderAdapter;
import com.android45.orderdrinks.fragment.CartFragment;
import com.android45.orderdrinks.models.CartModel;
import com.android45.orderdrinks.models.OrderModel;
import com.android45.orderdrinks.models.Products;
import com.android45.orderdrinks.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class PlacedOrderActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    FirebaseAuth auth;
    TextView name, sdt, diachi;
    ImageView back;
    TextView overTotalAmount, tog, sum;
    int totalPrice = 0;
    Button buyNow;
    OrderModel orderModel;

    RecyclerView rec_order;
    OrderAdapter orderAdapter;
    List<OrderModel> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placed_order);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        rec_order = findViewById(R.id.rec_order);
        overTotalAmount = findViewById(R.id.sum);
        tog = findViewById(R.id.tog);
        sum = findViewById(R.id.sumThanhtoan);
        buyNow = findViewById(R.id.buyNow);
        buyNow.setOnClickListener(v -> {
//            Toasty.success(this, "Order thành công");
            CartFragment.dialog.dismiss();
            Toasty.success(PlacedOrderActivity.this, "Đơn hàng của bạn đã được đặt thành công", Toast.LENGTH_SHORT).show();
            finish();
        });

        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mMessageReceiver, new IntentFilter("MyTotalAmount"));
        list = new ArrayList<>();
        rec_order.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        orderAdapter = new OrderAdapter(this, list);
        rec_order.setAdapter(orderAdapter);

        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("MyOrder").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                        String documentId = documentSnapshot.getId();
                        OrderModel orderModel = documentSnapshot.toObject(OrderModel.class);
                        orderModel.setDocumentID(documentId);
                        list.add(orderModel);
                        orderAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        name = findViewById(R.id.Hoten);
        sdt = findViewById(R.id.Sdt);
        diachi = findViewById(R.id.DiaChi);
        back = findViewById(R.id.quayLai);

        back.setOnClickListener(v -> {
            finish();
        });


        List<CartModel> list = (ArrayList<CartModel>) getIntent().getSerializableExtra("itemList");

        Intent i = getIntent();
        String ten = i.getStringExtra("name");
        String sdthoai = i.getStringExtra("phone");
        String diaChi = i.getStringExtra("address");

        name.setText(ten);
        sdt.setText(sdthoai);
        diachi.setText(diaChi);

        if (list != null && list.size() > 0) {
            for (CartModel cartModel : list) {
                final HashMap<String, Object> cartMap = new HashMap<>();

                cartMap.put("productName", cartModel.getProductName());
                cartMap.put("productImg", cartModel.getProductImg());
                cartMap.put("productPrice", cartModel.getProductPrice());
                cartMap.put("currentDate", cartModel.getCurrentDate());
                cartMap.put("currentTime", cartModel.getCurrentTime());
                cartMap.put("totalQuantity", cartModel.getTotalQuantity());
                cartMap.put("totalPrice", cartModel.getTotalPrice());
                cartMap.put("name",ten);
                cartMap.put("phone", sdthoai);
                cartMap.put("address", diaChi);


                firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                        .collection("MyOrder").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                    }
                });
            }
        }

    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int totalBill = intent.getIntExtra("totalAmount", 0);

            tog.setText(totalBill + " VND");
            totalBill = 20000 + totalBill;
            sum.setText(totalBill + "VND");
            overTotalAmount.setText(totalBill + " VND");

        }
    };
}