package com.zabih.chatBuzz.Activities.ui.send;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.zabih.chatBuzz.R;


public class SendFragment extends Fragment {

    private SendViewModel sendViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_send, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        startActivity(new Intent(getActivity(), Notification.class));
    }
}