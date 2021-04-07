package com.example.realcash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

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

import java.util.HashMap;
import java.util.Random;

public class dice extends AppCompatActivity {

    TextView coinsTv;
    ImageView dice;
    Button roll , pickNumber;
    Toolbar toolbar;
    Random random;
    Dialog dialog;
    DatabaseReference reference;
    FirebaseUser user;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice);
        init();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        getDataFromDatabase();

        roll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rolling();
            }
        });

        pickNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBox();
            }

        });

    }

    private void  init(){
        coinsTv = findViewById(R.id.coinsTv);
        dice = findViewById(R.id.dice);
        roll = findViewById(R.id.roll);
        pickNumber = findViewById(R.id.pick_number);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        random = new Random();
        dialog = new Dialog(this);



    }

    private void rolling(){

        String value = pickNumber.getText().toString();
        int randomNumber  = random.nextInt(6) +1;
        switch (randomNumber){
            case 1:
            dice.setImageResource(R.drawable.dice1);
            break;
            case 2:
                dice.setImageResource(R.drawable.dice2);
                break;
            case 3:
                dice.setImageResource(R.drawable.dice3);
                break;
            case 4:
                dice.setImageResource(R.drawable.dice4);
                break;
            case 5:
                dice.setImageResource(R.drawable.dice5);
                break;
            case 6:
                dice.setImageResource(R.drawable.dice6);
                break;
        }

        if(Integer.parseInt(value) == randomNumber){
            addCoins();

        }
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
                        Toast.makeText(dice.this, "Error:" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });

    }

    private void dialogBox(){

        dialog.setContentView(R.layout.dice_file);
        dialog.setTitle("Pick Number");

        final TextView  one = dialog.findViewById(R.id.one);
        final TextView  two = dialog.findViewById(R.id.two);
        final TextView  three = dialog.findViewById(R.id.three);
        final TextView  four = dialog.findViewById(R.id.four);
        final TextView  five = dialog.findViewById(R.id.five);
        final TextView  six = dialog.findViewById(R.id.six);

        dialog.setCancelable(false);
        dialog.show();

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = one.getText().toString();
                dialog.dismiss();
                pickNumber.setText(value);
            }

        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = two.getText().toString();
                dialog.dismiss();
                pickNumber.setText(value);
            }

        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = three.getText().toString();
                dialog.dismiss();
                pickNumber.setText(value);
            }

        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = four.getText().toString();
                dialog.dismiss();
                pickNumber.setText(value);
            }

        });
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = five.getText().toString();
                dialog.dismiss();
                pickNumber.setText(value);
            }

        });
        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = six.getText().toString();
                dialog.dismiss();
                pickNumber.setText(value);
            }

        });


    }

    private void addCoins(){
        reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int coins= Integer.parseInt(coinsTv.getText().toString()) ;
                int updateCoins=coins+ 20;

                HashMap<String, Object> map = new HashMap<>();
                map.put("coins", updateCoins);

                reference.child(user.getUid()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(dice.this, "Coins Added" , Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(dice.this, "Error" , Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
