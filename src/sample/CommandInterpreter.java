package sample;

import java.util.ArrayList;

/**
 * Attempts to find the best match for a command
 * Created by Tanner on 5/2/2017.
 */
public class CommandInterpreter {

    private static String[] commands = {
            "$examine",

            "@reveal", "@transfer", "@encounterfront", "@encounter", "@encounterback"
    };

    public static String interpret(String attemptCommand) {
        attemptCommand = attemptCommand.toLowerCase();
        int[] distances = new int[commands.length];
        for (int i = 0; i < commands.length; i++){
            if (commands[i].equals(attemptCommand)){
                return commands[i];
            }
        }
        for (int i = 0; i < commands.length; i++){
            distances[i] = distanceFrom(attemptCommand, commands[i]);
        }

        int cutoff = (int)((attemptCommand.length()*.2) + .99);
        int index = minIndex(distances);
        if (distances[index] <= cutoff){
            return commands[index];
        }
        return null;
    }

    private static int minIndex(int[] distances) {
        int min = 0;
        for (int i = 1; i < distances.length; i++){
            if (distances[i] < distances[min]){
                min = i;
            }
        }
        return min;
    }

    private static int distanceFrom(String attemptCommand, String command) {
        int[][] values = new int[attemptCommand.length()+1][command.length()+1];
        for (int i = 0; i <= attemptCommand.length(); i++){
            values[i][0] = i;
        }
        for (int i = 0; i <= command.length(); i++){
            values[0][i] = i;
        }

        for (int i = 0; i < attemptCommand.length(); i++){
            for (int j = 0; j < command.length(); j++){
                if (attemptCommand.charAt(i) == command.charAt(j)){
                    values[i+1][j+1] = values[i][j];
                } else {
                    values[i+1][j+1] = min(values[i][j]+1, values[i][j+1]+1, values[i+1][j]+1);
                }
            }
        }

        return values[attemptCommand.length()][command.length()];
    }

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

    public static Obj interpretObjects(String s, ArrayList<Obj> objects) {
        int[] distances = new int[objects.size()];
        for (int i = 0; i < objects.size(); i++){
            if (objects.get(i).getName().equals(s)){
                return objects.get(i);
            }
        }
        for (int i = 0; i < distances.length ; i++){
            distances[i] = distanceFrom(s, objects.get(i).getName());
        }
        int index = minIndex(distances);
        return objects.get(index);
    }

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
        return links.get(index);
    }

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

        return substages.get(index);
    }
}
