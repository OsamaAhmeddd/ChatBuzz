package com.zabih.chatBuzz.Activities.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zabih.chatBuzz.Activities.Adapters.AdapterAdvertisment;
import com.zabih.chatBuzz.Activities.Models.AdvertistmentModel;
import com.zabih.chatBuzz.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class EventFragment extends Fragment {

    private List<AdvertistmentModel> listData;
    private RecyclerView rv;
    private AdapterAdvertisment adapter;

    public EventFragment() {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv = (RecyclerView) view.findViewById(R.id.recyclerview);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        listData = new ArrayList<>();
        rv.setItemAnimator(new DefaultItemAnimator());

        final DatabaseReference nm = FirebaseDatabase.getInstance().getReference().child("Admin").child("Notification").child("Advert");
        nm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()) {
                        AdvertistmentModel l = npsnapshot.getValue(AdvertistmentModel.class);
                        listData.add(l);
                    }
                    adapter = new AdapterAdvertisment(listData,getApplicationContext());
                    rv.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}


