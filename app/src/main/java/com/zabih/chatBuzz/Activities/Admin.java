package com.zabih.chatBuzz.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zabih.chatBuzz.Activities.Models.AnnouncementModel;
import com.zabih.chatBuzz.Activities.service.MySingleton;
import com.zabih.chatBuzz.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Admin extends AppCompatActivity {
    final String TAG = "NOTIFICATION TAG";
    //  private FirebaseAuth mAuth;
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAuLqQ63Y:APA91bGFH49Hgg2Hmc54L5uL_5q0Ol2kq3p_M6Ba4TdMJ_bkyuySrpQplgsk2tgvJiMDk6y-ihy77dASrtBRqW_tV0P7nT9kidAw76JeLt30LnDjKDX-6lBybFNs39j2-nn3pAjm-NBa";
    final private String contentType = "application/json";
    //    EditText edtTitle;
//    EditText edtMessage;
    String currentDate;
    Spinner spinner;
    String[] items = new String[]{"lost and Found", "Announcement"};
    EditText notication_text;
    String valueOfEditText;
    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String currentTime;
    Button textView_event;
    String TOPIC;
    String text;
    Button adv,upl;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
//spinner=findViewById(R.id.spinner);
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(Admin.this,
                R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.rol));
        roleAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(roleAdapter);
        upl=findViewById(R.id.upl_txt);
        upl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Admin.this,UpoadFIle.class));
            }
        });

        textView_event = findViewById(R.id.event_textview);
        adv = findViewById(R.id.adve_txt);
        notication_text = findViewById(R.id.nofication_message_editText);
        //  spinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //      spinner.setAdapter(adapter);
        Button btnSend = findViewById(R.id.btnSend);
        currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
         currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        mDatabase = FirebaseDatabase.getInstance().getReference();
//        mAuth = FirebaseAuth.getInstance();
        adv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Admin.this, Advertisement.class));

            }
        });

        textView_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Admin.this, EventActivity.class));
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(notication_text.getText().toString().isEmpty())
                {
                    Toast.makeText(Admin.this, "please fill form", Toast.LENGTH_LONG).show();
                }
                TOPIC = "/topics/userABC"; //topic must match with what the receiver subscribed to
                //            text = spinner.getSelectedItem().toString();
             //   NOTIFICATION_TITLE="Faculty";
                //
                //      NOTIFICATION_TITLE = "Announcement";
                NOTIFICATION_MESSAGE = notication_text.getText().toString();
                valueOfEditText = notication_text.getText().toString();
                JSONObject notification = new JSONObject();
                JSONObject notifcationBody = new JSONObject();






                if (spinner.getSelectedItem().toString().equals("Faculty")) {
                    NOTIFICATION_TITLE="Faculty";

          //          sendNotification(notification,"Faculty");

                }
                else

                if (spinner.getSelectedItem().toString().equals("Student")) {
                    NOTIFICATION_TITLE="Student";
        //            sendNotification(notification,"Student");

                }
                else

                if (spinner.getSelectedItem().toString().equals("Sports")) {
                    //                  NOTIFICATION_TITLE="Sports";
                    NOTIFICATION_TITLE="Sports";
      //              sendNotification(notification,"Sports");

                }
                else

                if (spinner.getSelectedItem().toString().equals("Public")) {
                    //                NOTIFICATION_TITLE="Public";
                    NOTIFICATION_TITLE="public";
    //                sendNotification(notification,"Public");

                }
                else

                if (spinner.getSelectedItem().toString().equals("FYP")) {
                    //              NOTIFICATION_TITLE="FYP";
                    NOTIFICATION_TITLE="FYP";
  //                  sendNotification(notification,"FYP");

                }
                else

                if (spinner.getSelectedItem().toString().equals("Department")) {
                    //            NOTIFICATION_TITLE="Department";
                    NOTIFICATION_TITLE="Department";
//                    sendNotification(notification,"Department");

                }

                else

                if (spinner.getSelectedItem().toString().equals("Emergency")) {
                    //          NOTIFICATION_TITLE="Emergency";
                    NOTIFICATION_TITLE="Emergency";
                    //sendNotification(notification,"Emergency");

                }
                else

                if (spinner.getSelectedItem().toString().equals("Hod")) {
                    //        NOTIFICATION_TITLE="Hod";
                    NOTIFICATION_TITLE="Hod";
                    //sendNotification(notification,"Hod");

                }








                try {
                    notifcationBody.put("title", NOTIFICATION_TITLE);
                    notifcationBody.put("message", NOTIFICATION_MESSAGE);

                    notification.put("to", TOPIC);
                    notification.put("data", notifcationBody);
                } catch (JSONException e) {
                    Log.e(TAG, "onCreate: " + e.getMessage());
                }


                NOTIFICATION_TITLE=null;
                if (spinner.getSelectedItem().toString().equals("Faculty")) {

                    sendNotification(notification,"Faculty");

                }
                else

                if (spinner.getSelectedItem().toString().equals("Student")) {
                    NOTIFICATION_TITLE="";
                    sendNotification(notification,"Student");

                }
                else

                if (spinner.getSelectedItem().toString().equals("Sports")) {
  //                  NOTIFICATION_TITLE="Sports";
                    NOTIFICATION_TITLE="";
                    sendNotification(notification,"Sports");

                }
                else

                if (spinner.getSelectedItem().toString().equals("Public")) {
    //                NOTIFICATION_TITLE="Public";
                    NOTIFICATION_TITLE="";
                    sendNotification(notification,"Public");

                }
                else

                if (spinner.getSelectedItem().toString().equals("FYP")) {
      //              NOTIFICATION_TITLE="FYP";
                    NOTIFICATION_TITLE="";
                    sendNotification(notification,"FYP");

                }
                else

                if (spinner.getSelectedItem().toString().equals("Department")) {
        //            NOTIFICATION_TITLE="Department";
                    NOTIFICATION_TITLE="";
                    sendNotification(notification,"Department");

                }

                else

                if (spinner.getSelectedItem().toString().equals("Emergency")) {
          //          NOTIFICATION_TITLE="Emergency";
                    NOTIFICATION_TITLE="";
                    sendNotification(notification,"Emergency");

                }
                else

                if (spinner.getSelectedItem().toString().equals("Hod")) {
            //        NOTIFICATION_TITLE="Hod";
                    NOTIFICATION_TITLE="";
                    sendNotification(notification,"Hod");

                }


            }
        });
    }

    private void sendNotification(JSONObject notification, final String role) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "onResponse: " + response.toString());
                        NOTIFICATION_TITLE=role;
                        String val = text;
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String email = user.getEmail();
                        AnnouncementModel announcementModel = new AnnouncementModel(email, valueOfEditText,role);
                      announcementModel.setDate(currentDate);
                      announcementModel.setTime(currentTime);
                        mDatabase.child("Admin").child("Notification").child("Announcement").push().setValue(announcementModel);

                        Toast.makeText(Admin.this, "Successfully sended", Toast.LENGTH_LONG).show();

                        notication_text.setText("");


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Admin.this, "Request error", Toast.LENGTH_LONG).show();
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
}