package com.buyorsell.sastobook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.buyorsell.sastobook.adapters.MessagesAdapter;
import com.buyorsell.sastobook.model.Messages;
import com.buyorsell.sastobook.model.bookModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private String messageReceiverID, messageReceiverName, messageReceiverImage, messageSenderID;

    private TextView userName;
    private CircleImageView userImage;

    CardView sendBtn;
    EditText edtMessage;


    String senderRoom, reciverRoom;

    FirebaseUser fuser;
    FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;

//
//    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;

    private RecyclerView recyclerView;

    MessagesAdapter Adapter;
    List<Messages> mchat;

    private String saveCurrentTime, saveCurrentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messageReceiverID = getIntent().getExtras().get("visit_user_id").toString();
        messageReceiverName = getIntent().getExtras().get("visit_user_name").toString();

        fuser = FirebaseAuth.getInstance().getCurrentUser();


      /*  senderRoom = messageSenderID + messageReceiverID;
        reciverRoom = messageReceiverID + messageSenderID;*/


//        messageReceiverImage = getIntent().getExtras().get("visit_image").toString();

        userName = findViewById(R.id.usernameChat);

        sendBtn = findViewById(R.id.sendBtn);
        edtMessage = findViewById(R.id.edtMessage);

        recyclerView = findViewById(R.id.messageAdapter);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


   /*     Toast.makeText(ChatActivity.this, "userid: " + messageReceiverID, Toast.LENGTH_LONG).show();

        Log.d("com.milan.bhosdi", "recid: "+ messageReceiverID);*/

        userName.setText(""+ messageReceiverName);


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = edtMessage.getText().toString();
                if (!msg.equals("")){
                    sendMessage(fuser.getUid(), messageReceiverID, msg, fuser.getEmail());
                }else {
                    Toast.makeText(ChatActivity.this, "You cant send empty message", Toast.LENGTH_LONG).show();
                }

                edtMessage.setText("");
            }
        });


        reference = FirebaseDatabase.getInstance().getReference("Users").child(messageReceiverID);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookModel BookModel = snapshot.getValue(bookModel.class);
                userName.setText(BookModel.getBookOwner());

                readMessage(fuser.getUid(), messageReceiverID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage (String sender, String receiver, String message, String username){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("email", username);


        reference.child("Chats").push().setValue(hashMap);
        reference.child("Message").child(sender).setValue(hashMap);
    }

    private void readMessage(String sender, String messageReceiverID){
        mchat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mchat.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Messages messages = dataSnapshot.getValue(Messages.class);
                    mchat.add(messages);
                    Adapter = new MessagesAdapter(ChatActivity.this, mchat);
                    recyclerView.setAdapter(Adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
