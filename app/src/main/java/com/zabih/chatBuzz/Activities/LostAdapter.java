package com.zabih.chatBuzz.Activities;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zabih.chatBuzz.Activities.Models.LoastFoundModel;
import com.zabih.chatBuzz.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class LostAdapter extends RecyclerView.Adapter<LostAdapter.ViewHolder> {
    private List<LoastFoundModel> listData;
Context context;
    public LostAdapter(List<LoastFoundModel> listData, Context context) {
        this.listData = listData;
    this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lostfoundtlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LoastFoundModel ld = listData.get(position);
        holder.eventname.setText(ld.getName());
        holder.eventBy.setText(ld.getCreatedBy());
        //holder.eventDate.setText(ld.getDate());
        final Intent intent=new Intent(new Intent(context, AddlostDataShow.class).putExtra("infoo",ld));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Picasso.get().load(ld.getImagePath()).into(holder.imageView);
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
        ImageView imageView;
        View parent;
        TextView eventname, eventBy, eventDate;

        public ViewHolder(View mView) {
            super(mView);
parent=mView.findViewById(R.id.parent_layoue);
            imageView = mView.findViewById(R.id.imageView);
            //  eventDate = mView.findViewById(R.id.event_date);

            eventname = mView.findViewById(R.id.eventist_name_txt);

            eventBy = mView.findViewById(R.id.eventlist_by_txt);

        }
    }
}

