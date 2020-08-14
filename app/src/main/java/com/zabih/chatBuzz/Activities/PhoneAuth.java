package com.zabih.chatBuzz.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.zabih.chatBuzz.R;

import java.util.concurrent.TimeUnit;

public class PhoneAuth extends AppCompatActivity {
    Button btnGenerateOTP, btnSignIn;
    EditText etPhoneNumber, etOTP;
    String phoneNumber, otp;
    FirebaseAuth auth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private String verificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);
        findViews();
        StartFirebaseLogin();
        btnGenerateOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = etPhoneNumber.getText().toString();


                if(etPhoneNumber.getText().toString().isEmpty())
                {

                    etPhoneNumber.setText("Please enter phone no.");
                    etPhoneNumber.requestFocus();
                }
                else

                {
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneNumber,                     // Phone number to verify
                            60,                           // Timeout duration
                            TimeUnit.SECONDS,                // Unit of timeout
                            PhoneAuth.this,        // Activity (for callback binding)
                            mCallback);                      // OnVerificationStateChangedCallbacks
                }}
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                otp = etOTP.getText().toString();
//                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);
//                startActivity(new Intent(PhoneAuth.this, Dashboard.class));


                otp = etOTP.getText().toString();

                if(etPhoneNumber.getText().toString().isEmpty())
                {

                    etPhoneNumber.setText("Please enter phone no.");
                    etPhoneNumber.requestFocus();
                }


                if (etOTP.getText().toString().isEmpty()) {

                    etOTP.setError("Please Enter Otp");
                    etOTP.requestFocus();

                }
                else

                {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);

//Toast.makeText(PhoneAuth.this,"AUtomatically code adde",Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(PhoneAuth.this, NavDrawer.class));
                }


//SigninWithPhone(credential);
            }
        });
    }

    @Override
    public void onBackPressed() {

   //  startActivity(new Intent(PhoneAuth.this,PhoneAuth.class));
        // Do Here what ever you want do on back press;
    }
    private void SigninWithPhone(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(PhoneAuth.this, Dashboard.class));
                            finish();
                        } else {
//Toast.makeText(MainActivity.this,"Incorrect OTP",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void findViews() {
        btnGenerateOTP = findViewById(R.id.btn_generate_otp);
        btnSignIn = findViewById(R.id.btn_sign_in);
        etPhoneNumber = findViewById(R.id.et_phone_number);
        etOTP = findViewById(R.id.et_otp);
    }

    private void StartFirebaseLogin() {
        auth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {







                startActivity(new Intent(PhoneAuth.this, NavDrawer.class));










                    Toast.makeText(PhoneAuth.this, "verification Successful", Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    etPhoneNumber.setError("Invalid phone number.");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(PhoneAuth.this,
                            "Trying too many timeS",
                            Toast.LENGTH_SHORT).show();
                }

                //      Toast.makeText(PhoneAuth.this, "verification fialed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                Toast.makeText(PhoneAuth.this, "Code sent", Toast.LENGTH_SHORT).show();
            }
        };
    }
    private boolean validatePhoneNumber(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            etPhoneNumber.setError("Invalid phone number.");
            return false;
        }
        return true;
    }
    private void verifyPhoneNumberInit() {
        phoneNumber = etPhoneNumber.getText().toString();
        if (!validatePhoneNumber(phoneNumber)) {
            return;
        }
        //       verifyPhoneNumber(phoneNumber);

    }

}