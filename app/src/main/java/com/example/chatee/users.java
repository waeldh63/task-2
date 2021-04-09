package com.example.chatee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.chatee.myadapter.getValueAtIndex;



public class users extends AppCompatActivity implements recycleview{


    private Toolbar mToolbar;
    private RecyclerView list;
    myadapter myadapter;
    private DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);


        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");



        mToolbar = findViewById(R.id.user_toolbar);

        list = findViewById(R.id.users_list);


        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list = findViewById(R.id.users_list);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<user_class>options = new FirebaseRecyclerOptions.Builder<user_class>().setQuery(FirebaseDatabase.getInstance()
        .getReference().child("Users"),user_class.class).build();

       myadapter = new myadapter(options,this );
       list.setAdapter(myadapter);



    }

    @Override
    protected void onStart() {
        super.onStart();
        myadapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        myadapter.stopListening();
    }

    @Override
    public void onItemClick(int position) {
String a;
      a=  getValueAtIndex(position);

        Intent intent = new Intent(users.this,profile_user.class);
        intent.putExtra("user_id",a);
        startActivity(intent);
    }



    /*
            @Override
            protected void onStart() {
                super.onStart();
                FirebaseRecyclerAdapter<user_class,Userview> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<user_class, Userview>(user_class.class,
                        R.layout.user_layout,
                        Userview.class,
                        databaseReference) {
                    @Override
                    protected void onBindViewHolder(@NonNull Userview holder, int position, @NonNull user_class model) {

                    }

                    @NonNull
                    @Override
                    public Userview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        return null;
                    }
                };


            }


        */
    public class  Userview extends RecyclerView.ViewHolder{

         View view;
        public Userview(@NonNull View itemView) {
            super(itemView);
            view=itemView;
        }



    }



}