package sample;

import java.util.ArrayList;

/**
 * A command to be processed.
 *
 * Included the command enum, arguments, and a prompt to be printed out
 *
 * Created by Tanner on 2/14/2017.
 */
public class Command {

    public ArrayList<String> getArgs() {
        return args;
    }

    /*
    List of all different vaible commands that can be handled
     */
    enum commands{
        //init commands
        SET_PROFS, SET_EQUIPMENT, SET_TRAITS, SET_BACKSTORY, SET_FEATS, SET_P_NAME,
        SET_NAME, SET_SPELLS, GIVE_WEAPON, GIVE_ARMOR, SET_RACE, SET_CLASS,
        SET_BACKGROUND, SET_STATS, SET_LEVEL, SET_HITPOINTS, SET_CURRENT_HITPOINTS,
        SET_BASE_HITPOINTS, SET_LANGUAGES, SET_ALIGNMENT,
        SET_EXP, SET_GOLD,

        //player commands
        EXAMINE,

        //dm commands
        TRANSFER, REVEAL, ENCOUNTER_FRONT, ENCOUNTER, ENCOUNTER_BACK
    }

    //the command and args, as well as the prompt to be returned after
    private commands command;
    private ArrayList<String> args;
    private String prompt;

    public Command(commands command, ArrayList<String> args, String prompt){
        this.command = command;
        this.args = args;
        this.prompt = prompt;
    }

    public commands getCommand(){
        return command;
    }
}
