package sample;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Attempts to find the best match for a command
 * using a nearest neighbor algorithm
 * Created by Tanner on 5/2/2017.
 */
public class CommandInterpreter {

    /*
    Commands that are handled by the interpreter, does not include init commands
    as they are hard coded
     */
    private static String[] commands = {
            "$examine",

            "@reveal", "@transfer", "@encounterfront", "@encounter", "@encounterback"
    };

    /*
    Input an attempted command, output correct string for command
     */
    public static String interpret(String attemptCommand) {
        //switch command to lower case
        attemptCommand = attemptCommand.toLowerCase();

        //get distance from entered to others
        int[] distances = new int[commands.length];
        for (int i = 0; i < commands.length; i++){
            if (commands[i].equals(attemptCommand)){
                return commands[i];
            }
        }
        for (int i = 0; i < commands.length; i++){
            distances[i] = distanceFrom(attemptCommand, commands[i]);
        }

        //if at least 20% correct, use it
        int cutoff = (int)((attemptCommand.length()*.2) + .99);

        //return the correct command
        int index = minIndex(distances);
        if (distances[index] <= cutoff){
            return commands[index];
        }
        return null;
    }

    /*
    Find the min index in the distances
     */
    private static int minIndex(int[] distances) {
        int min = 0;
        for (int i = 1; i < distances.length; i++){
            if (distances[i] < distances[min]){
                min = i;
            }
        }
        return min;
    }

    /*
    Checks the distance between two commands.
    Dynamic programing algorithm.
     */
    private static int distanceFrom(String attemptCommand, String command) {
        //init 2d array
        int[][] values = new int[attemptCommand.length()+1][command.length()+1];
        //init start of array
        for (int i = 0; i <= attemptCommand.length(); i++){
            values[i][0] = i;
        }
        for (int i = 0; i <= command.length(); i++){
            values[0][i] = i;
        }

        /*
        populate
         */
        for (int i = 0; i < attemptCommand.length(); i++){
            for (int j = 0; j < command.length(); j++){
                //if they match inherit diagnal
                if (attemptCommand.charAt(i) == command.charAt(j)){
                    values[i+1][j+1] = values[i][j];

                    //else inherit from nearby or diagnal
                } else {
                    values[i+1][j+1] = min(values[i][j]+1, values[i][j+1]+1, values[i+1][j]+1);
                }
            }
        }

        return values[attemptCommand.length()][command.length()];
    }

    /*
    Return the min of three numbers
     */
    private static int min(int one, int two, int three){
        if (one <= two && one <= three){
            return one;
        }
        if (two <= one && two <= three){
            return two;
        }
        if (three <= one && three <= two){
            return three;
        }
        System.err.println("Could not find minimum of three numbers");
        return 0;
    }

    /*
    Given an array of objects, finds the closest object to the given string.
     */
    public static Obj interpretObjects(String s, ArrayList<Obj> objects) {
        s = s.toLowerCase();
        //find all distances
        int[] distances = new int[objects.size()];
        for (int i = 0; i < objects.size(); i++){
            if (objects.get(i).getName().toLowerCase().equals(s)){
                return objects.get(i);
            }
        }
        for (int i = 0; i < distances.length ; i++){
            distances[i] = distanceFrom(s, objects.get(i).getName().toLowerCase());
        }
        int index = minIndex(distances);

        //if the closest is still far away try a name partial match
        if (distances[index] > (int)(objects.get(index).getName().length() * .2 + .99)){
            //save old index
            int oldIndex = index;

            //scan through each part of the string
            Scanner scan = new Scanner(s);
            while(scan.hasNext()){
                String part = scan.next();

                //check if matches with any of the objects
                for (int i = 0; i < objects.size(); i++){
                    Obj o = objects.get(i);
                    if (o.getName().toLowerCase().contains(part)){
                        if (oldIndex == index || distances[i] < distances[index]){
                            index = i;
                        }
                    }
                }
            }
        }

        return objects.get(index);
    }

    /*
    Get the closest link to the given string
     */
    public static Link interpretLinks(String s, ArrayList<Link> links){
        int[] distances = new int[links.size()];
        for (int i = 0; i < links.size(); i++){
            if (links.get(i).end.getId().equals(s)){
                return links.get(i);
            }
        }
        for (int i = 0; i < distances.length; i++){
            distances[i] = distanceFrom(s, links.get(i).end.getId());
        }
        int index = minIndex(distances);

        //if the closest is still far away try a name partial match
        if (distances[index] > (int)(links.get(index).end.getId().length() * .2 + .99)) {
            //save old index
            int oldIndex = index;

            //scan through each part of the string
            Scanner scan = new Scanner(s);
            while (scan.hasNext()) {
                String part = scan.next();

                //check if matches with any of the objects
                for (int i = 0; i < links.size(); i++) {
                    Link o = links.get(i);
                    if (o.end.getId().toLowerCase().contains(part)) {
                        if (oldIndex == index || distances[i] < distances[index]) {
                            index = i;
                        }
                    }
                }
            }
        }

        return links.get(index);
    }

    /*
    Get the closest matching substage
     */
    public static Stage interpretSubStage(String s, ArrayList<Stage> substages) {
        int[] distances = new int[substages.size()];
        for (int i = 0; i < substages.size(); i++){
            if (substages.get(i).getId().equals(s)){
                return substages.get(i);
            }
        }
        for (int i = 0; i < distances.length; i++){
            distances[i] = distanceFrom(s, substages.get(i).getId());
        }
        int index = minIndex(distances);

        if (substages.size() == 0) return null;

        //if over 20% from match, do not return stage
        if (distances[index] > (int)(substages.get(index).getId().length() * .2 + .99)) {
            return null;
        }

        return substages.get(index);
    }
}
