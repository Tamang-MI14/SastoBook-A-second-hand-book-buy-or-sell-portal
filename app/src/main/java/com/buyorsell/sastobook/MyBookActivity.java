package com.buyorsell.sastobook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.buyorsell.sastobook.model.bookModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyBookActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    DrawerLayout drawerLayout;
    NavigationView navigationView;

    ArrayList<bookModel> list = new ArrayList<>();
    MyViewHolder myViewHolder;
    FloatingActionButton floatingbtn;
    FirebaseRecyclerOptions<bookModel> options;
    FirebaseRecyclerAdapter<bookModel,MyViewHolder> adapter;
    Toolbar toolbar;

    Context context;

    SearchView InputSearch;
    Spinner spinner;
    RecyclerView recyclerView;


    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference Dataref, UserDataRef,UserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_book);

        context = this;

        spinner = findViewById(R.id.spinner);

        floatingbtn = findViewById(R.id.floatingbtn);
        InputSearch = findViewById(R.id.inputSearch);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);

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

        navigationView.setCheckedItem(R.id.nav_mybook);

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

        Dataref = FirebaseDatabase.getInstance().getReference("MyBook").child(firebaseUser.getUid());
        UserDataRef = FirebaseDatabase.getInstance().getReference("Book");


        LoadData();
        
        
    }

    private void LoadData() {

        options = new FirebaseRecyclerOptions.Builder<bookModel>().setQuery(Dataref,bookModel.class).build();
        adapter = new FirebaseRecyclerAdapter<bookModel, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, final int position, @NonNull bookModel model) {
                holder.BookName.setText(model.getBookName());
                holder.BookCategory.setText(model.getBookCategory());
                holder.BookPrice.setText(model.getBookPrice());
                Picasso.get().load(model.getImageUrl()).into(holder.imageList);


                //updating data of user book

               holder.update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final DialogPlus dialogPlus = DialogPlus.newDialog(holder.imageList.getContext())
                                .setContentHolder(new ViewHolder(R.layout.dialog_content)).setExpanded(true, 1200)
                                .create();

                        View view = dialogPlus.getHolderView();
                       final EditText purl = view.findViewById(R.id.uimgurl);
                        final EditText name = view.findViewById(R.id.Bname);
                        final EditText price = view.findViewById(R.id.Bprice);
                        final EditText category = view.findViewById(R.id.Bcategory);
                        final Button submit = view.findViewById(R.id.usubmit);

                        purl.setText(model.getImageUrl());
                        name.setText(model.getBookName());
                        price.setText(model.getBookPrice());
                        category.setText(model.getBookCategory());

                        dialogPlus.show();

                                submit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Map<String,Object> map = new HashMap<>();

                                       FirebaseDatabase.getInstance().getReference().child("Mybook").child(firebaseUser.getUid());
                                       FirebaseDatabase.getInstance().getReference().child("Book");


                                        map.put("ImageUrl",purl.getText().toString());
                                        map.put("BookName",name.getText().toString());
                                        map.put("BookPrice",price.getText().toString());
                                        map.put("BookCategory",category.getText().toString());


                                        Dataref.child(getRef(position).getKey()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                dialogPlus.dismiss();
                                                Toast.makeText(MyBookActivity.this, "Book successfully updated", Toast.LENGTH_LONG).show();
                                            }
                                        });



                                        UserDataRef.child(getRef(position).getKey()).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                dialogPlus.dismiss();
                                            }
                                        });

                                        FirebaseDatabase.getInstance().getReference().child("MyBook").child(firebaseUser.getUid()).child(getRef(position).getKey()).updateChildren(map)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        dialogPlus.dismiss();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                        dialogPlus.dismiss();
                                            }
                                        });
                                    }
                                });

                    }
                });


                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.imageList.getContext());
                        builder.setTitle("Delete Panel");
                        builder.setMessage("Are you sure");

                        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Dataref.child(getRef(position).getKey()).removeValue();
                                Toast.makeText(MyBookActivity.this, "Book successfully deleted", Toast.LENGTH_LONG).show();

                            }
                        });

                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        builder.show();
                    }
                });



                //click listner

                /*holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MyBookActivity.this, EditBookActivity.class);
                        intent.putExtra("BookKey",getRef(position).getKey());
                        startActivity(intent);
                    }
                });*/

            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_edit_book,parent,false);
                return new MyViewHolder(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                Intent intent2 = new Intent(MyBookActivity.this, MainActivity.class);
                startActivity(intent2);
                break;

            case R.id.nav_addBook:
                Intent intent1 = new Intent(MyBookActivity.this, AddBookActivity.class);
                startActivity(intent1);
                break;

           case R.id.nav_mybook:
                break;

           case R.id.nav_message:
                Intent intent6 = new Intent(MyBookActivity.this, MessageActivity.class);
                startActivity(intent6);
                break;


            case R.id.nav_profile:
                Intent intent3 = new Intent(MyBookActivity.this, UserProfileActivity.class);
                startActivity(intent3);
                break;

            case R.id.nav_reset:
                Intent intent8 = new Intent(MyBookActivity.this, ResetPasswordActivity.class);
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
                Intent intent7 = new Intent(MyBookActivity.this, PolicyActivity.class);
                startActivity(intent7);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }
}