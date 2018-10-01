package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
     * This method returns the date in the formatted String MMDDyy
     * 
     * @return Today's date in form MMDDyy, ex: 093018 for 09/30/18
     */
    public static String getDate() {
        Date now = new Date();
        SimpleDateFormat dateFormatter =
            new SimpleDateFormat("MMDDyy");
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
}
