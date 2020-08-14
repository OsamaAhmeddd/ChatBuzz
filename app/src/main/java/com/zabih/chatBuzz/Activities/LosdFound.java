package com.zabih.chatBuzz.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zabih.chatBuzz.Activities.Models.LoastFoundModel;
import com.zabih.chatBuzz.R;

import java.util.ArrayList;
import java.util.List;

public class LosdFound extends AppCompatActivity {
    private List<LoastFoundModel> listData;
    private RecyclerView rv;
    private LostAdapter adapter;
    private Button mLostBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_losd_found);
        rv = (RecyclerView) findViewById(R.id.recyclerview);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        listData = new ArrayList<>();
        mLostBtn=findViewById(R.id.lostFound_btn);
        mLostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LosdFound.this,AddLost.class));
            }
        });
        final DatabaseReference nm = FirebaseDatabase.getInstance().getReference().child("lostandFound");
        nm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()) {
                        LoastFoundModel l = npsnapshot.getValue(LoastFoundModel.class);
                        listData.add(l);
                    }
                    adapter = new LostAdapter(listData,getApplicationContext());
                    rv.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
