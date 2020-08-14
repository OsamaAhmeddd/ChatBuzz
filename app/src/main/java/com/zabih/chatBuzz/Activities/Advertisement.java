package com.zabih.chatBuzz.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
import com.zabih.chatBuzz.Activities.Models.AdvertistmentModel;
import com.zabih.chatBuzz.Activities.service.MySingleton;
import com.zabih.chatBuzz.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Advertisement extends AppCompatActivity {
    private final int PICK_IMAGE_REQUEST = 22;
    EditText eventName, eventDate;
    Button create;
    FirebaseStorage storage;
    ImageView img_event;
    String uri;
    StorageReference storageReference;
    private Uri filePath;

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String urrrrl="";
    String TOPIC;
    private DatabaseReference mDatabase;
    final String TAG = "NOTIFICATION TAG";
//    private final int PICK_IMAGE_REQUEST = 22;
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAuLqQ63Y:APA91bGFH49Hgg2Hmc54L5uL_5q0Ol2kq3p_M6Ba4TdMJ_bkyuySrpQplgsk2tgvJiMDk6y-ihy77dASrtBRqW_tV0P7nT9kidAw76JeLt30LnDjKDX-6lBybFNs39j2-nn3pAjm-NBa";
    final private String contentType = "application/json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        eventName = findViewById(R.id.eventName);
        //  eventDate=findViewById(R.id.EventDate);
        create = findViewById(R.id.create_btn);
        img_event = findViewById(R.id.img_event);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        img_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();

            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

if(eventName.getText().toString().isEmpty()||filePath==null)
{
    Toast
            .makeText(Advertisement.this,
                    "please fill form",
                    Toast.LENGTH_SHORT)
            .show();


}
else {

    TOPIC = "/topics/userABC"; //topic must match with what the receiver subscribed to
    //            text = spinner.getSelectedItem().toString();

    NOTIFICATION_TITLE = "Advertisement";
    NOTIFICATION_MESSAGE = eventName.getText().toString();
    //  String valueOfEditText = notication_text.getText().toString();
    JSONObject notification = new JSONObject();
    JSONObject notifcationBody = new JSONObject();
    try {
        notifcationBody.put("title", NOTIFICATION_TITLE);
        notifcationBody.put("message", NOTIFICATION_MESSAGE);

        notification.put("to", TOPIC);
        notification.put("data", notifcationBody);
    } catch (JSONException e) {
        Log.e(TAG, "onCreate: " + e.getMessage());
    }
    uploadImage(notification);
  //  sendNotification(notification);

}               //uploadImage();
            }
        });
    }


    private void sendNotification(JSONObject notification, final String urlo) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                        // String val=text;
                    //    eventName.setText("");
                        //Advertisment_date.setText("");
              //          uploadImage();
//uploadImage();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        String email = user.getEmail();
                        AdvertistmentModel eventModel = new AdvertistmentModel(eventName.getText().toString(), email, urlo);
                        //  String key = mDatabase.child("Admin").child("Notification").child("Advert").push().getKey();
                        mDatabase.child("Admin").child("Notification").child("Advert").push().setValue(eventModel);
                        //    saveInfo.setValue(eventModel);
                        filePath=null;
                        eventName.setText("");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Advertisement.this, "Request error", Toast.LENGTH_LONG).show();
                        Log.i(TAG, "onErrorResponse: Didn't work");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }


    private void SelectImage() {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                img_event.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    // UploadImage method
    private void uploadImage(final JSONObject notification) {
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
                            "images/"
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
                     urrrrl=sdownload_url;

                     sendNotification(notification,sdownload_url);
                                    Toast
                                            .makeText(Advertisement.this,
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();

                                    Toast
                                            .makeText(Advertisement.this,
                                                    "Data saved",
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
                                    .makeText(Advertisement.this,
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
