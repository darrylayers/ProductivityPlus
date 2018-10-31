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

import gui.ExploreDataGui;
import gui.WhatToTrackGui;

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
 * @version 9/29/18
 * 
 */
public class DataHandling {

    public static File savedMap =
        new File("./saved_data/" + getDate() + ".map");

    /**
     * This method loads the appMap hash map in ProgramTimer.java
     * 
     * @throws IOException
     * @throws NumberFormatException
     */
    @SuppressWarnings("unchecked")
    public static void loadMap() throws NumberFormatException, IOException {

        try {
            ObjectInputStream ois =
                new ObjectInputStream(new FileInputStream(savedMap));
            Object readMap = ois.readObject();
            if (readMap != null && readMap instanceof HashMap) {
                ProgramTimer.appMap
                    .putAll((Map<? extends String, ? extends Long>) readMap);
            }
            ois.close();
        }
        catch (Exception e) {
            // Catch exceptions
        }
    }

    /**
     * This method saves the appMap hash map in ProgramTimer.java
     * 
     * @throws IOException
     */
    public static void saveMap() throws IOException {

        try {
            ObjectOutputStream oos =
                new ObjectOutputStream(new FileOutputStream(savedMap));
            oos.writeObject(ProgramTimer.appMap);
            oos.close();
        }
        catch (Exception e) {
            // Catch exceptions
        }
    }

    /**
     * This method returns the date in the formatted String Dyy
     * 
     * @return Today's date in form Dyy, ex: 093018 for 09/30/18
     */
    public static String getDate() {
        Date now = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("Dyy");
        return dateFormatter.format(now);
    }

    /**
     * This method accepts a single date from the beginning date calendar inside
     * of ExploreDataGui.java. The date is then used to load the correct date
     * program file and is passed to the ExcelWriter.java class.
     * 
     * @param date
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static void acceptDate(String date) throws IOException {

        Map<String, Long> loadedAppMap = new HashMap<>();
        try {
            ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream("./saved_data/" + date + ".map"));
            Object readMap = ois.readObject();
            if (readMap != null && readMap instanceof HashMap) {
                loadedAppMap
                    .putAll((Map<? extends String, ? extends Long>) readMap);
            }
            ois.close();
        }
        catch (Exception e) {

        }

        if (loadedAppMap.size() == 0) {
            JOptionPane.showMessageDialog(null,
                "Warning: Loaded map was empty.");
        }

        ExcelWriter.write(loadedAppMap, date);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Long> acceptDateTable(String date)
        throws IOException {

        Map<String, Long> loadedAppMap = new HashMap<>();
        try {
            ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream("./saved_data/" + date + ".map"));
            Object readMap = ois.readObject();
            if (readMap != null && readMap instanceof HashMap) {
                loadedAppMap
                    .putAll((Map<? extends String, ? extends Long>) readMap);
            }
            ois.close();
        }
        catch (Exception e) {

        }

        return loadedAppMap;
    }

    /**
     * This method returns an arraylist containing all of the dates that need to
     * be used to open the proper program history files.
     * 
     * @param date1
     * @param date2
     * @return returns an arraylist of String dates
     */
    @SuppressWarnings("unused")
    public static List<String> dateDiff(String date1, String date2) {

        List<String> dates = new ArrayList<String>();
        String days1 = date1.substring(0, 3);
        String days2 = date2.substring(0, 3);
        int days1_int = Integer.valueOf(days1);
        int days2_int = Integer.valueOf(days2);
        int dateCalc = (days2_int - days1_int);
        int year = Calendar.getInstance().get(Calendar.YEAR);

        for (int i = 0; i <= dateCalc; i++) {
            dates.add(String.valueOf((days1_int + i)) + "18");
        }
        return dates;
    }

    /**
     * loadMaps() loads an arraylist full of the requested hash maps from the
     * date picker.
     * 
     * @param dates
     * @return returns an arraylist of hashmaps used for program dates
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List<Map> loadMaps(List<String> dates) {

        List<Map> maps = new ArrayList<Map>();

        for (int j = 0; j < dates.size(); j++) {
            Map<String, Long> loadedAppMap = new HashMap<>();
            try {
                ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(
                        "./saved_data/" + dates.get(j) + ".map"));
                Object readMap = ois.readObject();
                if (readMap != null && readMap instanceof HashMap) {
                    loadedAppMap.putAll(
                        (Map<? extends String, ? extends Long>) readMap);
                }
                ois.close();
            }
            catch (Exception e) {

            }
            maps.add(loadedAppMap);
        }
        return maps;
    }

    /**
     * This method writes the date range into one hashmap and then writes that
     * map to an Excel file.
     * 
     * @param maps,
     *            dates
     * @throws IOException
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void writeDates(List<Map> maps, List<String> dates)
        throws IOException {

        Map<String, Long> combinedMaps = new HashMap<>();

        int i = 100 / maps.size();

        for (Map<String, Long> map : maps) {

            for (Map.Entry<String, Long> entry : map.entrySet()) {
                String key = entry.getKey();
                Long current = combinedMaps.get(key);
                combinedMaps.put(key, current == null ? entry.getValue()
                    : entry.getValue() + current);
            }

            ExploreDataGui.updateBar(i);
            i = 2 * i;

        }
        ExcelWriter.write(combinedMaps,
            "_date_range_" + dates.get(0) + "_" + dates.get(dates.size() - 1));

    }

    public static Map<String, Double> sortHashMapByValues(
        Map<String, Double> finalMap) {
        List<String> mapKeys = new ArrayList<>(finalMap.keySet());
        List<Double> mapValues = new ArrayList<>(finalMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        Map<String, Double> sortedMap = new LinkedHashMap<>();

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

    public static Map<String, Double> orderedMap() {
        /**
         * Need to put a check in here to know which map to load...
         */

        Map<String, Long> toDisplayMap = new HashMap<>(ProgramTimer.appMap);
        Map<String, Double> finalMap =
            TimeConvert.convertOutputTime(toDisplayMap);
        DataHandling.sortHashMapByValues(finalMap);
        return DataHandling.sortHashMapByValues(finalMap);
    }

    public static Map<String, Long> validateData(Map<String, Long> inputMap) {

        Map<String, Long> editedMap = new HashMap<String, Long>();
        Map<String, Long> doNotAdd = new HashMap<String, Long>();

        int size = WhatToTrackGui.getSavedList().size();
        WhatToTrackGui.loadList();
        String[] itemstoHide =
            WhatToTrackGui.list.toArray(new String[WhatToTrackGui.list.size()]);

        // For each item in the input map...
        for (Entry<String, Long> entry : inputMap.entrySet()) {

            // Save the current item's key-value pair
            String key = entry.getKey(); // Current prog we are looking at
            Long current = inputMap.get(key); // Current prog's time

            // Check to see if the key needs to be combined or not
            for (int i = 0; i < size; i++) {
                // for (String element : itemstoHide) {
                if (key.contains(itemstoHide[i])) {
                    if (editedMap
                        .get(itemstoHide[i].substring(2,
                            itemstoHide[i].length())) == null) {
                        editedMap.put(
                            itemstoHide[i].substring(2,
                                itemstoHide[i].length()),
                            current);
                    }
                    else {
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
            if (!doNotAdd.containsKey(key)) {
                editedMap.put(key, current);
            }
        }
        return editedMap;
    }
}
