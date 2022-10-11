package com.android45.orderdrinks.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android45.orderdrinks.R;
import com.android45.orderdrinks.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class UserFragment extends Fragment {

    EditText nameUser, emailUser, phonenumberUser, addressUser;
    RoundedImageView avt;
    Button capnhat;
    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;
    UserModel userModel ;

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        avt = view.findViewById(R.id.imgProfile);
        nameUser = view.findViewById(R.id.edtName);
        emailUser = view.findViewById(R.id.edtEmail);
        phonenumberUser = view.findViewById(R.id.edtPhone);
        addressUser = view.findViewById(R.id.edtAddress);
        capnhat = view.findViewById(R.id.btnSave);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        avt.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent,33);
        });
        capnhat.setOnClickListener(v -> {


            String email = emailUser.getText().toString().trim();
            String name = nameUser.getText().toString().trim();
            String phoneNumber = phonenumberUser.getText().toString();
            String address = addressUser.getText().toString().trim();
            updateProfile(name,email,phoneNumber,address);
        });
        return view;
    }

    private void updateProfile(String name, String email, String phoneNumber,String address) {
        HashMap User = new HashMap<>();
        User.put("email",email);
        User.put("phone",phoneNumber);
        User.put("address",address);

        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(name).updateChildren(User).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    nameUser.setText("");
                    phonenumberUser.setText("");
                    addressUser.setText("");
//                    sdt.setText("");
//                    diachi.setText("");
                    Toast.makeText(getActivity(),"Cập nhật thành công",Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(getActivity(),"Cập nhật không thành công",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getData() != null){
            Uri profile = data.getData();
            avt.setImageURI(profile);
            final StorageReference reference = storage.getReference().child("profile_picture")
                    .child(FirebaseAuth.getInstance().getUid());
            reference.putFile(profile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(),"Tải lên thành công", Toast.LENGTH_SHORT).show();

                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                                    .child("name").setValue(profile.toString());
                            Toasty.success(getContext(),"Uploaded img",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

    }
}