package com.example.amigo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.amigo.databinding.ActivityPhoneNumberBinding;
import com.google.firebase.auth.FirebaseAuth;


public class PhoneNumberActivity extends AppCompatActivity {

    ActivityPhoneNumberBinding binding;
    FirebaseAuth auth;


    @Override
   // Binding is done
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
   //.

   //// Process for to avoid signing up activity even the user is already sign up.
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null) {     // checking the user is new one or already signed up wala user.
            Intent intent = new Intent(PhoneNumberActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
   ////.

        getSupportActionBar().hide(); // To hide the app name on the screen

        binding.phoneBox.requestFocus();     // Used to display keyboard compulsory

   ///  Going to otp activity
        binding.continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhoneNumberActivity.this, OTPActivity.class);
                intent.putExtra("phoneNumber", binding.phoneBox.getText().toString());   // Sending data(phone number) to otp activity
                startActivity(intent);
            }
        });
   ///

    }
}