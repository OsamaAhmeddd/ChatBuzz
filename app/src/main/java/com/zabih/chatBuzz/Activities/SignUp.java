package com.zabih.chatBuzz.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zabih.chatBuzz.Activities.Models.UserModel;
import com.zabih.chatBuzz.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUp extends AppCompatActivity {
    private static final int IMAGE_PICK_CODE = 123;
    private EditText mUsername, mEmail, mPassword, mConfirmPassword;
    private Button mSubmitBtn;
    private Spinner spinner;
    private FirebaseAuth mAuth;
    private CircleImageView profImage;
    private String userID;
    private Uri image_uri;
    //private FirebaseAuth mAuth;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initializations();
        profImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGallery();
            }
        });
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString();
//comsats.edu.pk
                String vvv="";



                if (spinner.getSelectedItem().toString().equals("Faculty")) {
                    int n = 14;
                    String s = email;
                    String val = new StringBuilder(email).reverse().toString();
                    //val.length()==14
                    vvv = val.substring(0, n);
                    String gg = "kp.ude.stasmoc";


                    if (vvv.equals("kp.ude.stasmoc")) {

                        if ((mPassword.getText().toString().equals(mConfirmPassword.getText().toString()))) {
                            registerNewUser(mEmail.getText().toString(), mPassword.getText().toString(),
                                    mUsername.getText().toString());
                        } else {
                            mPassword.setText("");
                            mConfirmPassword.setText("");
                            Toast.makeText(SignUp.this, "Passwords don't match!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        mEmail.setError("Please Enter Email id");
                        mEmail.requestFocus();
//    mEmail.setText("");
                        Toast.makeText(SignUp.this, "Email don't match!", Toast.LENGTH_SHORT).show();
                    }
                } else if (spinner.getSelectedItem().toString().equals("Student")) {
                    if (vvv.equals("kp.ude.stasmoc")) {
                        mEmail.setError("Please Enter Email id");
                        mEmail.requestFocus();

//

                    } else if ((mPassword.getText().toString().equals(mConfirmPassword.getText().toString()))) {
                        registerNewUser(mEmail.getText().toString(), mPassword.getText().toString(),
                                mUsername.getText().toString());
                    } else {
                        mPassword.setText("");
                        mConfirmPassword.setText("");
                        Toast.makeText(SignUp.this, "Passwords don't match!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    public void registerNewUser(final String email, final String password, final String username) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            addUserToFbDatabase(user, email, username);
                            Toast.makeText(SignUp.this, "User created successfully", Toast.LENGTH_SHORT).show();
String varr= String.valueOf(spinner.getSelectedItem().toString());
                            if (spinner.getSelectedItem().toString().equals("Faculty")) {

                                sendVerificationEmail();
                                //    onAuthSuccess(task.getResult().getUser());
                                //  startActivity(new Intent(SignUp.this, Dashboard.class));
                            }
                            else {

                                startActivity(new Intent(SignUp.this, PhoneAuth.class));
                                finish();
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignUp.this, "User not created successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addUserToFbDatabase(FirebaseUser user, String email, String username) {
        userID = user.getUid();
        DatabaseReference userRef = database.getReference("users").child(userID);

        UserModel userModel = new UserModel();
        userModel.setEmail(email);
        userModel.setUserID(userID);
        userModel.setUsername(username);
        if (spinner.getSelectedItem().toString().equals("Student")) {
            userModel.setRole("student");
        } else if (spinner.getSelectedItem().toString().equals("Faculty")) {
            userModel.setRole("faculty");
        } else if (spinner.getSelectedItem().toString().equals("Admin")) {
            userModel.setRole("admin");
        }

        if (image_uri != null) {
            uploadImageToStorage(userRef);
        } else {
            userModel.setImage_url("");
        }

        userRef.setValue(userModel);
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
                    Toast.makeText(SignUp.this, "could not produce Url", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
        }
    }

    private void initializations() {
        mUsername = findViewById(R.id.signUp_username);
        mConfirmPassword = findViewById(R.id.signUp_confirmPassword);
        mEmail = findViewById(R.id.signUp_email);
        mPassword = findViewById(R.id.signUp_password);
        profImage = findViewById(R.id.signUp_image);
        mSubmitBtn = findViewById(R.id.signUp_submitBtn);
        mAuth = FirebaseAuth.getInstance();
        spinner = findViewById(R.id.signUp_role_spinner);
        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(SignUp.this,
                R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.roles));
        roleAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(roleAdapter);
      //  if()
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Check if Already authenticated
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(SignUp.this, Dashboard.class));
            finish();
        }
    }


    private void sendVerificationEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent

                            Toast.makeText(SignUp.this,"Email sent ",Toast.LENGTH_SHORT).show();
                            // after email is sent just logout the user and finish this activity
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(SignUp.this, SignIn.class));
                            finish();
                        }
                        else
                        {
                            // email not sent, so display message and restart the activity or do whatever you wish to do

                            //restart this activity
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());

                        }
                    }
                });
    }



}
