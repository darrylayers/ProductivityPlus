package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import gui.ConsolidationGui;
import gui.ExploreDataGui;
import gui.Main;

/**
 * This class stores, loads, and handles the saved data from the application. As
 * of now, the file storage system works like this: The program writes all of
 * the program time values to a .list filed named <TODAY'S DATE>.list. Each time
 * the program loads, it checks today's date through the system and then
 * attempts to load the corresponding file. This means the program times are now
 * sorted day by day, including month and year. This will allow for me to pull
 * data from specific dates and also choose time intervals of information to
 * display, like being able to search 9/15/18 to 9/17/18.
 * 
 * We can also find specific program times by simply accessing the hash map.
 * 
 * 
 * @author Austin Ayers
 * 
 */
public class DataHandling {
    /**
     * This method loads the appMap hash map in ProgramTimer.java
     * 
     * @throws IOException
     * @throws NumberFormatException
     */
    public static void loadMap() throws NumberFormatException, IOException {

        File savedMap =
            new File("./saved_data/" + getDate() + ".map");
        // Read in the saved .map file
        readMap(savedMap, ProgramTimer.appMap);
    }

    /**
     * This method saves the appMap hash map in ProgramTimer.java
     * 
     * @throws IOException
     */
    public static void saveMap() throws IOException {

        File savedMap =
            new File("./saved_data/" + getDate() + ".map");
        // Save the current map to a .map file
        try {
            ObjectOutputStream oos =
                new ObjectOutputStream(new FileOutputStream(savedMap));
            oos.writeObject(ProgramTimer.appMap);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method returns the date in the formatted String Dyy
     * 
     * @return Today's date in form Dyy, ex: 093018 for 09/30/18
     */
    private static String getDate() {

        Date now = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("Dyy");
        return dateFormatter.format(now);
    }

    /**
     * This method accepts a single date from the beginning date calendar inside
     * of ExploreDataGui.java. The date is then used to load the correct date
     * program file and is passed to the ExcelWriter.java class.
     * 
     * @param date,
     *            date we need to look up.
     * @return returns the map filled with correct data.
     * @throws IOException
     */
    public static Map<String, Long> acceptDate(String date,
        boolean returnState)
        throws IOException {

        Map<String, Long> loadedAppMap = new HashMap<>();
        // Load the saved .map file
        File savedMap =
            new File("./saved_data/" + getDate() + ".map");
        readMap(savedMap, loadedAppMap);

        // Alert user if the mpa is empty
        if (loadedAppMap.size() == 0) {
            JOptionPane.showMessageDialog(null,
                "Warning: Loaded map was empty.");
        }
        if (returnState) {
            return loadedAppMap;
        } else {
            // Write the values to Excel
            ExcelWriter.write(loadedAppMap, date);
        }
        return loadedAppMap;
    }

    /**
     * Writes a date range into a combined Map.
     * 
     * @param date,
     *            date we need to look up.
     * @return Combined map of data entries from a range.
     * @throws IOException
     */
    public static Map<String, Long> acceptDateTable(String date)
        throws IOException {

        Map<String, Long> loadedAppMap = new HashMap<>();
        // Load the saved .map file
        File savedMap =
            new File("./saved_data/" + getDate() + ".map");
        readMap(savedMap, loadedAppMap);
        return loadedAppMap;
    }

    /**
     * This method returns an arraylist containing all of the dates that need to
     * be used to open the proper program history files.
     * 
     * @param date1,
     *            date picker1
     * @param date2,
     *            date picker 2
     * @return returns an arraylist of String dates
     */
    public static List<String> dateDiff(String date1, String date2) {

        List<String> dates = new ArrayList<String>();
        // Grab both dates
        String days1 = date1.substring(0, 3);
        String days2 = date2.substring(0, 3);
        // Get the int values
        int days1_int = Integer.valueOf(days1);
        int days2_int = Integer.valueOf(days2);
        // Find differents
        int dateCalc = (days2_int - days1_int);
        // Get the last two digits of the current year
        int year = Calendar.getInstance().get(Calendar.YEAR);
        // Convert to string
        String strYear = Integer.toString(year);

        // Fill the array list with a list of dates
        for (int i = 0; i <= dateCalc; i++) {
            dates.add(String.valueOf((days1_int + i))
                + strYear.substring(2, strYear.length()));
        }
        return dates;
    }

    /**
     * loadMaps() loads an arraylist full of the requested hash maps from the
     * date picker.
     * 
     * @param dates,
     *            list of dates
     * @return returns an arraylist of hashmaps used for program dates
     */
    @SuppressWarnings("rawtypes")
    public static List<Map> loadMaps(List<String> dates) {

        List<Map> maps = new ArrayList<Map>();
        // Load the maps
        for (int j = 0; j < dates.size(); j++) {
            Map<String, Long> loadedAppMap = new HashMap<>();
            // Load the saved .map file
            File savedMap =
                new File("./saved_data/" + dates.get(j) + ".map");
            readMap(savedMap, loadedAppMap);
            maps.add(loadedAppMap);
        }
        return maps;
    }

    /**
     * This method writes the date range into one hashmap and then writes that
     * map to an Excel file.
     * 
     * @param maps,
     *            list of maps dates list of string dates
     * @throws IOException
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void writeDates(List<Map> maps, List<String> dates)
        throws IOException {

        Map<String, Long> combinedMaps = new HashMap<>();
        // Get info for progress bar
        int i = 100 / maps.size();
        // Combine the maps into one
        for (Map<String, Long> map : maps) {
            for (Map.Entry<String, Long> entry : map.entrySet()) {
                String key = entry.getKey();
                Long current = combinedMaps.get(key);
                combinedMaps.put(key, current == null ? entry.getValue()
                    : entry.getValue() + current);
            }
            // Update progress bar
            ExploreDataGui.updateBar(i);
            i = 2 * i;
        }
        // Write to Excel
        ExcelWriter.write(combinedMaps,
            "_date_range_" + dates.get(0) + "_" + dates.get(dates.size() - 1));
    }

    /**
     * Sort the input map and return the result. Sort the map by values.
     * 
     * @param finalMap,
     *            the map to be sorted.
     * @return a sorted map by values.
     */
    private static Map<String, Double> sortHashMapByValues(

        Map<String, Double> finalMap) {
        // Create two lists, one for keys one for values
        List<String> mapKeys = new ArrayList<>(finalMap.keySet());
        List<Double> mapValues = new ArrayList<>(finalMap.values());
        // Sort both lists
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        Map<String, Double> sortedMap = new LinkedHashMap<>();

        // Fill linked map with proper values
        Iterator<Double> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Double val = valueIt.next();
            Iterator<String> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                String key = keyIt.next();
                Double comp1 = finalMap.get(key);
                Double comp2 = val;

                if (comp1.equals(comp2)) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }

    /**
     * Returned an ordered map of today's values.
     * 
     * @return Ordered map of today's values.
     */
    public static Map<String, Double> orderedMap() {

        Map<String, Long> loadedCurrentMap = new HashMap<>();
        // Filter the results
        loadedCurrentMap = validate(convertMapToLong(Main.globalMap));
        // Convert the time
        Map<String, Double> finalMap =
            TimeConvert.convertTime(loadedCurrentMap);
        // Sort the map by values
        DataHandling.sortHashMapByValues(finalMap);
        return DataHandling.sortHashMapByValues(finalMap);
    }

    /**
     * Validate the data by checking the keys against programs in the
     * consolidation list.
     * 
     * @param inputMap,
     *            the map we are reviewing.
     * @return a map of consolidated entries.
     */
    public static Map<String, Long> validateData(Map<String, Long> inputMap) {

        Map<String, Long> editedMap = new HashMap<String, Long>();
        Map<String, Long> doNotAdd = new HashMap<String, Long>();

        // Get size of list of items to consolidate
        int size = ConsolidationGui.getSavedList().size();
        // Load the consolidation list
        ConsolidationGui.loadList();
        // Move the consolidation list to a local array
        String[] itemstoHide =
            ConsolidationGui.list
                .toArray(new String[ConsolidationGui.list.size()]);

        // For each item in the input map...
        for (Entry<String, Long> entry : inputMap.entrySet()) {

            // Save the current item's key-value pair
            String key = entry.getKey(); // Current prog we are looking at
            Long current = inputMap.get(key); // Current prog's time
            try {
                // Check to see if the key needs to be combined or not
                for (int i = 0; i < size; i++) {
                    if (key.contains(itemstoHide[i])) {
                        if (editedMap
                            .get(itemstoHide[i].substring(2,
                                itemstoHide[i].length())) == null) {
                            editedMap.put(
                                itemstoHide[i].substring(2,
                                    itemstoHide[i].length()),
                                current);
                        } else {
                            Long old = editedMap
                                .get(itemstoHide[i].substring(2,
                                    itemstoHide[i].length()));
                            editedMap.put(
                                itemstoHide[i].substring(2,
                                    itemstoHide[i].length()),
                                current + old);
                        }
                        doNotAdd.put(key, (long) 1);
                    }
                }
            } catch (StringIndexOutOfBoundsException e) {
                System.out.println("Error with retrieving consolidation list.");
            }
            // Check to only add the correct items
            if (!doNotAdd.containsKey(key)) {
                editedMap.put(key, current);
            }
        }
        return editedMap;
    }

    /**
     * Validate the data by checking the keys against programs in the
     * include/exclude lists.
     * 
     * @param inputMap,
     *            the map we are reviewing.
     * @return a map of removed/included entries.
     */
    public static Map<String, Long> validateWhatToDisplay(
        Map<String, Long> inputMap) {

        Map<String, Long> editedMap = new HashMap<String, Long>();
        Map<String, Long> doNotAdd = new HashMap<String, Long>();
        List<String> list = new ArrayList<String>();

        // Filter the display to display inclusions
        if (Main.getMode() == 2) {
            list = TableHelper.loadList("inclusion");
            list.remove("");
            int size = list.size();
            String[] itemsToShow = list.toArray(new String[list.size()]);
            for (Entry<String, Long> entry : inputMap.entrySet()) {
                // Save the current item's key-value pair
                String key = "- " + entry.getKey();
                Long current = inputMap.get(key.substring(2, key.length()));
                for (int i = 0; i < size; i++) {
                    if (key.contains(itemsToShow[i])
                        || key.equals(itemsToShow[i])) {
                        editedMap.put(key.substring(2, key.length()), current);
                    }
                }
            }
        }
        // Filter the display to display exclusions
        else if (Main.getMode() == 3) {
            list = TableHelper.loadList("exclusion");
            list.remove("");
            int size = list.size();
            String[] itemstoHide = list.toArray(new String[list.size()]);
            // For each item in the input map...
            for (Entry<String, Long> entry : inputMap.entrySet()) {
                // Save the current item's key-value pair
                String key = "- " + entry.getKey();
                Long current = inputMap.get(key.substring(2, key.length()));
                for (int i = 0; i < size; i++) {
                    if (key.contains(itemstoHide[i])
                        || key.equals(itemstoHide[i])) {
                        doNotAdd.put(key, (long) 1);
                    }
                }
                // Check to make sure the list is correct
                if (!doNotAdd.containsKey(key)) {
                    editedMap.put(key.substring(2, key.length()), current);
                }
            }
        }
        return editedMap;
    }

    /**
     * Check to see if input string is empty.
     * 
     * @param str
     *            input string.
     * @return true if the input string is empty.
     */
    public static boolean checkEmpty(String str) {
        return (str.isEmpty());
    }

    /**
     * Convert the time to written format.
     * 
     * @param secs,the
     *            input time
     * @return String, the time in written format
     */
    public static String convertToWritten(double secs) {
        String output = "";
        String pluralHour = " hour ";
        String pluralMin = " minute ";
        String pluralSec = " seconds ";
        int seconds = (int) secs;

        if (seconds > 3600) {
            if (seconds >= 3600 * 2) {
                pluralHour = " hours ";
            }
            output = seconds / 3600 + pluralHour
                + Integer.valueOf(
                    (int) Math.floor(((seconds / 3600.0) - 1) * 60))
                + " minutes "
                + seconds % 60 + pluralSec;
        } else if (seconds >= 60) {
            if (seconds >= 120) {
                pluralMin = " minutes ";
            }
            output = seconds / 60 + pluralMin
                + seconds % 60 + pluralSec;
        } else {
            if (seconds % 60 == 1) {
                pluralSec = " second ";
            }
            output = seconds + pluralSec;
        }
        return output;

    }

    /**
     * Convert a Map<String, Long> to Map<String, Double>
     * 
     * @param Map<String,
     *            Long>
     * @return Map<String, Double>
     */
    public static Map<String, Double> convertMap(Map<String, Long> input) {
        Map<String, Double> map = new HashMap<String, Double>();

        Iterator<Entry<String, Long>> it = input.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Long> pair = it.next();
            map.put(pair.getKey(), pair.getValue().doubleValue());
        }
        return map;
    }

    /**
     * Convert a Map<String, Double> to Map<String, Long> Convert a Map<String,
     * Double> to Map<String, Long>
     * 
     * @param Map<String,
     *            Double>
     * @return Map<String, Long>
     */
    public static Map<String, Long> convertMapToLong(
        Map<String, Double> input) {
        Map<String, Long> map = new HashMap<String, Long>();

        Iterator<Entry<String, Double>> it = input.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Double> pair = it.next();
            map.put(pair.getKey(), pair.getValue().longValue());
        }
        return map;
    }

    /**
     * Validate an input map to filter out the correct entries according to the
     * user's settings.
     * 
     * @param comboMap,
     *            the input map.
     * @return a correct map with filtered values.
     */
    public static Map<String, Long> validate(Map<String, Long> comboMap) {
        // Modify the map using the user's preferences
        Map<String, Long> loadedCurrentMap = new HashMap<String, Long>();

        // See if the consolidate programs checkbox is selected
        if (Main.chckbxConsolidatePrograms.isSelected()) {
            loadedCurrentMap = validateData(comboMap);
        } else {
            loadedCurrentMap = comboMap;
        }
        // Check the display mode
        if (Main.getMode() == 3 || Main.getMode() == 2) {
            loadedCurrentMap = validateWhatToDisplay(loadedCurrentMap);
        }
        return loadedCurrentMap;
    }

    /**
     * Read a .map file from memory.
     * 
     * @param savedMap,
     *            the location of the file
     * @param location,
     *            the hashmap to save the data to
     */
    @SuppressWarnings("unchecked")
    private static void readMap(File savedMap, Map<String, Long> location) {
        // If the map does not exist, return
        if (!savedMap.exists()) {
            return;
        }
        try {
            ObjectInputStream ois =
                new ObjectInputStream(new FileInputStream(savedMap));
            Object readMap = ois.readObject();
            if (readMap != null && readMap instanceof HashMap) {
                location
                    .putAll((Map<? extends String, ? extends Long>) readMap);
            }
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
