package com.example.ecocarbontracker;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends AppCompatActivity {

    private TextView name_txt;
    private TextView email_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        name_txt = findViewById(R.id.name_txt);
        email_txt = findViewById(R.id.email_txt);


    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {

                name_txt.setText("Name: " + user.getDisplayName());
                email_txt.setText("Email: " + user.getEmail());

                Toast.makeText(this, "Profile Page", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }
    }
}