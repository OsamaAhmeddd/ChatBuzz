package com.zabih.chatBuzz.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.zabih.chatBuzz.Activities.Adapters.MyAdapter;
import com.zabih.chatBuzz.Activities.Models.AnnouncementModel;
import com.zabih.chatBuzz.R;

import java.util.ArrayList;
import java.util.List;


public class AdvertismentDIffernetDAta extends AppCompatActivity {
    private List<AnnouncementModel> listData;
    private RecyclerView rv;
    private MyAdapter adapter;
    TextView title;
   String value ;
    DatabaseReference nm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisment_differnet_data);
       value= (String) getIntent().getSerializableExtra("val");
        DatabaseReference nm = FirebaseDatabase.getInstance().getReference().child("Admin").child("Notification").child("Announcement");
        Query query =nm.orderByChild("role").equalTo(value);

title=findViewById(R.id.abc);
title.setText(value);
        rv = (RecyclerView) findViewById(R.id.recyclerview);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listData=new ArrayList<>();
                if (dataSnapshot.exists()) {

                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()) {
                        AnnouncementModel l = npsnapshot.getValue(AnnouncementModel.class);
                        listData.add(l);
                    }
                    adapter = new MyAdapter(listData,getApplicationContext());
                    rv.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
