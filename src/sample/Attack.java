package sample;

/**
 * An attack for a monster or npc character
 * Created by Tanner on 5/3/2017.
 */
public class Attack {
    //the id and name
    private String id;
    private String name;

    //the bonus to hit the attack has
    private int hit;
    //number of dice the attack has
    private int numDie;
    //die it uses
    private Constants.dice die;
    //bonus damage to dice
    private int damageBon;

    /*
    Creates and populates an attack for a monster
     */
    public Attack(String id, String name, String hit, String dd, String dam){
        this.id = id;
        this.name = name;
        this.hit = Integer.parseInt(hit);
        //get the number of dice and the type
        String s = dd.substring(0, dd.indexOf('d'));
        this.numDie = Integer.parseInt(s);
        s = dd.substring(dd.indexOf('d'), dd.length());
        this.die = Constants.getDie(s);
        this.damageBon = Integer.parseInt(dam);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("ID : " + id + '\n');
        sb.append("Name : " + name + '\n');
        sb.append(("Hit : " + hit) + '\n');
        sb.append((("DD : " + numDie) + die) + '\n');
        sb.append("DAM : " + damageBon);
        return sb.toString();
    }

    public String getId() {
        return id;
    }
}
