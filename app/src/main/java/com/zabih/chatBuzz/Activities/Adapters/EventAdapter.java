package com.zabih.chatBuzz.Activities.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zabih.chatBuzz.Activities.ActivityShow;
import com.zabih.chatBuzz.Activities.Models.EventModel;
import com.zabih.chatBuzz.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private List<EventModel> listData;
    Context context;

    public EventAdapter(List<EventModel> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.eventlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EventModel ld = listData.get(position);
        holder.eventname.setText(ld.getName() + " at " + ld.getLocation());
        //  holder.eventBy.setText(ld.getCreatedBy());

        String vallllll = ld.getDate();
        int aaa = 0;
        holder.eventBy.setText(ld.getDate());
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        final Intent intent = new Intent(new Intent(context, ActivityShow.class).putExtra("hello", ld));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Glide.with(context).load(ld.getImgPath()).into(holder.img);

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View parent;

        TextView eventname, eventBy, eventDate;
        ImageView img;

        public ViewHolder(View mView) {
            super(mView);
            parent = mView.findViewById(R.id.ebent_parent);
            img = mView.findViewById(R.id.eventlist_image);
            //   eventDate = mView.findViewById(R.id.event_date);

            eventname = mView.findViewById(R.id.eventist_name_txt);

            eventBy = mView.findViewById(R.id.eventlist_by_txt);

        }
    }
}

