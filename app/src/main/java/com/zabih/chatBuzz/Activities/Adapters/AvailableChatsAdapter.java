package com.zabih.chatBuzz.Activities.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zabih.chatBuzz.R;

import java.util.List;

public class AvailableChatsAdapter extends RecyclerView.Adapter<AvailableChatsAdapter.ViewHolder> {

    private List<String> mAvailableChats;

    private Context context;

    public AvailableChatsAdapter(List<String> mChat, Context context) {
        this.mAvailableChats = mChat;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.user_row, parent, false);
        return new AvailableChatsAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return mAvailableChats.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mChatName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mChatName = itemView.findViewById(R.id.user_row_name);

        }
    }
}
