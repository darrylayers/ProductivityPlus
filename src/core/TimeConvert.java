package core;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import gui.PreferencesGui;

/**
 * The purpose of this class is to convert the saved or current data to the
 * specified unit.
 * 
 * @author Austin Ayers
 */
public class TimeConvert {

    private static String dataUnit = "";

    /**
     * This method converts the times in the given hashmap and returns a
     * properly converted hashmap.
     * 
     * @param appMap
     *            either the current data, or the current iteration of a saved
     *            value.
     * @return returns a properly formatted Map<String, Double>.
     */
    public static Map<String, Double> convertTime(
        Map<String, Long> toDisplayMap) {
        Map<String, Double> convertedMap = new HashMap<>();

        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.FLOOR);

        if (PreferencesGui.getDisplayIndex() == 0) {

            for (String name : toDisplayMap.keySet()) {
                Double convertedTime = (toDisplayMap.get(name) / 3600.0);
                Double modTime = Double.valueOf(df.format(convertedTime));
                convertedMap.put(name, modTime);
                dataUnit = "Time (Hours)";
            }
        } else if (PreferencesGui.getDisplayIndex() == 1) {
            for (String name : toDisplayMap.keySet()) {
                Double convertedTime = (toDisplayMap.get(name) / 60.0);
                Double modTime = Double.valueOf(df.format(convertedTime));
                convertedMap.put(name, modTime);
                dataUnit = "Time (Minutes)";
            }
        } else {
            for (String name : toDisplayMap.keySet()) {
                Double convertedTime = (toDisplayMap.get(name) / 1.0);
                Double modTime = Double.valueOf(df.format(convertedTime));
                convertedMap.put(name, modTime);
                dataUnit = "Time (Seconds)";
            }
        }
        return convertedMap;
    }

    /**
     * This method converts the times in the given hashmap and returns a
     * properly converted hashmap, this method exists so we can convert to the
     * written format.
     * 
     * @param appMap
     *            either the current data, or the current iteration of a saved
     *            value.
     * @return returns a properly formatted Map<String, Double>.
     */
    public static Map<String, String> convertWritten(Map<String, Long> map) {
        Map<String, String> stringMap = new HashMap<>();
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
            } else if (convertedTime >= 60) {
                if (convertedTime >= 120) {
                    pluralMin = " minutes ";
                }
                output = convertedTime / 60 + pluralMin
                    + convertedTime % 60 + pluralSec;
            } else {
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

    /**
     * This method returns whichever time unit the user has currently selected
     * for the display table.
     * 
     * @return string of proper time unit.
     */
    public static String getUnit() {
        if (PreferencesGui.getDisplayIndex() == 0) {
            dataUnit = "Time (Hours)";
        } else if (PreferencesGui.getDisplayIndex() == 1) {
            dataUnit = "Time (Minutes)";
        } else if (PreferencesGui.getDisplayIndex() == 2) {
            dataUnit = "Time (Seconds)";
        } else {
            dataUnit = "Time (Written)";
        }
        return dataUnit;
    }

    /**
     * This method converts the times in the given hashmap and returns a
     * properly converted hashmap.
     * 
     * @param appMap
     *            either the current data, or the current iteration of a saved
     *            value.
     * @return returns a properly formatted Map<String, Double>.
     */
    public static Map<String, Double> convertExportTime(
        Map<String, Long> toDisplayMap) {
        Map<String, Double> convertedMap = new HashMap<>();

        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.FLOOR);

        if (PreferencesGui.getExportIndex() == 0) {

            for (String name : toDisplayMap.keySet()) {
                Double convertedTime = (toDisplayMap.get(name) / 3600.0);
                Double modTime = Double.valueOf(df.format(convertedTime));
                convertedMap.put(name, modTime);
                dataUnit = "Time (Hours)";
            }
        } else if (PreferencesGui.getExportIndex() == 1) {
            for (String name : toDisplayMap.keySet()) {
                Double convertedTime = (toDisplayMap.get(name) / 60.0);
                Double modTime = Double.valueOf(df.format(convertedTime));
                convertedMap.put(name, modTime);
                dataUnit = "Time (Minutes)";
            }
        } else {
            for (String name : toDisplayMap.keySet()) {
                Double convertedTime = (toDisplayMap.get(name) / 1.0);
                Double modTime = Double.valueOf(df.format(convertedTime));
                convertedMap.put(name, modTime);
                dataUnit = "Time (Seconds)";
            }
        }
        return convertedMap;
    }
}
