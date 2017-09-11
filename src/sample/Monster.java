package sample;

import java.util.ArrayList;

/**
 * An instance of a monster definition
 * that can populate encounters in the
 * dungeons
 * Created by Tanner on 5/3/2017.
 */
public class Monster extends Combatant{
    //the id and description of the monster
    private String id;
    private String description;

    //armor class and hit points
    private int ac;
    private int hp;

    //monsters level, dictates exp given and difficulty
    private int level;
    //list of attacks a monster can make
    private ArrayList<Attack> attacks;

    /*
    A monster with the inputted parameters.
     */
    public Monster(String id, String name, String description, int ac, int hp, int level, ArrayList<Attack> attacks) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ac = ac;
        this.hp = hp;
        this.level = level;
        this.attacks = attacks;
    }

    /*
    A monster with no given parameters
     */
    public Monster() {
        id = "";
        name = "";
        description = "";
        ac = 0;
        hp = 0;
        level = 0;
        attacks = null;
    }

    /*
    Returns the string associated with the monster
     */
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("ID : " + id + '\n');
        sb.append("NAME : " + name + '\n');
        sb.append("DES : " + description + '\n');
        sb.append(("AC : " + ac) + '\n');
        sb.append(("HP : " + hp) + '\n');
        sb.append(("LEVEL : " + level) + '\n');
        for (Attack a : attacks){
            sb.append(a.toString() + '\n');
        }
        return sb.toString();

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public String getDescription() {
        return description;
    }
}
