package com.zabih.chatBuzz.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.zabih.chatBuzz.Activities.Models.AnnouncementModel;
import com.zabih.chatBuzz.R;

public class DataFeatures extends AppCompatActivity {
TextView textView;
AnnouncementModel announcementModel=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_features);
        announcementModel = (AnnouncementModel) getIntent().getSerializableExtra("abc");

        textView=findViewById(R.id.txt);
        textView.setText(announcementModel.getName());
    }
}
