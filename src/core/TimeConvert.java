package core;

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

        if (PreferencesGui.getDisplayIndex() == 0) {

            for (String name : map.keySet()) {
                Double convertedTime = (map.get(name) / 3600.0);
                convertedTime = Math.floor(convertedTime * 100) / 100;
                convertedMap.put(name, convertedTime);
            }
        }
        else if (PreferencesGui.getDisplayIndex() == 1) {
            for (String name : map.keySet()) {
                Double convertedTime = (map.get(name) / 60.0);
                convertedTime = Math.floor(convertedTime * 100) / 100;
                convertedMap.put(name, convertedTime);
            }
        }
        else {
            for (String name : map.keySet()) {
                Double convertedTime = (map.get(name) / 1.0);
                convertedTime = Math.floor(convertedTime * 100) / 100;
                convertedMap.put(name, convertedTime);
            }
        }
        return convertedMap;
    }

}
/*    public static void convertTime() {
        convertedMap = (HashMap<String, Double>) ProgramTimer.appMap.clone();
        // Check to see which option is checked in preferences class.
        if (PreferencesGui.getDisplayIndex() == 0) {
            for (String name : convertedMap.keySet()) {
                Double convertedTime = (convertedMap.get(name) / 3600.0);
                convertedTime = Math.floor(convertedTime * 100) / 100;
                convertedMap.put(name, convertedTime);
            }
        }
        else if (PreferencesGui.getDisplayIndex() == 1) {
            for (String name : convertedMap.keySet()) {
                Double convertedTime = (convertedMap.get(name) / 60.0);
                convertedTime = Math.floor(convertedTime * 100) / 100;
                convertedMap.put(name, convertedTime);
            }
        }
        else {
            for (String name : convertedMap.keySet()) {
                Double convertedTime = (convertedMap.get(name));
                convertedTime = Math.floor(convertedTime * 100) / 100;
                convertedMap.put(name, convertedTime);
            }
        }
    }
    public static Double getUnit() {
        // Check to see which option is checked in preferences class.
        if (PreferencesGui.getDisplayIndex() == 0) {
            return 3600.0;
        }
        else if (PreferencesGui.getDisplayIndex() == 1) {
            return 60.0;
        }
        else {
            return 1.0;
        }
    }*/
