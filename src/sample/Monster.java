package sample;

import java.util.ArrayList;

/**
 * A monster
 * Created by Tanner on 5/3/2017.
 */
public class Monster extends Combatant{
    private String id;
    private String description;
    private int ac;
    private int hp;
    private int level;
    private ArrayList<Attack> attacks;

    public Monster(String id, String name, String description, int ac, int hp, int level, ArrayList<Attack> attacks) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ac = ac;
        this.hp = hp;
        this.level = level;
        this.attacks = attacks;
    }

    public Monster() {
        id = "";
        name = "";
        description = "";
        ac = 0;
        hp = 0;
        level = 0;
        attacks = null;
    }

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
