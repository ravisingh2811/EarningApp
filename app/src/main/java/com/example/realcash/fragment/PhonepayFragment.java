package com.example.realcash.fragment;

import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.realcash.R;
import com.example.realcash.model.Phonepe;
import com.example.realcash.model.ProfileModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;


public class PhonepayFragment extends Fragment {

    private RadioGroup radioGroup;
    private TextView coinsTv;
    private Button withdrawBtn;
    DatabaseReference reference;

    FirebaseUser user;

    public PhonepayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_phonepay, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        init(view);
        loadData();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference().child("Users");

        clickListner();

    }



    private void loadData() {
        reference.child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ProfileModel model = snapshot.getValue(ProfileModel.class);

                coinsTv.setText(String.valueOf(model.getCoins()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error" +error.getMessage(), Toast.LENGTH_SHORT).show();
                getActivity().finish();

            }
        });

    }

    private void init(View view){
        radioGroup = view.findViewById(R.id.radioGroup);
        withdrawBtn = view.findViewById(R.id.submitBtn);
        coinsTv = view.findViewById(R.id.coinsTv);
        
    }
    private void clickListner() {
        withdrawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentCoins = Integer.parseInt(coinsTv.getText().toString());
                int  checkId = radioGroup.getCheckedRadioButtonId();
                switch (checkId){
                    case R.id.phonepay10:
                        phonepe(10 , currentCoins);

                        break;
                    case R.id.phonepay20:
                        phonepe(20 , currentCoins);


                }
            }
        });

    }

    private void phonepe(int phonepe , int currentCoins){
        if(phonepe == 10){

            if(currentCoins >= 3000){
                sendphonepe(1);
            }

        }
         else if(phonepe == 20){

            if(currentCoins >= 5000){
                sendphonepe(2);
            }

        }

    }
    DatabaseReference phonepeRef;
    Query query;
    private void  sendphonepe(int Amount){

        phonepeRef = FirebaseDatabase.getInstance().getReference().child("Request").child("phonepe");


        if(Amount == 1){

            query = phonepeRef.orderByChild("phonepe").equalTo(10);


        }else if(Amount == 2){
            query = phonepeRef.orderByChild("phonepe").equalTo(20);
        }
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Random random = new Random();


             int childCount = (int) snapshot.getChildrenCount();
             int rand = random.nextInt(childCount);
                Iterator iterator = snapshot.getChildren().iterator();
                for(int i =0; i<rand ; i++){
                    iterator.next();
                    DataSnapshot childSnap = (DataSnapshot) iterator.next();
                    Phonepe model = childSnap.getValue(Phonepe.class);
                    String id = model.getId();

                    String code = model.getPhonepeCode();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getContext(), "Error" + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }



}