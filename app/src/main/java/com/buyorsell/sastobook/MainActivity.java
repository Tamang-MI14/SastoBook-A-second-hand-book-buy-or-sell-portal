package com.buyorsell.sastobook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.buyorsell.sastobook.model.bookModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    ArrayList<bookModel> list = new ArrayList<>();
    MyViewHolder myViewHolder;
    FloatingActionButton floatingbtn;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    FirebaseRecyclerOptions<bookModel> options;
    FirebaseRecyclerAdapter<bookModel, MyViewHolder> adapter;
    Query query;

    Context context;

    SearchView InputSearch;
    Spinner spinner;
    RecyclerView recyclerView;

    DatabaseReference Dataref, UserRef;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        context = this;

        spinner = findViewById(R.id.spinner);

        floatingbtn = findViewById(R.id.floatingbtn);
        InputSearch = findViewById(R.id.inputSearch);
        recyclerView = findViewById(R.id.recyclerView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);

        View navView = navigationView.inflateHeaderView(R.layout.drawer_header);

        Dataref = FirebaseDatabase.getInstance().getReference().child("Book");


        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_draw_open, R.string.navigation_draw_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_home);


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



        ArrayAdapter<CharSequence> dataAdapter;
        dataAdapter = ArrayAdapter.createFromResource(this,
                R.array.BookCategory, android.R.layout.simple_spinner_item);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);

        query = Dataref.orderByChild("BookCategory");

        InputSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final String s = spinner.getSelectedItem().toString();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Book");
                Query query = reference.orderByChild("BookName").startAt(newText).endAt(newText + "\uf8ff");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot item : snapshot.getChildren()) {
                            options = new FirebaseRecyclerOptions.Builder<bookModel>().setQuery(query, bookModel.class).build();
                            adapter = new FirebaseRecyclerAdapter<bookModel, MyViewHolder>(options) {
                                @Override
                                protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull bookModel model) {
                                    holder.BookName.setText(model.getBookName());
                                    holder.BookCategory.setText(model.getBookCategory());
                                    holder.BookPrice.setText(model.getBookPrice());
                                    Picasso.get().load(model.getImageUrl()).into(holder.imageList);

                                    //click listner

                                    holder.v.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(MainActivity.this, ViewBookActivity.class);
                                            intent.putExtra("BookKey", getRef(position).getKey());
                                            startActivity(intent);
                                        }
                                    });
                                }

                                @NonNull
                                @Override
                                public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                                    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view, parent, false);
                                    return new MyViewHolder(v);
                                }
                            };
                            adapter.startListening();
                            recyclerView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                return true;
            }
        });

        floatingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddBookActivity.class));
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                String spinnerCountry = parent.getItemAtPosition(position).toString();
                if(spinnerCountry.equals("All Books")){
                    LoadData();
                }else {
                    list.clear();
                    query.equalTo(spinnerCountry).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot item : snapshot.getChildren()) {
                                String category = item.child("BookCategory").getValue(String.class);
                                Log.d("com.buyorsell.sastobook", "Cat: " + spinnerCountry);
                                Query query1 = query.equalTo(spinnerCountry);
                                options = new FirebaseRecyclerOptions.Builder<bookModel>().setQuery(query1, bookModel.class).build();
                                adapter = new FirebaseRecyclerAdapter<bookModel, MyViewHolder>(options) {
                                    @Override
                                    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull bookModel model) {
                                        holder.BookName.setText(model.getBookName());
                                        holder.BookCategory.setText(model.getBookCategory());
                                        holder.BookPrice.setText(model.getBookPrice());
                                        Picasso.get().load(model.getImageUrl()).into(holder.imageList);

                                        //click listner

                                        holder.v.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(MainActivity.this, ViewBookActivity.class);
                                                intent.putExtra("BookKey", getRef(position).getKey());
                                                startActivity(intent);
                                            }
                                        });
                                    }

                                    @NonNull
                                    @Override
                                    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                                        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view, parent, false);
                                        return new MyViewHolder(v);
                                    }
                                };
                                adapter.startListening();
                                recyclerView.setAdapter(adapter);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void processSearch(String s) {

    }

    private void LoadData() {
        options = new FirebaseRecyclerOptions.Builder<bookModel>().setQuery(Dataref, bookModel.class).build();
        adapter = new FirebaseRecyclerAdapter<bookModel, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull bookModel model) {
                holder.BookName.setText(model.getBookName());
                holder.BookCategory.setText(model.getBookCategory());
                holder.BookPrice.setText(model.getBookPrice());
                Picasso.get().load(model.getImageUrl()).into(holder.imageList);


                //click listner

                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, ViewBookActivity.class);
                        intent.putExtra("BookKey", getRef(position).getKey());
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view, parent, false);
                return new MyViewHolder(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }


    //navigation
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
                break;

            case R.id.nav_addBook:
                Intent intent2 = new Intent(MainActivity.this, AddBookActivity.class);
                startActivity(intent2);
                break;

            case R.id.nav_mybook:
                Intent intent5 = new Intent(MainActivity.this, MyBookActivity.class);
                startActivity(intent5);
                break;

           case R.id.nav_message:
                Intent intent6 = new Intent(MainActivity.this, MessageActivity.class);
                startActivity(intent6);
                break;


            case R.id.nav_profile:
                Intent intent3 = new Intent(MainActivity.this, UserProfileActivity.class);
                startActivity(intent3);
                break;

            case R.id.nav_reset:
                Intent intent8 = new Intent(MainActivity.this, ResetPasswordActivity.class);
                startActivity(intent8);
                break;

            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;


            case R.id.nav_share:
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String sub = "Your Subject";
                myIntent.putExtra(Intent.EXTRA_SUBJECT, sub);
                startActivity(Intent.createChooser(myIntent, "Share Using"));
                Toast.makeText(this, "Shared Successfully!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_policy:
                Intent intent7 = new Intent(MainActivity.this, PolicyActivity.class);
                startActivity(intent7);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();

        options = new FirebaseRecyclerOptions.Builder<bookModel>().setQuery(Dataref, bookModel.class).build();
        adapter = new FirebaseRecyclerAdapter<bookModel, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull bookModel model) {
                holder.BookName.setText(model.getBookName());
                holder.BookCategory.setText(model.getBookCategory());
                holder.BookPrice.setText(model.getBookPrice());
                Picasso.get().load(model.getImageUrl()).into(holder.imageList);

                //click listner

                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, ViewBookActivity.class);
                        intent.putExtra("BookKey", getRef(position).getKey());
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view, parent, false);
                return new MyViewHolder(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}
