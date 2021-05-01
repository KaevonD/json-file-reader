package ca.cmpt213.as2;
import java.io.File;
import java.io.FileFilter;

/**
 * Takes in a file and returns true
 * if the file is a json file
 */
public class JsonFilter implements FileFilter {
    @Override
    public boolean accept(File pathname){
        //returns true or false whether filename that contains substring .json
        return pathname.getAbsolutePath().toLowerCase().endsWith(".json");
    }
}
