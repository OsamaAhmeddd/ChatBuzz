package com.zabih.chatBuzz.Activities.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.zabih.chatBuzz.Activities.Models.MessageModel;
import com.zabih.chatBuzz.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private static final int MESSAGE_TYPE_RIGHT = 0;
    private static final int MSG_TYPE_LEFT = 1;
    private Context context;
    private List<MessageModel> mChat;
    private String myID, otherUserID;


    public MessageAdapter(List<MessageModel> mChat, String myID, String otherUserID, Context context) {
        this.context = context;
        this.mChat = mChat;
        this.myID = myID;
        this.otherUserID = otherUserID;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MESSAGE_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_message_row, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_message_row_other, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MessageModel messageModel = mChat.get(position);
        holder.mMessage.setText(messageModel.getMessage());
        holder.mTime.setText(messageModel.getTime());
        holder.mUsername.setText(messageModel.getUsername());
        if (!messageModel.getImage_url().equals("")) {
            Glide.with(context).load(messageModel.getImage_url()).into(holder.imageView);
        }else {
            holder.imageView.setVisibility(View.GONE);
        }

    }


    @Override
    public int getItemCount() {
        return mChat.size();
    }

    @Override
    public int getItemViewType(int position) {
        String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (mChat.get(position).getSender().equals(currentUserID)) {
            return MESSAGE_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mMessage;
        TextView mTime;
        TextView mUsername;
        ImageView imageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mMessage = itemView.findViewById(R.id.chat_message_row_message);
            mTime = itemView.findViewById(R.id.chat_message_row_time);
            mUsername=itemView.findViewById(R.id.chat_message_row_username);
            imageView=itemView.findViewById(R.id.chat_message_row_image);

        }
    }
}
