package com.example.chatee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    private TextInputLayout username , password, email;
    private Button create;
    private Toolbar mToolbar;
    private FirebaseAuth mAuth;
    private ProgressDialog mprogress;


    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        username=   findViewById(R.id.username);
        password=   findViewById(R.id.password);
        email=   findViewById(R.id.email);
        create=   findViewById(R.id.create_acc);



        mprogress = new ProgressDialog(this);


//tollbar
        mToolbar = findViewById(R.id.register_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        //firebase

        mAuth = FirebaseAuth.getInstance();



        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String musername = username.getEditText().getText().toString();
                String memail = email.getEditText().getText().toString();
                String mpassword = password.getEditText().getText().toString();
                if(!TextUtils.isEmpty(musername)|| !TextUtils.isEmpty(memail)|| !TextUtils.isEmpty(mpassword)){


                    mprogress.setTitle("Loading User");
                    mprogress.setMessage("please wait");
                    mprogress.setCanceledOnTouchOutside(false);
mprogress.show();

                    register_user(musername,memail,mpassword);





                }








            }
        });
    }

    private void register_user(final String musername, String memail, String mpassword) {
        mAuth.createUserWithEmailAndPassword(memail,mpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){


                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    String uid =firebaseUser.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    HashMap<String,String> userMap = new HashMap<>();
                    userMap.put("name", musername);
                    userMap.put("status", "Hi there,i'm using chatee");
                    userMap.put("image","https://firebasestorage.googleapis.com/v0/b/chatee-c39ed.appspot.com/o/profile_images%2FX5011Qx6cQP7Ip57dKY26ijeSZu2.jpg?alt=media&token=1e72e5c1-de2f-4d31-b51f-fedfad18850c");
                    userMap.put("thumb_image","default");

databaseReference.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
    @Override
    public void onComplete(@NonNull Task<Void> task) {

        if(task.isSuccessful()){

            mprogress.dismiss();
            Intent intent = new Intent(Register.this,MainActivity.class);
            startActivity(intent);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finish();


        }
    }
});





                }else{
                    mprogress.hide();
                    Toast.makeText(Register.this,"you got some error",  Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}