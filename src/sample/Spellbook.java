package sample;

import java.util.ArrayList;

/**
 * Created by Tanner on 2/13/2017.
 *
 * Holds Spells for a character.
 */
public class Spellbook {

    private boolean activated;
    private int[] spellSlots;
    private int[] slotsRemaining;
    private ArrayList<ArrayList<String>> spellsKnown;

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

    public void activate(){
        spellsKnown.clear();
        for (int i = 0; i < 10; i++){
            spellsKnown.add(new ArrayList<>());
        }
        activated = true;
    }

    public void setSpellsKnown(int level, Constants.classes c){
        switch(c){
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
            default:
                spellSlots = Constants.SPELL_SLOTS[level-1];
                slotsRemaining = Constants.SPELL_SLOTS[level-1];
                break;
        }
    }

    public ArrayList<ArrayList<String>> getSpellsKnown(){
        return spellsKnown;
    }

    public void addSpell(int level, String spell){
        spellsKnown.get(level).add(spell);
    }

    public void removeSpell(int level, String spell){
        spellsKnown.get(level).remove(spell);
    }

    public boolean isActivated(){
        return activated;
    }

    public void deactivate() {
        activated = false;
        spellsKnown.clear();
    }
}
