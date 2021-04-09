package com.example.chatee;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoProvider;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class settingsActivity extends AppCompatActivity {

private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;




    private CircleImageView circleImageView;
    private TextView mstatus,mname;
    private Button change_button,image_btn;

    private StorageReference storageReference;
private ProgressDialog progressDialog;


    private static final int pick=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mstatus = findViewById(R.id.display_status);
        mname = findViewById(R.id.display_name);
        change_button = findViewById(R.id.change_status);
        circleImageView = findViewById(R.id.display_image);
        image_btn = findViewById(R.id.change_image);
        storageReference= FirebaseStorage.getInstance().getReference();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = firebaseUser.getUid();
    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

image_btn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {






       Intent intent= new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select image "),pick);







    }
});

        change_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String status_value =mstatus.getText().toString();



                Intent intent = new Intent(settingsActivity.this,change_status.class);
                intent.putExtra("status_value",status_value);
                startActivity(intent);
            }
        });

    databaseReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
         String name = snapshot.child("name").getValue().toString();
         String image = snapshot.child("image").getValue().toString();
            String status = snapshot.child("status").getValue().toString();
            String thumb_image = snapshot.child("thumb_image").getValue().toString();



            mname.setText(name);
          mstatus.setText(status);
            Picasso.get().load(image).into(circleImageView);


        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });







    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    if(requestCode==pick && resultCode== RESULT_OK){

       Uri imageUri = data.getData();
        CropImage.activity(imageUri)
                .setAspectRatio(1,1)
                .start(this);




    }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {


                progressDialog = new ProgressDialog(settingsActivity.this);
                progressDialog.setTitle("Uploading Image...");
                progressDialog.setMessage("please wait");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();


                Uri resultUri = result.getUri();
            final    String user_id = firebaseUser.getUid();


                StorageReference filepath = storageReference.child("profile_images").child(user_id + ".jpg");
filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
    @Override
    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {


        if (task.isSuccessful()){
            storageReference.child("profile_images").child(user_id + ".jpg").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
               String url = task.getResult().toString();
                    databaseReference.child("image").setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                       if(task.isSuccessful()){
                           progressDialog.dismiss();

                           Toast.makeText(settingsActivity.this,"done",Toast.LENGTH_LONG).show();


                       }else{


                           Toast.makeText(settingsActivity.this,"error",Toast.LENGTH_LONG).show();

                       }
                        }
                    });
                }
            });

        }else{


            Toast.makeText(settingsActivity.this,"error",Toast.LENGTH_LONG).show();

            progressDialog.dismiss();
        }

    }
});


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }



    }
    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(20);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}