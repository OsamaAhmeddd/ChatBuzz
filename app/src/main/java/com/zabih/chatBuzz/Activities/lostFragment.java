package com.zabih.chatBuzz.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.zabih.chatBuzz.Activities.Models.LoastFoundModel;
import com.zabih.chatBuzz.R;

import java.util.ArrayList;
import java.util.List;

public class lostFragment extends Fragment {

    private List<LoastFoundModel> listData;
    private RecyclerView rv;
    private LostAdapter adapter;
    Button btn_lost;

    public lostFragment() {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lost, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv = (RecyclerView) view.findViewById(R.id.recyclerview);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        listData = new ArrayList<>();
        btn_lost = view.findViewById(R.id.btn_lostandfound);
        btn_lost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddLost.class));
            }
        });
        final DatabaseReference nm = FirebaseDatabase.getInstance().getReference().child("lostandFound");
        Query query = nm.orderByChild("role").equalTo("Lost");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()) {
                        LoastFoundModel l = npsnapshot.getValue(LoastFoundModel.class);
                        listData.add(l);
                    }
                    adapter = new LostAdapter(listData, getContext());
                    rv.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}



