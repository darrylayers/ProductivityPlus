package core;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;

import gui.PreferencesGui;

/**
 * The purpose of this class is to convert the saved or current data to the
 * specified unit. If the user wants their data presented in hours, this class
 * will convert the data from seconds to hours.
 * 
 * @author Austin
 * @version 10/8/18
 */
public class TimeConvert {

    public static HashMap<String, Double> convertedMap = new HashMap<>();;
    private static String dataUnit = "";

    /**
     * We need to be able to convert the given time in seconds to the desired
     * time, this time can either be minutes or hours.
     * 
     * We are actually taking a hashmap of time in seconds, and converting the
     * time to the desire time. (min or hours).
     */

    /**
     * This method converts the times in the given hashmap and returns a
     * properly converted hashmap.
     * 
     * @param appMap
     *            either the current data, or the current iteration of a saved
     *            value.
     */
    public static HashMap<String, Double> convertTime(
        HashMap<String, Long> map) {

        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.FLOOR);

        if (PreferencesGui.getDisplayIndex() == 0) {

            for (String name : map.keySet()) {
                Double convertedTime = (map.get(name) / 3600.0);
                Double modTime = Double.valueOf(df.format(convertedTime));
                convertedMap.put(name, modTime);
                dataUnit = "Time (Hours)";
            }
        }
        else if (PreferencesGui.getDisplayIndex() == 1) {
            for (String name : map.keySet()) {
                Double convertedTime = (map.get(name) / 60.0);
                Double modTime = Double.valueOf(df.format(convertedTime));
                convertedMap.put(name, modTime);
                dataUnit = "Time (Minutes)";
            }
        }

        else {
            for (String name : map.keySet()) {
                Double convertedTime = (map.get(name) / 1.0);
                Double modTime = Double.valueOf(df.format(convertedTime));
                convertedMap.put(name, modTime);
                dataUnit = "Time (Seconds)";
            }
        }
        return convertedMap;
    }

    public static HashMap<String, Double> convertOutputTime(
        HashMap<String, Long> map) {

        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.FLOOR);

        if (PreferencesGui.getExportIndex() == 0) {

            for (String name : map.keySet()) {
                Double convertedTime = (map.get(name) / 3600.0);
                Double modTime = Double.valueOf(df.format(convertedTime));
                convertedMap.put(name, modTime);
                dataUnit = "Time (Hours)";
            }
        }
        else if (PreferencesGui.getExportIndex() == 1) {
            for (String name : map.keySet()) {
                Double convertedTime = (map.get(name) / 60.0);
                Double modTime = Double.valueOf(df.format(convertedTime));
                convertedMap.put(name, modTime);
                dataUnit = "Time (Minutes)";
            }
        }
        else {
            for (String name : map.keySet()) {
                Double convertedTime = (map.get(name) / 1.0);
                Double modTime = Double.valueOf(df.format(convertedTime));
                convertedMap.put(name, modTime);
                dataUnit = "Time (Seconds)";
            }
        }
        return convertedMap;
    }

    public static HashMap<String, String> convertWritten(

        HashMap<String, Long> map) {
        HashMap<String, String> stringMap = new HashMap<>();
        for (String name : map.keySet()) {
            String output = "";
            String pluralHour = " hour ";
            String pluralMin = " minute ";
            String pluralSec = " seconds ";
            long convertedTime = map.get(name);

            if (convertedTime > 3600) {
                if (convertedTime >= 3600 * 2) {
                    pluralHour = " hours ";
                }
                output = convertedTime / 3600 + pluralHour
                    + Integer.valueOf(
                        (int) Math.floor(((convertedTime / 3600.0) - 1) * 60))
                    + " minutes "
                    + convertedTime % 60 + pluralSec;
            }
            else if (convertedTime >= 60) {
                if (convertedTime >= 120) {
                    pluralMin = " minutes ";
                }
                output = convertedTime / 60 + pluralMin
                    + convertedTime % 60 + pluralSec;
            }
            else {
                if (convertedTime % 60 == 1) {
                    pluralSec = " second ";
                }
                output = convertedTime + pluralSec;
            }
            stringMap.put(name, output);
            dataUnit = "Time (Written)";
        }
        return stringMap;

    }

    public static String getUnit() {
        return dataUnit;
    }
}
