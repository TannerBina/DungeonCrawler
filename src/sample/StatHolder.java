package sample;

/**
 * Holds all stats for a class.
 * Created by Tanner on 2/7/2017.
 */
public class StatHolder {
    private Constants.races race;

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

    public void setRace(Constants.races r){
        int[] statBon = Constants.getStatBonArr(race);

        chari -= statBon[0];
        cons -= statBon[1];
        dext -= statBon[2];
        inte -= statBon[3];
        stre -= statBon[4];
        wisd -= statBon[5];

        statBon = Constants.getStatBonArr(r);
        race = r;

        chari += statBon[0];
        cons += statBon[1];
        dext += statBon[2];
        inte += statBon[3];
        stre += statBon[4];
        wisd += statBon[5];

        setAllBons();
    }

    public void setAll(int[] stats){
        try{
            stre = stats[0];
            cons = stats[1];
            dext = stats[2];
            inte = stats[3];
            chari = stats[4];
            wisd = stats[5];
            setAllBons();
        } catch (IndexOutOfBoundsException e){
            System.err.println("Input array to set all stats incorrect length");
        }
    }

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
