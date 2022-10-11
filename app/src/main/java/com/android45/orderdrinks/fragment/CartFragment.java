package com.android45.orderdrinks.fragment;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android45.orderdrinks.R;
import com.android45.orderdrinks.activity.PlacedOrderActivity;
import com.android45.orderdrinks.adapter.CartModelAdapter;
import com.android45.orderdrinks.models.CartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartFragment extends Fragment {

    FirebaseFirestore db;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth auth;
    Button buynow;
    public static Dialog dialog;

    ConstraintLayout constraintLayoutone, constraintLayouttwo;

    TextView overTotalAmount;

    RecyclerView recyclerView;
    CartModelAdapter cartModelAdapter;
    List<CartModel> cartModelList;

    ProgressBar progressBar;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cart,container,false);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        overTotalAmount = root.findViewById(R.id.totalPrice);
        constraintLayoutone = root.findViewById(R.id.constraintOne);
        constraintLayouttwo = root.findViewById(R.id.constraint);
        buynow = root.findViewById(R.id.btnAddCart);

        progressBar =root.findViewById(R.id.progress_bar_cart);
        progressBar.setVisibility(View.VISIBLE);
        constraintLayoutone.setVisibility(View.VISIBLE);
        constraintLayouttwo.setVisibility(View.GONE);


        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(mMessageReceiver,new IntentFilter("MyTotalAmount"));
        recyclerView = root.findViewById(R.id.rec_Cart);
        recyclerView.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        cartModelList = new ArrayList<>();
        cartModelAdapter = new CartModelAdapter(getActivity(),cartModelList);
        recyclerView.setAdapter(cartModelAdapter);
        db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot documentSnapshot: task.getResult().getDocuments()){
                        String documentId = documentSnapshot.getId();
                        CartModel cartModel = documentSnapshot.toObject(CartModel.class);
                        cartModel.setDocumentID(documentId);
                        cartModelList.add(cartModel);
                        cartModelAdapter.notifyDataSetChanged();
                        constraintLayoutone.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        constraintLayouttwo.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        buynow.setOnClickListener(v -> {

            openDialog(Gravity.BOTTOM);

            constraintLayoutone.setVisibility(View.INVISIBLE);
            constraintLayouttwo.setVisibility(View.GONE);
        });

        return root;
    }

    private void openDialog(int gravity) {
         dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_update_tt);

        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
//        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttractive = window.getAttributes();
        windowAttractive.gravity = gravity;
        window.setAttributes(windowAttractive);

        if (Gravity.BOTTOM==gravity){
            dialog.setCancelable(true);
        }else {
            dialog.setCancelable(false);
        }

        EditText edtName = dialog.findViewById(R.id.edtNameDialog);
        EditText edtSdt = dialog.findViewById(R.id.edtSdtDialog);
        EditText edtDiachi = dialog.findViewById(R.id.edtDiaChiDialog);

        Button dongY = dialog.findViewById(R.id.DongY);
        dongY.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String sdt = edtSdt.getText().toString().trim();
            String diachi = edtDiachi.getText().toString().trim();

            HashMap User = new HashMap<>();
            User.put("name",name);
            User.put("phone",sdt);
            User.put("address",diachi);

            DatabaseReference reference = database.getReference().child("Users");
            reference.child("password").updateChildren(User).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(getActivity(),"Cập nhật thành công",Toast.LENGTH_SHORT).show();
                    }else
                    {
                        Toast.makeText(getActivity(),"Cập nhật không thành công",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            Intent intent = new Intent(getContext(), PlacedOrderActivity.class);
            intent.putExtra("itemList", (Serializable) cartModelList);
            intent.putExtra("name",name);
            intent.putExtra("phone",sdt);
            intent.putExtra("address",diachi);
            startActivity(intent);
        });
        dialog.show();

    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int totalBill = intent.getIntExtra("totalAmount",0);
            overTotalAmount.setText(totalBill + " VND");
        }
    };

}