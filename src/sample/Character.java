package sample;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Holds a dnd character
 * Created by Tanner on 1/30/2017.
 */
public class Character {

    //gold a character has on them
    private int gold;
    //current experience a character has
    private int curExp;
    //exp to next level for a haracter
    private int expNextLevel;

    //race of a character
    private Constants.races race;
    //background of a character
    private Constants.backgrounds back;
    //class a character has
    private Constants.classes clas;
    //the alignment of a character
    private String alig;

    //base hit points a character has
    private int baseHP;
    //the total hit points a character has
    private int hitP;
    //hit points a character currently has
    private int cureHP;
    //the hit dice a character has
    private Constants.dice hitD;

    //armor class of a character with equipped items
    private int ac;

    //whether the character is dead
    private boolean dead;

    //level and proficiency bonus of a character
    private int level;
    private int profBon;

    //stat and skill holders for a character
    private StatHolder stats;
    private SkillHolder skills;

    //charcter and player name
    private String playerName;
    private String name;

    //lists of proficiencies, languages, traits, feats, and equipment
    private ArrayList<String> profLis;
    private ArrayList<String> langLis;
    private ArrayList<String> traiLis;
    private ArrayList<String> featLis;
    private ArrayList<String> equiLis;

    //list of weapons and armor a character has
    //armor is equipped
    private ArrayList<Weapon> weapLis;
    private ArrayList<Armor> armoLis;

    //characters backstory
    private String backstory;

    //spellbook of a character
    Spellbook spellbook;

    /*
    Creates an empty default character
     */
    public Character() {
        spellbook = new Spellbook();

        stats = new StatHolder();
        skills = new SkillHolder();

        profLis = new ArrayList<>();
        langLis = new ArrayList<>();
        traiLis = new ArrayList<>();
        featLis = new ArrayList<>();
        weapLis = new ArrayList<>();
        armoLis = new ArrayList<>();
        equiLis = new ArrayList<>();

        dead = false;

        level = 1;
        profBon = 2;
        gold = 0;
        curExp = 0;
        ac = 0;
        expNextLevel = Constants.NEXT_LEVEL_EXP[level];
    }

    public void giveWeapon(Weapon a){
        weapLis.add(a);
    }

    public void setBackstory(String b){
        backstory = b;
    }

    public void addEqui(String e){
        equiLis.add(e);
    }

    public void addFeat(String f){
        featLis.add(f);
    }

    public void addTrai(String t){
        traiLis.add(t);
    }

    public void addLang(String l){
        langLis.add(l);
    }

    public void addProf(String p){
        profLis.add(p);
    }

    public Spellbook getSpellbook(){
        return spellbook;
    }

    public SkillHolder getSkills(){
        return skills;
    }

    public StatHolder getStats(){
        return stats;
    }

    public void setAlig(String s){
        alig = s;
    }

    /*
    Set race, auto updates stats
     */
    public void setRace(Constants.races r){
        race = r;

        stats.setRace(race);
        skills.update(stats, back);
    }

    /*
    Sets backstory, auto updates skills
     */
    public void setBack(Constants.backgrounds b){
        back = b;
        skills.update(stats, back);
    }

    public void setStre(int s){
        stats.setStre(s);
        skills.update(stats, back);
    }

    public void setChari(int c){
        stats.setChari(c);
        skills.update(stats, back);
    }

    /*
    Auto updates hit points
     */
    public void setCons(int c){
        stats.setCons(c);
        skills.update(stats, back);

        hitP = baseHP + stats.getConsBon()*level;
        cureHP = hitP;
    }

    /*
    Set dext, auto update ac with armor
     */
    public void setDext(int d){
        stats.setDext(d);
        skills.update(stats, back);
        ac = stats.getDextBon();
        for (Armor a: armoLis){
            ac += a.getAc();
        }
    }

    public void setInte(int i){
        stats.setInte(i);
        skills.update(stats, back);
    }

    public void setWisd(int w){
        stats.setWisd(w);
        skills.update(stats, back);
    }

    public Constants.classes getClas(){
        return clas;
    }

    /*
    Sets the class of a character
     */
    public void setClas(Constants.classes c){
        clas = c;
        //get hit die of a character
        hitD = Constants.getHitD(c);
        int sides = 6;
        switch(hitD){
            case d8:
                sides = 8;
                break;
            case d10:
                sides = 10;
                break;
            case d12:
                sides = 12;
                break;
        }
        baseHP = Constants.rollHalfUpDice(hitD, sides, level, level);
        setCons(stats.getCons());

        /*
        Activate spell book if caster
         */
        switch(c){
            case Bard:
            case Cleric:
            case Druid:
            case Magic_User:
            case Paladin:
            case Ranger:
            case Sorcerer:
            case Warlock:
                spellbook.activate();
                spellbook.setSpellsKnown(level, c);
                break;
            default:
                spellbook.deactivate();
                break;
        }
    }

    public boolean isDead() {
        return dead;
    }

    public int getCureHP() {
        return cureHP;
    }

    /*
    Increase or decrease hit points by set amount
     */
    public int addCureHP(int a) {
        cureHP += a;
        if (cureHP < 0){
            dead = true;
        }
        return cureHP;
    }

    public int getHitP() {
        return hitP;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlayerName() {
        return playerName;
    }

    /*
    Give and equip a piece of armor to a character

    TODO i dont know how equipping multiple pieces would work
     */
    public void giveArmor(Armor res1) {
        armoLis.add(res1);

        ac = stats.getDextBon();
        ac += armoLis.get(0).getAc();
        if (armoLis.size() > 1 && armoLis.get(1).isShield()){
            ac += armoLis.get(1).getAc();
        }
    }
    
    public void setEXP(int x){
        curExp = x;
    }
    
    public int getGold(){
        return gold;
    }
    
    public int getCurExp(){
        return curExp;
    }
    
    public int getExpNextLevel(){
        return expNextLevel;
    }
    
    public int getLevel(){
        return level;
    }
    
    public int getProfBon(){
        return profBon;
    }
    
    public void setGold(int g){
        gold = g;
    }

    /*
    Sets the level of a character, updates prof bonus for the character
     */
    public void setLevel(int a){
        if (level <= 20) level = a;
        else level = 20;
        
        if (level < 5){
            profBon = 2;
        } else if (level < 9){
            profBon = 3;
        } else if (level < 13){
            profBon = 4;
        } else if (level < 17){
            profBon = 5;
        } else {
            profBon = 6;
        }
        expNextLevel = Constants.NEXT_LEVEL_EXP[level];
    }
    
    public void setHitP(int a){
        hitP = a;
    }

    /*
    Send the character as a string to another player or dm
     */
    public String sendChar(){
        StringBuilder sb = new StringBuilder();
        sb.append("#setchar");
        sb.append(" $name " + name);
        sb.append(" $playername " + playerName);
        sb.append(" $level " + level);
        sb.append(" $exp " + curExp);
        sb.append(" $gold " + gold);
        sb.append(" $race " + race);
        sb.append(" $class " + clas);

        sb.append(" $stats ");
        sb.append(stats.getStre() + " ");
        sb.append(stats.getCons() + " ");
        sb.append(stats.getDext() + " ");
        sb.append(stats.getInte() + " ");
        sb.append(stats.getChari() + " ");
        sb.append(stats.getWisd());

        sb.append(" $background " + back);
        sb.append(" $alignment " + alig);

        sb.append(" $hitpoints " + hitP);
        sb.append(" $currenthitpoints " + cureHP);
        sb.append(" $basehitpoints " + baseHP);
        sb.append(" $backstory " + backstory);

        sb.append(" $proficiencies ");
        for (String s: profLis){
            sb.append(s + ", ");
        }
        sb.delete(sb.length() - 2, sb.length());

        sb.append(" $languages ");
        for (String s: langLis){
            sb.append(s + ", ");
        }
        sb.delete(sb.length() - 2, sb.length());

        sb.append(" $equipment ");
        for (String s: equiLis){
            sb.append(s + ", ");
        }
        sb.delete(sb.length() - 2, sb.length());

        sb.append(" $traits ");
        for (String s: traiLis){
            sb.append(s + ", ");
        }
        sb.delete(sb.length() - 2, sb.length());

        sb.append(" $feats ");
        for (String s: featLis){
            sb.append(s + ", ");
        }
        sb.delete(sb.length() - 2, sb.length());

        for (Weapon w: weapLis){
            sb.append(" $give weapon " + w.toString());
        }

        for (Armor a: armoLis){
            sb.append(" $give armor " + a.toString());
        }

        if (spellbook.isActivated()){
            for (int i = 0; i < spellbook.getSpellsKnown().size(); i++){
                StringBuilder sb2 = new StringBuilder();
                sb2.append(" $spells " );
                sb2.append(i + ", ");
                for (String s : spellbook.getSpellsKnown().get(i)){
                    sb2.append(s + ", ");
                }
                sb2.delete(sb2.length() - 2, sb2.length());
                if (sb2.length() > 10) sb.append(sb2);
            }
        }

        return sb.toString();
    }

    /*
    Method saves the character in a file named charName_playName.dchar
     */
    public void save() {
        StringBuilder sb = new StringBuilder();
        sb.append(name.replace(" ", "_"));
        sb.append("_");
        sb.append(playerName.replace(" ", "_"));
        sb.append(".dchar");
        try {
            PrintWriter pw = new PrintWriter(sb.toString());
            pw.println("$name " + name);
            pw.println("$playername " + playerName);
            pw.println("$level " + level);
            pw.println("$exp " + curExp);
            pw.println("$gold " + gold);
            pw.println("$race " + race);
            pw.println("$class " + clas);

            //set stats before background for correct skill levels
            //added in order of set all, sort of odd order.
            sb = new StringBuilder();
            sb.append("$stats ");
            sb.append(stats.getStre() + " ");
            sb.append(stats.getCons() + " ");
            sb.append(stats.getDext() + " ");
            sb.append(stats.getInte() + " ");
            sb.append(stats.getChari() + " ");
            sb.append(stats.getWisd());
            pw.println(sb.toString());

            pw.println("$background " + back);
            pw.println("$alignment " + alig);
            
            pw.println("$hitpoints " + hitP);
            pw.println("$currenthitpoints " + cureHP);
            pw.println("$basehitpoints " + baseHP);

            pw.println("$backstory " + backstory);

            sb = new StringBuilder();
            sb.append("$proficiencies ");
            for (String s: profLis){
                sb.append(s + ", ");
            }
            sb.delete(sb.length() - 2, sb.length());
            pw.println(sb.toString());

            sb = new StringBuilder();
            sb.append("$languages ");
            for (String s: langLis){
                sb.append(s + ", ");
            }
            sb.delete(sb.length() - 2, sb.length());
            pw.println(sb.toString());

            sb = new StringBuilder();
            sb.append("$equipment ");
            for (String s: equiLis){
                sb.append(s + ", ");
            }
            sb.delete(sb.length() - 2, sb.length());
            pw.println(sb.toString());

            sb = new StringBuilder();
            sb.append("$traits ");
            for (String s: traiLis){
                sb.append(s + ", ");
            }
            sb.delete(sb.length() - 2, sb.length());
            pw.println(sb.toString());

            sb = new StringBuilder();
            sb.append("$feats ");
            for (String s: featLis){
                sb.append(s + ", ");
            }
            sb.delete(sb.length() - 2, sb.length());
            pw.println(sb.toString());

            for (Weapon w : weapLis){
                pw.println("$give weapon " + w.toString());
            }

            for (Armor a : armoLis){
                pw.println("$give armor " + a.toString());
            }

            if (spellbook.isActivated()){
                for (int i = 0; i < spellbook.getSpellsKnown().size(); i++){
                    sb = new StringBuilder();
                    sb.append("$spells ");
                    sb.append(i + ", ");
                    for (String s : spellbook.getSpellsKnown().get(i)){
                        sb.append(s + ", ");
                    }
                    sb.delete(sb.length() - 2, sb.length());
                    if (sb.length() > 10) pw.println(sb.toString());
                }
            }

            pw.close();
        } catch (IOException e){
            System.err.println("Could not write to file: " + sb.toString());
        }
    }

    public void setCureHP(int cureHP) {
        this.cureHP = cureHP;
    }

    public void setBaseHP(int baseHP) {
        this.baseHP = baseHP;
    }

    public Constants.races getRace() {
        return race;
    }

    public ArrayList<String> getFeatLis() {
        return featLis;
    }

    public ArrayList<Weapon> getWeapLis() {
        return weapLis;
    }

    /*
    Gets the total hit bonus for the character including dex or strength
     */
    public Integer getHitBon(Weapon wep) {
        int res = 0;
        res += wep.getBonus();
        res += profBon;
        if (wep.isFine()){
            res += stats.getDextBon();
        } else {
            res += stats.getStreBon();
        }
        return (Integer) res;
    }

    /*
    Gets the string that represent the damage the character does (dice and bonus)
     */
    public String getDamageString(Weapon wep) {
        StringBuilder sb = new StringBuilder();
        sb.append(wep.getNumDie());
        sb.append(wep.getDie());
        int tot = wep.getBonus();
        if (wep.isFine()){
            tot += stats.getDextBon();
        } else {
            tot += stats.getStreBon();
        }
        if (tot != 0){
            sb.append("+");
            sb.append(tot);
        }
        return sb.toString();
    }

    public ArrayList<Armor> getArmoLis() {
        return armoLis;
    }

    public int getAC() {
        return ac;
    }

    public ArrayList<String> getEquiLis() {
        return equiLis;
    }
}
