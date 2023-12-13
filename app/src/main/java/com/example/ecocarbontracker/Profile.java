package com.example.ecocarbontracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Profile extends AppCompatActivity {

    private TextView name_txt;
    private TextView email_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name_txt = findViewById(R.id.name_txt);
        email_txt = findViewById(R.id.email_txt);

        // Retrieve user information from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("user_info", MODE_PRIVATE);
        String userEmail = preferences.getString("user_email", "");
        String userName = preferences.getString("user_name", "");

        // Set user information to TextViews
        name_txt.setText("Name: " + userName);
        email_txt.setText("Email: " + userEmail);
    }



}
