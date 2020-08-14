package com.zabih.chatBuzz.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
import com.zabih.chatBuzz.Activities.Models.AdvertistmentModel;
import com.zabih.chatBuzz.Activities.Models.AvailableChatModel;
import com.zabih.chatBuzz.Activities.Models.MessageModel;
import com.zabih.chatBuzz.Activities.Models.UserModel;
import com.zabih.chatBuzz.Activities.service.MySingleton;
import com.zabih.chatBuzz.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class GroupChatActivity extends AppCompatActivity {
    private static final int MESSAGE_TYPE_RIGHT = 0;
    private static final int MSG_TYPE_LEFT = 1;
    private ImageView mSendMssgBtn,mImageBtn;
    private EditText mMessage;
    private RecyclerView recyclerView;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private String myID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FirebaseRecyclerAdapter<MessageModel, MessageViewHolder> mAdapter;
    private UserModel myUserInfo;
    private AvailableChatModel groupChatInfo;
    TextView toolbarText;
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
        setContentView(R.layout.activity_group_chat);
        groupChatInfo=(AvailableChatModel) getIntent().getSerializableExtra("GroupChatInfo");
        initializations();
        receiveMessages();
        mSendMssgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(mMessage.getText().toString().equals("") && finalUri==null)) {
                    String s = mMessage.getText().toString();
                    List<String> words = Arrays.asList("fuck","asshole","cunt","dick","penis","gandu","bhenchod","dalla","chutya","chutia","harami","phudda","madrchod","shit");
                    for (String word : words) {
                        Pattern rx = Pattern.compile("\\b" + word + "\\b", Pattern.CASE_INSENSITIVE);
                        s = rx.matcher(s).replaceAll(new String(new char[word.length()]).replace('\0', '*'));
                    }

// s: i will hit your ** right *

                    String BadWordStarred=s;
                    sendMessage(BadWordStarred);

                    mMessage.setText("");
                } else{
                    Toast.makeText(GroupChatActivity.this, "Nothing to send!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        mImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGallery();
            }
        });
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
                    Toast.makeText(GroupChatActivity.this, "Request error", Toast.LENGTH_LONG).show();
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
    private void receiveMessages() {

        DatabaseReference groupChatRef = database.getReference("chats");
        Query query=groupChatRef.orderByChild("name").equalTo(groupChatInfo.getName());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren())
                {
                    DatabaseReference ref=ds.getRef().child("chat");
                    startAdapterProcess(ref);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void sendMessage(final String text) {

        DatabaseReference groupChatReff = database.getReference("users").child(myID);
        Query query=groupChatReff;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserModel userModel=dataSnapshot.getValue(UserModel.class);
                String myUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                sendnotification(text,userModel.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


        final MessageModel message = new MessageModel();
        message.setMessage(text);
        message.setSender(myID);
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm ", Locale.getDefault()).format(new Date());
        message.setDate(currentDate);
        message.setTime(currentTime);


        message.setUsername(myUserInfo.getUsername());
        if (finalUri!=null) {
            message.setImage_url(finalUri.toString());
        } else {
            message.setImage_url("");
        }
        if (mMessage.getText().toString().equals(""))
            message.setMessage("");
        DatabaseReference groupChatRef = database.getReference("chats");
        Query qquery=groupChatRef.orderByChild("name").equalTo(groupChatInfo.getName());
        qquery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    ds.getRef().child("chat").push().setValue(message);
                    finalUri=null;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(GroupChatActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

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
                    sendMessage(mMessage.getText().toString());

                } else {
                    Toast.makeText(GroupChatActivity.this, "could not produce Url", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(GroupChatActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }
    private void startAdapterProcess(DatabaseReference ref) {
        FirebaseRecyclerOptions<MessageModel> options =
                new FirebaseRecyclerOptions.Builder<MessageModel>()
                        .setQuery(ref, MessageModel.class)
                        .build();
        mAdapter = new FirebaseRecyclerAdapter<MessageModel, MessageViewHolder>(options) {

            @NonNull
            @Override
            public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (viewType == MESSAGE_TYPE_RIGHT) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.chat_message_row, parent, false);
                    return new MessageViewHolder(view);
                } else {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.chat_message_row_other, parent, false);
                    return new MessageViewHolder(view);
                }

            }

            @Override
            protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull MessageModel model) {
                holder.mMessage.setText(model.getMessage());
                holder.mTime.setText(model.getTime());
                holder.mUsername.setText(model.getUsername());
                if (!model.getImage_url().equals("")) {
                    Glide.with(GroupChatActivity.this).load(model.getImage_url()).into(holder.mImage);
                }else {
                    holder.mImage.setVisibility(View.GONE);
                }

            }

            @Override
            public int getItemViewType(int position) {
                if (myID.equals(getItem(position).getSender())) {
                    return MESSAGE_TYPE_RIGHT;
                } else {
                    return MSG_TYPE_LEFT;
                }
            }
        };

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mAdapter.getItemCount();

                int lastVisiblePosition =
                        ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    recyclerView.scrollToPosition(positionStart);
                }
            }
        });
        recyclerView.setAdapter(mAdapter);
        mAdapter.startListening();
    }

    private void initializations() {
        Intent i = getIntent();
        myUserInfo = (UserModel) i.getSerializableExtra("myUserInfo");
        toolbarText=findViewById(R.id.group_chat_activity_toolbar);
        toolbarText.setText(groupChatInfo.getName());
        mImageBtn=findViewById(R.id.group_chatRoom_image);
        mMessage = findViewById(R.id.group_chatRoom_message_editText);
        mSendMssgBtn = findViewById(R.id.group_chatRoom_sendBtn);
        recyclerView = findViewById(R.id.group_chatRoom_recycler);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setStackFromEnd(true);
        recyclerView.setLayoutManager(lm);
        recyclerView.setHasFixedSize(true);


    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter!=null)
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null)
            mAdapter.stopListening();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAdapter != null)
            mAdapter.stopListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null)
            mAdapter.stopListening();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView mMessage;
        TextView mTime;
        TextView mUsername;
        ImageView mImage;
        MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            mMessage = itemView.findViewById(R.id.chat_message_row_message);
            mTime = itemView.findViewById(R.id.chat_message_row_time);
            mUsername = itemView.findViewById(R.id.chat_message_row_username);
            mImage=itemView.findViewById(R.id.chat_message_row_image);
        }
    }
}
