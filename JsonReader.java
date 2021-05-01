package ca.cmpt213.as2;
import com.google.gson.*;
import java.io.*;
import java.util.ArrayList;


/**
 * Takes in files and empty information arrays and fills
 * them with information from the files. Information includes
 * teams, tokimon properties, and comments.
 */
public class JsonReader {
    String from, to, score, comment, extra_comments;
    public void JasonReaderFile( File file, ArrayList<String[]> information, ArrayList<ArrayList<String>> teams, ArrayList<String> names) {
        JsonParser parser = new JsonParser();
        try{
            JsonObject whole = ( JsonObject )parser.parse( new FileReader(file) ); //the whole file
            JsonArray arr = ( JsonArray )whole.get("team"); //array of the tokimon in the team
            extra_comments = whole.get("extra_comments").getAsString(); //the extra comments at the end

            for ( int i = 0; i < arr.size(); i++ ) {
                JsonObject tokimon = (JsonObject) arr.get(i);
                if ( i == 0 ) {
                    from = tokimon.get("id").getAsString();
                    to = "-";
                }
                else {
                    to = tokimon.get("id").getAsString();
                }
                JsonObject compatibility = (JsonObject) tokimon.get("compatibility");
                score = compatibility.get("score").getAsString();
                comment = compatibility.get("comment").getAsString();
                if (to.equals("-")) {
                    information.add(new String[]{"", from, to, score, comment, "", "" + extra_comments});
                    names.add(tokimon.get("name").getAsString());
                } else {
                    information.add(new String[]{"", from, to, score, comment, "", ""});
                    names.add(tokimon.get("name").getAsString());
                    int start = comment.indexOf("to '") + 4;
                    int stop = comment.indexOf("'\n");
                    String name2 = comment.substring(start,stop);
                    names.add(name2);
                }
                String temp = tokimon.get("name").getAsString();
                int index = temp.indexOf(" ");
                temp = temp.substring(0, index);
                String id = tokimon.get("id").getAsString();

                if(i == 0){ //adds the tokimon to their respective team with no duplicates
                    boolean newTeam = true;
                    for (int k = 0; k < teams.size(); k++) {
                        if (teams.get(k).contains(temp)) {
                            newTeam = false;
                        }
                    }

                    if (newTeam) {
                        teams.add(new ArrayList<String>());
                        teams.get(teams.size() - 1).add(temp);
                    }
                    for (int k = 0; k < teams.size(); k++) {
                        if (teams.get(k).contains(temp)) {
                            teams.get(k).add(id.trim());
                        }
                    }
                }
            }
        }
        catch(Exception e){
            System.out.println("Bad JSON file format or missing required fields in file:");
            System.out.println(file);
            System.exit(-1);
        }
        return;
    }

    public File findJsons(File file, ArrayList<File> jsonFiles) { //looks through the files to make sure they are there
        if (file.isDirectory()) {
            File[]  newFiles = file.listFiles();
            for(File test : newFiles){
                File found = findJsons(test,jsonFiles);
                if(new JsonFilter().accept(test)) {
                    jsonFiles.add(found);
                    return found;
                }
            }
        }
        else{
            if(new JsonFilter().accept(file)) {
                return file;
            }
        }
        return null;
    }

}
