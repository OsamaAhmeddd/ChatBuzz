package com.zabih.chatBuzz.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.zabih.chatBuzz.Activities.Adapters.ViewPagerAdapter;
import com.zabih.chatBuzz.Activities.Models.UserModel;
import com.zabih.chatBuzz.R;

public class LostFounDViewpager extends AppCompatActivity {

    //private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    UserModel user_Data = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_data);
        initializations();
    }


    private void initializations() {
        //  toolbar = findViewById(R.id.chat_toolbar);
        tabLayout = findViewById(R.id.chat_tablayout);
        viewPager = findViewById(R.id.chat_ViewPager);

        //Setting toolbar
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        setupViewPager(viewPager);
        user_Data = (UserModel) getIntent().getSerializableExtra("info");

        tabLayout.setupWithViewPager(viewPager);

    }
    public UserModel sendData() {
        return user_Data;
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new lostFragment(), "Lost");
        viewPagerAdapter.addFragment(new FoundFragment(), "Found");


        viewPager.setAdapter(viewPagerAdapter);


    }
}
