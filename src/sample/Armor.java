package sample;

/**
 * Holds a piece of armor
 * Created by Tanner on 1/30/2017.
 */
public class Armor {

    private int ac;
    private String name;
    private boolean shield;

    public Armor(String name, int ac, boolean shield){
        this.ac = ac;
        this.name = name;
        this.shield = shield;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(name + " ");
        sb.append(ac + " ");
        sb.append(shield);
        return sb.toString();
    }

    public int getAc() {
        return ac;
    }

    public String getName() {
        return name;
    }

    public boolean isShield() {
        return shield;
    }
}
