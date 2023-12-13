package com.example.ecocarbontracker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    Button SignOut;
    Button Api;
    Button profileButton;
    Button linksButton;







    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Button quizButton = findViewById(R.id.quiz_btn);
        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                startActivity(intent);
            }
        });
        mAuth = FirebaseAuth.getInstance();
        createSignInIntent();

        SignOut = (Button) findViewById(R.id.btnSignOut);
        SignOut.setBackgroundColor(Color.GREEN);
        SignOut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        Button openCarbonFootprintButton = findViewById(R.id.btnCarbonFootprint);
        openCarbonFootprintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCarbonFootprintActivity();
            }
        });





        Button maps_btn = (Button) findViewById(R.id.btnOpenMaps);
        maps_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, com.example.ecocarbontracker.MapsActivity.class);
                startActivity(i);
            }
        });

        Button carbonFootprintBtn = (Button) findViewById(R.id.btnCarbonFootprint);
        carbonFootprintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToCarbonFootprintActivity(view);
            }
        });

        profileButton = findViewById(R.id.btnProfile);
        linksButton = findViewById(R.id.btnLinks);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openProfileActivity();
            }
        });

        linksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLinksActivity();
            }
        });

    }

    private void openLinksActivity() {
        Intent intent = new Intent(this, LinksActivity.class);
        startActivity(intent);
    }


    private void openProfileActivity() {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }


    private void openCarbonFootprintActivity() {
        Intent intent = new Intent(this, CarbonFootprint.class);
        startActivity(intent);
    }


    public void redirectToCarbonFootprintActivity(View view) {
        Intent intent = new Intent(this, CarbonFootprint.class);
        startActivity(intent);
    }

    public void createSignInIntent() {

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.AnonymousBuilder().build());

        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.drawable.logo_1)
                .build();
        signInLauncher.launch(signInIntent);
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {



                Toast.makeText(this, "Sign in for (" + user.getEmail() + ") was successful", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Failed Login", Toast.LENGTH_SHORT).show();
        }
    }

    private void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>(){
                    public void onComplete(@NonNull Task<Void> task) {createSignInIntent(); }
                });
    }
}
