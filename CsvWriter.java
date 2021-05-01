package ca.cmpt213.as2;
import com.opencsv.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 *Takes in the information that was sorted in the processor class
 * and writes it onto a csv file in proper order
 */
public class CsvWriter {
    public void csvOutput (File file, ArrayList<String[]> information, ArrayList<ArrayList<String>> teams) throws IOException {

        FileWriter output = new FileWriter(file);
        CSVWriter writer = new CSVWriter(output);
        String[] header = {"Team#", "From Toki", "To Toki","","Score","Comment","","Extra"};

        writer.writeNext(header); //writes the header

        for(int k = 0; k < teams.size(); k++){
            String[] team = {"Team"+(k+1)};
            writer.writeNext(team); //writes the team number
            for(int j = 0; j < information.size(); j++){
                if(teams.get(k).contains(information.get(j)[1].trim())){
                    writer.writeNext(information.get(j));
                }
            }
            writer.writeNext(new String[]{""});
        }
        writer.close();
    }

    public void sort(ArrayList<String[]> information) {
        Collections.sort(information, new Comparator<String[]>() { //sorts the tokimon to be in order
            @Override
            public int compare(String[] strings, String[] t1) {
                if (strings[1].compareTo(t1[1]) == 0) {
                    if (strings[1].startsWith("-") && !t1[1].startsWith("-")) {
                        return strings[2].compareTo(t1[2]) - 100;
                    } else if (t1[2].startsWith("-") && !strings[1].startsWith("-")) {
                        return strings[2].compareTo(t1[2]) - 100;
                    } else {
                        return strings[2].compareTo(t1[2]);
                    }
                }
                return strings[1].compareTo(t1[1]);
            }
        });
    }
}
