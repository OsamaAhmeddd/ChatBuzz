package com.zabih.chatBuzz.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.zabih.chatBuzz.Activities.Models.UserModel;
import com.zabih.chatBuzz.R;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

public class NavDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView navigationView;
    private UserModel myUserInfo = null;
    UserModel adminInfo = null;

    private AppBarConfiguration mAppBarConfiguration;
    TextView profile_name;
    CircleImageView profileImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        adminInfo = (UserModel) getIntent().getSerializableExtra("adminInfo");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        if (adminInfo == null) {


            loadUserImage();
        }
        setNavigationViewListener();
        navigationView = findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        profileImg = (CircleImageView) hView.findViewById(R.id.imageView);
        profile_name = (TextView) hView.findViewById(R.id.textView_name);

    }




    private void loadUserImage() {

        String myID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users")
                .child(myID);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel user = new UserModel();
                user = dataSnapshot.getValue(UserModel.class);


                if (user != null) {
                        checkBlocked(user);
                    if (!user.getImage_url().equals(""))
                        Glide.with(NavDrawer.this).load(user.getImage_url()).into(profileImg);
                    profile_name.setText(user.getUsername());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void checkBlocked(UserModel myUserInfo) {
        DatabaseReference blockRef=FirebaseDatabase.getInstance().getReference("blockedUsers");
        Query query=blockRef.orderByChild("id").equalTo(myUserInfo.getUserID());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount()>0)
                {//is in blocked list
                            Toast.makeText(NavDrawer.this, "User is blocked by Admin", Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(getApplicationContext(),SignIn.class));
                            finish();


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {


            case R.id.ld: {
                startActivity(new Intent(getApplicationContext(), LostFounDViewpager.class));
                //do somthing
                break;
            }


            case R.id.Notification: {
                startActivity(new Intent(NavDrawer.this, EventData.class).putExtra("info", adminInfo));

//                startActivity(new Intent(getApplicationContext(), EventData.class));
                //do somthing
                break;
            }

            case R.id.nav_share: {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    startActivity(new Intent(getApplicationContext(), ProfileUpdate.class));
//                }
                break;
            }



            case R.id.nav_gallery: {
                // startActivity(new Intent(getApplicationContext(),EventData.class));
                //do somthing
                FirebaseAuth.getInstance().signOut();
//                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), SignIn.class));
                finish();

                break;
            }

        }
        //close navigation drawer
        //    mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


}
