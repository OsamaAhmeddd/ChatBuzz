package com.zabih.chatBuzz.Activities.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zabih.chatBuzz.Activities.Adapters.AdvertiseMyAdapter;
import com.zabih.chatBuzz.Activities.AdvertismentDIffernetDAta;
import com.zabih.chatBuzz.Activities.EventData;
import com.zabih.chatBuzz.Activities.Models.AdvertistmentModel;
import com.zabih.chatBuzz.Activities.Models.UserModel;
import com.zabih.chatBuzz.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class AdvertiseFragment extends Fragment {
    UserModel userModel=null;
    Button std,fac,hod,emer,sports,dep,pub,mFyp;
    private List<AdvertistmentModel> listData;
    private RecyclerView rv;
    private AdvertiseMyAdapter adapter;
    private UserModel myUserInfo = null;

    public AdvertiseFragment() {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_advertise, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        rv = (RecyclerView) view.findViewById(R.id.recyclerview);
//        rv.setHasFixedSize(true);
//        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        //  listData = new ArrayList<AdvertistmentModel>();
        std=view.findViewById(R.id.btn_student);
        fac=view.findViewById(R.id.btn_facuty);
        hod=view.findViewById(R.id.btn_hod);
        emer=view.findViewById(R.id.button4);
        sports=view.findViewById(R.id.btn_sports);
        dep=view.findViewById(R.id.button);
        pub=view.findViewById(R.id.button5);
        mFyp=view.findViewById(R.id.btn_FYP);


        EventData eventData= (EventData) getActivity();
        userModel=eventData.sendData();

        getUserInfo();
        if(userModel!=null)
        {

            if(userModel.getRole().equals("student"))
            {
                hod.setVisibility(View.GONE);
                fac.setVisibility(View.GONE);
            }

            if(userModel.getRole().equals("faculty"))
            {
                std.setVisibility(View.GONE);
                hod.setVisibility(View.GONE);
            }

            if(userModel.getRole().equals("Hod"))
            {
                std.setVisibility(View.GONE);
                fac.setVisibility(View.GONE);
            }

        }
        mFyp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), AdvertismentDIffernetDAta.class).putExtra("val","FYP"));
            }
        });
        std.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), AdvertismentDIffernetDAta.class).putExtra("val","Student"));
            }
        });
        fac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), AdvertismentDIffernetDAta.class).putExtra("val","Faculty"));
            }
        });
        hod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), AdvertismentDIffernetDAta.class).putExtra("val","Hod"));
            }
        });
        pub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), AdvertismentDIffernetDAta.class).putExtra("val","Public"));
            }
        });
        dep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), AdvertismentDIffernetDAta.class).putExtra("val","Department"));
            }
        });
        sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), AdvertismentDIffernetDAta.class).putExtra("val","Sports"));
            }
        });
        emer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), AdvertismentDIffernetDAta.class).putExtra("val","Emergency"));
            }
        });
//
//        final DatabaseReference nm = FirebaseDatabase.getInstance().getReference().child("Admin").child("Notification").child("Advert");
//        nm.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()) {
//                        AdvertistmentModel l = npsnapshot.getValue(AdvertistmentModel.class);
//                        listData.add(l);
//                    }
//                    adapter = new AdvertiseMyAdapter(listData);
//                    rv.setAdapter(adapter);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


    }


    private void getUserInfo() {

        final String myID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users").child(myID);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myUserInfo = dataSnapshot.getValue(UserModel.class);
                if (myUserInfo != null) {
                    if(myUserInfo.getRole().equals("student"))
                    {
                        hod.setVisibility(View.GONE);
                        fac.setVisibility(View.GONE);
                    }

                    if(myUserInfo.getRole().equals("faculty"))
                    {
                        std.setVisibility(View.GONE);
                        hod.setVisibility(View.GONE);
                    }

                    if(myUserInfo.getRole().equals("Hod"))
                    {
                        std.setVisibility(View.GONE);
                        fac.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



}