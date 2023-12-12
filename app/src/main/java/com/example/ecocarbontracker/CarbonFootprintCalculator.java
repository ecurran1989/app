package com.example.ecocarbontracker;

public class CarbonFootprintCalculator {

    // Constants for carbon emission factors (realistic values)
    private static final double CAR_MILEAGE_EMISSION_FACTOR = 0.2; // in kg CO2 per mile
    private static final double ELECTRICITY_EMISSION_FACTOR = 0.4; // in kg CO2 per kWh
    private static final double MEAT_EMISSION_FACTOR = 27.0; // in kg CO2 per kg of meat

    // Calculate carbon footprint for transportation
    public static double calculateTransportationFootprint(double milesDriven) {
        return milesDriven * CAR_MILEAGE_EMISSION_FACTOR;
    }

    // Calculate carbon footprint for home energy usage
    public static double calculateEnergyFootprint(double kWhUsed) {
        return kWhUsed * ELECTRICITY_EMISSION_FACTOR;
    }

    // Calculate carbon footprint for diet
    public static double calculateDietFootprint(double meatConsumed) {
        // Convert meat consumption from grams to kilograms
        double meatInKg = meatConsumed/1000;
        return meatInKg * MEAT_EMISSION_FACTOR;
    }

    // Calculate total carbon footprint
    public static double calculateTotalCarbonFootprint(double milesDriven, double kWhUsed, double meatConsumed) {
        double transportation = calculateTransportationFootprint(milesDriven);
        double energy = calculateEnergyFootprint(kWhUsed);
        double diet = calculateDietFootprint(meatConsumed);
        return (transportation + energy + diet);
    }
}
