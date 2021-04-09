package com.example.chatee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class change_status extends AppCompatActivity {
private Toolbar toolbar;
private Button button;
private TextInputLayout inputLayout;


private DatabaseReference databaseReference;
private FirebaseUser firebaseUser;


private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_status);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);


String status_value = getIntent().getStringExtra("status_value");

        progressDialog= new ProgressDialog(this);

        toolbar = findViewById(R.id.status_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Account Status");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inputLayout = findViewById(R.id.your_status);
        inputLayout.getEditText().setText(status_value);
        button = findViewById(R.id.change);

    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progressDialog= new ProgressDialog(change_status.this);
            progressDialog.setTitle("Changing status");
            progressDialog.setMessage("please wait");
            progressDialog.show();



            String status = inputLayout.getEditText().getText().toString();
            databaseReference.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){
                        progressDialog.dismiss();


                    }else{


Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_LONG).show();
                    }




                }
            });
        }
    });


    }}