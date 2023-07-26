package com.example.amigo.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.amigo.Activities.ChatActivity;
import com.example.amigo.R;
import com.example.amigo.Models.User;
import com.example.amigo.databinding.RowConversationBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {

    Context context;
    ArrayList<User> users;

// Constructor created
    public UsersAdapter(Context context, ArrayList<User> users) {
        this.context = context;
        this.users = users;
    }
//.

    @NonNull

/// Binding is overed.
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_conversation, parent, false);

        return new UsersViewHolder(view);
    }
///.

//  Process for Binding data.
    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        User user = users.get(position);

   ////   Process to show the Last Message(if any) & Last Message Time.
        String senderId = FirebaseAuth.getInstance().getUid();
        String senderRoom = senderId + user.getUid();
        FirebaseDatabase.getInstance().getReference()
                .child("chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {                                                       // Checking is there any Last Message or not.
                            String lastMsg = snapshot.child("lastMsg").getValue(String.class);
                            long time = snapshot.child("lastMsgTime").getValue(Long.class);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                            holder.binding.msgTime.setText(dateFormat.format(new Date(time)));
                            holder.binding.lastMsg.setText(lastMsg);
                        } else {                                                                     // If there is not a single message chatted.
                            holder.binding.lastMsg.setText("Tap to chat");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
   ////.

   ///  Process to display name, dp image, chat messages in view at resoective correct places.
        holder.binding.username.setText(user.getName());

        // Using Glide Library for image processing i.e, striing to int...
        Glide.with(context).load(user.getProfileImage())
                .placeholder(R.drawable.avatar)  // Default image is setted for those who have not set their image on dp.
                .into(holder.binding.profile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                    // Passing user's info to chat activity.
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("name", user.getName());
                intent.putExtra("image", user.getProfileImage());       // Passing user's selected image for DP, for to show it in toolbar. So the contacted_user can see the user's dp even the user's profile is opened in contacted_user's phone.
                intent.putExtra("uid", user.getUid());
                intent.putExtra("token", user.getToken());          // Will get the token of clicked contanct i.e, receiver for to send notification.
                context.startActivity(intent);
            }
   ///.
        });
    }
//.

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder {

        RowConversationBinding binding;

///   Matching constructor is created.
        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowConversationBinding.bind(itemView);  //All id's of row conversation_xml file will bi accessible to this adapter.
        }
///.
    }

}