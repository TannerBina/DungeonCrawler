package sample;

import java.util.ArrayList;

/**
 * Created by Tanner on 2/13/2017.
 *
 * Holds Spells for a character.
 * Keeps track of the spells remaining before rest and those known
 */
public class Spellbook {

    //whether the character has a spell book
    private boolean activated;
    //number of spell slots for each level
    private int[] spellSlots;
    //number of spell slots remaining at each level
    private int[] slotsRemaining;
    //list of all spells known
    private ArrayList<ArrayList<String>> spellsKnown;

    /*
    Create an uninitalized spell book
     */
    public Spellbook(){
        activated = false;
        spellsKnown = new ArrayList<>();
    }

    public int getSpellSlots(int level){
        return spellSlots[level-1];
    }

    public int getRemainingSlots(int level){
        return slotsRemaining[level-1];
    }

    /*
    Activate the spell book
     */
    public void activate(){
        //clear all spells
        spellsKnown.clear();
        //init spells known
        for (int i = 0; i < 10; i++){
            spellsKnown.add(new ArrayList<>());
        }
        //activate
        activated = true;
    }

    /*
    Set the number of spells slots with a given level and class
     */
    public void setSpellsKnown(int level, Constants.classes c){
        switch(c){
            //if semi caster class init at level-2
            case Paladin:
            case Ranger:
                if (level != 1) {
                    spellSlots = Constants.SPELL_SLOTS[level - 2];
                    slotsRemaining = Constants.SPELL_SLOTS[level - 2];
                }
                else {
                    spellSlots = new int[9];
                    slotsRemaining = new int[9];
                }
                break;
            //otherwise pull number of spells
            default:
                spellSlots = Constants.SPELL_SLOTS[level-1];
                slotsRemaining = Constants.SPELL_SLOTS[level-1];
                break;
        }
    }

    public ArrayList<ArrayList<String>> getSpellsKnown(){
        return spellsKnown;
    }

    //add a spell at the given level with the entered name
    public void addSpell(int level, String spell){
        spellsKnown.get(level).add(spell);
    }

    //remove a spell with a given string
    public void removeSpell(int level, String spell){
        spellsKnown.get(level).remove(spell);
    }

    public boolean isActivated(){
        return activated;
    }

    //deactivate the spell book
    public void deactivate() {
        activated = false;
        spellsKnown.clear();
    }
}
