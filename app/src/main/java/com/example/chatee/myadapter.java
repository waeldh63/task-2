package com.example.chatee;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class myadapter extends FirebaseRecyclerAdapter <user_class,myadapter.myviewholder> {
private  recycleview recycleview ;
public static  String a = null;
   public static ArrayList<String> key = new ArrayList<String>();
    public  static String getValueAtIndex( int index ) {
        return key.get( index );
    }
    public static String getX() {
        return a;
    }
    // method to get the value of y
    public static String getY() {
        return a;
    }
    public myadapter(@NonNull FirebaseRecyclerOptions<user_class> options, recycleview recycleview) {
        super(options);
        this.recycleview=recycleview;
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull user_class model) {

        holder.name.setText(model.getName());
        holder.status.setText(model.getStatus());
        Picasso.get().load(model.getImage()).into(holder.img);
         a = getRef(position).getKey();
key.add(a);

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_layout, parent, false);
        return new myviewholder(view);



    }

    class myviewholder extends RecyclerView.ViewHolder {


        CircleImageView img;
        TextView name;
        TextView status;
        String key;


        public myviewholder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.user_img);
            status = itemView.findViewById(R.id.user_status);
            name = itemView.findViewById(R.id.user_name);
 itemView.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         recycleview.onItemClick(getAdapterPosition());

     }
 });

        }


    }
}