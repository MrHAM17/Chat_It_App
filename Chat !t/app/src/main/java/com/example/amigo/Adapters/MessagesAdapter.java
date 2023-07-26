package com.example.amigo.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.amigo.Models.Message;
import com.example.amigo.R;
import com.example.amigo.databinding.DeleteDialogBinding;
import com.example.amigo.databinding.ItemReceiveBinding;
import com.example.amigo.databinding.ItemSentBinding;
import com.github.pgreze.reactions.ReactionPopup;
import com.github.pgreze.reactions.ReactionsConfig;
import com.github.pgreze.reactions.ReactionsConfigBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Message> messages;

    final int ITEM_SENT = 1;
    final int ITEM_RECEIVE = 2;

/// Below rooms are taken/defined, because we need to do changes/updates (in feelings) of messages of sender & receiver.
    String senderRoom;
    String receiverRoom;
///.
    FirebaseRemoteConfig remoteConfig;

//  Simpe constructor is created.
    public MessagesAdapter(Context context, ArrayList<Message> messages, String senderRoom, String receiverRoom) {
        remoteConfig = FirebaseRemoteConfig.getInstance();
        this.context = context;
        this.messages = messages;
        this.senderRoom = senderRoom;
        this.receiverRoom = receiverRoom;
    }
//.


/// Display the views of chatts according to the message/position/confirmation from  getItemViewType.
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ITEM_SENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_sent, parent, false);
            return new SentViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_receive, parent, false);
            return new ReceiverViewHolder(view);
        }
    }
///.

// getItemViewType:: Returns the view type of item at position for the purpose of view recycling.  //  Intializing getItemViewType method ; because we've not extended any viewholder...
    @Override
    public int getItemViewType(int position) {                                       // Decides the message is send or received.
        Message message = messages.get(position);                                     // Object of Message  // specifies the message is sent or receive for to set, to pass,..
        if(FirebaseAuth.getInstance().getUid().equals(message.getSenderId())) {        // To Check: unique id of logged in user is get match with sender id; them message is got send.
            return ITEM_SENT;
        } else {
            return ITEM_RECEIVE;
        }
    }
//.

//***//  ( DOES THE PROCESSES FOR MESSAGE "..SEND.."  or "..RECEIVE.." , BY CONFIRMATION FROM ABOVE ACTIVITIES + LAST 2 ACTIVITIES ) Selects the correct data to be set in the view, for passing it to the onCreateViewHolder.
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);      // Object of Message  // specifies the message is sent or receive for to set, to pass,..; By getItemViewType.

   ///  Implementation of reactions' library.
        int reactions[] = new int[]{
                R.drawable.ic_fb_like,
                R.drawable.ic_fb_love,
                R.drawable.ic_fb_laugh,
                R.drawable.ic_fb_wow,
                R.drawable.ic_fb_sad,
                R.drawable.ic_fb_angry
        };

        ReactionsConfig config = new ReactionsConfigBuilder(context)
                .withReactions(reactions)
                .build();

        ReactionPopup popup = new ReactionPopup(context, config, (pos) -> {

            if(pos < 0)
                return false;
        // *** process of Implementation of reactions' library is overed at above line. But for some x processes function is closed after those...  ***


       //   Showing the selected feeling by user: Process 2: for "send message" or "received message" after confirmed by object of Message i.e, message respectively.    // Process 1: at 162 (for send)  // Process 1: at 268 (for receive)  // Process 3: at 120 to 134
            if(holder.getClass() == SentViewHolder.class) {
                SentViewHolder viewHolder = (SentViewHolder)holder;
                viewHolder.binding.feeling.setImageResource(reactions[pos]);
                viewHolder.binding.feeling.setVisibility(View.VISIBLE);  // Selected feeling is get visible on respective message. If this line isn't written then feeling will applied but applier could not see it.
            } else {
                ReceiverViewHolder viewHolder = (ReceiverViewHolder)holder;
                viewHolder.binding.feeling.setImageResource(reactions[pos]);
                viewHolder.binding.feeling.setVisibility(View.VISIBLE);  // Selected feeling is get visible on respective message. If this line isn't written then feeling will applied but applier could not see it.
            }
       //.  BUT THE PROCESS NOT COMPLETE YET; DUE TO THIS ONLY APPLIER CANSEE THE FEELING. // TO OVERCOME THIS, SOLUTION CODE IS BELOW.

            message.setFeeling(pos);                  // To set the choosed feeling.

       ////  FEELINGS: PROCESS 3: SOLUTION CODE: Storing the selected feeling in database & making available it , So other user can also see it.  // Process 2: at 108.  // Process 4: at 156 (For Sender)  // Process 4: at 264 (For Receiver.)
            FirebaseDatabase.getInstance().getReference()
                    .child("chats")
                    .child(senderRoom)
                    .child("messages")
                    .child(message.getMessageId()).setValue(message);

            FirebaseDatabase.getInstance().getReference()
                    .child("chats")
                    .child(receiverRoom)
                    .child("messages")
                    .child(message.getMessageId()).setValue(message);
       ////.

            return true; // true is closing popup, false is requesting a new selection
        });
   ///.


   //**//   DOES THE PROCESSES FOR MESSAGE "..BEFORE SEND..", "..AFTER SEND..."
        if(holder.getClass() == SentViewHolder.class) {
            SentViewHolder viewHolder = (SentViewHolder)holder;

       //   Attachment_Message_Send:  Process 2.3: Displaying "send_imaged"_message in sender's room.    // Process 2.2: at Chat Activity.java File at 332    // Process 2.4: at 257.
            if(message.getMessage().equals("photo")) {
                viewHolder.binding.image.setVisibility(View.VISIBLE);
                viewHolder.binding.message.setVisibility(View.GONE);
                Glide.with(context)
                        .load(message.getImageUrl())
                        .placeholder(R.drawable.placeholder)                  // To make feel to the user that, image is sending by showing the empty white card till the actual image is taking place in there in chatts ( i.e, set by developers )
                        .into(viewHolder.binding.image);
            }
       //.

            viewHolder.binding.message.setText(message.getMessage());

       ///  FEELINGS: Process 4: Making feelings visible after the message is reacted (for Sender).  // Process 3: at 122
            if(message.getFeeling() >= 0) {
                viewHolder.binding.feeling.setImageResource(reactions[message.getFeeling()]);   // It will show the original feeling to viewer, which send by the sender.
                viewHolder.binding.feeling.setVisibility(View.VISIBLE);
            } else {
                viewHolder.binding.feeling.setVisibility(View.GONE);
            }
       ///.


       //   Showing the available feelings to select: Process 1.1: for "send Text Message" after confirmed by object of Message i.e, message respectively.  // Process 2: at 107    // Process 1.2: For rections to images at 183.
            viewHolder.binding.message.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    boolean isFeelingsEnabled = remoteConfig.getBoolean("isFeelingsEnabled");     // Trying to handle feelings feature from backend.
                    if(isFeelingsEnabled)
                        popup.onTouch(v, event);
                    else
                        Toast.makeText(context, "This feature is disabled temporarily.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
       //.

       ///  Showing the available feelings to select: Process 1.2: for "send Imaged Message" after confirmed by object of Message i.e, message respectively.  // Next Processes: Common processes are there for text & image rections.
            viewHolder.binding.image.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popup.onTouch(v, event);
                    return false;
                }
            });
       ///.

       //   Delete Message: All processes & on effect w.r.t. sender.
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    View view = LayoutInflater.from(context).inflate(R.layout.delete_dialog, null);
                    DeleteDialogBinding binding = DeleteDialogBinding.bind(view);
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setTitle("Delete Message")
                            .setView(binding.getRoot())
                            .create();

               ///  Handling "Delete from everyone" feature from backend as well.
                    if(remoteConfig. getBoolean("isEveryoneDeletionEnabled")) {
                        binding.everyone.setVisibility(View.VISIBLE);
                    } else {
                        binding.everyone.setVisibility(View.GONE);
                    }
               ///.
                    
                    binding.everyone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            message.setMessage("This message is removed.");
                            message.setFeeling(-1);
                            FirebaseDatabase.getInstance().getReference()
                                    .child("chats")
                                    .child(senderRoom)
                                    .child("messages")
                                    .child(message.getMessageId()).setValue(message);

                            FirebaseDatabase.getInstance().getReference()
                                    .child("chats")
                                    .child(receiverRoom)
                                    .child("messages")
                                    .child(message.getMessageId()).setValue(message);
                            dialog.dismiss();
                        }
                    });

                    binding.delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("chats")
                                    .child(senderRoom)
                                    .child("messages")
                                    .child(message.getMessageId()).setValue(null);
                            dialog.dismiss();
                        }
                    });

                    binding.cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                    return false;
                }
            });
       //.

        }
   //**//

   //*//     DOES THE PROCESSES FOR MESSAGE "..BEFORE RECEIVED..", "..AFTER RECEIVED..."
        else {
            ReceiverViewHolder viewHolder = (ReceiverViewHolder)holder;

       //   Attachment_Message_Send:  Process 2.4: Displaying "send_imaged"_message in receiver's room.    // Process 2.3: at 145
            if(message.getMessage().equals("photo")) {
                viewHolder.binding.image.setVisibility(View.VISIBLE);
                viewHolder.binding.message.setVisibility(View.GONE);
                Glide.with(context)
                        .load(message.getImageUrl())
                        .placeholder(R.drawable.placeholder)              // To make feel to the user that, image is receiving by showing the empty white card till the actual image is taking place in there in chatts ( i.e, set by developers )
                        .into(viewHolder.binding.image);
            }
       //.
            viewHolder.binding.message.setText(message.getMessage());

       ///  FEELINGS: Process 4: Making feelings visible after the message is reacted (for Receiver).      // Process 3: at 122
            if(message.getFeeling() >= 0) {
                //message.setFeeling(reactions[message.getFeeling()]);
                viewHolder.binding.feeling.setImageResource(reactions[message.getFeeling()]);         // It will show the original feeling to viewer, which send by the sender.
                viewHolder.binding.feeling.setVisibility(View.VISIBLE);
            } else {
                viewHolder.binding.feeling.setVisibility(View.GONE);
            }
       ///.

       //   Showing the available feelings to select: Process 1.1: for "received Text Message" after confirmed by object of Message i.e, message respectively.  // Process 2: at 107   // Process 1.2: For rections to images at 291.
            viewHolder.binding.message.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popup.onTouch(v, event);
                    return false;
                }
            });
       //.

       ///  Showing the available feelings to select: Process 1.2: for "received Imaged Message" after confirmed by object of Message i.e, message respectively.      // Next Processes: Common processes are there for text & image rections.
            viewHolder.binding.image.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    popup.onTouch(v, event);
                    return false;
                }
            });
       ///.

       //   Delete Message: All processes & on effect w.r.t. receiver.
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    View view = LayoutInflater.from(context).inflate(R.layout.delete_dialog, null);
                    DeleteDialogBinding binding = DeleteDialogBinding.bind(view);
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setTitle("Delete Message")
                            .setView(binding.getRoot())
                            .create();

                    binding.everyone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            message.setMessage("This message is removed.");
                            message.setFeeling(-1);
                            FirebaseDatabase.getInstance().getReference()
                                    .child("chats")
                                    .child(senderRoom)
                                    .child("messages")
                                    .child(message.getMessageId()).setValue(message);

                            FirebaseDatabase.getInstance().getReference()
                                    .child("chats")
                                    .child(receiverRoom)
                                    .child("messages")
                                    .child(message.getMessageId()).setValue(message);
                            dialog.dismiss();
                        }
                    });

                    binding.delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseDatabase.getInstance().getReference()
                                    .child("chats")
                                    .child(senderRoom)
                                    .child("messages")
                                    .child(message.getMessageId()).setValue(null);
                            dialog.dismiss();
                        }
                    });

                    binding.cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                    return false;
                }
            });
       //.

        }
   //*//
    }
//***//.

    @Override
    public int getItemCount() {
        return messages.size();
    }

    // Viewholder for sent message.
    public class SentViewHolder extends RecyclerView.ViewHolder {

        ItemSentBinding binding;
        public SentViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemSentBinding.bind(itemView);
        }
    }

    // Viewholder for receive message.
    public class ReceiverViewHolder extends RecyclerView.ViewHolder {

        ItemReceiveBinding binding;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemReceiveBinding.bind(itemView);
        }
    }

}