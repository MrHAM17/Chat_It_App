package com.example.amigo.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.amigo.Adapters.MessagesAdapter;
import com.example.amigo.Models.Message;
import com.example.amigo.R;
import com.example.amigo.databinding.ActivityChatBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;

    MessagesAdapter adapter;
    ArrayList<Message> messages;

    String senderRoom, receiverRoom;

    FirebaseDatabase database;
    FirebaseStorage storage;

    ProgressDialog dialog;
    String senderUid;
    String receiverUid;
    String token;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
   ///  Show online_offline: Process 1: To hide previous toolbar (having no feature of this) & showing only this toolbar ( With expectation that, old features also should show )   [ NOTE: Till now 2 different toolbars are created. Online feature wala toolbar is visible at old toolbar. And still we get the error i.e, already one tool bar is exist. To solve this follow the next processes. ]     // Process 2: at resources: theme: themes.xml file at 17
        setSupportActionBar(binding.toolbar);
   ///.

        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

   //   Loading process is showing while Attachment_Message is sending.
        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading image...");
        dialog.setCancelable(false);                 // For to show loading continously even the user clicked anywhere till the message is sending.
   //.

        messages = new ArrayList<>();

   ///   Getting user's data from user adapter.
        String name = getIntent().getStringExtra("name");
        String profile = getIntent().getStringExtra("image");
        String token = getIntent().getStringExtra("token");
   ///.

        //Toast.makeText(this, token, Toast.LENGTH_SHORT).show();


   //   Filling user's name, dp in toolbar so contacte_user can see it in app from his phone.
        binding.name.setText(name);                           // Adding name in toolbar
        Glide.with(ChatActivity.this).load(profile)     // Using "Glide" for loading images from internet.
                .placeholder(R.drawable.avatar)                // Sample image will visible till the loading.
                .into(binding.profile);

       ///    Process for to go back while clicked on arrow.
              binding.imageView2.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                finish();
            }
              });
       ///.

   //.

        receiverUid = getIntent().getStringExtra("uid");
        senderUid = FirebaseAuth.getInstance().getUid();         // Gives current logged in user.

   ///  Show online_offline: Process 7: Checking "Is the receiver online or offline ?". And according to that setting the key. ( NOTE: Till this process, online/offline works completely ; But if user clicks home button then that case is solved in process 8. )           //  Process 6: at 407.    //  Process 8: at 415.
        database.getReference().child("presence").child(receiverUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String status = snapshot.getValue(String.class);
                    if(!status.isEmpty()) {
                        if(status.equals("Offline")) {                 // Checking, is the user minimized or closed the app.
                            binding.status.setVisibility(View.GONE);    // If user is offline then showing nothing i.e, offline or last seen.
                        } else {
                            binding.status.setText(status);
                            binding.status.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
   ///.

   //   Unique rooms are created to send & receive the messages.
        senderRoom = senderUid + receiverUid;
        receiverRoom = receiverUid + senderUid;
   //.

        //  Adapter attached. so messages can be successfully shown in profiles...
        adapter = new MessagesAdapter(this, messages, senderRoom, receiverRoom);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

   ///   Load the all stored messages from database to  respective sender's & receiver's profile view.
        database.getReference().child("chats")
                .child(senderRoom)
                .child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Message message = snapshot1.getValue(Message.class);  // Typecasting is done for messages...
                            message.setMessageId(snapshot1.getKey());           // MessageId (defined in message.java) is setted to used in message adapter for to set the feelings such that updates will store in database (so other user can also see the feelings)
                            messages.add(message);
                        }

                        adapter.notifyDataSetChanged();         // Signal is given to adapter i.e, DAtaset of messages is changed.Show the updated dataset.
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
   ///.

   //   Process after tapping send button & storing the messages in database.
        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageTxt = binding.messageBox.getText().toString();         // Taking typed message from type box.

                Date date = new Date();                                                // It will give the current time, date 7 all...
                Message message = new Message(messageTxt, senderUid, date.getTime());   // Creating object of Message i.e, message.
                binding.messageBox.setText("");                                          // Making message box empty as the message is send.

                String randomKey = database.getReference().push().getKey();               // Generating random & unique id for every message instead of ".Push()"...

           ////  Structre created to store the Last Message & Last Message Time.
                HashMap<String, Object> lastMsgObj = new HashMap<>();
                lastMsgObj.put("lastMsg", message.getMessage());
                lastMsgObj.put("lastMsgTime", date.getTime());

                database.getReference().child("chats").child(senderRoom).updateChildren(lastMsgObj);
                database.getReference().child("chats").child(receiverRoom).updateChildren(lastMsgObj);
           ////.


           ///  Sending messages in sender room.
                database.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .child(randomKey)                // Using random & unique id for every message instead of ".Push()" & its issues...
                        .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                       //       Sending messages in receiver room.
                                database.getReference().child("chats")
                                        .child(receiverRoom)
                                        .child("messages")
                                        .child(randomKey)                 // Using random & unique id for every message instead of ".Push()" & its issues...
                                        .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                sendNotification(name, message.getMessage(), token);
                                            }
                                        });
                       //.
                            }
                        });
           ///.

            }
        });
   //.

   ///  Attachment_Message_Send:  Process 1: Making available the all images, when clicked on attachment button left to the camera.    //  Process 2: at 312
        binding.attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);   // To get the image.
                intent.setType("image/*");                      // Getting data only of all images type, for ex: jpg,jpeg,png,...  // For to take all type of data: change "image" by "u" or "U"
                startActivityForResult(intent, 25);
            }
        });
   ///.

   ////  Typing_mode: Process 1: Updating the key from online/offline to typing in the "presence" folder, if there.
           final Handler handler = new Handler();
           binding.messageBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                database.getReference().child("presence").child(senderUid).setValue("typing...");   // updating the value of key for the current user who is typing right now.
                handler.removeCallbacksAndMessages(null);          // Checks whether the user stops typing. If yes, then control goes to next line.
                handler.postDelayed(userStoppedTyping,1000);    // Line decides the time to show immediately "online/offline" when user stops the typing.
            }

            Runnable userStoppedTyping = new Runnable() {
                @Override
                public void run() {
                    database.getReference().child("presence").child(senderUid).setValue("Online");
                }
            };

        });
   ////.


   //    Show online_offline: Process 4: Managed the both toolbars in a such way that, the features of both are available on single toolbar. But there is extra gap from left side. So, Removing the extra gap & arrow for going back from the toolbar.   // process 3: at Android Main Fest.xml file (Plz go to note file Amigo_V2 point 1.)     // Process 2: at resources: theme: themes.xml file at 17      //  Process 5: at Main Activity.java file  at 320
         getSupportActionBar().setDisplayShowTitleEnabled(false);
         //  getSupportActionBar().setTitle(name);                    // To display user's contacted person's name
         //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);    // Symbol to go back, visible at side to name with display user's contacted person's name
   //.
    }

///  Sending notiification: Process 1: By this if user is offline then will get the notification. If user is offline then will not get the notification, to solve this there is process 2.      // Process 2: at MyFireabaseService.java File at 24
    void sendNotification(String name, String message, String token) {
        try {
            RequestQueue queue = Volley.newRequestQueue(this);     // Generating a request que to call an API.

            String url = "https://fcm.googleapis.com/fcm/send";       // This is the Volley API of google.

            JSONObject data = new JSONObject();           // Creating JSON object_1 to pass the data in JSON format.
            data.put("title", name);
            data.put("body", message);
            JSONObject notificationData = new JSONObject();      //  Creating JSON object_2 to pass the data in JSON format.
            notificationData.put("notification", data);
            notificationData.put("to",token);

            JsonObjectRequest request = new JsonObjectRequest(url, notificationData
                    , new Response.Listener<JSONObject>() {                           //   If message successfully send, then notification for that.
                @Override
                public void onResponse(JSONObject response) {
                    // Toast.makeText(ChatActivity.this, "success", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {                                        //   If message failed to send, then notification for that.
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ChatActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {                                                                   //  Sending Header (a server's(firebase) key)
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();
                    String key = "Key=AAAAmh4cBA0:APA91bFyMuesJ0awPD_ry3CNDeDoNaOGDo8ZOdUbrQM_9migVA_593E3i4hgDqItRgv3r1z2dIoe_JMVuQNj-z32F37suxkHKeYMlJvhrgXevmqnHvbAa6DzqDA0x2qOd0n5M6RlIdtf";  // This is our private server key generated in firebase.
                    map.put("Content-Type", "application/json");     // Saving Authorisation key provided by the developers when the good unscared APIs are called.
                    map.put("Authorization", key);

                    return map;
                }
            };

            queue.add(request);


        } catch (Exception ex) {

        }


    }
///.

////  Attachment_Message_Send:  Process 2.0: Process after all images get available.                     // Process 1: at 219    // Process 2.1: at 324
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 25) {
            if(data != null) {
                if(data.getData() != null) {
                    Uri selectedImage = data.getData();               // URL of image is storing.
                    Calendar calendar = Calendar.getInstance();
                    StorageReference reference = storage.getReference().child("chats").child(calendar.getTimeInMillis() + "");    // Creating reference of storage for to add the imaged_messages.
                    dialog.show();           // To start show the loading process.

               //   Attachment_Message_Send:  Process 2.1: Uploading image in storage.                 // process 2.0: at 311    // process 2.2: at 331
                    reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            dialog.dismiss();                                 // To off the loading process.
                            if(task.isSuccessful()) {                          // Checking is task succcessfull.

                           ///  Attachment_Message_Send:  Process 2.2: Displaying "send_imaged"_message in sender's & receiver's room. ( NOTE: IMAGED MESSAGE WILL VISIBLE BECAUSE OF MAINLY 2.2 , 2.3 , 2.4)    [ NOTE: This function is almost as same as "170 to 218" sending Message_wala function. Can check that & will get the idea from it's comment.]     // process 2.1: at 325   // Process 2.3: at messages Adapter.Java file at 145
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {            // Downloading URL of stored image.
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String filePath = uri.toString();             // Saving the URL of stored image.

                                        String messageTxt = binding.messageBox.getText().toString();

                                        Date date = new Date();
                                        Message message = new Message(messageTxt, senderUid, date.getTime());
                                        message.setMessage("photo");
                                        message.setImageUrl(filePath);
                                        binding.messageBox.setText("");

                                        String randomKey = database.getReference().push().getKey();

                                        HashMap<String, Object> lastMsgObj = new HashMap<>();
                                        lastMsgObj.put("lastMsg", message.getMessage());
                                        lastMsgObj.put("lastMsgTime", date.getTime());

                                        database.getReference().child("chats").child(senderRoom).updateChildren(lastMsgObj);
                                        database.getReference().child("chats").child(receiverRoom).updateChildren(lastMsgObj);

                                        database.getReference().child("chats")
                                                .child(senderRoom)
                                                .child("messages")
                                                .child(randomKey)
                                                .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        database.getReference().child("chats")
                                                                .child(receiverRoom)
                                                                .child("messages")
                                                                .child(randomKey)
                                                                .setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {

                                                                    }
                                                                });
                                                    }
                                                });

                                        //Toast.makeText(ChatActivity.this, filePath, Toast.LENGTH_SHORT).show();
                                    }
                                });
                           ///.

                            }
                        }
                    });
               //.
                }
            }
        }
    }
////.


// Show online_offline: Process 6: Checking, "Is user opened the anybody's profile (i.e, nothing but user is online & using the app) or not ? If yes, then also key for current user in the presence folder is set to online else also set to online if user is in app.     // Process 5: at Main Activity.java file at 320    //  Process 7: at 123
    @Override
    protected void onResume() {
        super.onResume();
        String currentId = FirebaseAuth.getInstance().getUid();
        database.getReference().child("presence").child(currentId).setValue("Online");
    }
//.

///  show online_offline: Process 8: If user clicks home button, then also showing him/her as an offline.     // Process 7: at123
    @Override
    protected void onPause() {
        super.onPause();
        String currentId = FirebaseAuth.getInstance().getUid();
        database.getReference().child("presence").child(currentId).setValue("Offline");
    }
///.

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }



///  Process for to go back from anyones' profile after clicking on  // getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Symbol to go back, visible at side to name with display user's contacted person's name
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
///.



}

