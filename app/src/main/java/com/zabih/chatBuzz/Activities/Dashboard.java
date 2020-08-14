package com.zabih.chatBuzz.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zabih.chatBuzz.Activities.Models.UserModel;
import com.zabih.chatBuzz.R;

import java.io.Serializable;

public class Dashboard extends AppCompatActivity {

    private Button mLogout,mNav;
    private FirebaseAuth mAuth;
    private ImageView mNotifications, mChats, mEvents, mLost, mAdmin,mAdmin2;
    private UserModel myUserInfo = null;
    private TextView toolbarText, mAdminTextView,mAdminTextView2;
    UserModel adminInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initializations();
        adminInfo = (UserModel) getIntent().getSerializableExtra("adminInfo");

        if (adminInfo == null) {
            getUserInfo();
        }
        else{
            mAdminTextView.setVisibility(View.VISIBLE);
            mAdmin.setVisibility(View.VISIBLE);

            mAdminTextView2.setVisibility(View.VISIBLE);
            mAdmin2.setVisibility(View.VISIBLE);
        }
        mChats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adminInfo == null) {
                    startActivity(new Intent(Dashboard.this, Chat.class).putExtra("myUserInfo", (Serializable) myUserInfo));
                } else {
                    startActivity(new Intent(Dashboard.this, AdminChatActivity.class));
                }
            }
        });
        mEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Dashboard.this, EventsShow.class).putExtra("info",myUserInfo));

            }
        });
        mNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, EventData.class));

            }
        });
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(Dashboard.this, SignIn.class));
                finish();
            }
        });
        mNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this,NavDrawer.class));
                finish();
            }
        });






        mLost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Dashboard.this, LosdFound.class));

            }
        });
        mAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this,FacultyListActivity.class));
            }
        });
        mAdmin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this,Admin.class));
            }
        });


    }

    private void getUserInfo() {
        String myID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(myID);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    myUserInfo = dataSnapshot.getValue(UserModel.class);
                    if (myUserInfo!=null) {
                        if (myUserInfo.getRole() == "hod") {
                            mAdmin2.setVisibility(View.VISIBLE);

                        }
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Dashboard.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializations() {
        mChats = findViewById(R.id.dashboard_Chats);
        mEvents = findViewById(R.id.dashboard_Events);
        mNotifications = findViewById(R.id.dashboard_notifications);
        mLogout = findViewById(R.id.dashboard_SignOut);
        mAuth = FirebaseAuth.getInstance();
        mLost = findViewById(R.id.mLost);
        mAdmin = findViewById(R.id.dashboard_admin);
        mAdminTextView = findViewById(R.id.dashboard_adminTextView);
        mAdmin2 = findViewById(R.id.dashboard_admin2);
        mAdminTextView2 = findViewById(R.id.dashboard_adminTextView2);
        mNav=findViewById(R.id.navdraw);

    }


}
