package com.zabih.chatBuzz.Activities.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zabih.chatBuzz.Activities.Adapters.MessageListAdapter;
import com.zabih.chatBuzz.Activities.ChatRoom;
import com.zabih.chatBuzz.Activities.Models.MessageModel;
import com.zabih.chatBuzz.Activities.Models.UserModel;
import com.zabih.chatBuzz.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyChat extends Fragment implements MessageListAdapter.onUserListener {

    private RecyclerView recyclerView;
    private MessageListAdapter messengerListAdapter;
    private String myID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference chatRef = database.getReference("chatRef");
    private DatabaseReference usersRef = database.getReference("users");
    private ValueEventListener chatListener, usersListener;
    private List<String> userList;
    private List<UserModel> mUsers;


    public MyChat() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializations();
        function();
    }

    private void function() {
        userList = new ArrayList<>();
        chatListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    MessageModel message = ds.getValue(MessageModel.class);
                    if (message.getSender().equals(myID)) {
                        if (!userList.contains(message.getReceiver()))
                            userList.add(message.getReceiver());
                    }
                    if (message.getReceiver().equals(myID)) {
                        if (!userList.contains(message.getSender()))
                            userList.add(message.getSender());
                    }
                }
                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        chatRef.addValueEventListener(chatListener);
    }

    private void readChats() {
        mUsers = new ArrayList<>();
        usersListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    UserModel user = ds.getValue(UserModel.class);
                    for (String id : userList) {
                        if (user.getUserID().equals(id)) {
                            if (mUsers.size() != 0) {
                                for (UserModel user1 : mUsers) {
                                    if (!user.getUserID().equals(user1.getUserID())) {
                                        mUsers.add(user);
                                        break;
                                    }
                                }
                            } else {
                                mUsers.add(user);
                            }
                        }

                    }
                }//endFor
                messengerListAdapter = new MessageListAdapter(mUsers, getContext(), MyChat.this);
                recyclerView.setAdapter(messengerListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        usersRef.addValueEventListener(usersListener);

    }

    private void initializations() {
        recyclerView = getActivity().findViewById(R.id.chat_tab1_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (usersListener != null)
            usersRef.removeEventListener(usersListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (usersListener != null)
            usersRef.removeEventListener(usersListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (usersListener != null)
            usersRef.removeEventListener(usersListener);
    }

    @Override
    public void onUserClick(int position) {
        UserModel otherUser = mUsers.get(position);
        startActivity(new Intent(getActivity(), ChatRoom.class).putExtra("otherUser", otherUser));
    }
}
