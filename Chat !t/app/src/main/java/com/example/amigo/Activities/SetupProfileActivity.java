package com.example.amigo.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.amigo.Models.User;
import com.example.amigo.databinding.ActivitySetupProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.HashMap;

public class SetupProfileActivity extends AppCompatActivity {

    ActivitySetupProfileBinding binding;  // Binding process
    FirebaseAuth auth; // Tells us who is logged in,by which phone number,by which user id; to save his data in database
    FirebaseDatabase database;
    FirebaseStorage storage;
    Uri selectedImage;
    ProgressDialog dialog;      // Intialization process for dialog to show loading process

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
   //   Binding process to avoid to deal with findviewby.id process....ig update effect of android stdio.
        binding = ActivitySetupProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
   //.

   ///  Defining process for dialog to show loading process
        dialog = new ProgressDialog(this);
        dialog.setMessage("Updating profile...");
        dialog.setCancelable(false);
   ///.


        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

        getSupportActionBar().hide();          // Hiding action bar

 ////  Process for, to take image for dp while user crating his account
        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           //   Process that allows to choose image from user's device...
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");                         // Setting up the type of data is going to take, i.e. is image
           //.
                startActivityForResult(intent, 45);     // Function defined, to take choosed image for futher process
            }
        });

    /// Function created, to take choosed image for futher process by clicking continue button...
        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.nameBox.getText().toString();

            //  Checking the name is typed or not...
                if(name.isEmpty()) {
                    binding.nameBox.setError("Please type a name");
                    return;
                }
            //.

                dialog.show();  // Start showing loading because user press the continue button.

            /// If image is selected for dp...
                if(selectedImage != null) {
                    StorageReference reference = storage.getReference().child("Profiles").child(auth.getUid());       // Reference Defined: to make the Main Profile folder in database & make seprate file for seprate user...
                //  Reference Defined: to make the Main Profile folder in database & make seprate file for seprate user...
                    reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {    // Image file is putted to process...
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {                                 //  Function  for Task, i.e. to upload the received image...
                            if(task.isSuccessful()) {                                                                        //  Process to store the image, if image is upload i.e. task is done
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {              //  Function Decleared & Defined which gives the URL of place where the image is saved in storage; for to set on user's dp
                                    @Override
                                    public void onSuccess(Uri uri) {                     // 2nd "uri" is the link of user's compile
                                        String imageUrl = uri.toString();

                                        String uid = auth.getUid();
                                        String phone = auth.getCurrentUser().getPhoneNumber();
                                        String name = binding.nameBox.getText().toString();

                                        User user = new User(uid, name, phone, imageUrl);            // Object of user is done

                                       /// Process for to save the object of user & save it in firebase's database. // seprate foder will creaated for every new user in User's folder in database
                                        database.getReference()
                                                .child("users")
                                                .child(uid)
                                                .setValue(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        dialog.dismiss();       // Stop showing loading process because account is created successfully.
                                                        Intent intent = new Intent(SetupProfileActivity.this, MainActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });
                                       ///.
                                    }

                                });
                            }
                        }
                    });
                //.
                }
            ///.

            //  If image is not selected for dp...
                else {
                    String uid = auth.getUid();
                    String phone = auth.getCurrentUser().getPhoneNumber();

                    User user = new User(uid, name, phone, "No Image");

                    database.getReference()
                            .child("users")
                            .child(uid)
                            .setValue(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(SetupProfileActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                }
            //.

            }
        });

    ///.

 ////.
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null) {
            if(data.getData() != null) {
                Uri uri = data.getData(); // filepath
                FirebaseStorage storage = FirebaseStorage.getInstance();
                long time = new Date().getTime();
                StorageReference reference = storage.getReference().child("Profiles").child(time+"");
                reference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String filePath = uri.toString();
                                    HashMap<String, Object> obj = new HashMap<>();
                                    obj.put("image", filePath);
                                    database.getReference().child("users")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .updateChildren(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                }
                                            });
                                }
                            });
                        }
                    }
                });


                binding.imageView.setImageURI(data.getData());
                selectedImage = data.getData();                  //  Refference saved of selected image.
            }
        }
    }
}