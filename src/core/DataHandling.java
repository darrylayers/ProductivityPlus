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
 * This class stores, loads, and handles the saved data from the application.
 * 
 * @author Austin Ayers
 * @version 9/29/18
 * 
 */
public class DataHandling {

    public static File savedMap = new File(getDate() + ".list");
    public static HashMap<String, Long> map = new HashMap<String, Long>();

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
}
