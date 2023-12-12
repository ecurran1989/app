package com.example.ecocarbontracker;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AverageCarbonFootprintUK extends AppCompatActivity {

    TextView textViewAverageFootprint, textViewAverageResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_average_carbon_footprint_uk);

        // Find TextViews by their IDs
        textViewAverageFootprint = findViewById(R.id.textViewAverageFootprint);
        textViewAverageResult = findViewById(R.id.textViewAverageResult);

        calculateAndDisplayResult();
    }

    private void calculateAndDisplayResult() {
        double averageMiles = 18.0;
        double averagekWh = 7.94;
        double averageMeat = 86.3;

        // Display average values
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
                "AVERAGE UK CARBON FOOTPRINT\n" +
                        "Transportation Footprint: " + transportationFootprint + " kg CO2\n" +
                        "Energy Footprint: " + energyFootprint + " kg CO2\n" +
                        "Diet Footprint: " + dietFootprint + " kg CO2\n" +
                        "Total Carbon Footprint: " + totalFootprint + " kg CO2"
        );

        // Log individual results
        Log.d("CarbonFootprint", "Transportation Footprint: " + transportationFootprint + " kg CO2");
        Log.d("CarbonFootprint", "Energy Footprint: " + energyFootprint + " kg CO2");
        Log.d("CarbonFootprint", "Diet Footprint: " + dietFootprint + " kg CO2");
        Log.d("CarbonFootprint", "Total Carbon Footprint: " + totalFootprint + " kg CO2");
    }
}
