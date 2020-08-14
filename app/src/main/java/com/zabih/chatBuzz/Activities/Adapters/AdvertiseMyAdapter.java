package com.zabih.chatBuzz.Activities.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zabih.chatBuzz.Activities.Models.AdvertistmentModel;
import com.zabih.chatBuzz.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdvertiseMyAdapter extends RecyclerView.Adapter<AdvertiseMyAdapter.ViewHolder> {
    private List<AdvertistmentModel> listData;

    public AdvertiseMyAdapter(List<AdvertistmentModel> listData) {
        this.listData = listData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.advertiselist, parent, false);
        return new AdvertiseMyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdvertiseMyAdapter.ViewHolder holder, int position) {
        AdvertistmentModel ld = listData.get(position);
        holder.advertname.setText(ld.getName());

        String value = ld.getImgPath();
        holder.advertBy.setText(ld.getCreatedBy());
        Picasso.get().load(ld.getImgPath()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView advertname, advertBy;
        CircleImageView imageView;

        public ViewHolder(View mView) {
            super(mView);
            imageView = mView.findViewById(R.id.imageView);

            advertname = mView.findViewById(R.id.eventist_name_txt);

            advertBy = mView.findViewById(R.id.eventlist_by_txt);

        }
    }
}

