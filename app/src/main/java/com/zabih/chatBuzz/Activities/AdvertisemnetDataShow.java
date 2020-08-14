package com.zabih.chatBuzz.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zabih.chatBuzz.Activities.Models.AdvertistmentModel;
import com.zabih.chatBuzz.R;

public class AdvertisemnetDataShow extends AppCompatActivity {
    TextView date,name,by,desc;
    AdvertistmentModel eventModel=null;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisemnet_data_show);

        by=findViewById(R.id.textView10);
        img
                =findViewById(R.id.show_image);

        desc=findViewById(R.id.description);
        name=findViewById(R.id.textView2);
        date=findViewById(R.id.textView9);
        eventModel= (AdvertistmentModel)       getIntent().getSerializableExtra("infoo");

        if(eventModel!=null)
        {
            by.setText(by.getText().toString()+eventModel.getCreatedBy());
        date.setVisibility(View.GONE);
          //  date.setText(date.getText().toString()+eventModel.getDate());
            name.setText(name.getText().toString()+eventModel.getName());
            //desc.setText(desc.getText().toString()+eventModel.getDescription());
desc.setVisibility(View.GONE);
            Glide.with(this).load(eventModel.getImgPath()).into(img);


        }

    }
}
