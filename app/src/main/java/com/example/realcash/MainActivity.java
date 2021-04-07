package com.example.realcash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.example.realcash.model.ProfileModel;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private CardView dailyCheckCard , luckySpin , taskCard , referCard , wallet , watchCard , AboutCard;
    private CircleImageView profile_Image;
    private TextView coinsTv , nameTv , emailTv , useprofile;
    private   DatabaseReference reference;
    private FirebaseUser user;
    FirebaseDatabase firebaseDatabase;

    Internet internet;
    AdView adView;
     private InterstitialAd minterstitialAd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        internet = new Internet(MainActivity.this);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        MobileAds.initialize(this);
//        AdView adView = new AdView(this);
//
//        adView.setAdSize(AdSize.BANNER);
//
//        adView.setAdUnitId(getString(R.string.admob_banner_id));

        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        adView = findViewById(R.id.banner_ad);
        AdRequest adsRequest = new AdRequest.Builder().build();
        adView.loadAd(adsRequest);

        loadInterstitialAd();
//        checkInternet();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference().child("Users");
        getDataFromDatabase();
        clickListener();

    }
    private void clickListener(){
        useprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(MainActivity.this , ProfileActivity.class ));

            }
        });
        referCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this , inviteActivity.class ));

            }
        });
        dailyCheckCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dailyCheck();
            }
        });
        luckySpin.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , luckyspin.class);
                startActivity(intent);
        }

       });
        taskCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , dice.class);
                startActivity(intent);
            }

        });
        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this , RedeemActivity.class);
                startActivity(intent);
            }

        });



    }


    private void init(){
        dailyCheckCard = findViewById(R.id.dailyCheckCard);
        luckySpin = findViewById(R.id.luckySpin);
        taskCard = findViewById(R.id.taskCard);
        referCard = findViewById(R.id.referCard);
        wallet = findViewById(R.id.wallet);
        watchCard = findViewById(R.id.watchCard);
//        AboutCard= findViewById(R.id.AboutCard);
        coinsTv = findViewById(R.id.coinsTv);
        nameTv = findViewById(R.id.nameTv);
        emailTv = findViewById(R.id.emailTv);
        useprofile = findViewById(R.id.userprofile);
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
                Toast.makeText(MainActivity.this, "Error:" + error.getMessage(), Toast.LENGTH_SHORT).show();
                finish();

            }
        });

    }

//    private void checkInternet(){
//        if(internet.isConnected()){
//            new isInterntActive().execute();
//        }else {
//
//            Toast.makeText(MainActivity.this, "no internet Connected", Toast.LENGTH_SHORT).show();
//
//        }
//    }

    private void dailyCheck(){
//        if(internet.isConnected()) {

            final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            sweetAlertDialog.setTitleText("Please wait");
            sweetAlertDialog.setCancelable(false);
            sweetAlertDialog.show();

            final Date currentDate = Calendar.getInstance().getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            final String date = dateFormat.format(currentDate);


            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("Daily Check").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {


                    if (snapshot.exists()) {
                        String dbDataString = snapshot.child("date").getValue(String.class);
                        try {
                            assert dbDataString != null;
                            Date dbData = dateFormat.parse(dbDataString);

                            String xDate = dateFormat.format(currentDate);
                            Date date = dateFormat.parse(xDate);

                            if (date.after(dbData) && date.compareTo(dbData) != 0) {

                                reference.child("Users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        ProfileModel model = snapshot.getValue(ProfileModel.class);

                                        int currentCoins = model.getCoins();
                                        int update = currentCoins + 30;

                                        int spinC = model.getSpins();
                                        int updateSpin = spinC + 2;

                                        HashMap<String, Object> map = new HashMap<>();
                                        map.put("coins", update);
                                        map.put("spins", updateSpin);

                                        reference.child("Users").child(user.getUid()).updateChildren(map);

                                        Date newDate = Calendar.getInstance().getTime();
                                        String newDateString = dateFormat.format(newDate);

                                        HashMap<String, String> datamap = new HashMap<>();
                                        datamap.put("date", newDateString);

                                        reference.child("Daily Check").child(user.getUid()).setValue(datamap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                                sweetAlertDialog.setTitleText("Success");
                                                sweetAlertDialog.setContentText("Congratulation Coins is added successfully ");
                                                sweetAlertDialog.setConfirmButton("Dismiss", new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                        sweetAlertDialog.dismissWithAnimation();
                                                    }
                                                }).show();

                                            }
                                        });


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(MainActivity.this, "Error" + error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });


                            } else {
                                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                                sweetAlertDialog.setTitleText("Failed");
                                sweetAlertDialog.setContentText("You have already rewared, come back tomorrow");
                                sweetAlertDialog.setConfirmButton("OK", null);
                                sweetAlertDialog.show();

                            }
                        } catch (ParseException e) {
                            e.printStackTrace();

                            sweetAlertDialog.dismissWithAnimation();
                        }
                    } else {
                        sweetAlertDialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
                        sweetAlertDialog.setTitleText("System busy");
                        sweetAlertDialog.setContentText("System is busy , please try again later");
                        sweetAlertDialog.setConfirmButton("Dismiss", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismissWithAnimation();

                            }
                        });
                        sweetAlertDialog.show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MainActivity.this, "Error" + error.getMessage(), Toast.LENGTH_SHORT).show();

                    sweetAlertDialog.dismissWithAnimation();
                }
            });
//        }
//
//        else
//        {
//            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show();
//        }
    }

//    class isInterntActive extends AsyncTask<Void , Void , String>{
//
//        @Override
//        protected String doInBackground(Void... voids) {
//
//            InputStream inputStream = null;
//            String json = "";
//
//            try {
//                String strURL = "";
//                URL url = new URL(strURL);
//                URLConnection urlConnection = url.openConnection();
//
//                urlConnection.setDoOutput(true);
//
//                inputStream = urlConnection.getInputStream();
//                json = "success";
//
//            }catch (Exception e){
//                e.printStackTrace();
//                json = "failed";
//            }
//            return json;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            if(s != null){
//                if (s.equals("success")){
//                    Toast.makeText(MainActivity.this, "Internet Connected", Toast.LENGTH_SHORT).show();
//
//                }else {
//                    Toast.makeText(MainActivity.this, "no internet Connected", Toast.LENGTH_SHORT).show();
//
//                }
//            }else {
//                Toast.makeText(MainActivity.this, "NO internet", Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        @Override
//        protected void onPreExecute() {
//            Toast.makeText(MainActivity.this, "Validating Internet", Toast.LENGTH_SHORT).show();
//            super.onPreExecute();
//        }
//    }


    private void loadInterstitialAd(){
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                minterstitialAd = interstitialAd;
                Log.i("TAG", "onAdLoaded");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Log.i("TAG", loadAdError.getMessage());
                minterstitialAd = null;
            }
        });
        minterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when fullscreen content is dismissed.
                Log.d("TAG", "The ad was dismissed.");
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when fullscreen content failed to show.
                Log.d("TAG", "The ad failed to show.");
            }

            @Override
            public void onAdShowedFullScreenContent() {
                // Called when fullscreen content is shown.
                // Make sure to set your reference to null so you don't
                // show it a second time.
                minterstitialAd = null;
                Log.d("TAG", "The ad was shown.");
            }
        });



    }



}