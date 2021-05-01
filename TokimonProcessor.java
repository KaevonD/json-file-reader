package ca.cmpt213.as2;
import java.sql.SQLOutput;
import java.util.*;
import java.io.*;


/**
 * The tokimon processor class checks the inputs it is
 * given and makes sure the correct arguments were used.
 * It sends the given files to the JsonReader class and
 * gets the information needed back.
 * It also catches many errors that stop the program
 * from working properly
 */
public class TokimonProcessor {

    public static void main(String[] args) throws FileNotFoundException, IOException {

        if(args.length != 2){ //makes sure they put 2 arguments
            System.out.println("You need 2 arguments, the .json file directory and the path you want your .csv file to go");
            System.exit(-1);
        }

        File input = new File(args[0]);
        File test_output = new File(args[1]);
        ArrayList<ArrayList<String>> teams = new ArrayList<ArrayList<String>>(); //teams and their members

        if(!input.isDirectory()){
            System.out.println("this input directory does not exist"); //exits if given nonexistant input
            System.exit(-1);
        }

        if(!test_output.isDirectory()){
            System.out.println("this output directory does not exist"); //exits if given nonexistant output
            System.exit(-1);
        }

        File output = new File(test_output + "/team_info.csv");
        JsonReader test = new JsonReader();
        ArrayList<File> allFiles = new ArrayList<File>();

        test.findJsons(input, allFiles);
        if(allFiles.size() == 0){
            System.out.println("there are no json files");
            System.exit(-1);
        }

        ArrayList<String[]> information = new ArrayList<String[]>();
        ArrayList<String> names = new ArrayList<String>();
        for(File x : allFiles){
            test.JasonReaderFile(x,information, teams, names);
        }

        //first error checker, makes sure that there are no tokis in more than one team
        for(int i = 0; i < teams.size(); i++){
            for(int j = 1; j < teams.get(i).size(); j++){
                for(int k = 0; k < teams.size(); k++){
                    if(k != i){
                        if(teams.get(k).contains(teams.get(i).get(j))){
                            System.out.println("Error: a tokimon is mentioned in multiple teams");
                            System.exit(-1);
                        }
                    }
                }
            }
        }

        //second error checker. makes sure that there are no tokimon's without their own json file
        boolean hasJson = false;
        for(int i = 0; i < information.size(); i++){
            hasJson = false;

            if(!(information.get(i)[2].equals("-"))){
                for(int j = 0; j < teams.size(); j++){
                    if(teams.get(j).contains(information.get(i)[2])){
                        hasJson = true;
                        break;
                    }
                }
            }
            else{
                hasJson = true;
            }
            if(!hasJson){
                System.out.println("Error: there is a tokimon that does not have its own json file");
                System.exit(-1);
            }
        }

        //third error checker, makes sure that ther are no tokimon that do not include one or more of their team members
        ArrayList<String> mentions = new ArrayList<String>();
        for(int i = 0; i < teams.size(); i ++) {
            for(int j = 1; j < teams.get(i).size(); j++){
                for(int k = 0; k < information.size(); k++){
                    if(information.get(k)[1].trim().equals(teams.get(i).get(j))){
                        mentions.add(information.get(k)[2]);
                    }
                }
                for(int k = 1; k < teams.get(i).size(); k++){
                    if(!(teams.get(i).get(k).equals(teams.get(i).get(j)))){
                        if(!mentions.contains(teams.get(i).get(k))){
                            System.out.println("Error: there is a tokimon that does not mention its team member");
                            System.exit(-1);
                        }
                    }
                }
            }
        }

        //fourth error checker, checks to see that all of the tokimon have the same properties as initially given
        int counter = 0;
        int teamNumber = 0;
        int innerTeam = 0;
        Collections.sort(names);
        String currentToki = names.get(0);
        for(int i = 0; i < names.size(); i++){
            if(names.get(i).equals(currentToki)){
                counter++;
            }
            else{
                if(counter != ((teams.get(teamNumber).size()-1)*2)-1){
                    System.out.println("Error: a tokimon's property does not match its existing recorded properties");
                    System.exit(-1);
                }
                counter = 1;
                innerTeam++;
                if(innerTeam == teams.get(teamNumber).size()-1){
                    innerTeam = 0;
                    teamNumber++;
                }
                currentToki = names.get(i);
            }
        }

        CsvWriter result = new CsvWriter();
        result.sort(information);

        for( int i = 0; i < teams.size(); i++) {
            Collections.sort(teams.get(i));
        }
        result.csvOutput(output,information,teams);

    }
}
