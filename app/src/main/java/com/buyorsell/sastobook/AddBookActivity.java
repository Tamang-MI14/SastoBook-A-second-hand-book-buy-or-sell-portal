package com.buyorsell.sastobook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddBookActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    private static final int REQUEST_CODE_IMAGE = 101;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ProgressBar progressBar;
    private ImageView imageViewAdd;
    private EditText inputBookName, inputBookPrice, bookOwner, PhoneNumber;
    private Button btnUpload;
    Uri imageUri;
    Spinner spinner;
    Boolean isImageAdded=false;
    FirebaseAuth firebaseAuth;
    DatabaseReference Dataref, UserDataRef, UserRef;
    StorageReference StorageRef;

    FirebaseUser firebaseUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        progressBar = findViewById(R.id.progressbar);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_draw_open, R.string.navigation_draw_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        View navView = navigationView.inflateHeaderView(R.layout.drawer_header);

        navigationView.setCheckedItem(R.id.nav_addBook);

        imageViewAdd = findViewById(R.id.imageViewAdd);
        inputBookName = findViewById(R.id.inputBookName);
        inputBookPrice = findViewById(R.id.inputBookPrice);
        btnUpload = findViewById(R.id.btnUpload);
        bookOwner = findViewById(R.id.bookownerET);
        PhoneNumber = findViewById(R.id.PhoneNumberET);


        firebaseAuth = FirebaseAuth.getInstance();
        Dataref = FirebaseDatabase.getInstance().getReference().child("Book");
        StorageRef = FirebaseStorage.getInstance().getReference().child("BookImage");


        //FUNCTION for showing the name of logged in Users
        TextView headername = navView.findViewById(R.id.header_username);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        UserRef = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fullnamelabel = snapshot.child("username").getValue().toString();
                headername.setText(fullnamelabel);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        spinner = findViewById(R.id.category);

        List<String> categories = new ArrayList<>();
        categories.add(0, "Choose Book Category");
        categories.add("Academic");
        categories.add("Si-Fi");
        categories.add("Comic");
        categories.add("Classic");
        categories.add("Finance");
        ArrayAdapter<String> dataAdapter;
        dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).equals("choose event")) {

                } else {
                    String item = adapterView.getItemAtPosition(i).toString();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        imageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE_IMAGE);
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String bookName = inputBookName.getText().toString();
                final String bookPrice = inputBookPrice.getText().toString();
                final  String bookowner = bookOwner.getText().toString();
                final String phoneNumber = PhoneNumber.getText().toString();
                String spin = spinner.getSelectedItem().toString();


                if (isImageAdded != false && bookName != null && bookPrice != null && spin != null && phoneNumber != null) {
                    uploadBook(bookName, bookPrice, spin, bookowner, phoneNumber);
                }else{
                    Toast.makeText(getApplicationContext(), "Please Fill all fields information", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void uploadBook(final String bookName, final String bookPrice, String spin, String bookowner, String phoneNumber) {
        progressBar.setVisibility(View.VISIBLE);
        final String key = Dataref.push().getKey();
        StorageRef.child(key + ".jpg").putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                StorageRef.child(key + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        FirebaseUser rUser = firebaseAuth.getCurrentUser();
                        String userId = rUser.getUid();
                        Dataref = FirebaseDatabase.getInstance().getReference("Book");
                        UserDataRef = FirebaseDatabase.getInstance().getReference().child("MyBook").child(userId);

                        assert rUser != null;

                        HashMap hashMap = new HashMap();
                        hashMap.put("BookId", key);
                        hashMap.put("userId", userId);
                        hashMap.put("BookName", bookName);
                        hashMap.put("BookPrice", bookPrice);
                        hashMap.put("ImageUrl", uri.toString());
                        hashMap.put("BookCategory", spin);
                        hashMap.put("BookOwner", bookowner);
                        hashMap.put("PhoneNumber", phoneNumber);

                        UserDataRef.push().setValue(hashMap);
                        Dataref.push().setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressBar.setVisibility(View.GONE);
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                Toast.makeText(AddBookActivity.this, "Book Added Successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE && data != null) {
            imageUri = data.getData();
            isImageAdded = true;
            imageViewAdd.setImageURI(imageUri);
        }
    }



    //for navigation bar

    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                Intent intent2 = new Intent(AddBookActivity.this, MainActivity.class);
                startActivity(intent2);
                break;

            case R.id.nav_addBook:
                break;

           case R.id.nav_mybook:
                Intent intent5 = new Intent(AddBookActivity.this, MyBookActivity.class);
                startActivity(intent5);
                break;

          /*  case R.id.nav_message:
                Intent intent6 = new Intent(MainActivity.this, MessageActivity.class);
                startActivity(intent6);
                break;*/


            case R.id.nav_profile:
                Intent intent3 = new Intent(AddBookActivity.this, UserProfileActivity.class);
                startActivity(intent3);
                break;

            case R.id.nav_reset:
                Intent intent8 = new Intent(AddBookActivity.this, ResetPasswordActivity.class);
                startActivity(intent8);
                break;

            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;


            case R.id.nav_share:
                Toast.makeText(this, "Shared Successfully!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_policy:
                Intent intent7 = new Intent(AddBookActivity.this, PolicyActivity.class);
                startActivity(intent7);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}