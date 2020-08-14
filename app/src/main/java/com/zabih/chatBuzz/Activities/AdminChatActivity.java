package com.zabih.chatBuzz.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.zabih.chatBuzz.Activities.Models.AvailableChatModel;
import com.zabih.chatBuzz.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AdminChatActivity extends AppCompatActivity {
    private FirebaseRecyclerAdapter<AvailableChatModel, ChatViewHolder> mAdapter;
    private EditText mChatName;
    private RecyclerView recyclerView;
    private Button mSubmitBtn;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_chat);
        initializations();
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mChatName.getText().toString().equals(""))
                    addChatToFirebase();
            }
        });
        getChatsToRecyclerView();
    }

    private void getChatsToRecyclerView() {
        DatabaseReference chats= FirebaseDatabase.getInstance().getReference("chats");

        FirebaseRecyclerOptions<AvailableChatModel> options =
                new FirebaseRecyclerOptions.Builder<AvailableChatModel>()
                        .setQuery(chats, AvailableChatModel.class)
                        .build();
        mAdapter=new FirebaseRecyclerAdapter<AvailableChatModel, ChatViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ChatViewHolder holder, int position, @NonNull final AvailableChatModel model) {
                holder.mChatName.setText(model.getName());
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        removeChat(model.getName());
                        return false;
                    }
                });
            }

            @NonNull
            @Override
            public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.group_chat_list_row, parent, false);
                return new ChatViewHolder(view);
            }
        };
        recyclerView.setAdapter(mAdapter);
        mAdapter.startListening();
    }

    private void removeChat(String chatName) {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("chats");
        Query query=ref.orderByChild("name").equalTo(chatName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    DatabaseReference chatRef=ds.getRef();
                    chatRef.removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addChatToFirebase() {
        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("chats");
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        AvailableChatModel model = new AvailableChatModel();
        model.setDateCreated(currentDate);
        model.setName(mChatName.getText().toString());

        if (spinner.getSelectedItem().toString().equals("Student")) {
            model.setChatRole("student");
        } else if (spinner.getSelectedItem().toString().equals("Faculty")) {
            model.setChatRole("faculty");
        } else if (spinner.getSelectedItem().toString().equals("Admin")) {
            model.setChatRole("admin");
        }
        chatRef.push().setValue(model);
        mChatName.setText("");
        Toast.makeText(this, "Chat Added!", Toast.LENGTH_SHORT).show();
    }

    private void initializations() {
        mChatName = findViewById(R.id.add_chat_editText);
        mSubmitBtn = findViewById(R.id.add_chat_btn);
        spinner=findViewById(R.id.admin_chat_spinner);
        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(AdminChatActivity.this,
                R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.roles));
        roleAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(roleAdapter);
        recyclerView = findViewById(R.id.add_chat_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }
    public class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView mChatName;

        ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            mChatName = itemView.findViewById(R.id.group_chat_list_chatName);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
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


}
