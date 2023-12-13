package com.example.ecocarbontracker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LinksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_links);

        // Find buttons by ID
        Button buttonUnicef = findViewById(R.id.buttonUnicef);
        Button buttonUN = findViewById(R.id.buttonUN);
        Button buttonCambridge = findViewById(R.id.buttonCambridge);
        Button buttonImperial = findViewById(R.id.buttonImperial);

        // Set click listeners for each button
        buttonUnicef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl("https://www.unicef.org/environment-and-climate-change");
            }
        });

        buttonUN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl("https://www.un.org/en/climatechange/net-zero-coalition#:~:text=to%202010%20levels.-,To%20keep%20global%20warming%20to%20no%20more%20than%201.5%C2%B0,reach%20net%20zero%20by%202050.");
            }
        });

        buttonCambridge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl("https://cambridgecarbonfootprint.org/carbon-reduction-goals/");
            }
        });

        buttonImperial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUrl("https://www.imperial.ac.uk/stories/climate-action/");
            }
        });
    }

    private void openUrl(String url) {
        // Open the URL in a browser
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
