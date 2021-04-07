package com.example.realcash.fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.realcash.R;

public class FragmentReplacerActivity extends AppCompatActivity {

    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_replacer);
        frameLayout = findViewById(R.id.framelayout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        int position = getIntent().getIntExtra("position" , 0);
        if(position ==1) {
            if(getSupportActionBar() != null)

            getSupportActionBar().setTitle("PhonePe");
            fragmentReplacer(new PhonepayFragment());
        }



    }

    private void fragmentReplacer(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left , android.R.anim.slide_out_right);

        fragmentTransaction.replace(frameLayout.getId() , fragment);

        fragmentTransaction.commit();
    }
}