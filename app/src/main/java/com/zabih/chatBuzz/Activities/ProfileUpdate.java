package com.zabih.chatBuzz.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zabih.chatBuzz.Activities.Models.UserModel;
import com.zabih.chatBuzz.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileUpdate extends AppCompatActivity {
    private static final int IMAGE_PICK_CODE = 123;
    private TextView mUsername, mEmail;
           // ..mPassword, mConfirmPassword;
    private Button mSubmitBtn;
    private Spinner spinner;
    public  int counter=0;
    EditText mCurrentPassword,mNewPassword,moldpassword;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String email = user.getEmail();

    private FirebaseAuth mAuth;
    private ImageView profImage;
    private String userID;
    private Uri image_uri;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    UserModel userModel = null;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile_update);
        initializations();
    //   profImage.setBackgroundResource(R.drawable.updateimg);

        getData();
profImage.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        counter++;
        pickImageFromGallery();
    }
});

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


 String vvvvv=mNewPassword.getText().toString();
 String aaa=moldpassword.getText().toString();

                if(moldpassword.getText().toString().isEmpty()||mCurrentPassword.getText().toString().isEmpty()||mNewPassword.getText().toString().isEmpty())
                {
                    Toast.makeText(ProfileUpdate.this,"Please fill the form",Toast.LENGTH_SHORT).show();
                }
                else
                {

                    if (!(mCurrentPassword.getText().toString().equals(mNewPassword.getText().toString()))) {

                        Toast.makeText(ProfileUpdate.this,"Password not matched ",Toast.LENGTH_SHORT).show();

                    }
                    else {

if(counter==0) {

    updatedata();

}
else
{
    updateUser(mUsername.getText().toString(), mEmail.getText().toString());
    updatedata();

}                   }

                 }
            }
        });
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            profImage.setImageURI(data.getData());
            image_uri = data.getData();

        updateUser(mUsername.getText().toString(),mEmail.getText().toString());

        }
    }

    private void initializations() {
        mCurrentPassword=findViewById(R.id.ccp);//new
        mNewPassword=findViewById(R.id.np);//new
        moldpassword=findViewById(R.id.cp);//old
       // mConfirmPassword=findViewById(R.id.sign_confirmPassword);
        mUsername = findViewById(R.id.signUp_username);
        //mConfirmPassword = findViewById(R.id.signUp_confirmPassword);
        mEmail = findViewById(R.id.signUp_email);
  //      mPassword = findViewById(R.id.signUp_password);
        profImage = findViewById(R.id.signUp_image);
        mSubmitBtn = findViewById(R.id.signUp_submitBtn);
        mAuth = FirebaseAuth.getInstance();
        spinner = findViewById(R.id.signUp_role_spinner);
    }

    private void getData() {

        DatabaseReference userRef = database.getReference("users").child(uid);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userModel = dataSnapshot.getValue(UserModel.class);

                mUsername.setText(userModel.getUsername());
                mEmail.setText(userModel.getEmail());

                if (userModel.getImage_url().equals("")) {


      //              profImage.setBackgroundResource(0);

                    profImage.setImageResource(R.drawable.updateimg);

                }
else
                {

                    Glide.with(ProfileUpdate.this).load(userModel.getImage_url()).apply(new RequestOptions().override(150, 150))
                            .into(profImage);

                // Glide.with(this).load(userModel.getImage_url).into(profImage);
            }}


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void updateUser(String name, String email) {
        DatabaseReference d = database.getReference("users").child(uid);    // updating the user via child nodes
        d.child("username").setValue(name);
//d.child("image_url").setValue()
        d.child("email").setValue(email);
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

                    if(downloadUri!=null) {
                        userRef.child("image_url").setValue(downloadUri.toString());
                    }
                    //userRef.child("image_url").setValue(downloadUri.toString());

                } else {
                    Toast.makeText(ProfileUpdate.this, "could not produce Url", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileUpdate.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public  void updatedata()
    {

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.
        String userEmail = user.getEmail();
        String password=moldpassword.getText().toString();
        AuthCredential credential = EmailAuthProvider
                .getCredential(userEmail, password);

// Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(mNewPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ProfileUpdate.this,"Password updated",Toast.LENGTH_SHORT).show();
                                        FirebaseAuth.getInstance().signOut();

                                        startActivity(new Intent(ProfileUpdate.this,SignIn.class));
                                        //

                                        //       Log.d(TAG, "Password updated");
                                    } else {

                                        Toast.makeText(ProfileUpdate.this,"Error password not updated",Toast.LENGTH_SHORT).show();
                                        // Log.d(TAG, "")
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(ProfileUpdate.this,"Auth Faild",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
