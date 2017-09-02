package sample;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Parses commands from text
 *
 * Created by Tanner on 2/14/2017.
 */
public class CommandParser {

    public static ArrayList<Command> parseCommands(String string){
        StringBuilder sb = new StringBuilder();
        Scanner s = new Scanner(string);
        ArrayList<Command> result = new ArrayList<>();

        String next = s.next();
        if (next.charAt(0) != '$' && next.charAt(0) != '@'){
            System.err.println("Not valid command form.");
            return null;
        }
        sb.append(next);
        sb.append(" ");

        while (s.hasNext()){
            next = s.next();
            if (next.charAt(0) != '$'){
                sb.append(next);
                sb.append(" ");
            } else {
                result.add(parseCommand(sb.toString()));
                sb = new StringBuilder();
                sb.append(next);
                sb.append(" ");
            }
        }
        result.add(parseCommand(sb.toString()));

        return result;
    }

    public static Command parseCommand(String string){
        Command res = null;
        Command.commands command = null;
        ArrayList<String> args = new ArrayList<>();

        Scanner s = new Scanner(string);
        String com = s.next();

        if (com.charAt(0) != '$' && com.charAt(0) != '@'){
            System.err.println("Not valid command form. Command: " + com);
            return res;
        }

        String prompt = null;

        com = com.substring(1, com.length());

        //These commands are all settup commands for creating a character
        switch(com){
            case "proficiencies":
                command = Command.commands.SET_PROFS;
                args = parseArgs(s, 0,",");
                break;
            case "equipment":
                command = Command.commands.SET_EQUIPMENT;
                args = parseArgs(s, 0, ",");
                break;
            case "traits":
                command = Command.commands.SET_TRAITS;
                args = parseArgs(s, 0, ",");
                break;
            case "backstory":
                command = Command.commands.SET_BACKSTORY;
                args = parseArgs(s, 1, null);
                break;
            case "feats":
                command = Command.commands.SET_FEATS;
                args = parseArgs(s, 0, ",");
                break;
            case "languages":
                command = Command.commands.SET_LANGUAGES;
                args = parseArgs(s, 0, ",");
                break;
            case "playername":
                command = Command.commands.SET_P_NAME;
                args = parseArgs(s, 1, null);
                break;
            case "name":
                command = Command.commands.SET_NAME;
                args = parseArgs(s, 1, null);
                break;
            case "spells":
                command = Command.commands.SET_SPELLS;
                args = parseArgs(s, 0, ", ");
                break;
            case "give":
                String secondCom = s.next();
                switch(secondCom){
                    case "weapon":
                        command = Command.commands.GIVE_WEAPON;
                        //name bonus type numbDie dice finess
                        args = parseArgs(s, 6, null);
                        break;
                    case "armor":
                        command = Command.commands.GIVE_ARMOR;
                        //name ac shield
                        args = parseArgs(s, 3, null);
                        break;
                }
                break;
            case "race":
                command = Command.commands.SET_RACE;
                args = parseArgs(s, 1, null);
                break;
            case "class":
                command = Command.commands.SET_CLASS;
                args = parseArgs(s, 1, null);
                break;
            case "background":
                command = Command.commands.SET_BACKGROUND;
                args = parseArgs(s, 1, null);
                break;
            case "alignment":
                command = Command.commands.SET_ALIGNMENT;
                args = parseArgs(s, 1, null);
                break;
            case "stats":
                command = Command.commands.SET_STATS;
                args = parseArgs(s, 6, null);
                break;
            case "level":
                command = Command.commands.SET_LEVEL;
                args = parseArgs(s, 1, null);
                break;
            case "hitpoints":
                command = Command.commands.SET_HITPOINTS;
                args = parseArgs(s, 1, null);
                break;
            case "currenthitpoints":
                command = Command.commands.SET_CURRENT_HITPOINTS;
                args = parseArgs(s, 1, null);
                break;
            case "basehitpoints":
                command = Command.commands.SET_BASE_HITPOINTS;
                args = parseArgs(s, 1, null);
                break;
            case "exp":
                command = Command.commands.SET_EXP;
                args = parseArgs(s, 1, null);
                break;
            case "gold":
                command = Command.commands.SET_GOLD;
                args = parseArgs(s, 1, null);
                break;
        }

        //TODO add commands for actions in the game. Add also to interpreter.
        switch (com){
            case "examine":
                command = Command.commands.EXAMINE;
                args = parseArgs(s, 1, null);
                break;
        }

        //TODO add commands for stages
        switch (com){
            case "transfer":
                command = Command.commands.TRANSFER;
                args = parseArgs(s, 1, null);
                break;
            case "reveal":
                command = Command.commands.REVEAL;
                args = parseArgs(s, 1, null);
                break;
            case "encounter":
                command = Command.commands.ENCOUNTER;
                break;
            case "encounterback":
                command = Command.commands.ENCOUNTER_BACK;
                break;
            case "encounterfront":
                command = Command.commands.ENCOUNTER_FRONT;
                break;
        }

        if (command != null){
            res = new Command(command, args, prompt);
        }


        if (res == null){
            System.err.println("Command not recognized, Command: " + com);
        }

        return res;
    }

    private static ArrayList<String> parseArgs(Scanner s, int numArgs, String deliminator){
        ArrayList<String> res = new ArrayList<>();

        if (deliminator != null){
            s.useDelimiter(deliminator);
        }

        switch (numArgs){
            case 0:
                while(s.hasNext()){
                    res.add(s.next().trim());
                }
                break;
            case 1:
                StringBuilder sb = new StringBuilder();
                while(s.hasNext()){
                    sb.append(s.next());
                    sb.append(" ");
                }
                res.add(sb.toString().trim());
                break;
            default:
                while (s.hasNext()){
                    res.add(s.next().trim());
                }
                break;
        }

        if (numArgs != 0 && numArgs != res.size()){
            System.err.println("Issue parsing args. Not enough args found. Num Found/Num Supposed: " + res.size() + "/" + numArgs);
        }

        return res;
    }
}
