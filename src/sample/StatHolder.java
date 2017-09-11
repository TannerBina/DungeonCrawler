package sample;

/**
 * Holds all stats for a class.
 * As well as the bonuses for each stat
 *
 * Created by Tanner on 2/7/2017.
 */
public class StatHolder {
    //the race which effect the given stats
    private Constants.races race;

    //all stats and their bonuses
    private int stre;
    private int streBon;
    private int cons;
    private int consBon;
    private int dext;
    private int dextBon;
    private int inte;
    private int inteBon;
    private int chari;
    private int charBon;
    private int wisd;
    private int wisdBon;

    /*
    Create initally empty stat holder
     */
    public StatHolder(){
        stre = 0;
        cons = 0;
        dext = 0;
        inte = 0;
        chari = 0;
        wisd = 0;

        streBon = 0;
        consBon = 0;
        dextBon = 0;
        inteBon = 0;
        charBon = 0;
        wisdBon = 0;
    }

    /*
    Set the race and update all given stats
     */
    public void setRace(Constants.races r){
        //get the stag bonus for the current race
        int[] statBon = Constants.getStatBonArr(race);

        //remove stats from the previous race
        chari -= statBon[0];
        cons -= statBon[1];
        dext -= statBon[2];
        inte -= statBon[3];
        stre -= statBon[4];
        wisd -= statBon[5];

        //get new race stat bonuses
        statBon = Constants.getStatBonArr(r);
        race = r;

        //update stats
        chari += statBon[0];
        cons += statBon[1];
        dext += statBon[2];
        inte += statBon[3];
        stre += statBon[4];
        wisd += statBon[5];

        //set all bonuses
        setAllBons();
    }

    /*
    Set all to given stats to the inputted value, uses current race value
     */
    public void setAll(int[] stats){
        //try to set all stats to the entered stats than update bonuses
        try{
            stre = stats[0];
            cons = stats[1];
            dext = stats[2];
            inte = stats[3];
            chari = stats[4];
            wisd = stats[5];
            setRace(race);
            setAllBons();
        } catch (IndexOutOfBoundsException e){
            System.err.println("Input array to set all stats incorrect length");
        }
    }

    /*
    Set all bonuses for the stats
     */
    private void setAllBons(){
        streBon = getBon(stre);
        consBon = getBon(cons);
        dextBon = getBon(dext);
        inteBon = getBon(inte);
        charBon = getBon(chari);
        wisdBon = getBon(wisd);
    }

    private int getBon(int stat){
        return stat/2 - 5;
    }

    public int getChari() {
        return chari;
    }

    public int getCons() {
        return cons;
    }

    public int getDext() {
        return dext;
    }

    public int getInte() {
        return inte;
    }

    public int getStre() {
        return stre;
    }

    public int getWisd() {
        return wisd;
    }

    public void setChari(int a){
        chari = a;
        setRace(race);
    }

    public void setCons(int a){
        cons = a;
        setRace(race);
    }

    public void setDext(int a){
        dext = a;
        setRace(race);
    }

    public void setInte(int a){
        inte = a;
        setRace(race);
    }

    public void setStre(int a){
        stre = a;
        setRace(race);
    }

    public void setWisd(int a){
        wisd = a;
        setRace(race);
    }

    public int getWisdBon() {
        return wisdBon;
    }

    public int getCharBon() {
        return charBon;
    }

    public int getInteBon() {
        return inteBon;
    }

    public int getDextBon() {
        return dextBon;
    }

    public int getConsBon() {
        return consBon;
    }

    public int getStreBon() {
        return streBon;
    }
}
