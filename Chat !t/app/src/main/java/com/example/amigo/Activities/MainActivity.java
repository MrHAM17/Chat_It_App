package com.example.amigo.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.amigo.Adapters.TopStatusAdapter;
import com.example.amigo.Adapters.UsersAdapter;
import com.example.amigo.Models.Status;
import com.example.amigo.Models.User;
import com.example.amigo.Models.UserStatus;
import com.example.amigo.R;
import com.example.amigo.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseDatabase database;
    ArrayList<User> users;
    UsersAdapter usersAdapter;
    TopStatusAdapter statusAdapter;
    ArrayList<UserStatus> userStatuses;
    ProgressDialog dialog;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

   //   Process to control our app from backend like google home page.
        FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(0)         // Checking for updates continously ( after zero seconds)
                .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
   //.

   ////   Function for to fetch the changes i.e, control from backend.
        mFirebaseRemoteConfig.fetchAndActivate().addOnSuccessListener(new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                // Fetching the image from database via internet.
                String backgroundImage = mFirebaseRemoteConfig.getString("backgroundImage");
                Glide.with(MainActivity.this)
                        .load(backgroundImage)
                        .into(binding.backgroundImage);

                /* Toolbar Color */
                String toolbarColor = mFirebaseRemoteConfig.getString("toolbarColor");
                getSupportActionBar()
                        .setBackgroundDrawable(new ColorDrawable(Color.parseColor(toolbarColor)));
                String toolBarImage = mFirebaseRemoteConfig.getString("toolbarImage");
                boolean isToolBarImageEnabled = mFirebaseRemoteConfig.getBoolean("toolBarImageEnabled");


           ///   Updating toolbar image after Processed in firebase.
                if(isToolBarImageEnabled) {
                    Glide.with(MainActivity.this)
                            .load(toolBarImage)
                            .into(new CustomTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull @NotNull Drawable resource, @Nullable @org.jetbrains.annotations.Nullable Transition<? super Drawable> transition) {
                                    getSupportActionBar()
                                            .setBackgroundDrawable(resource);
                                    Toast.makeText(MainActivity.this, "Welcome !", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onLoadCleared(@Nullable @org.jetbrains.annotations.Nullable Drawable placeholder) {
                                }
                            });
                } else {
                           Toast.makeText(MainActivity.this, "Welcome !", Toast.LENGTH_SHORT).show();
                       }
           ///.

            }
        });
   ////.

        database = FirebaseDatabase.getInstance();

   ///  Process for getting unique token for every new user who created the account in the app.This token is used for to send the notification.
        FirebaseMessaging.getInstance()
                .getToken()
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String token) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("token", token);
                        database.getReference()
                                .child("users")
                                .child(FirebaseAuth.getInstance().getUid())
                                .updateChildren(map);
                        //Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });
   ///.

       // Creating loading when the image is uploading while adding the status.
        dialog = new ProgressDialog(this);
        dialog.setMessage("Uploading Image...");
        dialog.setCancelable(false);


        users = new ArrayList<>();
        userStatuses = new ArrayList<>();

   //  Status: Process 2.2: Users aa gaye.                                                              // process 2.1: at 250.  // process 2.3: at 269.
        database.getReference().child("users").child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user = snapshot.getValue(User.class);                         // users aa gaye.
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
   //.

        usersAdapter = new UsersAdapter(this, users);                  // User adapter initialized.
        statusAdapter = new TopStatusAdapter(this, userStatuses);
        //  binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));  // To show users' list on screen of user.  //Line is commented because the same function is done by the line code/typed in main_xml file. // Either code there & comment here or leave comment here & code there.

        // To show the statuses horizontally...
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.statusList.setLayoutManager(layoutManager);
        binding.statusList.setAdapter(statusAdapter);

        binding.recyclerView.setAdapter(usersAdapter);

        binding.recyclerView.showShimmerAdapter();       // Show cool loading process.
        binding.statusList.showShimmerAdapter();          // Show cool loading process.

   //   Process to get the user's data from database on user's screen.
        database.getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                    User user = snapshot1.getValue(User.class);
                   // This "if" for: To add/load users profile in app except khudki profile.
                    if(!user.getUid().equals(FirebaseAuth.getInstance().getUid()))
                        users.add(user);
                }
                binding.recyclerView.hideShimmerAdapter();     // Closing cool loading process.
                usersAdapter.notifyDataSetChanged();        // Notified adapter about his data set is updated.
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
   //.

   ////  Status: Process 2.8 : "Fetching Not start showing" the saved stories (uploaded by the user) from database into the app.          // process 2.7: at 297.     // process 2.9: at TopStatusAdapter.java  at 42.
        database.getReference().child("stories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    userStatuses.clear();
                    for(DataSnapshot storySnapshot : snapshot.getChildren()) {
                        UserStatus status = new UserStatus();
                        status.setName(storySnapshot.child("name").getValue(String.class));
                        status.setProfileImage(storySnapshot.child("profileImage").getValue(String.class));
                        status.setLastUpdated(storySnapshot.child("lastUpdated").getValue(Long.class));

                        ArrayList<Status> statuses = new ArrayList<>();

                        for(DataSnapshot statusSnapshot : storySnapshot.child("statuses").getChildren()) {
                            Status sampleStatus = statusSnapshot.getValue(Status.class);
                            statuses.add(sampleStatus);
                        }

                        status.setStatuses(statuses);
                        userStatuses.add(status);           // Adding the statuses into arrayList of stories.
                    }
                    binding.statusList.hideShimmerAdapter();    // Closing cool loading process.
                    statusAdapter.notifyDataSetChanged();      // Notifying the Adapter.
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
   ////.


   ///  Status: Process 1:  Just creating seprate function & calling it.                              // process 2.1: at 249.
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.status:
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, 75);
                        break;
                }
                return false;
            }
        });
   ///.

    }

//  Status: Process 2.1: Complete Process.                                                              // process 1: at 231.  // process 2.2: at 147.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null) {                                         // Checking  any data is selected or not.
            if(data.getData() != null) {                            // Checking selected data is image or not.
                dialog.show();                                       // Showing loading effect when the image is uploaded for status.
                FirebaseStorage storage = FirebaseStorage.getInstance();                                                           // To upload in storage.
                Date date = new Date();
                StorageReference reference = storage.getReference().child("status").child(date.getTime() + "");    // Generating unique reference in database.

           ///  Process for to upload the image in database.
                reference.putFile(data.getData()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                   //   If image is successfully upload, then doing following tasks.
                        if(task.isSuccessful()) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {                    // Downloading URL of uploaded image (storage) to store it in database.
                                @Override
                           ///  Status: Process 2.3:                                                        // process 2.2: at 147.   // process 2.4: at 284.
                                public void onSuccess(Uri uri) {
                                    UserStatus userStatus = new UserStatus();
                                    userStatus.setName(user.getName());
                                    userStatus.setProfileImage(user.getProfileImage());
                                    userStatus.setLastUpdated(date.getTime());

                               //   Status: Process 2.5:  User status wala data update.              // process 2.4: at 286.    // process 2.6: at 283.
                                    HashMap<String, Object> obj = new HashMap<>();
                                    obj.put("name", userStatus.getName());
                                    obj.put("profileImage", userStatus.getProfileImage());
                                    obj.put("lastUpdated", userStatus.getLastUpdated());
                               //.

                               ///   Status: Process 2.6:  User status updated.              // process 2.5: at 276.    // process 2.7: at 295.
                                    String imageUrl = uri.toString();
                                    Status status = new Status(imageUrl, userStatus.getLastUpdated());
                               ///.

                               //   Status: Process 2.4: Update the database                               // process 2.3: at 269.   // process 2.5: at 276.
                                    database.getReference()
                                            .child("stories")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .updateChildren(obj);
                               //.

                               ////  Status: Process 2.7  User status setted. (NOTE : Till this process, User can set his status for stories. All stories get stored in database ; But any user can not see anyones' stories. Even that band for stories is also not visible. There is only messages are showing. )  // process 2.6: at 283.  // process 2.8: at 198.
                                    database.getReference().child("stories")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .child("statuses")
                                            .push()
                                            .setValue(status);
                               ////.

                                    dialog.dismiss();                                                   // Doing off loading effect when the image is uploaded for status.
                                }
                           ///.
                            });
                        }
                   //.
                    }
                });
           ///.
            }
        }
    }
//.

///  Show online_offline: Process 5: Checking, "The user is on the Chats Page(i.e, nothing but user is online & using the app) or not ? If yes, then key for current user in the presence folder is set to online else set to offline ( NOTE: Status of online/offline is updated in database only.To make it visible for eachother, there are the next steps )."       //   Process 4: at Chat Activity.java file at 269.   //  Process 6: at Chat Activity.java file at 407.
    @Override
    protected void onResume() {
        super.onResume();
        String currentId = FirebaseAuth.getInstance().getUid();
        database.getReference().child("presence").child(currentId).setValue("Online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        String currentId = FirebaseAuth.getInstance().getUid();
        database.getReference().child("presence").child(currentId).setValue("Offline");
    }
///.

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.group:
                startActivity(new Intent(MainActivity.this, GroupChatActivity.class));
                break;
            case R.id.search:
                Toast.makeText(this, "Search...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this, "ViewingSettings", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.topmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}