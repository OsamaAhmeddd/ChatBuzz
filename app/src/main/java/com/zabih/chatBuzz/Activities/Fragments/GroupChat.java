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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zabih.chatBuzz.Activities.Models.MessageModel;
import com.zabih.chatBuzz.Activities.Models.UserModel;
import com.zabih.chatBuzz.R;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class GroupChat extends Fragment {

    private static final int MESSAGE_TYPE_RIGHT = 0;
    private static final int MSG_TYPE_LEFT = 1;
    private ImageView mSendMssgBtn;
    private EditText mMessage;
    private RecyclerView recyclerView;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private String myID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FirebaseRecyclerAdapter<MessageModel, MessageViewHolder> mAdapter;
    private UserModel myUserInfo;


    public GroupChat() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_chat, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializations();
        receiveMessages();
        mSendMssgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mMessage.getText().toString().equals("")) {
                    String s = mMessage.getText().toString();
                    List<String> words = Arrays.asList("head", "now");
                    for (String word : words) {
                        Pattern rx = Pattern.compile("\\b" + word + "\\b", Pattern.CASE_INSENSITIVE);
                        s = rx.matcher(s).replaceAll(new String(new char[word.length()]).replace('\0', '*'));
                    }

// s: i will hit your ** right *

                    String BadWordStarred=s;
                    sendMessage(BadWordStarred);
                    mMessage.setText("");
                }
            }
        });
    }


    private void receiveMessages() {
        DatabaseReference groupChatRef = null;
        if (myUserInfo.getRole().equals("student")) {
            groupChatRef = database.getReference("studentGroupChat");
        } else if (myUserInfo.getRole().equals("faculty")) {
            groupChatRef = database.getReference("facultyGroupChat");
        } else if (myUserInfo.getRole().equals("admin")) {
            groupChatRef = database.getReference("adminGroupChat");
        }
        FirebaseRecyclerOptions<MessageModel> options =
                new FirebaseRecyclerOptions.Builder<MessageModel>()
                        .setQuery(groupChatRef, MessageModel.class)
                        .build();
        mAdapter = new FirebaseRecyclerAdapter<MessageModel, MessageViewHolder>(options) {

            @NonNull
            @Override
            public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (viewType == MESSAGE_TYPE_RIGHT) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.chat_message_row, parent, false);
                    return new MessageViewHolder(view);
                } else {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.chat_message_row_other, parent, false);
                    return new MessageViewHolder(view);
                }

            }

            @Override
            protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull MessageModel model) {
                holder.mMessage.setText(model.getMessage());
                holder.mTime.setText(model.getTime());
                holder.mUsername.setText(model.getUsername());
            }

            @Override
            public int getItemViewType(int position) {
                if (myID.equals(getItem(position).getSender())) {
                    return MESSAGE_TYPE_RIGHT;
                } else {
                    return MSG_TYPE_LEFT;
                }
            }
        };

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mAdapter.getItemCount();

                int lastVisiblePosition =
                        ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    recyclerView.scrollToPosition(positionStart);
                }
            }
        });
        recyclerView.setAdapter(mAdapter);
        mAdapter.startListening();
    }

    private void sendMessage(String text) {
        MessageModel message = new MessageModel();
        message.setMessage(text);
        message.setSender(myID);
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        message.setDate(currentDate);
        message.setTime(currentTime);
        message.setUsername(myUserInfo.getUsername());

        DatabaseReference groupChatRef = null;
        switch (myUserInfo.getRole()) {
            case "student":
                groupChatRef = database.getReference("studentGroupChat");
                break;
            case "faculty":
                groupChatRef = database.getReference("facultyGroupChat");
                break;
            case "admin":
                groupChatRef = database.getReference("adminGroupChat");
                break;
        }
        groupChatRef.push().setValue(message);
    }

    private void initializations() {
        Intent i = getActivity().getIntent();
        myUserInfo = (UserModel) i.getSerializableExtra("myUserInfo");
        mMessage = getActivity().findViewById(R.id.group_chatRoom_message_editText);
        mSendMssgBtn = getActivity().findViewById(R.id.group_chatRoom_sendBtn);
        recyclerView = getActivity().findViewById(R.id.group_chatRoom_recycler);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setStackFromEnd(true);
        recyclerView.setLayoutManager(lm);
        recyclerView.setHasFixedSize(true);


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

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView mMessage;
        TextView mTime;
        TextView mUsername;

        MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            mMessage = itemView.findViewById(R.id.chat_message_row_message);
            mTime = itemView.findViewById(R.id.chat_message_row_time);
            mUsername = itemView.findViewById(R.id.chat_message_row_username);
        }
    }
}
