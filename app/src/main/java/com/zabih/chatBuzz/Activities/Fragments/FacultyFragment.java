package com.zabih.chatBuzz.Activities.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.zabih.chatBuzz.Activities.Models.UserModel;
import com.zabih.chatBuzz.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class FacultyFragment extends Fragment {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userRef = database.getReference("users");
    FirebaseRecyclerAdapter<UserModel, UserViewHolder> mAdapter;
    RecyclerView recyclerView;
public  FacultyFragment()
{


}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_faculty, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializations();
        getFacultyList();
    }



    private void searchUserfromFb(String name) {
        Query query = userRef.orderByChild("username").startAt(name).endAt(name + "\\uf8ff");
        FirebaseRecyclerOptions<UserModel> options =
                new FirebaseRecyclerOptions.Builder<UserModel>()
                        .setQuery(query, UserModel.class)//Query(streamsDbRef, liveStreamModel.class)
                        .build();
        mAdapter = new FirebaseRecyclerAdapter<UserModel, FacultyFragment.UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FacultyFragment.UserViewHolder holder, int position, @NonNull final UserModel model) {
                holder.setDetails(model.getUsername(), model.getEmail(), model.getImage_url(),getContext());


            }

            @NonNull
            @Override
            public FacultyFragment.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_row, parent, false);

                return new FacultyFragment.UserViewHolder(view);
            }
        };
        recyclerView.setAdapter(mAdapter);
        mAdapter.startListening();


    }




    private void getFacultyList() {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        Query query = usersRef.orderByChild("role").equalTo("faculty");
        FirebaseRecyclerOptions<UserModel> options =
                new FirebaseRecyclerOptions.Builder<UserModel>()
                        .setQuery(query, UserModel.class)//Query(streamsDbRef, liveStreamModel.class)
                        .build();
        mAdapter = new FirebaseRecyclerAdapter<UserModel, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull final UserModel model) {
                holder.setDetails(model.getUsername(), model.getEmail(),
                        model.getImage_url(), getContext());
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference usersReff = FirebaseDatabase.getInstance().getReference("users");
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        usersReff.child(uid).child("role").setValue("HOD");
                        Toast.makeText(getContext(),"Role Changed to HOD",Toast.LENGTH_SHORT).show();

                        //startActivity(new Intent(FacultyListActivity.this, ChatRoom.class).putExtra("otherUser", model));
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
        recyclerView.setAdapter(mAdapter);
        mAdapter.startListening();
    }

    private void initializations() {
        recyclerView = getActivity().findViewById(R.id.faculty_list_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
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



    public  class UserViewHolder extends RecyclerView.ViewHolder {
Button txtROle;
        View mView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDetails(String name, String email, String image_url, Context context) {
            CircleImageView userImage = mView.findViewById(R.id.user_row_image);
            TextView userName = mView.findViewById(R.id.user_row_name);
            TextView userEmail = mView.findViewById(R.id.user_row_email);
           // txtROle=mView.findViewById(R.id.textRole);

            if (!image_url.equals(""))
                Glide.with(context).load(image_url).into(userImage);
            userName.setText(name);
            userEmail.setText(email);
        }
    }






}
