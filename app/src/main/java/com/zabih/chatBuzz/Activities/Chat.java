package com.zabih.chatBuzz.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zabih.chatBuzz.Activities.Adapters.ViewPagerAdapter;
import com.zabih.chatBuzz.Activities.Fragments.GroupChatList;
import com.zabih.chatBuzz.Activities.Fragments.MyChat;
import com.zabih.chatBuzz.Activities.Fragments.SearchTab;
import com.zabih.chatBuzz.R;

public class Chat extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("users").child(uid).child("role").equalTo("HOD").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                flag = 1;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        initializations();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Annunement:
                startActivity(new Intent(Chat.this, EventData.class));
                // do your code
                return true;
            case R.id.LostandFound:
                // do your code
                startActivity(new Intent(Chat.this, AddLost.class));

                return true;

            case R.id.Notification:
                // do your code
                if (flag == 1) {
                    item.setVisible(true);
                    startActivity(new Intent(Chat.this, Admin.class));

                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initializations() {
        toolbar = findViewById(R.id.chat_toolbar);
        tabLayout = findViewById(R.id.chat_tablayout);
        viewPager = findViewById(R.id.chat_ViewPager);

//        Setting toolbar
//        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new GroupChatList(), "Group chat");
        viewPagerAdapter.addFragment(new MyChat(), "My Chats");
        viewPagerAdapter.addFragment(new SearchTab(), "Search");

        viewPager.setAdapter(viewPagerAdapter);


    }
}
