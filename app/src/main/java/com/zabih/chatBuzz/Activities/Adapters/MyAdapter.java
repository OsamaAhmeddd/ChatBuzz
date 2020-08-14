package com.zabih.chatBuzz.Activities.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zabih.chatBuzz.Activities.DataFeatures;
import com.zabih.chatBuzz.Activities.Models.AnnouncementModel;
import com.zabih.chatBuzz.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<AnnouncementModel> listData;
Context context;
    public MyAdapter(List<AnnouncementModel> listData,Context  context) {
        this.listData = listData;
    this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.annoucelist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AnnouncementModel ld = listData.get(position);
        holder.eventname.setText(ld.getName());
        holder.eventBy.setText(ld.getCreater());
        //   holder.eventDate.setText(ld.getDate());

holder.mTime.setText(ld.getTime());
holder.mDate.setText(ld.getDate());
        final Intent intent = new Intent(new Intent(context, DataFeatures.class).putExtra("abc", ld));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
TextView mDate,mTime;
        public ViewHolder(View mView) {
            super(mView);


//            eventDate = mView.findViewById(R.id.event_date);
mDate=mView.findViewById(R.id.date);
mTime=mView.findViewById(R.id.timee);
            eventname = mView.findViewById(R.id.eventist_name_txt);

            eventBy = mView.findViewById(R.id.eventlist_by_txt);
parent=mView.findViewById(R.id.viewww);
        }
    }
}

