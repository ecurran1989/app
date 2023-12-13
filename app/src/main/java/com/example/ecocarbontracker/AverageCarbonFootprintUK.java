package com.example.ecocarbontracker;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AverageCarbonFootprintUK extends AppCompatActivity {

    TextView textViewAverageFootprint, textViewAverageResult, textViewComparisonMessage;
    Button buttonLinks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_average_carbon_footprint_uk);


        textViewAverageFootprint = findViewById(R.id.textViewAverageFootprint);
        textViewAverageResult = findViewById(R.id.textViewAverageResult);
        textViewComparisonMessage = findViewById(R.id.textViewComparisonMessage);
        buttonLinks = findViewById(R.id.buttonLinks);


        calculateAndDisplayResult();


        textViewAverageResult.setMovementMethod(LinkMovementMethod.getInstance());
        textViewAverageResult.setText(Html.fromHtml(textViewAverageResult.getText().toString()));

        buttonLinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLinksActivity();  // Call the method to open the LinksActivity
            }
        });
    }



    private void openLinksActivity() {
        Intent intent = new Intent(AverageCarbonFootprintUK.this, LinksActivity.class);
        startActivity(intent);
    }


    private void calculateAndDisplayResult() {
        double averageMiles = 18.0;
        double averagekWh = 7.94;
        double averageMeat = 86.3;


        textViewAverageFootprint.setText(
                "Average Transportation: " + averageMiles + " miles\n" +
                        "Average Energy: " + averagekWh + " kWh\n" +
                        "Average Meat Consumption: " + averageMeat + " grams");

        // Calculate individual footprints
        double transportationFootprint = CarbonFootprintCalculator.calculateTransportationFootprint(averageMiles);
        double energyFootprint = CarbonFootprintCalculator.calculateEnergyFootprint(averagekWh);
        double dietFootprint = CarbonFootprintCalculator.calculateDietFootprint(averageMeat);

        // Calculate total footprint
        double totalFootprint = transportationFootprint + energyFootprint + dietFootprint;

        // Display individual results
        textViewAverageResult.setText(
                "<b>Carbon Footprint Calculation</b><br> " +
                        "<br>Transportation Footprint: " + transportationFootprint + " kg CO2<br>" +
                        "Energy Footprint: " + energyFootprint + " kg CO2<br>" +
                        "Diet Footprint: " + dietFootprint + " kg CO2<br>" +
                        "Total Carbon Footprint: " + totalFootprint + " kg CO2"
        );
        double userFootprint = getIntent().getDoubleExtra("userFootprint", 0.0);
        displayMessageBasedOnComparison(userFootprint);

        Log.d("CarbonFootprint", "Transportation Footprint: " + transportationFootprint + " kg CO2");
        Log.d("CarbonFootprint", "Energy Footprint: " + energyFootprint + " kg CO2");
        Log.d("CarbonFootprint", "Diet Footprint: " + dietFootprint + " kg CO2");
        Log.d("CarbonFootprint", "Total Carbon Footprint: " + totalFootprint + " kg CO2");
    }


    private void displayMessageBasedOnComparison(double userFootprint) {
        double averageFootprint = calculateAverageFootprint();
        String comparisonMessage;

        if (userFootprint > averageFootprint) {
            comparisonMessage = "Oh no! Your carbon footprint is higher than the average. Embark on your eco-friendly journey today! Explore the Links page for a head start on your sustainable living adventure.";
            showToast("Oh no! That's not good.");
        } else if (userFootprint < averageFootprint) {
            comparisonMessage = "Congrats! Your carbon footprint is lower than the average. Keep up the good work! :)";
            showToast("Nice one!");
        } else {
            comparisonMessage = "Your carbon footprint is equal to the average... Well, it could be worse.";
            showToast("What were the chances of that? Crazy stuff");
        }



        textViewComparisonMessage.setText(comparisonMessage);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private double calculateAverageFootprint() {
        double averageMiles = 18.0;
        double averagekWh = 7.94;
        double averageMeat = 86.3;

        double transportationFootprint = CarbonFootprintCalculator.calculateTransportationFootprint(averageMiles);
        double energyFootprint = CarbonFootprintCalculator.calculateEnergyFootprint(averagekWh);
        double dietFootprint = CarbonFootprintCalculator.calculateDietFootprint(averageMeat);

        // Calculate total footprint
        double totalFootprint = transportationFootprint + energyFootprint + dietFootprint;

        return totalFootprint;
    }



}
