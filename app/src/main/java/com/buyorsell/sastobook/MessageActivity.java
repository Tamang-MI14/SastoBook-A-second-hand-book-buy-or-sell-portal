package com.buyorsell.sastobook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.buyorsell.sastobook.adapters.MessageList;
import com.buyorsell.sastobook.adapters.UserAdapter;
import com.buyorsell.sastobook.model.Messages;
import com.buyorsell.sastobook.model.UserHelperClass;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {


    ListView listView;

    TextView msgTV;

//    private UserAdapter userAdapter;
//    private List<UserHelperClass> mUsers;
    List<Messages> userlist = new ArrayList<>();

    FirebaseUser fuser;
    DatabaseReference reference;
    Query postQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        listView = findViewById(R.id.listViewArtists);

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        String userId = fuser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("Message");

        postQuery =reference.child(userId);
        userlist = new ArrayList<>();

        Log.d("com.milan.test", userId);



    }
    @Override
    protected void onStart() {
        super.onStart();

        userlist.clear();
        postQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot postsnapshot : snapshot.getChildren()) {
                    Messages violation = postsnapshot.getValue(Messages.class);
                    userlist.add(violation);
                }
                MessageList adapter = new MessageList(MessageActivity.this, userlist);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}


