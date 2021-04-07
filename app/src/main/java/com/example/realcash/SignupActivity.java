package com.example.realcash;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class SignupActivity extends AppCompatActivity {
    private Button btn_signup;
    private EditText etUserName , etEmailLogin, etnumber , etPasswordLogin;
    private FirebaseAuth auth;
    private TextView tvAlreadyAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView (R.layout.activity_signup);
        init();
        auth = FirebaseAuth.getInstance();

       clickListener();
    }
    private void init(){
        btn_signup = findViewById(R.id.btn_signUp);
        etUserName = findViewById(R.id.etUserName);
        etEmailLogin = findViewById(R.id.etEmailLogin);
        etnumber= findViewById(R.id.etnumber);
        etPasswordLogin = findViewById(R.id.etPasswordLogin);
        tvAlreadyAccount = findViewById(R.id.tvAlreadyAccount);

    }

    private void clickListener(){
        tvAlreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this , LoginActivity.class));
                finish();
            }
        });
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String name = etUserName.getText().toString();
                String email = etEmailLogin.getText().toString();
                String number = etnumber.getText().toString();
                String pass = etPasswordLogin.getText().toString();

                if(name.isEmpty()){
                    etUserName.setError("Required");
                    return;
                }
                if(email.isEmpty()){
                    etEmailLogin.setError("Required");
                    return;
                }
                if(number.isEmpty()){
                    etnumber.setError("Required");
                    return;
                }
                if(pass.isEmpty()){
                    etPasswordLogin.setError("Required");
                    return;
                }
                createAccount(email, pass);
            }
        });
    }
    private void createAccount(String email , String password){

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = auth.getCurrentUser();
                    upadteUi(user , email);


                }else {
                    Toast.makeText(SignupActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    private void upadteUi(FirebaseUser user, String email){
        String refer = email.substring(0 , email.lastIndexOf("@"));
        String referCode = refer.replace("." , "");

        HashMap<String, Object> map = new HashMap<>();
        map.put("name", etUserName.getText().toString());
        map.put("email" , etEmailLogin.getText().toString());
        map.put("images" , " ");
        map.put("uid" , user.getUid());
        map.put("coins", 0 );
        map.put("mobile" , etnumber.getText().toString());
        map.put("referCode" , referCode);
        map.put("spins" , 2);

        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy" , Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH , -1);

        Date previousDate = calendar.getTime();
        String dateString = dateFormat.format(previousDate);

        FirebaseDatabase.getInstance().getReference().child("Daily Check").child(user.getUid()).child("date").setValue(dateString);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.child(user.getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignupActivity.this, "Your Account is Successfully created Start earning now", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignupActivity.this , LoginActivity.class));
                    finish();
                }else {
                    Toast.makeText(SignupActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });



    }


}