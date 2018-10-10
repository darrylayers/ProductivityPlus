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

    public static String getUnit() {
        return dataUnit;
    }
}
