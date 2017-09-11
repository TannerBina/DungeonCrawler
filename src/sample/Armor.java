package sample;

/**
 * Holds a piece of armor that a
 * PC an equip to increase his defense
 * Created by Tanner on 1/30/2017.
 */
public class Armor {

    //armor class of the armor
    private int ac;
    //name of the armor
    private String name;
    //whether or not the armor is a shield
    private boolean shield;

    /*
    Constructor for a piece of armor
     */
    public Armor(String name, int ac, boolean shield){
        this.ac = ac;
        this.name = name;
        this.shield = shield;
    }

    /*
    Converts armor to a string
     */
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
