package com.android45.orderdrinks.fragment;

import android.content.Intent;
import android.icu.number.Scale;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android45.orderdrinks.R;
import com.android45.orderdrinks.activity.SearchMainActivity;
import com.android45.orderdrinks.activity.ViewAllActivity;
import com.android45.orderdrinks.adapter.HomeCategoryAdapter;
import com.android45.orderdrinks.adapter.ProductsAdapter;
import com.android45.orderdrinks.adapter.SliderAdapter;
import com.android45.orderdrinks.models.HomeCategory;
import com.android45.orderdrinks.models.Products;
import com.android45.orderdrinks.models.SliderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class HomeFragment extends Fragment {
//
    ScrollView scrollView;
    ProgressBar progressBar;
    private TextView name;


    TextView search;

    private SliderAdapter adapter;
    private ArrayList<SliderModel> sliderDataArrayList;
    private SliderView sliderView;

    HomeCategoryAdapter categoryAdapter;
    List<HomeCategory> categoryList;

    ProductsAdapter popularAdapter,searchAdapter;
    List<Products> popularList,searchList;

    final List<SliderModel> listSlider = new ArrayList<>();

    RecyclerView recPopular,recCategory,rec_search;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        search=view.findViewById(R.id.textSearch);
        recCategory = view.findViewById(R.id.rec_list_item);
        recPopular = view.findViewById(R.id.rec_popular);
        rec_search = view.findViewById(R.id.rec_search);
        scrollView = view.findViewById(R.id.scrollView);
        progressBar = view.findViewById(R.id.progress_bar);
        name = view.findViewById(R.id.tvName);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();

//        reference.child("Users/name").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//              String nameUser = dataSnapshot.getValue(String.class);
//              name.setText(nameUser);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        searchList = new ArrayList<>();
        searchAdapter = new ProductsAdapter(getContext(),searchList);
        rec_search.setLayoutManager(new GridLayoutManager(getContext(),2));
        rec_search.setAdapter(searchAdapter);
        rec_search.setHasFixedSize(true);

        search.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), SearchMainActivity.class);
            startActivity(intent);
        });


        sliderDataArrayList = new ArrayList<>();
        sliderView = view.findViewById(R.id.slider);
        loadImages();



        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);
        recPopular.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL, false));
        popularList = new ArrayList<>();
        popularAdapter = new ProductsAdapter(getActivity(),popularList);
        recPopular.setAdapter(popularAdapter);
        db.collection("Popular")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Products products = document.toObject(Products.class);
                                popularList.add(products);
                                popularAdapter.notifyDataSetChanged();

                                progressBar.setVisibility(View.GONE);
                                scrollView.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Toasty.error(getActivity(),"Lỗi"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        recCategory.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        categoryList = new ArrayList<>();
        categoryAdapter = new HomeCategoryAdapter(getActivity(),categoryList);
        recCategory.setAdapter(categoryAdapter);
        db.collection("HomeCategory")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                HomeCategory homeCategory = document.toObject(HomeCategory.class);
                                categoryList.add(homeCategory);
                                categoryAdapter.notifyDataSetChanged();

                                progressBar.setVisibility(View.GONE);
                                scrollView.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Toasty.error(getActivity(),"Lỗi"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    private void loadImages() {
        db.collection("Slider").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                    SliderModel sliderData = documentSnapshot.toObject(SliderModel.class);
                    SliderModel model = new SliderModel();
                    model.setImgUrl(sliderData.getImgUrl());
                    sliderDataArrayList.add(model);
                    adapter = new SliderAdapter(getContext(), sliderDataArrayList);
                    sliderView.setSliderAdapter(adapter);
                    sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                    sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);

                    sliderView.setScrollTimeInSec(4);
                    sliderView.setAutoCycle(true);
                    sliderView.startAutoCycle();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Lỗi tải ảnh...", Toast.LENGTH_SHORT).show();
            }
        });
    }


}