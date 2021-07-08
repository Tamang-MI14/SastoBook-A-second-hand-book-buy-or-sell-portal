package com.buyorsell.sastobook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ViewBookActivity extends AppCompatActivity {

    private ImageView imagelist;
    private TextView bookName, bookCategory, bookPrice, bookOwner, phoneNumber;
    DatabaseReference reference;
    Button gotoChat;
    ImageView callOwner;
    String receiverid, receivername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);

        imagelist = findViewById(R.id.imageList_viewActivity);
        bookName = findViewById(R.id.BookName_viewActivity);
        bookCategory = findViewById(R.id.BookCategory_viewActivity);
        bookPrice = findViewById(R.id.BookPrice_viewActivity);
        gotoChat = findViewById(R.id.gotochat);
        bookOwner = findViewById(R.id.BookOwner_viewActivity);
        phoneNumber = findViewById(R.id.BookPhone_viewActivity);
        callOwner = findViewById(R.id.callOwner);

        reference = FirebaseDatabase.getInstance().getReference("Book");

        String BookKey = getIntent().getStringExtra("BookKey");

        reference.child(BookKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        String Category = snapshot.child("BookCategory").getValue().toString();
                        String Name = snapshot.child("BookName").getValue().toString();
                        String Price = snapshot.child("BookPrice").getValue().toString();
                        String imageUrl = snapshot.child("ImageUrl").getValue().toString();
                        String owner = snapshot.child("BookOwner").getValue().toString();
                        String Phone = snapshot.child("PhoneNumber").getValue().toString();
                        receiverid = snapshot.child("userId").getValue().toString();
                        receivername = snapshot.child("BookOwner").getValue().toString();

                        Picasso.get().load(imageUrl).into(imagelist);
                        bookCategory.setText(Category);
                        bookName.setText(Name);
                        bookPrice.setText(Price);
                        bookOwner.setText(owner);
                        phoneNumber.setText(Phone);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        gotoChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String visit_user_id = receiverid;
                String visit_username = receivername;
                Intent profileIntent = new Intent(ViewBookActivity.this, ChatActivity.class);
                profileIntent.putExtra("visit_user_id", visit_user_id);
                profileIntent.putExtra("visit_user_name", visit_username);
                startActivity(profileIntent);
            }
        });

        callOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                reference = FirebaseDatabase.getInstance().getReference().child("Book").child(BookKey);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String phone = snapshot.child("PhoneNumber").getValue().toString();
                        intent.setData(Uri.parse("tel: "+ phone));
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}