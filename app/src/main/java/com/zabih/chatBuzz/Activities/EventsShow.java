package com.zabih.chatBuzz.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zabih.chatBuzz.Activities.Adapters.EventAdapter;
import com.zabih.chatBuzz.Activities.Models.EventModel;
import com.zabih.chatBuzz.Activities.Models.UserModel;
import com.zabih.chatBuzz.R;

import java.util.ArrayList;
import java.util.List;

public class EventsShow extends AppCompatActivity {

    private List<EventModel> listData;
    private RecyclerView rv;
    private EventAdapter adapter;
    UserModel user_Data = null;

    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_show);
        rv = (RecyclerView) findViewById(R.id.recyclerview);
        rv.setHasFixedSize(true);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
      //  String email = user.getEmail();
        rv.setLayoutManager(new LinearLayoutManager(this));
        listData = new ArrayList<>();
        btn=findViewById(R.id.EventshowBtn);

        user_Data = (UserModel) getIntent().getSerializableExtra("info");
if(user_Data!=null)
{
    if(user_Data.getRole()!="HOD")
    {
        btn.setVisibility(View.GONE);
    }
}

        //
//        if(email=="admin@gmail.com")
//        {
//btn.setVisibility(View.VISIBLE);
//        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EventsShow.this,EventActivity.class));
            }
        });
        final DatabaseReference nm = FirebaseDatabase.getInstance().getReference().child("Admin").child("Event");
        nm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()) {
                        EventModel l = npsnapshot.getValue(EventModel.class);
                        listData.add(l);
                    }
                    adapter = new EventAdapter(listData,getApplicationContext());
                    rv.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
