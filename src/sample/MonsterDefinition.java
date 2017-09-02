package sample;

import java.util.ArrayList;

/**
 * A monster
 * Created by Tanner on 5/3/2017.
 */
public class MonsterDefinition {
    private String id;
    private String name;
    private String description;
    private int ac;
    private int numDie;
    private int hp;
    private Constants.dice die;
    private int level;
    private ArrayList<Attack> attacks;

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

    public Monster createMonster(ArrayList<Monster> created){
        int count = 1;
        for (int i = 0; i < created.size(); i++){
            if (created.get(i).getId().equals(id)){
                count ++;
            }
        }

        int totHp = Constants.rollDice(die, numDie, numDie) + hp;

        return new Monster(id, (name + " " + count), description,
                ac, totHp, level, attacks);
    }

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
