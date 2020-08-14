package com.zabih.chatBuzz.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zabih.chatBuzz.R;

import java.util.UUID;

public class UpoadFIle extends AppCompatActivity {
    private Uri filePath;
    //
    private final int PICK_IMAGE_REQUEST = 22;
    String uri;
    StorageReference storageReference;
    FirebaseStorage storage;
    private DatabaseReference mDatabase;

    Button upload,selct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upoad_file);
    upload=findViewById(R.id.btn_upload);
    selct=findViewById(R.id.btn_choose);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
upload.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        uploadFile();
    }
});
        selct.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("application/vnd.ms-excel/.csv");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select File from here..."),
                PICK_IMAGE_REQUEST);
    }
});
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //when the user choses the file
        if (requestCode ==                 PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
int ah=00;
                filePath = data.getData();
                int ccc=0;

                //uploading the file
                //uploadFile(data.getData());
            }else{
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }
    // UploadImage method
    private void uploadFile() {


       String cc= String.valueOf(filePath);
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "admin/"
                                    + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                                    while (!urlTask.isSuccessful()) ;
                                    Uri downloadUrl = urlTask.getResult();

                                    final String sdownload_url = String.valueOf(downloadUrl);
                                    uri = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                                    // Image uploaded successfully
                                    // Dismiss dia
                                    progressDialog.dismiss();
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    String email = user.getEmail();
                                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                  //                  String valllll=Advertisemnt_name.getText().toString();
//String aaaa=Advertisment_date.getText().toString();
                    //                EventModel eventModel = new EventModel(Advertisemnt_name.getText().toString(),date_String, email,sdownload_url, location.getText().toString(),description.getText().toString());
                                    //                       mDatabase.child("Admin").child("Notification").child("Event").child(email).push().setValue(eventModel);
mDatabase= FirebaseDatabase.getInstance().getReference();
                      //              eventModel.setDate( date_String);
                        //            eventModel.setName( Advertisemnt_name.getText().toString());
                          //          String key = mDatabase.child("Admin").child("Notification").child("Event").push().getKey();
                               final DatabaseReference saveInfo = mDatabase.child("Admin").child("File").child(sdownload_url);
                              //      saveInfo.setValue(eventModel);
                                //            .show();




                                    Toast
                                            .makeText(UpoadFIle.this,
                                                    "Successfull" ,
                                                    Toast.LENGTH_SHORT)
                                            .show();

                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(UpoadFIle.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int) progress + "%");
                                }
                            });
        }
    }
}
