package com.zabih.chatBuzz.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.zabih.chatBuzz.Activities.Models.UserModel;
import com.zabih.chatBuzz.R;

public class SignIn extends AppCompatActivity {
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public EditText mEmail, mPassword;
    private Button mSignUpLink;
    TextView mForgotPassword;
    private Button mLoginBtn;
    private FirebaseAuth mAuth;
    UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initializations();

        user = new UserModel();
        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEmail.getText().toString().isEmpty()) {

                    mEmail.setError("Please Enter Otp");
                    mEmail.requestFocus();

                } else {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(mEmail.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignIn.this, "Email sent ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mEmail.getText().toString().isEmpty() || mPassword.getText().toString().isEmpty()) {

                    Toast
                            .makeText(SignIn.this,
                                    "Please fill the form",
                                    Toast.LENGTH_SHORT)
                            .show();
                } else {

                    if (mEmail.getText().toString().equals("admin@gmail.com") && mPassword.getText().toString().equals("hello123")) {
                        // UserModel user = new UserModel();
                        user.setRole("admin");
                        user.setUsername("admin");
                        user.setEmail("admin@gmail.com");
                        //loginUser(mEmail.getText().toString(), mPassword.getText().toString());
                        loginUser2(mEmail.getText().toString(), mPassword.getText().toString());


                    } else {
                        int n = 14;
                        String vvv = "";

                        String val = new StringBuilder(mEmail.getText().toString()).reverse().toString();
                        //val.length()==14
                        if(mEmail.getText().toString().length()<14)
                        {
                            loginUser2(mEmail.getText().toString(), mPassword.getText().toString());

                        }
                        else {
                            vvv = val.substring(0, n);

                            String gg = "kp.ude.stasmoc";
                            if (vvv.equals("kp.ude.stasmoc")) {
                                loginUser(mEmail.getText().toString(), mPassword.getText().toString());

                            } else
                                loginUser2(mEmail.getText().toString(), mPassword.getText().toString());

                        }
                    }
                }
            }

        });

        mSignUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // startActivity(new Intent(SignIn.this, PhoneAuth.class));

                startActivity(new Intent(SignIn.this, SignUp.class));
            }
        });
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //FirebaseUser user = mAuth.getCurrentUser();


                            FirebaseUser userr = FirebaseAuth.getInstance().getCurrentUser();
                            boolean emailVerified = userr.isEmailVerified();
                            //        if(user.getRole().equals("student")) {
                            if (emailVerified) {
                                // onAuthSuccess(task.getResult().getUser());
                                Toast.makeText(SignIn.this, "Email  verified", Toast.LENGTH_SHORT).show();

                                Toast.makeText(SignIn.this, "Authentication successful", Toast.LENGTH_SHORT).show();
                                checkBlocked(userr.getUid());

                            } else {
                                Toast.makeText(SignIn.this, "Email not verified", Toast.LENGTH_SHORT).show();
                                //Toast.makeText(SignIn.this, "Authentication successful", Toast.LENGTH_SHORT).show();
//                                checkBlocked(userr.getUid());
                            }

                            //                           startActivity(new Intent(SignIn.this, Dashboard.class));
//                            finish();


//                            }
//                            else
//                            {
//
//                                Toast.makeText(SignIn.this, "Authentication successful", Toast.LENGTH_SHORT).show();
////
//                                startActivity(new Intent(SignIn.this, Dashboard.class));
//
//                            }
                        } else {
                            Toast.makeText(SignIn.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void checkBlocked(String uid) {
        DatabaseReference blockRef = FirebaseDatabase.getInstance().getReference("blockedUsers");
        Query query = blockRef.orderByChild("id").equalTo(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    Toast.makeText(SignIn.this, "User Blocked by admin", Toast.LENGTH_LONG).show();
                    FirebaseAuth.getInstance().signOut();
                } else {
                    startActivity(new Intent(SignIn.this, NavDrawer.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void loginUser2(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //FirebaseUser user = mAuth.getCurrentUser();


                            startActivity(new Intent(SignIn.this, NavDrawer.class));
                            finish();

                        } else {
                            Toast.makeText(SignIn.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void initializations() {
        mEmail = findViewById(R.id.editText);
        mPassword = findViewById(R.id.txtpassword);
        mSignUpLink = findViewById(R.id.btn_signup);
        mLoginBtn = findViewById(R.id.btn_signin);
        mForgotPassword = findViewById(R.id.sign_in_forgetPassword);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(SignIn.this, NavDrawer.class));
            finish();
        }
    }


}