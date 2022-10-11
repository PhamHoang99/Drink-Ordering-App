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
import com.android45.orderdrinks.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import es.dmoral.toasty.Toasty;

public class SignUpActivity extends AppCompatActivity {
    private EditText edtEmail, edtPassword,edtRepeatPass;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sig_up);
        edtEmail = findViewById(R.id.editTextTextEmailAddress);
        edtPassword = findViewById(R.id.editTextTextPassword);
        edtRepeatPass = findViewById(R.id.editTextRepeatPass);
        progressDialog = new ProgressDialog(this);

    }

    public void LogIn(View view) {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String repeatPass = edtRepeatPass.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            Toasty.error(SignUpActivity.this,"Không được bỏ trống", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (TextUtils.isEmpty(password)){
            Toasty.error(SignUpActivity.this,"Không được bỏ trống", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (TextUtils.isEmpty(repeatPass)){
            Toasty.error(SignUpActivity.this,"Không được bỏ trống", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (!password.equals(repeatPass)){
            Toasty.error(SignUpActivity.this,"Mật khẩu không trùng!", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (edtPassword.length() < 3 & edtRepeatPass.length()<3){
            Toasty.error(SignUpActivity.this,"Ít nhất 5 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Vui lòng đợi...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    UserModel userModel = new UserModel(email,password);
                    db.getReference().child("Users").setValue(userModel);
                    Toasty.success(SignUpActivity.this,"Đăng ký thành công!",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SignUpActivity.this, SignInActivity.class);
                    startActivity(i);
                    finish();
                }
                else {
                    Toasty.error(SignUpActivity.this,"Đăng ký thất bại!",Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });

    }

    public void SignIn(View view) {
        startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
    }
}