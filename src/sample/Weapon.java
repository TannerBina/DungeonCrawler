package sample;

import java.io.PrintWriter;

/**
 * Holds a weapon that can be used
 * in combat by a pc
 *
 * Created by Tanner on 1/30/2017.
 */
public class Weapon {
    //the weapon damage type
    private Constants.weapontypes type;

    //the die that is used for the weapon
    private Constants.dice die;
    //number of dice rolled for the weapon
    private int numDie;
    //the name
    private String name;
    //any magic bonuses
    private int bonus;
    //whether it is a finess weapon or not
    private boolean fine;

    /*
    Creates a populated weapon with inputted params
     */
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
