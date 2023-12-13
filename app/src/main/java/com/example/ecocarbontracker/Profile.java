package com.example.ecocarbontracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Profile extends AppCompatActivity {

    private TextView name_txt;
    private TextView email_txt;

    Button menu_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name_txt = findViewById(R.id.name_txt);
        email_txt = findViewById(R.id.email_txt);


        SharedPreferences preferences = getSharedPreferences("user_info", MODE_PRIVATE);
        String userEmail = preferences.getString("user_email", "");
        String userName = preferences.getString("user_name", "");

        // Set user information to TextViews
        name_txt.setText("Name: " + userName);
        email_txt.setText("Email: " + userEmail);

        menu_btn = findViewById(R.id.menu_btn);

        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Use FLAG_ACTIVITY_CLEAR_TOP and FLAG_ACTIVITY_SINGLE_TOP to go back without restarting the sign-in
                Intent intent = new Intent(Profile.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
    }
}





