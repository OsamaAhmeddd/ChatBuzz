package com.zabih.chatBuzz.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zabih.chatBuzz.Activities.Adapters.ViewPagerAdapter;
import com.zabih.chatBuzz.Activities.Fragments.AdvertiseFragment;
import com.zabih.chatBuzz.Activities.Fragments.EventFragment;
import com.zabih.chatBuzz.Activities.Models.UserModel;
import com.zabih.chatBuzz.R;

public class EventData extends AppCompatActivity {

    //private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    UserModel user_Data = null;
    private UserModel myUserInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_data);
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
        viewPagerAdapter.addFragment(new AdvertiseFragment(), "Announcement");
        viewPagerAdapter.addFragment(new EventFragment(), "Advertisement");


        viewPager.setAdapter(viewPagerAdapter);


    }

}
