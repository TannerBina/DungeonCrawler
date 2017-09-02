package sample;

import java.io.PrintWriter;

/**
 * Holds a weapon
 * Created by Tanner on 1/30/2017.
 */
public class Weapon {
    private Constants.weapontypes type;
    private Constants.dice die;
    private int numDie;
    private String name;
    private int bonus;
    private boolean fine;

    public Weapon(String name, int bonus, Constants.weapontypes type, int numDie, Constants.dice die, boolean fine){
        this.die = die;
        this.numDie = numDie;
        this.name = name;
        this.bonus = bonus;
        this.fine = fine;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getBonus() {
        return bonus;
    }

    public int getDam(){
        return Constants.rollDice(die, numDie, numDie) + bonus;
    }

    public boolean isFine() {
        return fine;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name + " ");
        sb.append(bonus + " ");
        sb.append(type + " ");
        sb.append(numDie + " ");
        sb.append(die + " ");
        sb.append(fine);
        return sb.toString();
    }

    public int getNumDie() {
        return numDie;
    }

    public Constants.dice getDie() {
        return die;
    }
}
