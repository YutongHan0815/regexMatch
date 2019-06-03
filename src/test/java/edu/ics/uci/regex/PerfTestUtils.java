package edu.ics.uci.regex;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author Hailey Pan
 *
 *         Performance test helper functions
 **/

public class PerfTestUtils {

    /**
     * 
     * Checks whether the given file exists. If not, create such a file with a
     * header written into it.
     */
    public static void createFile(Path filePath, String header) throws IOException {
        if (Files.notExists(filePath)) {
            Files.createFile(filePath);
            Files.write(filePath, header.getBytes());
        }
    }
    /**
     * Delete all files recursively in a directory
     * 
     * @param indexDirectory
     * @throws Exception
     */
    public static void deleteDirectory(File indexDirectory) throws Exception {
        boolean deleteTopDir= false; 
        for (File file : indexDirectory.listFiles()) {
            if (file.getName().equals(".gitignore")) {
                continue;
            }
            if (file.isDirectory()) {
                deleteDirectory(file);
            } else {
                deleteTopDir = file.delete();
            }
        }
        if(deleteTopDir){
            indexDirectory.delete();
        }
        
    }

    /**
     * Formats a time to string
     * 
     * @param time
     *            (the milliseconds since January 1, 1970, 00:00:00 GMT)
     * @return string representation of the time
     */
    public static String formatTime(long time) {
        Date date = new Date(time);

        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

        return sdf.format(date).toString();
    }

}
