package com.example.realcash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;

import static android.widget.LinearLayout.*;

public class inviteActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private String oppositeUID;
    private Toolbar toolbar;
    private TextView referCodeTv;
    private Button shareBtn , redeemBtn;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        init();


        reference = FirebaseDatabase.getInstance().getReference().child("Users");


        loadData();
        redeemAvailabilty();
        clickListener();
    }

    private void redeemAvailabilty(){
        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists() && snapshot.hasChild("redeemed")) {
                    boolean isAvailable = snapshot.child("redeemed").getValue(Boolean.class);
                    if (isAvailable) {
                        redeemBtn.setVisibility(View.GONE);
                        redeemBtn.setEnabled(false);
                    } else {
                        redeemBtn.setEnabled(true);
                        redeemBtn.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }



    private void init(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        referCodeTv = findViewById(R.id.referCodeTv);
        shareBtn = findViewById(R.id.shareBtn);
        redeemBtn = findViewById(R.id.redeemBtn);

         FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }
    private void loadData() {
        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String referCode = snapshot.child("referCode").getValue(String.class);
                referCodeTv.setText(referCode);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(inviteActivity.this, "Error" +error.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private  void clickListener(){
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String referCode = referCodeTv.getText().toString();

                String shareBody = "Hey , check out this best earning app that i am using. Join using my invite code to instantly get 200"
                        +"coins. My invite code is" + referCode +"\n"+
                        "Download from Play Store\n"+ "link of app"+getPackageName();


                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");

                intent.putExtra(Intent.EXTRA_TEXT , shareBody);
                startActivity(intent);
            }
        });

        redeemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = new EditText(inviteActivity.this);
                editText.setHint("abc12");
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT ,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                editText.setLayoutParams(layoutParams);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(inviteActivity.this);
                alertDialog.setTitle("Redeem code");

                alertDialog.setView(editText);
                alertDialog.setPositiveButton("Redeem", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String inputcode = editText.getText().toString();

                        if(TextUtils.isEmpty(inputcode)){
                            Toast.makeText(inviteActivity.this, "Input the valid code", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(inputcode.equals(referCodeTv.getText().toString())){
                            Toast.makeText(inviteActivity.this, "You can not use your own code", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        redeemQuery(inputcode , dialog);


                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                alertDialog.show();

            }
        });

    }


    private void redeemQuery(String inputcode, final DialogInterface dialog){
        Query query = reference.orderByChild("referCode").equalTo(inputcode);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    oppositeUID = dataSnapshot.getKey();

                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ProfileModel model = snapshot.child(oppositeUID).getValue(ProfileModel.class);
                            ProfileModel myModel = snapshot.child(user.getUid()).getValue(ProfileModel.class);


                            int coins = model.getCoins();
                            int updateCoins = coins + 200;


                            int myCoins = myModel.getCoins();
                            int myUpdate = myCoins + 200;

                            HashMap<String , Object> map = new HashMap<>();
                            map.put("coins" , updateCoins);

                            HashMap<String , Object> myMap = new HashMap<>();
                            myMap.put("coins" , myUpdate);

                            myMap.put("redeemed" , true);

                            reference.child(oppositeUID).updateChildren(map);
                            reference.child(user.getUid()).updateChildren(myMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            dialog.dismiss();
                                            Toast.makeText(inviteActivity.this, "Congrats", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(inviteActivity.this, "Error" +error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(inviteActivity.this, "Error" +error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}