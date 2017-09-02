package sample;

/**
 * Holds all skills for a class.
 * Created by Tanner on 2/7/2017.
 */
public class SkillHolder {
    //stre boosted
    private int athlBon;
    //dext boosted
    private int acroBon;
    private int sleiHandBon;
    private int steaBon;
    //inte boosted
    private int arcaBon;
    private int histBon;
    private int inveBon;
    private int natuBon;
    private int reliBon;
    //wisd boosted
    private int animHandBon;
    private int insiBon;
    private int mediBon;
    private int percBon;
    private int survBon;
    //char boosted
    private int deceBon;
    private int intiBon;
    private int perfBon;
    private int persBon;

    public SkillHolder(StatHolder sh, Constants.backgrounds backgrounds){
        int[] bons = Constants.getBackBonArr(backgrounds);

        athlBon = sh.getStreBon() + bons[0];

        acroBon = sh.getDextBon()+ bons[1];
        sleiHandBon = sh.getDextBon()+ bons[2];
        steaBon = sh.getDextBon()+ bons[3];

        arcaBon = sh.getInteBon()+ bons[4];
        histBon = sh.getInteBon()+ bons[5];
        inveBon = sh.getInteBon()+ bons[6];
        natuBon = sh.getInteBon()+ bons[7];
        reliBon = sh.getInteBon()+ bons[8];

        animHandBon = sh.getWisdBon()+ bons[9];
        insiBon = sh.getWisdBon()+ bons[10];
        mediBon = sh.getWisdBon()+ bons[11];
        percBon = sh.getWisdBon()+ bons[12];
        survBon = sh.getWisdBon()+ bons[13];

        deceBon = sh.getCharBon()+ bons[14];
        intiBon = sh.getCharBon()+ bons[15];
        perfBon = sh.getCharBon()+ bons[16];
        persBon = sh.getCharBon()+ bons[17];
    }

    public void update(StatHolder sh, Constants.backgrounds backgrounds){
        int[] bons = Constants.getBackBonArr(backgrounds);

        athlBon = sh.getStreBon() + bons[0];

        acroBon = sh.getDextBon()+ bons[1];
        sleiHandBon = sh.getDextBon()+ bons[2];
        steaBon = sh.getDextBon()+ bons[3];

        arcaBon = sh.getInteBon()+ bons[4];
        histBon = sh.getInteBon()+ bons[5];
        inveBon = sh.getInteBon()+ bons[6];
        natuBon = sh.getInteBon()+ bons[7];
        reliBon = sh.getInteBon()+ bons[8];

        animHandBon = sh.getWisdBon()+ bons[9];
        insiBon = sh.getWisdBon()+ bons[10];
        mediBon = sh.getWisdBon()+ bons[11];
        percBon = sh.getWisdBon()+ bons[12];
        survBon = sh.getWisdBon()+ bons[13];

        deceBon = sh.getCharBon()+ bons[14];
        intiBon = sh.getCharBon()+ bons[15];
        perfBon = sh.getCharBon()+ bons[16];
        persBon = sh.getCharBon()+ bons[17];
    }

    public SkillHolder(){
        athlBon = 0;
        acroBon = 0;
        sleiHandBon = 0;
        steaBon = 0;
        arcaBon = 0;
        histBon = 0;
        inveBon = 0;
        natuBon = 0;
        reliBon = 0;
        animHandBon = 0;
        insiBon = 0;
        mediBon = 0;
        percBon = 0;
        survBon = 0;
        deceBon = 0;
        intiBon = 0;
        perfBon = 0;
        persBon = 0;
    }

    public int getAthlBon() {
        return athlBon;
    }

    public int getAcroBon() {
        return acroBon;
    }

    public int getSleiHandBon() {
        return sleiHandBon;
    }

    public int getSteaBon() {
        return steaBon;
    }

    public int getArcaBon() {
        return arcaBon;
    }

    public int getHistBon() {
        return histBon;
    }

    public int getInveBon() {
        return inveBon;
    }

    public int getNatuBon() {
        return natuBon;
    }

    public int getReliBon() {
        return reliBon;
    }

    public int getAnimHandBon() {
        return animHandBon;
    }

    public int getInsiBon() {
        return insiBon;
    }

    public int getMediBon() {
        return mediBon;
    }

    public int getPercBon() {
        return percBon;
    }

    public int getSurvBon() {
        return survBon;
    }

    public int getDeceBon() {
        return deceBon;
    }

    public int getIntiBon() {
        return intiBon;
    }

    public int getPerfBon() {
        return perfBon;
    }

    public int getPersBon() {
        return persBon;
    }
}
