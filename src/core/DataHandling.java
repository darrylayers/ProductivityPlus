package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import gui.ExploreDataGui;

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

    public static File savedMap = new File(getDate() + ".map");

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
    public static void saveMap()
        throws IOException {

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
        SimpleDateFormat dateFormatter =
            new SimpleDateFormat("Dyy");
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

        HashMap<String, Long> loadedAppMap = new HashMap<>();
        try {
            ObjectInputStream ois =
                new ObjectInputStream(new FileInputStream(date + ".map"));
            Object readMap = ois.readObject();
            if (readMap != null && readMap instanceof HashMap) {
                loadedAppMap
                    .putAll((Map<? extends String, ? extends Long>) readMap);
            }
            ois.close();
        }
        catch (Exception e) {

        }

        ExcelWriter.write(loadedAppMap, date);
    }

    /**
     * This method returns an arraylist containing all of the dates that need to
     * be used to open the proper program history files. TODO: Make this work
     * better, for years especially.
     * 
     * @param date1
     * @param date2
     * @return returns an arraylist of String dates
     */
    public static ArrayList<String> dateDiff(String date1, String date2) {

        ArrayList<String> dates = new ArrayList<String>();
        String days1 = date1.substring(0, 3);
        String days2 = date2.substring(0, 3);
        int days1_int = Integer.valueOf(days1);
        int days2_int = Integer.valueOf(days2);
        int dateCalc = (days2_int - days1_int);

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
    public static ArrayList<HashMap> loadMaps(ArrayList<String> dates) {

        ArrayList<HashMap> maps = new ArrayList<HashMap>();

        for (int j = 0; j < dates.size(); j++) {
            HashMap<String, Long> loadedAppMap = new HashMap<>();
            try {
                ObjectInputStream ois =
                    new ObjectInputStream(
                        new FileInputStream(dates.get(j) + ".map"));
                Object readMap = ois.readObject();
                if (readMap != null && readMap instanceof HashMap) {
                    loadedAppMap
                        .putAll(
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
    public static void writeDates(ArrayList<HashMap> maps,
        ArrayList<String> dates)
        throws IOException {

        HashMap<String, Long> combinedMaps = new HashMap<>();

        int i = 100 / maps.size();

        for (HashMap<String, Long> map : maps) {

            for (HashMap.Entry<String, Long> entry : map.entrySet()) {
                String key = entry.getKey();
                Long current = combinedMaps.get(key);
                combinedMaps.put(key, current == null ? entry.getValue()
                    : entry.getValue() + current);
            }

            ExploreDataGui.updateBar(i);
            i = 2 * i;

        }

        System.out.println(combinedMaps);

        ExcelWriter.write(combinedMaps,
            "_date_range_" + dates.get(0) + "_" + dates.get(dates.size() - 1));

    }
}
