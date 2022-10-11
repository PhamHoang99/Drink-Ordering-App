package com.android45.orderdrinks.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android45.orderdrinks.R;
import com.android45.orderdrinks.fragment.UserFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;

public class SignInActivity extends AppCompatActivity {

    private EditText mEmail, mPassword;
    private String name;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        mEmail = findViewById(R.id.edtEmailAddress);
        mPassword = findViewById(R.id.edtPassword);
        progressDialog = new ProgressDialog(this);


    }

    public void Register(View view) {
        startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
    }

    public void sigIn(View view) {
//        isUser();
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email)){
            Toasty.error(SignInActivity.this,"Email không được bỏ trống", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (TextUtils.isEmpty(password)){
            Toasty.error(SignInActivity.this,"Mật khẩu không được bỏ trống", Toast.LENGTH_SHORT).show();
            return;
        }


        progressDialog.setMessage("Vui lòng đợi...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()){

                    Toasty.success(SignInActivity.this,"Đăng nhập thành công!",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SignInActivity.this, MainActivity.class);
                    i.putExtra("email",email);
                    startActivity(i);
                    finish();
                }
                else {
                    Toasty.error(SignInActivity.this,"Tài khoản hoặc mật khẩu chưa đúng!", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }
}