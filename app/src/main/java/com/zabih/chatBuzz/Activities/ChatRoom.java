package com.zabih.chatBuzz.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zabih.chatBuzz.Activities.Adapters.MessageAdapter;
import com.zabih.chatBuzz.Activities.Models.MessageModel;
import com.zabih.chatBuzz.Activities.Models.UserModel;
import com.zabih.chatBuzz.Activities.service.MySingleton;
import com.zabih.chatBuzz.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class ChatRoom extends AppCompatActivity {
    Toolbar toolbar;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference chatRef = database.getReference("chatRef");
    List<MessageModel> mChat;
    MessageAdapter mAdapter;
    ValueEventListener chatListener;
    String myID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private ImageView mSendBtn,mImageBtn;
    private EditText mMessage;
    private RecyclerView recyclerView;
    private TextView mToolbarText;
    private UserModel otherUser;
    private Uri image_uri,finalUri=null;
    private static final int IMAGE_PICK_CODE = 123;

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
        setContentView(R.layout.activity_chat_room);
        initializations();
        mImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGallery();
            }
        });
        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(mMessage.getText().toString().equals("") && image_uri==null)) {
                    String s = mMessage.getText().toString();
                    List<String> words = Arrays.asList("fuck","asshole","cunt","dick","penis","gandu","bhenchod","dalla","chutya","chutia","harami","phudda","madrchod","shit");
                    for (String word : words) {
                        Pattern rx = Pattern.compile("\\b" + word + "\\b", Pattern.CASE_INSENSITIVE);
                        s = rx.matcher(s).replaceAll(new String(new char[word.length()]).replace('\0', '*'));
                    }

// s: i will hit your ** right *

                    String BadWordStarred=s;
                    sendMessageToFb(BadWordStarred);
                }else{
                    Toast.makeText(ChatRoom.this, "Nothing to send!", Toast.LENGTH_SHORT).show();

                }
            }
        });
        readMessages(myID, otherUser.getUserID());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            image_uri = data.getData();
            uploadImageToStorage();
        }
    }
    private void uploadImageToStorage() {
        final StorageReference picRef = FirebaseStorage.getInstance().getReference("messageImages");
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
                    finalUri= task.getResult();
                    sendMessageToFb(mMessage.getText().toString());
                    readMessages(myID,otherUser.getUserID());
                } else {
                    Toast.makeText(ChatRoom.this, "could not produce Url", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChatRoom.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    private void readMessages(final String myID, final String otheruserID) {
        mChat = new ArrayList<>();

        chatListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    MessageModel message = ds.getValue(MessageModel.class);
                    if (message.getReceiver().equals(myID) && message.getSender().equals(otheruserID) ||
                            message.getReceiver().equals(otheruserID) && message.getSender().equals(myID)) {
                        mChat.add(message);
                    }
                    mAdapter = new MessageAdapter(mChat, myID, otheruserID, ChatRoom.this);
                    recyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        chatRef.addValueEventListener(chatListener);

    }

    private void sendnotification(String message,String sender) {

        TOPIC = "/topics/userABC"; //topic must match with what the receiver subscribed to
        //            text = spinner.getSelectedItem().toString();

        NOTIFICATION_TITLE = sender;
        NOTIFICATION_MESSAGE = message;
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
        //  sendNotification(notification);

    }
    public  void sendNotification(JSONObject notification)
    {
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


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChatRoom.this, "Request error", Toast.LENGTH_LONG).show();
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

    private void sendMessageToFb(final String message) {
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        MessageModel mssg = new MessageModel();
        mssg.setMessage(message);
        mssg.setTime(currentTime);
        mssg.setDate(currentDate);
        mssg.setReceiver(otherUser.getUserID());
        mssg.setSender(myID);
        if (finalUri!=null) {
            mssg.setImage_url(finalUri.toString());
        } else {
            mssg.setImage_url("");
        }
        if (mMessage.getText().toString().equals(""))
            mssg.setMessage("");



        DatabaseReference groupChatReff = database.getReference("users").child(myID);
        Query query=groupChatReff;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserModel userModel=dataSnapshot.getValue(UserModel.class);
                String myUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                sendnotification(message,userModel.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });



        chatRef.push().setValue(mssg);
        mMessage.setText("");
        finalUri=null;
        image_uri=null;
    }


    private void initializations() {
        mMessage = findViewById(R.id.chatRoom_searchBar);
        mSendBtn = findViewById(R.id.chatRoom_searchBtn);
        mToolbarText = findViewById(R.id.chatRoom_toolbar_text);
        mImageBtn=findViewById(R.id.chatRoom_imageBtn);


        recyclerView = findViewById(R.id.chatRoom_recycler);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setStackFromEnd(true);
        recyclerView.setLayoutManager(lm);
        recyclerView.setHasFixedSize(true);

        otherUser = (UserModel) getIntent().getSerializableExtra("otherUser");
        mToolbarText.setText(otherUser.getUsername());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (chatListener != null)
            chatRef.removeEventListener(chatListener);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (chatListener != null)
            chatRef.removeEventListener(chatListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (chatListener != null)
            chatRef.removeEventListener(chatListener);
    }
}
