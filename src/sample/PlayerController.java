package sample;

import com.sun.xml.internal.bind.v2.TODO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Player Controller
 * Created by Tanner on 2/26/2017.
 */
public class PlayerController {

    public static Character player;
    private PlayerClient pc;
    public boolean updated;
    private Timer update;
    public Stage currentStage;

    public PlayerController(){
        updated = false;
    }

    @FXML
    public void onEnterSend(ActionEvent ae){
        String s = enteFie.getText();
        enteFie.clear();
        if (s.length() == 0) return;
        if (pc == null){
            if (s.charAt(0) == '$'){
                if (s.startsWith("$connect")){
                    Scanner scan = new Scanner(s);
                    scan.next();
                    pc = new PlayerClient(scan.next(), scan.nextInt(), commAre, player, this);
                    if (!pc.isActive()){
                        pc = null;
                    }
                } else {
                    if (s.startsWith("$clear"))
                        commAre.clear();
                }
            } else {
                commAre.appendText(player.getName() + ": " + s + '\n');
            }
        } else {
            if (s == "$clear"){
                commAre.clear();
            }else {
                pc.send(player.getName() + ": " + s);
            }
        }
    }

    @FXML
    private void initialize(){
        update = new Timer();

        update.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable(){
                    public void run(){
                        if (updated){
                            if (pc != null) {
                                if (pc.players.size() > 0) {
                                    setTab1(pc.players.get(0).character);
                                }
                                if (pc.players.size() > 1) {
                                    setTab2(pc.players.get(1).character);
                                }
                                if (pc.players.size() > 2) {
                                    setTab3(pc.players.get(2).character);
                                }
                                if (pc.players.size() > 3) {
                                    setTab4(pc.players.get(3).character);
                                }
                            }
                            if (currentStage != null){
                                updateStage();
                            }
                            updated = false;
                        }
                    }
                });
            }
        }, 0, 100);

        if (player != null){
            pc = null;
            setInventoryTab();
            setArmorTab();
            setWeaponsTab();
            setSkillsTab();
            setCharTab();
            setStatTab();
            setLootTab();
            setFeatTab();
            setSpellsTab();
            } else {
            System.err.println("Error with player character in controller init");
        }
    }

    private void updateStage() {
        StringBuilder sb = new StringBuilder();
        sb.append(currentStage.getName());
        sb.append('\n');
        sb.append(currentStage.getDescription());

        enviAre.setText(sb.toString());

        sb = new StringBuilder();
        sb.append("Objects");
        sb.append('\n');
        for (int i = 0; i < currentStage.getObjects().size(); i++){
            sb.append(currentStage.getObjects().get(i).getName());
            if (i != currentStage.getObjects().size() - 1){
                sb.append('\n');
            }
        }
        objeAre.setText(sb.toString());

        sb = new StringBuilder();
        if (currentStage.getEncounter().getMonsters().isEmpty()){
            encoAre.setText("No Current Encounter");
        } else {
            sb.append("Current Encounter" + '\n');
            for (int i = 0; i < currentStage.getEncounter().getMonsters().size(); i++){
                sb.append((currentStage.getEncounter().getMonsters().get(i).getName() + '\t') + '\t' + "Damage Taken : " + currentStage.getEncounter().getMonsters().get(i).getHp());
                if (i != currentStage.getEncounter().getMonsters().size()-1) sb.append('\n');
            }
            encoAre.setText(sb.toString());
        }
    }

    private void setInventoryTab() {
        StringBuilder sb = new StringBuilder();
        for (String e: player.getEquiLis()){
            sb.append(e);
            sb.append('\n');
        }
        inveFie.setText(sb.toString());
    }

    private void setArmorTab() {
        cureACLab.setText("   Current AC:" + player.getAC());
        if (player.getArmoLis().size() > 0){
            Armor a = player.getArmoLis().get(0);
            armo1NameLab.setText("   " + a.getName());
            armo1ACLab.setText(((Integer)a.getAc()).toString());
            armo1ShieLab.setText("No");
            if (a.isShield()){
                armo1ShieLab.setText("Yes");
            }
        }
        if (player.getArmoLis().size() > 1){
            Armor a = player.getArmoLis().get(1);
            armo1NameLab.setText("   " + a.getName());
            armo1ACLab.setText(((Integer)a.getAc()).toString());
            armo1ShieLab.setText("No");
            if (a.isShield()){
                armo1ShieLab.setText("Yes");
            }
        }
    }

    private void setWeaponsTab() {
        ArrayList<Weapon> w = player.getWeapLis();
        if (w.size() > 0){
            Weapon wep = w.get(0);
            weap1NameLab.setText("   " + wep.getName());
            weap1HitLab.setText("+" + player.getHitBon(wep).toString());
            weap1DamaLab.setText(player.getDamageString(wep));
        }
        if (w.size() > 1){
            Weapon wep = w.get(1);
            weap2NameLab.setText("   " + wep.getName());
            weap2HitLab.setText("+" + player.getHitBon(wep).toString());
            weap2DamaLab.setText(player.getDamageString(wep));
        }
        if (w.size() > 2){
            Weapon wep = w.get(2);
            weap3NameLab.setText("   " + wep.getName());
            weap3HitLab.setText("+" + player.getHitBon(wep).toString());
            weap3DamaLab.setText(player.getDamageString(wep));
        }
    }

    private void setSkillsTab() {
        SkillHolder sh = player.getSkills();

        athlBonLab.setText("+" + sh.getAthlBon());
        acroBonLab.setText("+" + sh.getAcroBon());
        sleiHandBonLab.setText("+" + sh.getSleiHandBon());
        steaBonLab.setText("+" + sh.getSteaBon());
        arcaBonLab.setText("+" + sh.getArcaBon());
        histBonLab.setText("+" + sh.getHistBon());
        inveBonLab.setText("+" + sh.getInveBon());
        natuBonLab.setText("+" + sh.getNatuBon());
        reliBonLab.setText("+" + sh.getReliBon());
        animHandBonLab.setText("+" + sh.getAnimHandBon());
        insiBonLab.setText("+" + sh.getInsiBon());
        mediBonLab.setText("+" + sh.getMediBon());
        percBonLab.setText("+" + sh.getPercBon());
        survBonLab.setText("+" + sh.getSurvBon());
        deceBonLab.setText("+" + sh.getDeceBon());
        intiBonLab.setText("+" + sh.getIntiBon());
        perfBonLab.setText("+" + sh.getPerfBon());
        persBonLab.setText("+" + sh.getPersBon());
    }

    private void setSpellSlots(){
        Spellbook s = player.getSpellbook();
        leve1SlotLab.setText(((Integer)s.getRemainingSlots(1)).toString()
                + "/" + ((Integer)s.getSpellSlots(1)).toString());
        leve2SlotLab.setText(((Integer)s.getRemainingSlots(2)).toString()
                + "/" + ((Integer)s.getSpellSlots(2)).toString());
        leve3SlotLab.setText(((Integer)s.getRemainingSlots(3)).toString()
                + "/" + ((Integer)s.getSpellSlots(3)).toString());
        leve4SlotLab.setText(((Integer)s.getRemainingSlots(4)).toString()
                + "/" + ((Integer)s.getSpellSlots(4)).toString());
        leve5SlotLab.setText(((Integer)s.getRemainingSlots(5)).toString()
                + "/" + ((Integer)s.getSpellSlots(5)).toString());
        leve6SlotLab.setText(((Integer)s.getRemainingSlots(6)).toString()
                + "/" + ((Integer)s.getSpellSlots(6)).toString());
        leve7SlotLab.setText(((Integer)s.getRemainingSlots(7)).toString()
                + "/" + ((Integer)s.getSpellSlots(7)).toString());
        leve8SlotLab.setText(((Integer)s.getRemainingSlots(8)).toString()
                + "/" + ((Integer)s.getSpellSlots(8)).toString());
        leve9SlotLab.setText(((Integer)s.getRemainingSlots(9)).toString()
                + "/" + ((Integer)s.getSpellSlots(9)).toString());
    }

    public void setSpellsKnown(){
        Spellbook s = player.getSpellbook();
        StringBuilder sb = new StringBuilder();
        for (String spell : s.getSpellsKnown().get(0)){
            sb.append(spell);
            sb.append(", ");
        }
        if (sb.length() > 2)
            sb.delete(sb.length()-2, sb.length());
        leve0SpelFie.setText(sb.toString());

        sb = new StringBuilder();
        for (String spell : s.getSpellsKnown().get(1)){
            sb.append(spell);
            sb.append(", ");
        }
        if (sb.length() > 2)
            sb.delete(sb.length()-2, sb.length());
        leve1SpelFie.setText(sb.toString());

        sb = new StringBuilder();
        for (String spell : s.getSpellsKnown().get(2)){
            sb.append(spell);
            sb.append(", ");
        }
        if (sb.length() > 2)
            sb.delete(sb.length()-2, sb.length());
        leve2SpelFie.setText(sb.toString());

        sb = new StringBuilder();
        for (String spell : s.getSpellsKnown().get(3)){
            sb.append(spell);
            sb.append(", ");
        }
        if (sb.length() > 2)
            sb.delete(sb.length()-2, sb.length());
        leve3SpelFie.setText(sb.toString());

        sb = new StringBuilder();
        for (String spell : s.getSpellsKnown().get(4)){
            sb.append(spell);
            sb.append(", ");
        }
        if (sb.length() > 2)
            sb.delete(sb.length()-2, sb.length());
        leve4SpelFie.setText(sb.toString());

        sb = new StringBuilder();
        for (String spell : s.getSpellsKnown().get(5)){
            sb.append(spell);
            sb.append(", ");
        }
        if (sb.length() > 2)
            sb.delete(sb.length()-2, sb.length());
        leve5SpelFie.setText(sb.toString());

        sb = new StringBuilder();
        for (String spell : s.getSpellsKnown().get(6)){
            sb.append(spell);
            sb.append(", ");
        }
        if (sb.length() > 2)
            sb.delete(sb.length()-2, sb.length());
        leve6SpelFie.setText(sb.toString());

        sb = new StringBuilder();
        for (String spell : s.getSpellsKnown().get(7)){
            sb.append(spell);
            sb.append(", ");
        }
        if (sb.length() > 2)
            sb.delete(sb.length()-2, sb.length());
        leve7SpelFie.setText(sb.toString());

        sb = new StringBuilder();
        for (String spell : s.getSpellsKnown().get(8)){
            sb.append(spell);
            sb.append(", ");
        }
        if (sb.length() > 2)
            sb.delete(sb.length()-2, sb.length());
        leve8SpelFie.setText(sb.toString());

        sb = new StringBuilder();
        for (String spell : s.getSpellsKnown().get(9)){
            sb.append(spell);
            sb.append(", ");
        }
        if (sb.length() > 2)
            sb.delete(sb.length()-2, sb.length());
        leve9SpelFie.setText(sb.toString());
    }

    private void setSpellsTab() {
        Spellbook s = player.getSpellbook();
        if (s.isActivated()) {
            setSpellSlots();
            setSpellsKnown();
        }
    }

    private void setFeatTab(){
        StringBuilder sb = new StringBuilder();
        ArrayList<String> fl = player.getFeatLis();
        for (String f : fl){
            sb.append(f);
            sb.append('\n');
        }
        featAre.setText(sb.toString());
    }

    private void setLootTab(){
        goldLab.setText("     " + player.getGold());
        expLab.setText("     " + player.getCurExp() + "/" + player.getExpNextLevel());
    }

    private void setStatTab(){
        StatHolder sh = player.getStats();
        streLab.setText("     " + sh.getStre() + " (+" + sh.getStreBon() +")");
        dextLab.setText("     " + sh.getDext() + " (+" + sh.getDextBon() +")");
        inteLab.setText("     " + sh.getInte() + " (+" + sh.getInteBon() +")");
        consLab.setText("     " + sh.getCons() + " (+" + sh.getConsBon() +")");
        charLab.setText("     " + sh.getChari() + " (+" + sh.getCharBon() +")");
        wisdLab.setText("     " + sh.getWisd() + " (+" + sh.getWisdBon() +")");

    }

    private void setCharTab(){
        raceLab.setText("     " + player.getRace());
        clasLab.setText("     " + player.getClas());
        nameLab.setText("     " + player.getName());
        hitpLab.setText("     " + player.getCureHP() + "/" + player.getHitP());
        leveLab.setText("          " + player.getLevel());
        profBonLab.setText("          +" + player.getProfBon());
    }

    @FXML
    private TextArea inveFie;
    @FXML
    private TextField enteFie;
    @FXML
    private TextArea commAre;
    @FXML
    private TextArea encoAre;
    @FXML
    private TextArea enviAre;
    @FXML
    private TextArea objeAre;
    @FXML
    private TextArea suggActiAre;
    @FXML
    private TextArea featAre;
    @FXML
    private Label leve1SlotLab;
    @FXML
    private Label leve2SlotLab;
    @FXML
    private Label leve3SlotLab;
    @FXML
    private Label leve4SlotLab;
    @FXML
    private Label leve5SlotLab;
    @FXML
    private Label leve6SlotLab;
    @FXML
    private Label leve7SlotLab;
    @FXML
    private Label leve8SlotLab;
    @FXML
    private Label leve9SlotLab;
    @FXML
    private TextField leve0SpelFie;
    @FXML
    private TextField leve1SpelFie;
    @FXML
    private TextField leve2SpelFie;
    @FXML
    private TextField leve3SpelFie;
    @FXML
    private TextField leve4SpelFie;
    @FXML
    private TextField leve5SpelFie;
    @FXML
    private TextField leve6SpelFie;
    @FXML
    private TextField leve7SpelFie;
    @FXML
    private TextField leve8SpelFie;
    @FXML
    private TextField leve9SpelFie;

    @FXML
    private Label weap1NameLab;
    @FXML
    private Label weap1HitLab;
    @FXML
    private Label weap1DamaLab;
    @FXML
    private Label weap2NameLab;
    @FXML
    private Label weap2HitLab;
    @FXML
    private Label weap2DamaLab;
    @FXML
    private Label weap3NameLab;
    @FXML
    private Label weap3HitLab;
    @FXML
    private Label weap3DamaLab;

    @FXML
    private Label cureACLab;
    @FXML
    private Label armo1NameLab;
    @FXML
    private Label armo1ACLab;
    @FXML
    private Label armo1ShieLab;
    @FXML
    private Label armo2NameLab;
    @FXML
    private Label armo2ACLab;
    @FXML
    private Label armo2ShieLab;


    @FXML
    private Label animHandBonLab;
    @FXML
    private Label arcaBonLab;
    @FXML
    private Label athlBonLab;
    @FXML
    private Label acroBonLab;
    @FXML
    private Label deceBonLab;
    @FXML
    private Label histBonLab;
    @FXML
    private Label insiBonLab;
    @FXML
    private Label intiBonLab;
    @FXML
    private Label inveBonLab;
    @FXML
    private Label mediBonLab;
    @FXML
    private Label natuBonLab;
    @FXML
    private Label percBonLab;
    @FXML
    private Label perfBonLab;
    @FXML
    private Label persBonLab;
    @FXML
    private Label reliBonLab;
    @FXML
    private Label sleiHandBonLab;
    @FXML
    private Label steaBonLab;
    @FXML
    private Label survBonLab;

    @FXML
    private Label raceLab;
    @FXML
    private Label clasLab;
    @FXML
    private Label nameLab;
    @FXML
    private Label hitpLab;
    @FXML
    private Label leveLab;
    @FXML
    private Label profBonLab;
    @FXML
    private Label streLab;
    @FXML
    private Label dextLab;
    @FXML
    private Label inteLab;
    @FXML
    private Label consLab;
    @FXML
    private Label charLab;
    @FXML
    private Label wisdLab;
    @FXML
    private Label goldLab;
    @FXML
    private Label expLab;

    private void setTab1(Character c){
        playTab1.setText(c.getName());
        playNameTab1Lab.setText(c.getPlayerName());
        charNameTab1Lab.setText(c.getName());
        raceTab1Lab.setText("" + c.getRace());
        clasTab1Lab.setText("" + c.getClas());
        leveTab1Lab.setText("" + c.getLevel());
        hitpTab1Lab.setText(c.getCureHP() + "/" + c.getHitP());
        acTab1Lab.setText("" + c.getAC());
    }

    private void setTab2(Character c){
        playTab2.setText(c.getName());
        playNameTab2Lab.setText(c.getPlayerName());
        charNameTab2Lab.setText(c.getName());
        raceTab2Lab.setText("" + c.getRace());
        clasTab2Lab.setText("" + c.getClas());
        leveTab2Lab.setText("" + c.getLevel());
        hitpTab2Lab.setText(c.getCureHP() + "/" + c.getHitP());
        acTab2Lab.setText("" + c.getAC());
    }

    private void setTab3(Character c){
        playTab3.setText(c.getName());
        playNameTab3Lab.setText(c.getPlayerName());
        charNameTab3Lab.setText(c.getName());
        raceTab3Lab.setText(c.getRace().toString());
        clasTab3Lab.setText("" + c.getClas());
        leveTab3Lab.setText("" + c.getLevel());
        hitpTab3Lab.setText(c.getCureHP() + "/" + c.getHitP());
        acTab3Lab.setText("" + c.getAC());
    }

    private void setTab4(Character c){
        playTab4.setText(c.getName());
        playNameTab4Lab.setText(c.getPlayerName());
        charNameTab4Lab.setText(c.getName());
        raceTab4Lab.setText(c.getRace().toString());
        clasTab4Lab.setText("" + c.getClas());
        leveTab4Lab.setText("" + c.getLevel());
        hitpTab4Lab.setText(c.getCureHP() + "/" + c.getHitP());
        acTab4Lab.setText("" + c.getAC());
    }

    @FXML
    private Tab playTab2;
    @FXML
    private Label playNameTab2Lab;
    @FXML
    private Label charNameTab2Lab;
    @FXML
    private Label raceTab2Lab;
    @FXML
    private Label clasTab2Lab;
    @FXML
    private Label leveTab2Lab;
    @FXML
    private Label hitpTab2Lab;
    @FXML
    private Label acTab2Lab;
    @FXML
    private Tab playTab3;
    @FXML
    private Label playNameTab3Lab;
    @FXML
    private Label charNameTab3Lab;
    @FXML
    private Label raceTab3Lab;
    @FXML
    private Label clasTab3Lab;
    @FXML
    private Label leveTab3Lab;
    @FXML
    private Label hitpTab3Lab;
    @FXML
    private Label acTab3Lab;
    @FXML
    private Tab playTab4;
    @FXML
    private Label playNameTab4Lab;
    @FXML
    private Label charNameTab4Lab;
    @FXML
    private Label raceTab4Lab;
    @FXML
    private Label clasTab4Lab;
    @FXML
    private Label leveTab4Lab;
    @FXML
    private Label hitpTab4Lab;
    @FXML
    private Label acTab4Lab;
    @FXML
    private Tab playTab1;
    @FXML
    private Label playNameTab1Lab;
    @FXML
    private Label charNameTab1Lab;
    @FXML
    private Label raceTab1Lab;
    @FXML
    private Label clasTab1Lab;
    @FXML
    private Label leveTab1Lab;
    @FXML
    private Label hitpTab1Lab;
    @FXML
    private Label acTab1Lab;
}
