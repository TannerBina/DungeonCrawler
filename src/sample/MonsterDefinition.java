package sample;

import java.util.ArrayList;

/**
 * The definition of a monster.
 * Used to create individual instances
 * of monsters.
 * Created by Tanner on 5/3/2017.
 */
public class MonsterDefinition {

    //id name and description of the definition
    private String id;
    private String name;
    private String description;
    //armor class
    private int ac;
    //number of hit dice
    private int numDie;
    //hit point bonus
    private int hp;
    //the dice used for hd
    private Constants.dice die;

    //level and attacks
    private int level;
    private ArrayList<Attack> attacks;

    /*
    Creates a monster definition with given parameters
     */
    public MonsterDefinition(String id, String name, String description, String ac,
                             String hd, String hp, String level, ArrayList<Attack> attacks){
        this.id = id;
        this.name = name;
        this.description = description;
        this.ac = Integer.parseInt(ac);
        this.numDie = Integer.parseInt(hd.substring(0, hd.indexOf('d')));
        this.die = Constants.getDie(hd.substring(hd.indexOf('d'), hd.length()));
        this.hp = Integer.parseInt(hp);
        this.level = Integer.parseInt(level);
        this.attacks = attacks;
    }

    /*
    Creates a monster from the definition.
    Inputted a list of previously created monsters to avoid
    name duplication
     */
    public Monster createMonster(ArrayList<Monster> created){
        //set count for numbers of same monsters
        //already created
        int count = 1;
        for (int i = 0; i < created.size(); i++){
            if (created.get(i).getId().equals(id)){
                count ++;
            }
        }

        //gets the total hit points the monster has
        int totHp = Constants.rollDice(die, numDie, numDie) + hp;

        //returns the new monster instance
        return new Monster(id, (name + " " + count), description,
                ac, totHp, level, attacks);
    }

    /*
    returns the monster as a string
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID : " + id + '\n');
        sb.append("NAME : " + name + '\n');
        sb.append("DES : " + description + '\n');
        sb.append(("AC : " + ac) + '\n');
        sb.append((("HD : " + numDie) + die) + '\n');
        sb.append(("HP : " + hp) + '\n');
        sb.append(("LEVEL : " + level) + '\n');
        for (Attack a : attacks) {
            sb.append(a.toString() + '\n');
        }
        return sb.toString();

    }

    public String getId() {
        return id;
    }
}
