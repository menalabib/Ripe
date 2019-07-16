package ripe.ripe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileSearch {
    public static ArrayList<String> getDirectoryPaths(String directory) {
        ArrayList<String> pathArray = new ArrayList<>();
        File file = new File(directory);
        File[] files = file.listFiles();
        for (File fil : files) {
            if(fil.isDirectory()) {
                pathArray.add(fil.getAbsolutePath());
            }
        }
        return pathArray;
    }

    public static ArrayList<String> getFilePaths(String directory) {
        ArrayList<String> pathArray = new ArrayList<>();
        File file = new File(directory);
        File[] files = file.listFiles();
        for (File fil : files) {
            if(fil.isFile()) {
                pathArray.add(fil.getAbsolutePath());
            }
        }
        return pathArray;
    }
}
