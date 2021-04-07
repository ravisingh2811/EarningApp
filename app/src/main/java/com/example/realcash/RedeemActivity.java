package com.example.realcash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.realcash.fragment.FragmentReplacerActivity;
import com.example.realcash.model.ProfileModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

public class RedeemActivity extends AppCompatActivity {

   Toolbar toolbar;
   EditText method , number , coins ;
   Button submit;
   TextView coinsTv;
    DatabaseReference reference;
    FirebaseUser user;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);



        init();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        getDataFromDatabase();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String m = method.getText().toString();
                String n = number.getText().toString();
                String  c = coins.getText().toString();

                if(m.isEmpty() || n.isEmpty() || c.isEmpty()){
                    Toast.makeText(RedeemActivity.this, "This Field Cannot be Empty", Toast.LENGTH_SHORT).show();
                }else {

                    reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            int currentCoins= Integer.parseInt(coinsTv.getText().toString());
                            String edit_coins = coins.getText().toString();


                            if(Integer.parseInt(edit_coins) >= 5000 ){
                                if(currentCoins >=  Integer.parseInt(edit_coins)){


                                request(currentCoins , edit_coins);
                                }else{
                                    Toast.makeText(RedeemActivity.this, "Not enough coins to redeem ", Toast.LENGTH_SHORT).show();

                                }
                            }else
                            {
                                Toast.makeText(RedeemActivity.this, "Please Enter value greater than or equal to  5000", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

    private void init(){
        submit = findViewById(R.id.submit);
        number = findViewById(R.id.number);
        method = findViewById(R.id.method);
        coins = findViewById(R.id.coins);
        coinsTv = findViewById(R.id.coinsTv);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }



    private void getDataFromDatabase() {
        reference.child(user.getUid()).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ProfileModel model = dataSnapshot.getValue(ProfileModel.class);


                        coinsTv.setText(String.valueOf(model.getCoins()));




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(RedeemActivity.this, "Error:" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });

    }

    private void request(int currentCoins , String edit_coins){
        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int updateCoins = currentCoins - Integer.parseInt(edit_coins);
                String date = DateFormat.getDateInstance().format(new Date());
                String time = DateFormat.getTimeInstance().format(new Date());

                HashMap <String , Object> map = new HashMap<>();
                map.put("coins" , updateCoins);
                map.put("method" , method.getText().toString());
                map.put("number" , number.getText().toString());
                map.put("withdrawCoins" , edit_coins );
                map.put("date" , date);
                map.put("time" , time);

                reference.child(user.getUid()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RedeemActivity.this, "Request Has been send Please Wait", Toast.LENGTH_SHORT).show();
                        }else
                        {
                            Toast.makeText(RedeemActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RedeemActivity.this, "Database Error", Toast.LENGTH_SHORT).show();

            }
        });




    }
}