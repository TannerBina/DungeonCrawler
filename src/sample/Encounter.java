package sample;

import java.util.*;

/**
 * An Encounter
 * Created by Tanner on 5/3/2017.
 */
public class Encounter {
    private String id;
    private String name;
    private String description;
    private ArrayList<Monster> monsters;

    private ArrayList<Combatant> turnOrder;

    private boolean active;

    private int encountersMerged;

    public Encounter(String id, String name, String description, ArrayList<Monster> monsters){
        this.id = id;
        this.name = name;
        this.description = description;
        this.monsters = monsters;
        turnOrder = new ArrayList<>();
        active = false;
        encountersMerged = 0;
    }

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

    public String getId() {
        return id;
    }

    public ArrayList<Monster> getMonsters() {
        return monsters;
    }

    public String merge(Encounter encounter) {
        encountersMerged++;
        for (Monster m : encounter.getMonsters()){
            if (!conflict(m, monsters)){
                monsters.add(m);
            } else {
                m.setName((m.getName() + "-" + encountersMerged));
            }
        }

        String res = null;
        if (active){
            StringBuilder sb = new StringBuilder();
            for (Monster m : encounter.monsters){
                sb.append(m.getName() + " has joined the encounter!" + '\n');
                Combatant roundEnd = new Combatant();
                roundEnd.name = "End of Round";
                turnOrder.add(turnOrder.indexOf(roundEnd), m);
            }

            for (Combatant c : turnOrder){
                if (!c.roundEnd)
                    sb.append(c.name + ", ");
            }
            sb.delete(sb.length()-2, sb.length());
            return sb.toString();
        }


        return res;
    }

    private boolean conflict(Monster m, ArrayList<Monster> monsters) {
        for (Monster mon : monsters){
            if (m.getName().equals(mon.getName())){
                return true;
            }
        }
        return false;
    }

    public String getDescription() {
        return description;
    }

    public String enter(Vector<DMServer.Player> players) {
        active = true;
        StringBuilder sb = new StringBuilder();
        sb.append("Rolling Initiative" + '\n');
        for (DMServer.Player p : players){
            int roll = Constants.roll(Constants.dice.d20);
            sb.append(p.character.getName() + " rolled a " + roll + "." + '\n');
            p.roll = 20 - roll;
            p.name = p.character.getName();
            turnOrder.add(p);
        }
        for (Monster m : monsters){
            int roll = Constants.roll(Constants.dice.d20);
            sb.append(m.getName() + " rolled a " + roll + "." + '\n');
            m.roll = 20 - roll;
            turnOrder.add(m);
        }
        Collections.sort(turnOrder);
        Combatant roundEnd = new Combatant();
        roundEnd.name = "End of Round";
        roundEnd.roundEnd = true;
        turnOrder.add(roundEnd);
        sb.append("The final turn order is : ");
        for (Combatant c : turnOrder){
            if (!c.roundEnd)
                sb.append(c.name + ", ");
        }
        sb.delete(sb.length()-2, sb.length());
        return sb.toString();
    }

    public String enterBack(Vector<DMServer.Player> players) {
        active = true;
        StringBuilder sb = new StringBuilder();
        sb.append("You were surprised! (-10 initiative)" + '\n');
        sb.append("Rolling Initiative" + '\n');
        for (DMServer.Player p : players){
            int roll = Constants.roll(Constants.dice.d20);
            sb.append(p.character.getName() + " rolled a " + roll + "." + '\n');
            p.roll = 20 - roll;
            p.name = p.character.getName();
            turnOrder.add(p);
        }
        for (Monster m : monsters){
            int roll = Constants.roll(Constants.dice.d20);
            sb.append(m.getName() + " rolled a " + roll + "." + '\n');
            m.roll = 10 - roll;
            turnOrder.add(m);
        }
        Collections.sort(turnOrder);
        sb.append("The final turn order is : ");
        for (Combatant c : turnOrder){
            sb.append(c.name + ", ");
        }
        sb.delete(sb.length()-2, sb.length());
        return sb.toString();
    }

    public String enterFront(Vector<DMServer.Player> players) {
        active = true;
        StringBuilder sb = new StringBuilder();
        sb.append("You surprised them! (+10 initiative)" + '\n');
        sb.append("Rolling Initiative" + '\n');
        for (DMServer.Player p : players){
            int roll = Constants.roll(Constants.dice.d20);
            sb.append(p.character.getName() + " rolled a " + roll + "." + '\n');
            p.roll = 20 - roll;
            p.name = p.character.getName();
            turnOrder.add(p);
        }
        for (Monster m : monsters){
            int roll = Constants.roll(Constants.dice.d20);
            sb.append(m.getName() + " rolled a " + roll + "." + '\n');
            m.roll = 30 - roll;
            turnOrder.add(m);
        }
        Collections.sort(turnOrder);
        sb.append("The final turn order is : ");
        for (Combatant c : turnOrder){
            sb.append(c.name + ", ");
        }
        sb.delete(sb.length()-2, sb.length());
        return sb.toString();
    }

    public boolean isActive() {
        return active;
    }
}
