package sample;

/**
 * Someone who can take a turn in combat
 * monsters and players both extend this class
 * Created by Tanner on 5/15/2017.
 */
public class Combatant implements Comparable<Combatant> {
    //the initiative roll
    public int roll = 0;
    public String name;
    //whether they are the end of the round
    public boolean roundEnd;

    public int compareTo(Combatant other){
        return roll - other.roll;
    }

    public boolean equals(Object other){
        Combatant c = (Combatant)(other);
        return name.equals(c.name);
    }
}
