package core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * Class that saves the program data to a .txt file
 * 
 * @author Austin Ayers
 * 
 * @version 9/12/18
 */
public class FileHandling {

    /**
     * Method to write data to file.
     */
    public static void writeToFile(HashMap<String, Long> appMap) {
        String fileName = "Program_Log.txt";

        StringBuilder string = new StringBuilder();

        File f = new File("Program_Log.txt");

        if (!f.exists()) {
            BufferedWriter bw = null;
            FileWriter fw = null;

            try {

                for (String name : appMap.keySet()) {
                    String key = name.toString();
                    // String value = appMap.get(name).toString();
                    long value = appMap.get(name);

                    if (value > 60) {
                        string.append(
                            "Time spent in " + key + " was " + value / 60
                                + " minutes and " + value % 60 + " seconds.");

                    }
                    else {
                        string.append(
                            "Time spent in " + key + " was " + value
                                + " seconds.");
                    }
                }

                fw = new FileWriter(fileName);
                bw = new BufferedWriter(fw);
                bw.write(string.toString());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    if (bw != null) {
                        bw.close();
                    }

                    if (fw != null) {
                        fw.close();
                    }
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        else {
        }
    }

}
