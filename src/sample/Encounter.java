package sample;

import java.util.*;

/**
 * An Encounter
 * Created by Tanner on 5/3/2017.
 */
public class Encounter {
    //id the encounter is refered to with
    private String id;
    //name of the encounter
    private String name;
    //description printed when encounter starts
    private String description;
    //array of monsters in the encounter
    private ArrayList<Monster> monsters;

    //sorted arraylist of combatants based on turns,
    //only populated once encounter is active
    private ArrayList<Combatant> turnOrder;

    //states whether the encounter is active or not
    private boolean active;

    //keeps track of the number of encounters merged
    private int encountersMerged;

    /*
    Creates an encounter with the given parameters
     */
    public Encounter(String id, String name, String description, ArrayList<Monster> monsters){
        this.id = id;
        this.name = name;
        this.description = description;
        this.monsters = monsters;
        turnOrder = new ArrayList<>();

        //declare active as false orignally
        active = false;
        encountersMerged = 0;
    }

    /*
    Returns the string version of the encounter
     */
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Name : " + name + '\n');
        sb.append("Description : " + description + '\n' + '\n');
        sb.append("Monsters" + '\n');
        for (Monster m : monsters){
            sb.append(m.toString() + '\n');
        }

        //TODO add functionality to client to handle this
        if (active){
            sb.append("Turn Order" + '\n');
            for (Combatant c : turnOrder){
                sb.append(c.name + '\n');
            }
            sb.append("Current Turn : " + turnOrder.get(0).name);
        }

        return sb.toString();
    }

    //get the id of the encounter
    public String getId() {
        return id;
    }

    //return all monsters in the encounter
    public ArrayList<Monster> getMonsters() {
        return monsters;
    }

    /*
    Merge this encounter with another one.
    Used when substages that contain encounters
    are revealed
     */
    public String merge(Encounter encounter) {
        //increment merge counter
        encountersMerged++;
        //for every monster
        for (Monster m : encounter.getMonsters()){
            //check if there is a conflict, if not add it
            if (!conflict(m, monsters)){
                monsters.add(m);

                //else change the name of the monster to be added
            } else {
                m.setName((m.getName() + "-" + encountersMerged));
                monsters.add(m);
            }
        }

        String res = null;
        //if the encounter is active
        if (active){
            StringBuilder sb = new StringBuilder();

            //return the joined the encounter message for each monster
            for (Monster m : encounter.monsters){
                sb.append(m.getName() + " has joined the encounter!" + '\n');
                //create dummy round end to find index to add
                Combatant roundEnd = new Combatant();
                roundEnd.name = "End of Round";
                //add each monster after the roundEnd combatant
                turnOrder.add(turnOrder.indexOf(roundEnd), m);
            }

            return sb.toString();
        }


        return res;
    }

    /*
    Checks if there is already a monster in the encounter with the same
    name as the entered monster
     */
    private static boolean conflict(Monster m, ArrayList<Monster> monsters) {
        for (Monster mon : monsters){
            if (m.getName().equals(mon.getName())){
                return true;
            }
        }
        return false;
    }

    //get description of encounter
    public String getDescription() {
        return description;
    }

    /*
    Enters players into the encounter at initative value
    spots
     */
    public String enter(Vector<DMServer.Player> players) {
        //set active to true
        active = true;
        //create string builder
        StringBuilder sb = new StringBuilder();
        sb.append("Rolling Initiative" + '\n');

        //roll initative for each player
        for (DMServer.Player p : players){
            int roll = Constants.roll(Constants.dice.d20);
            //add to the string builder
            sb.append(p.character.getName() + " rolled a " + roll + "." + '\n');
            p.roll = 20 - roll;
            p.name = p.character.getName();
            turnOrder.add(p);
        }

        //for each monster roll iniative and add to turn order
        for (Monster m : monsters){
            int roll = Constants.roll(Constants.dice.d20);
            sb.append(m.getName() + " rolled a " + roll + "." + '\n');
            m.roll = 20 - roll;
            turnOrder.add(m);
        }

        //sort the turn order
        Collections.sort(turnOrder);

        //print out the final turn order
        sb.append("The final turn order is : ");
        for (Combatant c : turnOrder){
            if (!c.roundEnd)
                sb.append(c.name + ", ");
        }
        sb.delete(sb.length()-2, sb.length());

        //add round end to the turn order
        Combatant roundEnd = new Combatant();
        roundEnd.name = "End of Round";
        roundEnd.roundEnd = true;
        turnOrder.add(roundEnd);

        return sb.toString();
    }

    /*
    Enters the players into the encounter at a 10
    disadvantage to initaitive
     */
    public String enterBack(Vector<DMServer.Player> players) {
        //turn active to true
        active = true;

        //init string builder
        StringBuilder sb = new StringBuilder();
        sb.append("You were surprised! (-10 initiative)" + '\n');
        sb.append("Rolling Initiative" + '\n');

        //for each player roll initiative
        for (DMServer.Player p : players){
            int roll = Constants.roll(Constants.dice.d20);
            sb.append(p.character.getName() + " rolled a " + roll + "." + '\n');
            //roll equals 20 - roll
            p.roll = 20 - roll;
            p.name = p.character.getName();
            turnOrder.add(p);
        }

        //roll initative for each monster
        for (Monster m : monsters){
            int roll = Constants.roll(Constants.dice.d20);
            sb.append(m.getName() + " rolled a " + roll + "." + '\n');
            //roll is 10 - given roll as monsters have advantage
            m.roll = 10 - roll;
            turnOrder.add(m);
        }

        //sort the turn order
        Collections.sort(turnOrder);
        sb.append("The final turn order is : ");
        for (Combatant c : turnOrder){
            sb.append(c.name + ", ");
        }
        sb.delete(sb.length()-2, sb.length());

        //add round end to the turn order
        Combatant roundEnd = new Combatant();
        roundEnd.name = "End of Round";
        roundEnd.roundEnd = true;
        turnOrder.add(roundEnd);

        return sb.toString();
    }

    /*
    Add players with a 10 advantage to initative
    compared to the monsters
     */
    public String enterFront(Vector<DMServer.Player> players) {
        //set active to true
        active = true;

        //create string builder
        StringBuilder sb = new StringBuilder();
        sb.append("You surprised them! (+10 initiative)" + '\n');
        sb.append("Rolling Initiative" + '\n');

        //roll for each player
        for (DMServer.Player p : players){
            int roll = Constants.roll(Constants.dice.d20);
            sb.append(p.character.getName() + " rolled a " + roll + "." + '\n');
            p.roll = 20 - roll;
            p.name = p.character.getName();
            turnOrder.add(p);
        }

        //roll for each monster, out of 30
        for (Monster m : monsters){
            int roll = Constants.roll(Constants.dice.d20);
            sb.append(m.getName() + " rolled a " + roll + "." + '\n');
            m.roll = 30 - roll;
            turnOrder.add(m);
        }

        //sort final turn order
        Collections.sort(turnOrder);
        sb.append("The final turn order is : ");
        for (Combatant c : turnOrder){
            sb.append(c.name + ", ");
        }
        sb.delete(sb.length()-2, sb.length());

        //add round end to the turn order
        Combatant roundEnd = new Combatant();
        roundEnd.name = "End of Round";
        roundEnd.roundEnd = true;
        turnOrder.add(roundEnd);

        return sb.toString();
    }

    public boolean isActive() {
        return active;
    }
}
