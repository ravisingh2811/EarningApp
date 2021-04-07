package com.example.realcash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
   private EditText etEmailLogin , etPasswordLogin ;
   private Button btn_signIn;
   private FirebaseAuth auth;
   private TextView tvAlreadyAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        auth = FirebaseAuth.getInstance();
        clickListner();

    }

    private void init(){
        etEmailLogin = findViewById(R.id.etEmailLogin);
        etPasswordLogin = findViewById(R.id.etPasswordLogin);
        tvAlreadyAccount = findViewById(R.id.tvAlreadyAccount);
        btn_signIn = findViewById(R.id.btn_signIn);
    }
    private void clickListner(){
        tvAlreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this , SignupActivity.class));
                finish();
            }
        });
        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmailLogin.getText().toString();
                String password = etPasswordLogin.getText().toString();
                if (TextUtils.isEmpty(email)){
                    etEmailLogin.setError("Invaild email");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    etPasswordLogin.setError("Requried");
                    return;
                }
                signin(email , password);



            }
        });
    }
    private void signin(String email, String password){
        auth.signInWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()){
                startActivity(new Intent(LoginActivity.this , MainActivity.class));
                finish();

            }else {
                Toast.makeText(LoginActivity.this, "Error:"+ task.getException().getMessage() , Toast.LENGTH_SHORT).show();
            }
            }
        });

    }
}