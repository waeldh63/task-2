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

public class login extends AppCompatActivity {
    private Toolbar mToolbar;
    private Button login;
    private TextInputLayout  password, email;


    private FirebaseAuth mAuth;

    private ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        password=   findViewById(R.id.lpassword);
        email=   findViewById(R.id.lemail);
        login=   findViewById(R.id.login_acc);

progress= new ProgressDialog(this);



mAuth= FirebaseAuth.getInstance();


        mToolbar = findViewById(R.id.register_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Log in");


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String memail = email.getEditText().getText().toString();
                String mpass = password.getEditText().getText().toString();
                if(!TextUtils.isEmpty(memail)|| !TextUtils.isEmpty(mpass)){

progress.setTitle("Logging in");
progress.setMessage("please wait ");
progress.setCanceledOnTouchOutside(false);
progress.show();

                    loginuser(memail,mpass);
                }
            }
        });



    }

    private void loginuser(String memail, String mpass) {
mAuth.signInWithEmailAndPassword(memail,mpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if(task.isSuccessful()){



            progress.dismiss();
            Intent intent = new Intent(login.this, MainActivity.class);
            startActivity(intent);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
finish();

        }else{



            progress.hide();
            Toast.makeText(login.this,"you got some error",  Toast.LENGTH_LONG).show();
        }
    }
});



    }
}