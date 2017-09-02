package sample;

/**
 * An attack
 * Created by Tanner on 5/3/2017.
 */
public class Attack {
    private String id;
    private String name;
    private int hit;
    private int numDie;
    private Constants.dice die;
    private int damageBon;

    public Attack(String id, String name, String hit, String dd, String dam){
        this.id = id;
        this.name = name;
        this.hit = Integer.parseInt(hit);
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
