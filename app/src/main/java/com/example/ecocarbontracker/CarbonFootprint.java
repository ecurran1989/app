package com.example.ecocarbontracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CarbonFootprint extends AppCompatActivity {

    private TextView textViewResult, textViewAverageFootprint;;
    private EditText editTextMiles, editTextEnergy, editTextMeat;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carbon_footprint);

        textViewResult = findViewById(R.id.textViewResult);
        editTextMiles = findViewById(R.id.editTextMilesDriven);
        editTextEnergy = findViewById(R.id.editTextKWhUsed);
        editTextMeat = findViewById(R.id.editTextMeatConsumed);

        Button calculateButton = findViewById(R.id.buttonCalculate);

        Button buttonAverage = findViewById(R.id.buttonAverage);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call a method to perform the calculation and update the result
                calculateAndDisplayResult();

            }
        });

        buttonAverage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CarbonFootprint.this, com.example.ecocarbontracker.AverageCarbonFootprintUK.class);
                startActivity(i);
            }
        });
    }


    private void calculateAndDisplayResult() {
        // Get user input
        double milesDriven = Double.parseDouble(editTextMiles.getText().toString());
        double kWhUsed = Double.parseDouble(editTextEnergy.getText().toString());
        double meatConsumed = Double.parseDouble(editTextMeat.getText().toString());

        // Calculate individual footprints
        double transportationFootprint = CarbonFootprintCalculator.calculateTransportationFootprint(milesDriven);
        double energyFootprint = CarbonFootprintCalculator.calculateEnergyFootprint(kWhUsed);
        double dietFootprint = CarbonFootprintCalculator.calculateDietFootprint(meatConsumed);

        // Calculate total footprint
        double totalFootprint = (transportationFootprint + energyFootprint + dietFootprint);

        // Display individual results
        textViewResult.setText(
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
