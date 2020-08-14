package com.zabih.chatBuzz.Activities.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.zabih.chatBuzz.Activities.Models.UserModel;
import com.zabih.chatBuzz.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> {

    private List<UserModel> mUsers;
    private Context context;
    private onUserListener mOnUserListener;

    public MessageListAdapter(List<UserModel> mUsers, Context context, onUserListener onUserListener) {
        this.mUsers = mUsers;
        this.context = context;
        this.mOnUserListener = onUserListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_row, parent, false);
        return new MessageListAdapter.ViewHolder(view, mOnUserListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserModel userModel = mUsers.get(position);
        holder.mEmail.setText(userModel.getEmail());
        holder.mUsername.setText(userModel.getUsername());
        if (!userModel.getImage_url().equals(""))
            Glide.with(context).load(userModel.getImage_url()).into(holder.mImage);

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }


    public interface onUserListener {
        void onUserClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mUsername;
        TextView mEmail;
        CircleImageView mImage;
        onUserListener onUserListener;

        ViewHolder(@NonNull View itemView, onUserListener onUserListener) {
            super(itemView);
            mUsername = itemView.findViewById(R.id.user_row_name);
            mEmail = itemView.findViewById(R.id.user_row_email);
            mImage = itemView.findViewById(R.id.user_row_image);
            this.onUserListener = onUserListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onUserListener.onUserClick(getAdapterPosition());
        }
    }
}
