package com.example.chatee;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.ProgressDialog;
import android.database.ContentObservable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class profile_user extends AppCompatActivity {
private TextView name,status,frd_count;
private Button add,decline_btn;
private ImageView pp;
private ProgressDialog progressDialog;
private String state;
private FirebaseUser mcurrentuser;
private DatabaseReference mUserdatabase;
private  DatabaseReference rootref;
private DatabaseReference mfriendreqdatabase;
private  DatabaseReference mfrienddatabase;
private DatabaseReference mNotificationDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);setContentView(R.layout.activity_profile_user);


    final String user_id = getIntent().getStringExtra("user_id");
    frd_count = findViewById(R.id.profile_count);
        status = findViewById(R.id.profile_status);
        name = findViewById(R.id.profile_name);
        decline_btn = findViewById(R.id.decline_btn);
        pp= findViewById(R.id.pp);

    rootref = FirebaseDatabase.getInstance().getReference();

        decline_btn.setVisibility(View.INVISIBLE);
        decline_btn.setEnabled(false);
mUserdatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
mfriendreqdatabase = FirebaseDatabase.getInstance().getReference().child("friend_req");
mfrienddatabase = FirebaseDatabase.getInstance().getReference().child("friends");
        mcurrentuser = FirebaseAuth.getInstance().getCurrentUser();
mNotificationDatabase = FirebaseDatabase.getInstance().getReference().child("noti");




    add = findViewById(R.id.add);
state = "false";



    progressDialog= new ProgressDialog(this);
    progressDialog.setTitle("Loadind User");
progressDialog.setMessage("Please wait");
progressDialog.setCanceledOnTouchOutside(false);
progressDialog.show();




    mUserdatabase.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {

            String tname = snapshot.child("name").getValue().toString();
            String timage = snapshot.child("image").getValue().toString();
            String tstatus = snapshot.child("status").getValue().toString();
            name.setText(tname);
            status.setText(tstatus);
            Picasso.get().load(timage).into(pp);



            //----------------- rnd list


            mfriendreqdatabase.child(mcurrentuser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.hasChild(user_id)){
String req_type = snapshot.child(user_id).child("req_type").getValue().toString();
if(req_type.equals("received")){

    state = "req_received";
    add.setText("Accept Friend Request");

    decline_btn.setVisibility(View.VISIBLE);
    decline_btn.setEnabled(true);


} else if(req_type.equals("sent")){

state = "req_sent";
add.setText("Cancel Friend Request");
decline_btn.setVisibility(View.INVISIBLE);
    decline_btn.setEnabled(false);

}
                        progressDialog.dismiss();

                    }else{
mfrienddatabase.child(mcurrentuser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(snapshot.hasChild(user_id)){

            state = "true";
            add.setText("Unfriend");

        }
        progressDialog.dismiss(); 
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        progressDialog.dismiss();
    }
});


                    }









                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {




        }
    });


add.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
add.setEnabled(false);


//--------------------------Not Friend
        if(state.equals("false")) {

            DatabaseReference newnoti = rootref.child("noti").child(user_id).push();
            String newnotiid = newnoti.getKey();
            HashMap<String ,String>notifiData = new HashMap<>();
            notifiData.put("from",mcurrentuser.getUid());
            notifiData.put("type","request");

            Map requestMap = new HashMap();
            requestMap.put("friend_req/" + mcurrentuser.getUid()+ "/" +user_id + "request_type" , "sent");

            requestMap.put("friend_req/" + user_id + "/" + mcurrentuser.getUid()    + "request_type", "received");
            requestMap.put("noti/"+ user_id + "/" + newnotiid , notifiData);




            rootref.updateChildren(requestMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete( DatabaseError error,  DatabaseReference ref) {

if( error == null ){

    add.setEnabled(true);
    state = "req_sent";
    add.setText("CANCEL FRIEND REQUEST");


} else {

    Toast.makeText(profile_user.this,"error",Toast.LENGTH_LONG).show();
}


        }


            });

        }


//------------------------Cancel friend


if(state.equals("req_sent")){


    mfriendreqdatabase.child(mcurrentuser.getUid()).child(user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
       mfriendreqdatabase.child(user_id).child(mcurrentuser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
           @Override
           public void onSuccess(Void aVoid) {
               add.setEnabled(true);
               state = "false";
               add.setText("ADD FRIEND");
               decline_btn.setVisibility(View.INVISIBLE);
               decline_btn.setEnabled(false);
           }
       });
        }
    });







}

//------------------REQ RECEIVED STATE
        if(state.equals("req_received")){

final String current_date = DateFormat.getDateTimeInstance().format(new Date());
Map frndMap = new HashMap();
frndMap.put("Friends/" + mcurrentuser.getUid() + "/" + user_id + "/date", current_date);
frndMap.put("Friends/" + user_id + "/" + mcurrentuser.getUid() + "/date", current_date);
frndMap.put("friend_req/" + mcurrentuser.getUid() + "/" + user_id, null);
frndMap.put("friend_req/" + user_id + "/" + mcurrentuser.getUid(), null);
rootref.updateChildren(frndMap, new DatabaseReference.CompletionListener() {
    @Override
    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

        if (error == null){
            add.setEnabled(true);
            state = "friends";
            add.setText("Unfriend This Person");
            decline_btn.setVisibility(View.VISIBLE);
            decline_btn.setEnabled(false);
        }else{

            String merror = error.getMessage();
            Toast.makeText(profile_user.this,merror,Toast.LENGTH_LONG).show();

        }
    }
});




        }


        //------------UNFRIENDS
        if (state.equals("friends")){
Map unfriendMap = new HashMap();
unfriendMap.put("Friends/" + mcurrentuser.getUid() + "/" + user_id, null);
unfriendMap.put("Friends/"+ user_id+ "/"+ mcurrentuser.getUid(),null);


            rootref.updateChildren(unfriendMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                    if (error == null){

                        state = "false";
                        add.setText("ADD FRIEND");
                        decline_btn.setVisibility(View.VISIBLE);
                        decline_btn.setEnabled(false);
                    }else{

                        String merror = error.getMessage();
                        Toast.makeText(profile_user.this,merror,Toast.LENGTH_LONG).show();

                    }

                    add.setEnabled(true);
                }
            });




        }


















    }
});


}}