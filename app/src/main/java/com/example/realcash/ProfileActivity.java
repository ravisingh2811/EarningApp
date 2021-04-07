package com.example.realcash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.realcash.model.ProfileModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private TextView coinsTv , nameTv , emailTv ,shareTv, redeemHistoryTv ,logoutTv , aboutus;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();

        getDataFromDatabase();
        clickListner();
    }
    private void init(){
        nameTv = findViewById(R.id.nameTv);
        emailTv = findViewById(R.id.emailTv);
        coinsTv = findViewById(R.id.coinsTv);
        shareTv = findViewById(R.id.shareTv);
        redeemHistoryTv =findViewById(R.id.redeemHistoryTv);
        logoutTv = findViewById(R.id.logoutTv);
        aboutus = findViewById(R.id.aboutus);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");

    }
    private void getDataFromDatabase() {
        reference.child(user.getUid()).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ProfileModel model = dataSnapshot.getValue(ProfileModel.class);

                        nameTv.setText(model.getName());
                        emailTv.setText(model.getEmail());
                        coinsTv.setText(String.valueOf(model.getCoins()));




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ProfileActivity.this, "Error:" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });

    }

    private void clickListner(){
        logoutTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(ProfileActivity.this , LoginActivity.class));
                finish();
            }
        });
        shareTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sharebody = "Check out the best earning app.Download " +getString(R.string.app_name)+
                        "from Play Store\n" + "link of app" + getPackageName();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT , sharebody);
                intent.setType("text/plain");
                startActivity(intent);
            }
        });
        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this , About.class));
                finish();

            }
        });

    }
}