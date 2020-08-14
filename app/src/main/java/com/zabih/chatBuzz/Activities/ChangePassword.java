package com.zabih.chatBuzz.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zabih.chatBuzz.R;

public class ChangePassword extends AppCompatActivity {
EditText psd,re_psd,curreent_password;
Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

    psd=findViewById(R.id.psd);
    re_psd=findViewById(R.id.reenter_psd);
    update=findViewById(R.id.updaye);
        curreent_password=findViewById(R.id.curreent_password);

      update.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              if ((psd.getText().toString().equals(re_psd.getText().toString()))) {

                  updatedata();
                  //            registerNewUser(mEmail.getText().toString(), mPassword.getText().toString(),
//                    mUsername.getText().toString());
              } else {
                  psd.setText("");
                  re_psd.setText("");
                  Toast.makeText(ChangePassword.this, "Passwords don't match!", Toast.LENGTH_SHORT).show();
              }
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
String password=curreent_password.getText().toString();
        AuthCredential credential = EmailAuthProvider
                .getCredential(userEmail, password);

// Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(psd.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ChangePassword.this,"Password updated",Toast.LENGTH_SHORT).show();
                                        FirebaseAuth.getInstance().signOut();

                                        startActivity(new Intent(ChangePassword.this,SignIn.class));
                                        //

                                 //       Log.d(TAG, "Password updated");
                                    } else {

                                        Toast.makeText(ChangePassword.this,"Error password not updated",Toast.LENGTH_SHORT).show();
                                       // Log.d(TAG, "")
                                    }
                                }
                            });
                        } else {
                   Toast.makeText(ChangePassword.this,"Auth Faild",Toast.LENGTH_SHORT).show();
                         }
                    }
                });
    }
}
