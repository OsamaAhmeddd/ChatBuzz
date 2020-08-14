package com.zabih.chatBuzz.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zabih.chatBuzz.Activities.Models.EventModel;
import com.zabih.chatBuzz.R;

import android.os.Bundle;
import android.widget.ImageView;

public class ActivityShow extends AppCompatActivity {
    TextView date, name, by, desc;
    EventModel eventModel = null;
    ImageView img;
TextView loc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        by = findViewById(R.id.textView10);
        img
                = findViewById(R.id.show_image);

        desc = findViewById(R.id.description);
        name = findViewById(R.id.textView2);
        date = findViewById(R.id.textView9);
        loc=findViewById(R.id.loc);

        eventModel = (EventModel) getIntent().getSerializableExtra("hello");
 desc.setVisibility(View.GONE);
        date.setVisibility(View.GONE);

        if (eventModel != null) {
            desc.setVisibility(View.VISIBLE);
            date.setVisibility(View.VISIBLE);
loc.setText(loc.getText().toString()+eventModel.getLocation());
            by.setText(by.getText().toString() + eventModel.getCreatedBy());
            date.setText(date.getText().toString()+eventModel.getDate());
            name.setText(name.getText().toString() + eventModel.getName());
            desc.setText(desc.getText().toString()+eventModel.getDescription());

            Glide.with(this).load(eventModel.getImgPath()).into(img);


        }

    }
}
