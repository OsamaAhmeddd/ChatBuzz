package com.zabih.chatBuzz.Activities.ui.share;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zabih.chatBuzz.Activities.Chat;
import com.zabih.chatBuzz.Activities.Models.UserModel;
import com.zabih.chatBuzz.R;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class ShareFragment extends Fragment {

    private ShareViewModel shareViewModel;
    private static final int IMAGE_PICK_CODE = 123;
    private EditText mUsername, mEmail, mPassword, mConfirmPassword;
    private Button mSubmitBtn;
    private Spinner spinner;
    private FirebaseAuth mAuth;
    private CircleImageView profImage;
    private String userID;
    private Uri image_uri;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    UserModel userModel=null;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_share, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

if(uid==null)
{
    Toast.makeText(getActivity(),"No Data ",Toast.LENGTH_SHORT).show();
}
else {
    initializations();
    getData();
}
        profImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGallery();
            }
        });
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUser(mUsername.getText().toString(),mEmail.getText().toString());
            }
        });
    }
    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }
    private void updateUser(String name, String email) {
        DatabaseReference d = database.getReference("users").child(uid);    // updating the user via child nodes
        if (!mUsername.getText().toString().equals(""))
            d.child("username").setValue(name);
        if(!mEmail.getText().toString().equals(""))
            d.child("email").setValue(email);
        if (image_uri!=null)
            uploadImageToStorage(d);
    }
    private void uploadImageToStorage(final DatabaseReference userRef) {
        final StorageReference picRef = FirebaseStorage.getInstance().getReference("profileImages/" + userID);
        UploadTask uploadTask = picRef.putFile(image_uri);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                // Continue with the task to get the download URL
                return picRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    userRef.child("image_url").setValue(downloadUri.toString());

                } else {
                    Toast.makeText(getContext(), "could not produce Url", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void  getData()
    {

        DatabaseReference userRef = database.getReference("users").child(uid);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userModel=  dataSnapshot.getValue(UserModel.class);
                mUsername.setText(userModel.getUsername());
                mEmail.setText(userModel.getEmail());

                if(!userModel.getImage_url().equals("")) {
                    Glide.with(getContext()).load(userModel.getImage_url()).into(profImage);
                }
                // Glide.with(this).load(userModel.getImage_url).into(profImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            profImage.setImageURI(data.getData());
            image_uri = data.getData();
        }
    }

    private void initializations() {
        mUsername = getActivity().findViewById(R.id.signUp_username);
        mConfirmPassword = getActivity().findViewById(R.id.signUp_confirmPassword);
        mEmail =getActivity().findViewById(R.id.signUp_email);
        mPassword = getActivity().findViewById(R.id.signUp_password);
        profImage = getActivity().findViewById(R.id.signUp_image);
        mSubmitBtn = getActivity().findViewById(R.id.signUp_submitBtn);
        mAuth = FirebaseAuth.getInstance();
        spinner = getActivity().findViewById(R.id.signUp_role_spinner);
    }




    private void getUserInfo() {
        String myId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef= FirebaseDatabase.getInstance().getReference("users").child(myId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel user=dataSnapshot.getValue(UserModel.class);
                startActivity(new Intent(getActivity(),Chat.class).putExtra("myUserInfo",user));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}