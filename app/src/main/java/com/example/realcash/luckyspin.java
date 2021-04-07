package com.example.realcash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
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

public class luckyspin extends AppCompatActivity {

    private ImageView roulette , cursor;
    private Button btn_spin;

    private TextView coin_tv;
    DatabaseReference reference;
    FirebaseUser user;
    FirebaseAuth auth;
    Toolbar toolbar;
    FirebaseDatabase firebaseDatabase;

    private static final float FACTOR=4.735f;

    Random random;
    int degree = 0, degrees_old = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luckyspin);

        roulette =  findViewById(R.id.roulette);
        cursor = findViewById(R.id.cursor);
        btn_spin = findViewById(R.id.btn_spin);
        coin_tv = findViewById(R.id.coin_tv);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        random = new Random();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        getDataFromDatabase();


        btn_spin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                degrees_old = degree % 360;
                degree = random.nextInt(3600) + 720;
                final RotateAnimation rotate = new RotateAnimation(degree , degrees_old , RotateAnimation.RELATIVE_TO_SELF,
                        0.5f , RotateAnimation.RELATIVE_TO_SELF , 0.5f);

                rotate.setDuration(3600);
                rotate.setFillAfter(true);
                rotate.setInterpolator(new DecelerateInterpolator());
                rotate.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        updateCoins();

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                roulette.startAnimation(rotate);

            }
        });


    }

    private void getDataFromDatabase() {
        reference.child(user.getUid()).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ProfileModel model = dataSnapshot.getValue(ProfileModel.class);


                        coin_tv.setText(String.valueOf(model.getCoins()));




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(luckyspin.this, "Error:" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });

    }

    private String currentNumber(int degrees)
    {
        String text="";
        //1
        if (degrees >=(FACTOR*1) && degrees<FACTOR *3)
        {
            text="27";
        }

        //2
        if (degrees >=(FACTOR*3) && degrees<FACTOR *5)
        {
            text="10";
        }

        //3
        if (degrees >=(FACTOR*5) && degrees<FACTOR *7)
        {
            text="35";
        }

        //4
        if (degrees >=(FACTOR *7) && degrees<FACTOR *9)
        {
            text="29";
        }

        //5
        if (degrees >=(FACTOR *9) && degrees<FACTOR *11)
        {
            text="12";
        }

        //6
        if (degrees >=(FACTOR *11) && degrees<FACTOR *13)
        {
            text="8";
        }
        //7
        if (degrees >=(FACTOR *13) && degrees<FACTOR *15)
        {
            text="19";
        }

        //8
        if (degrees >=(FACTOR *15) && degrees<FACTOR *17)
        {
            text="31";
        }

        //9
        if (degrees >=(FACTOR *17) && degrees<FACTOR *19)
        {
            text="18";
        }

        //10
        if (degrees >=(FACTOR *19) && degrees<FACTOR *21)
        {
            text="6";
        }

        //11
        if (degrees >=(FACTOR *21) && degrees<FACTOR *23)
        {
            text="21";
        }

        //12
        if (degrees >=(FACTOR *23) && degrees<FACTOR *25)
        {
            text="33";
        }

        //13
        if (degrees >=(FACTOR *25) && degrees<FACTOR *27)
        {
            text="16";
        }

        //14
        if (degrees >=(FACTOR *27) && degrees<FACTOR *29)
        {
            text="4";
        }

        //15
        if (degrees >=(FACTOR *29) && degrees<FACTOR *31)
        {
            text="23";
        }

        //16
        if (degrees >=(FACTOR *31) && degrees<FACTOR *33)
        {
            text="35";
        }

        //17
        if (degrees >=(FACTOR *33) && degrees<FACTOR *35)
        {
            text="14";
        }

        //18
        if (degrees >=(FACTOR *35) && degrees<FACTOR *37)
        {
            text="2";
        }

        //19
        if (degrees >=(FACTOR *37) && degrees<FACTOR *39)
        {
            text="0";
        }

        //20
        if (degrees >=(FACTOR *39) && degrees<FACTOR *41)
        {
            text="28";
        }

        //21
        if (degrees >=(FACTOR *41) && degrees<FACTOR *43)
        {
            text="9";
        }
        //22
        if (degrees >=(FACTOR *43) && degrees<FACTOR *45)
        {
            text="26";
        }
        //23
        if (degrees >=(FACTOR *45) && degrees<FACTOR *47)
        {
            text="30";
        }
        //24
        if (degrees >=(FACTOR *47) && degrees<FACTOR *49)
        {
            text="11";
        }
        //25
        if (degrees >=(FACTOR *49) && degrees<FACTOR *51)
        {
            text="7";
        }
        //26
        if (degrees >=(FACTOR *51) && degrees<FACTOR *53)
        {
            text="20";
        }
        //27
        if (degrees >=(FACTOR *53) && degrees<FACTOR *55)
        {
            text="32";
        }
        //28
        if (degrees >=(FACTOR *55) && degrees<FACTOR *57)
        {
            text="17";
        }
        //29
        if (degrees >=(FACTOR *57) && degrees<FACTOR *59)
        {
            text="5";
        }
        //30
        if (degrees >=(FACTOR *59) && degrees<FACTOR *61)
        {
            text="22";
        }
        //31
        if (degrees >=(FACTOR *61) && degrees<FACTOR *63)
        {
            text="34";
        }
        //32
        if (degrees >=(FACTOR *63) && degrees<FACTOR *65)
        {
            text="15";
        }
        //33
        if (degrees >=(FACTOR *65) && degrees<FACTOR *67)
        {
            text="3";
        }
        //34
        if (degrees >=(FACTOR *67) && degrees<FACTOR *69)
        {
            text="24";
        }
        //35
        if (degrees >=(FACTOR *69) && degrees<FACTOR *71)
        {
            text="36";
        }
        //36
        if (degrees >=(FACTOR *71) && degrees<FACTOR *73)
        {
            text="13";
        }
        //37
        if (degrees >=(FACTOR *73) && degrees<FACTOR *75)
        {
            text="1";
        }
        //38
        if (degrees >=(FACTOR *75) && degrees<FACTOR *77)
        {
            text="0";
        }



        return text;
    }

    private void updateCoins(){
        reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                ProfileModel model = snapshot.getValue(ProfileModel.class);
//
//                  String c=snapshot.child ( "coins" ).getValue ().toString ();

                int coins= Integer.parseInt(coin_tv.getText().toString()) ;
                int updateCoins=coins+Integer.parseInt ( currentNumber ( 360-(degree %  360) ) );

                HashMap<String, Object> map = new HashMap<>();
                map.put("coins", updateCoins);

                reference.child(user.getUid()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(luckyspin.this, "Coins Added " + currentNumber(( 360-(degree %  360) )), Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(luckyspin.this, "Error" , Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(luckyspin.this, "Error" , Toast.LENGTH_SHORT).show();

            }
        });
    }
}