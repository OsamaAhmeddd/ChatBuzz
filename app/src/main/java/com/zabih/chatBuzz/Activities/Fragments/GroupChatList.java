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
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.zabih.chatBuzz.Activities.GroupChatActivity;
import com.zabih.chatBuzz.Activities.Models.AvailableChatModel;
import com.zabih.chatBuzz.Activities.Models.UserModel;
import com.zabih.chatBuzz.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupChatList extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<AvailableChatModel, ChatViewHolder> mAdapter;
    private UserModel myUserInfo = null;

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String urrrrl="";
    String TOPIC;
    private DatabaseReference mDatabase;
    final String TAG = "NOTIFICATION TAG";
    //    private final int PICK_IMAGE_REQUEST = 22;
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAuLqQ63Y:APA91bGFH49Hgg2Hmc54L5uL_5q0Ol2kq3p_M6Ba4TdMJ_bkyuySrpQplgsk2tgvJiMDk6y-ihy77dASrtBRqW_tV0P7nT9kidAw76JeLt30LnDjKDX-6lBybFNs39j2-nn3pAjm-NBa";
    final private String contentType = "application/json";

    public GroupChatList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_chat_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myUserInfo = (UserModel) getActivity().getIntent().getSerializableExtra("myUserInfo");
        initializations();
        getAvailableChats();
    }

    private void getAvailableChats() {
        DatabaseReference chats = FirebaseDatabase.getInstance().getReference("chats");
        Query query = null;

        switch (myUserInfo.getRole()) {
            case "student":
                query = chats.orderByChild("chatRole").equalTo("student");
                break;
            case "faculty":
                query = chats.orderByChild("chatRole").equalTo("faculty");
                break;
            case "admin":
                query = chats.orderByChild("chatRole").equalTo("admin");
                break;
            case "hod":
                query = chats.orderByChild("chatRole").equalTo("faculty");
                break;

        }
        assert query != null;
        FirebaseRecyclerOptions<AvailableChatModel> options =
                new FirebaseRecyclerOptions.Builder<AvailableChatModel>()
                        .setQuery(query, AvailableChatModel.class)
                        .build();
        mAdapter = new FirebaseRecyclerAdapter<AvailableChatModel, ChatViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ChatViewHolder holder, int position, @NonNull final AvailableChatModel model) {
                holder.mChatName.setText(model.getName());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), GroupChatActivity.class).putExtra("GroupChatInfo", model)
                                .putExtra("myUserInfo", myUserInfo));
                    }
                });
            }

            @NonNull
            @Override
            public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.group_chat_list_row, parent, false);
                  return new ChatViewHolder(view);
            }
        };
        recyclerView.setAdapter(mAdapter);
        mAdapter.startListening();
    }

    private void initializations() {
        recyclerView = getActivity().findViewById(R.id.group_chat_list_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView mChatName;

        ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            mChatName = itemView.findViewById(R.id.group_chat_list_chatName);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null)
            mAdapter.stopListening();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAdapter != null)
            mAdapter.stopListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null)
            mAdapter.stopListening();
    }
}
