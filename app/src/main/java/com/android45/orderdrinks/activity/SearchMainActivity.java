package com.android45.orderdrinks.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android45.orderdrinks.R;
import com.android45.orderdrinks.adapter.ProductsAdapter;
import com.android45.orderdrinks.models.Products;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class SearchMainActivity extends AppCompatActivity {

    private EditText edtSearch;
    private ImageButton imgSearch;
    private RecyclerView rec_Search;
    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private ProductsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_main);

        edtSearch = findViewById(R.id.editTextSearch);
        imgSearch=findViewById(R.id.edtSearch);
        rec_Search=findViewById(R.id.rec_Search);
        rec_Search.setLayoutManager(new GridLayoutManager(this,2));

        Query query = mDb.collection("AllProduct")
                .orderBy("name",Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Products> options = new FirestoreRecyclerOptions.Builder<Products>()
                .setQuery(query,Products.class)
                .build();
//        adapter=new ProductsAdapter(this,options);
        rec_Search.setAdapter(adapter);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Query query = mDb.collection("AllProduct")
                        .whereEqualTo("name",s.toString())
                        .orderBy("name",Query.Direction.ASCENDING);
                FirestoreRecyclerOptions<Products> options = new FirestoreRecyclerOptions.Builder<Products>()
                        .setQuery(query,Products.class)
                        .build();

            }
        });


    }

//    private void firebaseProductSearch() {
//        FirebaseRecyclerAdapter<Products,ProductViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>() {
////            @Override
////            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
////                Products.class,
////                R.layout.ic_list_product,
////                ProductViewHolder.class,
////            }
//
//            @NonNull
//            @Override
//            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                return null;
//            }
//        };
//    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }

    }
}