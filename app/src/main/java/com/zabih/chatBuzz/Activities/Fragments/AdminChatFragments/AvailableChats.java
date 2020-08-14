package com.zabih.chatBuzz.Activities.Fragments.AdminChatFragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zabih.chatBuzz.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AvailableChats extends Fragment {

    private RecyclerView recyclerView;
    public AvailableChats() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_available_chats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializations();
        getAvailableChats();
    }

    private void getAvailableChats() {

    }

    private void initializations() {
        recyclerView=getActivity().findViewById(R.id.available_chats_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

    }
}
