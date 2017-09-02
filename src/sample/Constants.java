package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Arrays;
import java.util.Random;

/**
 * Holds all constants
 * Created by Tanner on 1/27/2017.
 */
public class Constants {

    //only used in NewCharController for initialize
    public static boolean LEARSPELWIN = false;

    public static final String DMWIN = "dmWindow.fxml";
    public static final String PLAYFIRS = "playerFirst.fxml";
    public static final String NEWCHAR = "newChar.fxml";
    public static final String LEARSPEL = "learnSpells.fxml";
    public static final String PLAYWIND = "playerWindow.fxml";

    public static final ObservableList<String> ALIGOPT = FXCollections.observableArrayList(
            "Lawful Good", "Lawful Neutral", "Lawful Evil",
            "Neutral Good", "True Neutral", "Neutral Evil",
            "Chaotic Good", "Chaotic Neutral", "Chaotic Evil"
    );
    public static final ObservableList<String> WEAPTYPEOPT = FXCollections.observableArrayList(
            "Slashing", "Bludgeoning", "Piercing"
    );
    public static final ObservableList<String> DIEOPT = FXCollections.observableArrayList(
            "d4", "d6", "d8", "d10", "d12", "d20", "d100"
    );
    public static final ObservableList<String> NUMBDICEOPT = FXCollections.observableArrayList(
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"
    );
    public static final ObservableList<String> CLASOPT = FXCollections.observableArrayList(
            "Barbarian", "Bard", "Cleric", "Druid",
            "Fighter", "Magic_User", "Monk", "Paladin", "Ranger",
            "Sorcerer", "Thief", "Warlock"
    );
    public static final ObservableList<String> RACEOPT = FXCollections.observableArrayList(
            "Dragonborn", "Dwarf", "Elf", "Gnome",
            "Halfling", "Half_Elf", "Half_Orc",
            "Human", "Tiefling"
    );
    public static final ObservableList<String> BACKOPT = FXCollections.observableArrayList(
            "Acolyte", "Charlatan", "Criminal",
            "Entertainer", "Folk_Hero", "Guild_Artisan",
            "Hermit", "Noble", "Outlander", "Sage",
            "Sailor", "Soldier", "Urchin"
    );

    public static final int [][] SPELL_SLOTS = new int[][]{
            {2,0,0,0,0,0,0,0,0},
            {3,0,0,0,0,0,0,0,0},
            {4,2,0,0,0,0,0,0,0},
            {4,3,0,0,0,0,0,0,0},
            {4,3,2,0,0,0,0,0,0},
            {4,3,3,0,0,0,0,0,0},
            {4,3,3,1,0,0,0,0,0},
            {4,3,3,2,0,0,0,0,0},
            {4,3,3,3,1,0,0,0,0},
            {4,3,3,3,2,0,0,0,0},
            {4,3,3,3,2,1,0,0,0},
            {4,3,3,3,2,1,0,0,0},
            {4,3,3,3,2,1,1,0,0},
            {4,3,3,3,2,1,1,0,0},
            {4,3,3,3,2,1,1,1,0},
            {4,3,3,3,2,1,1,1,0},
            {4,3,3,3,2,1,1,1,1},
            {4,3,3,3,3,1,1,1,1},
            {4,3,3,3,3,2,1,1,1},
            {4,3,3,3,3,2,2,1,1}
    };

    public static final int [] NEXT_LEVEL_EXP = new int[]{
            0,300,900,2700,6500,14000,23000,34000,48000,64000,
            85000,100000,120000,140000,165000,195000,225000,
            265000,305000,355000
    };

    enum dice {
        d4, d6, d8, d10, d12, d20, d0, d100
    }

    enum classes{
        Barbarian, Bard, Cleric, Druid, Fighter, Magic_User, Monk,
        Paladin, Ranger, Sorcerer, Thief, Warlock
    }

    enum races{
        Dragonborn, Dwarf, Elf, Gnome, Halfling, Half_Elf, Half_Orc, Human, Tiefling
    }
    enum backgrounds{
        Acolyte, Charlatan, Criminal, Entertainer, Folk_Hero,
        Guild_Artisan, Hermit, Noble, Outlander, Sage, Sailor,
        Soldier, Urchin
    }
    enum weapontypes {
        Slashing, Bludgeoning, Piercing
    }

    public static boolean getBoolean(String s){
        switch (s){
            case "true":
                return true;
            case "false":
                return false;
            default:
                System.err.println("Could not get boolean. Input: " + s);
                break;
        }
        return false;
    }

    public static dice getDie(String s){
        dice res = null;
        switch (s){
            case "d0":
                res = dice.d0;
                break;
            case "d4":
                res = dice.d4;
                break;
            case "d6":
                res = dice.d6;
                break;
            case "d8":
                res = dice.d8;
                break;
            case "d10":
                res = dice.d10;
                break;
            case "d12":
                res = dice.d12;
                break;
            case "d20":
                res = dice.d20;
                break;
            case "d100":
                res = dice.d100;
                break;
            default:
                System.err.println("Correct die not found. Die: " + s);
                break;
        }

        return res;
    }

    public static weapontypes getWeaponType(String s){
        if (s == null) return null;
        s = s.toLowerCase();

        weapontypes res = null;
        switch(s){
            case "slashing":
                res = weapontypes.Slashing;
                break;
            case "bludgeoning":
                res = weapontypes.Bludgeoning;
                break;
            case "piercing":
                res = weapontypes.Piercing;
                break;
            default:
                System.err.println("Type not found. Type: " + s);
                break;
        }
        return res;
    }

    public static classes getClass(String s){
        if (s == null) return null;

        s = s.toLowerCase();

        classes res = null;
        switch(s){
            case "barbarian":
                res = classes.Barbarian;
                break;
            case "bard":
                res = classes.Bard;
                break;
            case "cleric":
                res = classes.Cleric;
                break;
            case "druid":
                res = classes.Druid;
                break;
            case "fighter":
                res = classes.Fighter;
                break;
            case "magic_user":
                res = classes.Magic_User;
                break;
            case "monk":
                res = classes.Monk;
                break;
            case "paladin":
                res = classes.Paladin;
                break;
            case "ranger":
                res = classes.Ranger;
                break;
            case "sorcerer":
                res = classes.Sorcerer;
                break;
            case "thief":
                res = classes.Thief;
                break;
            case "warlock":
                res = classes.Warlock;
                break;
        }
        return res;
    }

    public static races getRace(String s){
        if (s == null){
            return null;
        }

        s = s.toLowerCase();

        races res = null;

        switch (s){
            case "dragonborn":
                res = races.Dragonborn;
                break;
            case "dwarf":
                res = races.Dwarf;
                break;
            case "elf":
                res = races.Elf;
                break;
            case "gnome":
                res = races.Gnome;
                break;
            case "halfling":
                res = races.Halfling;
                break;
            case "half_elf":
                res = races.Half_Elf;
                break;
            case "half_orc":
                res = races.Half_Orc;
                break;
            case "human":
                res = races.Human;
                break;
            case "tiefling":
                res = races.Tiefling;
                break;
        }

        return res;
    }

    public static backgrounds getBack(String s) {
        if (s == null) return null;
        s = s.toLowerCase();
        backgrounds res = null;
        switch (s){
            case "acolyte":
                res = backgrounds.Acolyte;
                break;
            case "charlatan":
                res = backgrounds.Charlatan;
                break;
            case "criminal":
                res = backgrounds.Criminal;
                break;
            case "entertainer":
                res = backgrounds.Entertainer;
                break;
            case "folk_hero":
                res = backgrounds.Folk_Hero;
                break;
            case "guild_artisan":
                res = backgrounds.Guild_Artisan;
                break;
            case "hermit":
                res = backgrounds.Hermit;
                break;
            case "noble":
                res = backgrounds.Noble;
                break;
            case "outlander":
                res = backgrounds.Outlander;
                break;
            case "sage":
                res = backgrounds.Sage;
                break;
            case "sailor":
                res = backgrounds.Sailor;
                break;
            case "soldier":
                res = backgrounds.Soldier;
                break;
            case "urchin":
                res = backgrounds.Urchin;
                break;
        }
        return res;
    }

    public static int rollDice(dice die, int numRolled, int numUsed){
        int[] tot = new int[numRolled];
        for (int i = 0; i < numRolled; i++){
            tot[i] = roll(die);
        }
        Arrays.sort(tot);
        int sum = 0;
        for (int i = 0; i < numUsed; i++){
            sum += tot[i];
        }
        return sum;
    }

    public static int rollHalfUpDice(dice die, int sides, int numRolled, int numUsed){
        int[] tot = new int[numRolled];
        for (int i = 0; i < numRolled; i++){
            tot[i] = roll(die);
            if (tot[i] < sides/2){
                i--;
            }
        }
        Arrays.sort(tot);
        int sum = 0;
        for (int i = 0; i < numUsed; i++){
            sum += tot[i];
        }

        return sum;
    }

    public static int roll(dice die){
        Random r = new Random();
        switch (die){
            case d0:
                return 0;
            case d4:
                return r.nextInt(4)+1;
            case d6:
                return r.nextInt(6)+1;
            case d8:
                return r.nextInt(8)+1;
            case d10:
                return r.nextInt(10)+1;
            case d12:
                return r.nextInt(12)+1;
            case d20:
                return r.nextInt(20)+1;
            case d100:
                return r.nextInt(100)+1;
        }

        System.err.println("Failed to roll specified die.");
        return -1;
    }

    public static dice getHitD(classes clas){
        dice res = dice.d8;

        if (clas == null){
            return res;
        }

        switch (clas){
            case Barbarian:
                res = dice.d12;
                break;
            case Bard:
                res = dice.d8;
                break;
            case Cleric:
                res = dice.d8;
                break;
            case Druid:
                res = dice.d8;
                break;
            case Fighter:
                res = dice.d10;
                break;
            case Magic_User:
                res = dice.d6;
                break;
            case Monk:
                res = dice.d8;
                break;
            case Paladin:
                res = dice.d10;
                break;
            case Ranger:
                res = dice.d10;
                break;
            case Sorcerer:
                res = dice.d6;
                break;
            case Thief:
                res = dice.d8;
                break;
            case Warlock:
                res = dice.d8;
                break;
        }

        return res;
    }

    public static int[] getStatBonArr(races race){
        int[] res = {0,0,0,0,0,0};

        if (race == null){
            return res;
        }

        //char = 0, con = 1, dext, = 2, int = 3, stre = 4, wis = 5
        switch (race){
            case Dragonborn:
                res[4] = 2;
                res[0] = 1;
                break;
            case Dwarf:
                res[1] = 2;
                res[5] = 1;
                break;
            case Elf:
                res[2] = 2;
                res[3] = 1;
                break;
            case Gnome:
                res[3] = 2;
                res[2] = 1;
                break;
            case Halfling:
                res[2] = 2;
                res[1] = 1;
                break;
            case Half_Elf:
                res[0] = 2;
                break;
            case Half_Orc:
                res[4] = 2;
                res[1] = 1;
                break;
            case Human:
                for (int i = 0; i < res.length; i++){
                    res[i] = 1;
                }
                break;
            case Tiefling:
                res[0] = 2;
                res[1] = 1;
                break;
        }

        return res;
    }

    public static int[] getBackBonArr(backgrounds backgrounds) {
        /*
        0-athlBon;
        1-acroBon;
        2-sleiHandBon;
        3-steaBon;
        4-arcaBon;
        5-histBon;
        6-inveBon;
        7-natuBon;
        8-reliBon;
        9-animHandBon;
        10-insiBon;
        11-mediBon;
        12-percBon;
        13-survBon;
        14-deceBon;
        15-intiBon;
        16-perfBon;
        17-persBon;*/

        int[] res = new int[18];

        if (backgrounds == null){
            return res;
        }

        switch (backgrounds){
            case Acolyte:
                res[10] = 2;
                res[8] = 2;
                break;
            case Charlatan:
                res[14] = 2;
                res[2] = 2;
                break;
            case Criminal:
                res[14] = 2;
                res[3] = 2;
                break;
            case Entertainer:
                res[1] = 2;
                res[16] = 2;
                break;
            case Folk_Hero:
                res[9] = 2;
                res[13] = 2;
                break;
            case Guild_Artisan:
                res[10] = 2;
                res[17] = 2;
                break;
            case Hermit:
                res[11] = 2;
                res[8] = 2;
                break;
            case Noble:
                res[5] = 2;
                res[17] = 2;
                break;
            case Outlander:
                res[0] = 2;
                res[13] = 2;
                break;
            case Sage:
                res[4] = 2;
                res[5] = 2;
                break;
            case Sailor:
                res[0] = 2;
                res[12] = 2;
                break;
            case Soldier:
                res[0] = 2;
                res[15] = 2;
                break;
            case Urchin:
                res[2] = 2;
                res[3] = 2;
                break;
        }

        return res;
    }
}
