package sample;

/**
 * SOmeone who can take a turn in combat
 * Created by Tanner on 5/15/2017.
 */
public class Combatant implements Comparable<Combatant> {
    public int roll = 0;
    public String name;
    public boolean roundEnd;

    public int compareTo(Combatant other){
        return roll - other.roll;
    }

    public boolean equals(Object other){
        Combatant c = (Combatant)(other);
        return name.equals(c.name);
    }
}
