package com.zabih.chatBuzz.Activities.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.zabih.chatBuzz.Activities.ChatRoom;
import com.zabih.chatBuzz.Activities.Models.UserModel;
import com.zabih.chatBuzz.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchTab extends Fragment {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userRef = database.getReference("users");
    private EditText mSearchBar;
    private Button mSearchBtn;
    private RecyclerView mRecyclerView;
    private FirebaseRecyclerAdapter<UserModel, UserViewHolder> mAdapter;

    public SearchTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_search_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializations();
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchUserfromFb(mSearchBar.getText().toString());
            }
        });
    }

    private void searchUserfromFb(String name) {
        Query query = userRef.orderByChild("username").startAt(name).endAt(name + "\\uf8ff");
        FirebaseRecyclerOptions<UserModel> options =
                new FirebaseRecyclerOptions.Builder<UserModel>()
                        .setQuery(query, UserModel.class)//Query(streamsDbRef, liveStreamModel.class)
                        .build();
        mAdapter = new FirebaseRecyclerAdapter<UserModel, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull final UserModel model) {
                holder.setDetails(model.getUsername(), "url", model.getEmail(), model.getImage_url(), getContext());
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startActivity(new Intent(getActivity(), ChatRoom.class).putExtra("otherUser", model));
                    }
                });

            }

            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_row, parent, false);

                return new UserViewHolder(view);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.startListening();


    }

    private void initializations() {
        mRecyclerView = getActivity().findViewById(R.id.chat_tab2_recyclerView);
        mSearchBar = getActivity().findViewById(R.id.chat_tab2_searchBar);
        mSearchBtn = getActivity().findViewById(R.id.chat_tab2_searchBtn);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null)
            mAdapter.stopListening();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAdapter != null)
            mAdapter.stopListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null)
            mAdapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mAdapter!=null)
            mAdapter.startListening();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

        }

        public void setDetails(String name, String url, String email, String image_url, Context context) {
            CircleImageView userImage = mView.findViewById(R.id.user_row_image);
            TextView userName = mView.findViewById(R.id.user_row_name);
            TextView userEmail = mView.findViewById(R.id.user_row_email);

            if (!image_url.equals(""))
                Glide.with(context).load(image_url).into(userImage);
            userName.setText(name);
            userEmail.setText(email);
        }
    }

}
