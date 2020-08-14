package com.zabih.chatBuzz.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.zabih.chatBuzz.Activities.Models.EventModel;
import com.zabih.chatBuzz.Activities.service.MySingleton;
import com.zabih.chatBuzz.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class EventActivity extends AppCompatActivity {
    final String TAG = "NOTIFICATION TAG";
    FirebaseStorage storage;
    ImageView img_event;
    String uri;
    StorageReference storageReference;


    private final int PICK_IMAGE_REQUEST = 22;
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAuLqQ63Y:APA91bGFH49Hgg2Hmc54L5uL_5q0Ol2kq3p_M6Ba4TdMJ_bkyuySrpQplgsk2tgvJiMDk6y-ihy77dASrtBRqW_tV0P7nT9kidAw76JeLt30LnDjKDX-6lBybFNs39j2-nn3pAjm-NBa";
    final private String contentType = "application/json";
    EditText Advertisemnt_name, Advertisment_date, location, description;

    Button btn_adv;
    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;
    CircleImageView img;
    private DatabaseReference mDatabase;
    //    String uri;
    //  StorageReference storageReference;
    private Uri filePath;
    //    private DatabaseReference mDatabase;
//String urii;
    //  StorageReference storageReference;
    String date_String = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        Advertisemnt_name = findViewById(R.id.edittext_name);
        Advertisment_date = findViewById(R.id.edittext_date);
        btn_adv = findViewById(R.id.btnSend);
        location = findViewById(R.id.editText2);
        description = findViewById(R.id.EventDescriptionTextView);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        img = findViewById(R.id.activity_event_image);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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
        });
        btn_adv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Advertisemnt_name.getText().toString().isEmpty() ||
                        Advertisment_date.getText().toString().isEmpty() ||
                 filePath==null   ||location.getText().toString().isEmpty() || description.getText().toString().isEmpty()


                ) {
                    Toast
                            .makeText(EventActivity.this,
                                    "Please fill the form",
                                    Toast.LENGTH_SHORT)
                            .show()      ;          } else {

                    TOPIC = "/topics/userABC"; //topic must match with what the receiver subscribed to
                    //            text = spinner.getSelectedItem().toString();
                    uploadImage();
                    NOTIFICATION_TITLE = "Event";
                    NOTIFICATION_MESSAGE = Advertisemnt_name.getText().toString();
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


                    sendNotification(notification);
//uploadImage();
                }
            }
        });
        final Calendar myCalendar = Calendar.getInstance();


        //   EditText edittext= (EditText) findViewById(R.id.Birthday);
        final DatePickerDialog.OnDateSetListener datee = new DatePickerDialog.OnDateSetListener() {


            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                date_String = String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear) + "/" + String.valueOf(year);
                //   updateLabel();
            }

        };
        Advertisment_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EventActivity.this, datee, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                Advertisment_date.setText(date_String);
            }
        });

    }

    //
//    private void updateLabel() {
//
//        String myFormat = "MM/dd/yy"; //In which you need put here
//        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
//date_String=sdf.format(myCalendar.getTime());
//        Advertisment_date.setText(sdf.format(myCalendar.getTime()));
//    }
    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                        // String val=text;

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EventActivity.this, "Request error", Toast.LENGTH_LONG).show();
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
                img.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    // UploadImage method
    private void uploadImage() {
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
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    String email = user.getEmail();
                                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    String valllll = Advertisemnt_name.getText().toString();
//String aaaa=Advertisment_date.getText().toString();
                                    EventModel eventModel = new EventModel(Advertisemnt_name.getText().toString(), date_String, email, sdownload_url, location.getText().toString(), description.getText().toString());
                                    //                       mDatabase.child("Admin").child("Notification").child("Event").child(email).push().setValue(eventModel);

                                    eventModel.setDate(date_String);
                                    eventModel.setName(Advertisemnt_name.getText().toString());
                                    String key = mDatabase.child("Admin").child("Notification").child("Event").push().getKey();
                                    final DatabaseReference saveInfo = mDatabase.child("Admin").child("Event").child(key);
                                    saveInfo.setValue(eventModel);
                                    Toast
                                            .makeText(EventActivity.this,
                                                    "Data saved",
                                                    Toast.LENGTH_SHORT)
                                            .show();


                                    Toast
                                            .makeText(EventActivity.this,
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();

                                    Toast
                                            .makeText(EventActivity.this,
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
                                    .makeText(EventActivity.this,
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
