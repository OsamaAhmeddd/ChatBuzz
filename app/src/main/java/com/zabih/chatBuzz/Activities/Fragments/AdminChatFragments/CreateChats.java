package com.zabih.chatBuzz.Activities.Fragments.AdminChatFragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zabih.chatBuzz.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateChats extends Fragment {


    public CreateChats() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_chats, container, false);
    }

}
