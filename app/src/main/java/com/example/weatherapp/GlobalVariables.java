package com.example.weatherapp;

public class GlobalVariables {
    // singleton
    private static GlobalVariables instance = null;

    // global variables
    public enum Units { METRIC, IMPERIAL };
    private static Units units = Units.METRIC;

    public static GlobalVariables getInstance() {
        if (instance == null) {
            instance = new GlobalVariables();
        }
        return instance;
    }

    public static Units getUnits() {
        return units;
    }

    public static void setUnits(Units units) {
        GlobalVariables.units = units;
    }
}
