package sample;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Controls the new character screen
 * Used for creating a character file
 * Created by Tanner on 1/30/2017.
 */
public class NewCharController {

    //the static character that is being created
    private static Character newChar;

    public NewCharController(){

    }

    /*
    Iniatlize all the tabs for character creation
     */
    @SuppressWarnings("unchecked")
    @FXML
    private void initialize(){
        //if there isnt a new character create one
        if (newChar == null){
            newChar = new Character();
        }
        //if its not the learn spell window being init
        if (!Constants.LEARSPELWIN) {
            //add class options
            clasCho.getItems().addAll(Constants.CLASOPT);
            clasCho.getSelectionModel().selectedIndexProperty()
                    .addListener(new ChangeListener<Number>() {
                        @Override
                        public void changed(ObservableValue<? extends Number> observable,
                                            Number oldValue, Number newValue) {
                            //update the class as well as the hitpoints
                            newChar.setClas(Constants.getClass((String) clasCho.getItems().get((Integer) newValue)));
                            hitpLab.setText("     " + ((Integer) newChar.getHitP()).toString());
                        }
                    });

            //add race options
            raceCho.getItems().addAll(Constants.RACEOPT);
            raceCho.getSelectionModel().selectedIndexProperty()
                    .addListener(new ChangeListener<Number>() {
                        @Override
                        public void changed(ObservableValue<? extends Number> observable,
                                            Number oldValue, Number newValue) {
                            //update race, stats, and skills
                            newChar.setRace(Constants.getRace((String) raceCho.getItems().get((Integer) newValue)));
                            setStatLabs();
                            setSkilLabs();
                        }
                    });


            //add alignment options
            aligCho.getItems().addAll(Constants.ALIGOPT);
            aligCho.getSelectionModel().selectedIndexProperty()
                    .addListener(new ChangeListener<Number>() {
                        @Override
                        public void changed(ObservableValue<? extends Number> observable,
                                            Number oldValue, Number newValue) {
                            newChar.setAlig((String) aligCho.getItems().get((Integer) newValue));
                        }
                    });

            //add background options
            backCho.getItems().addAll(Constants.BACKOPT);
            backCho.getSelectionModel().selectedIndexProperty()
                    .addListener(new ChangeListener<Number>() {
                        @Override
                        public void changed(ObservableValue<? extends Number> observable,
                                            Number oldValue, Number newValue) {
                            //update skills
                            newChar.setBack(Constants.getBack((String) backCho.getItems().get((Integer) newValue)));
                            setSkilLabs();
                        }
                    });

            //init all weapon choice lists
            weap1DieCho.getItems().addAll(Constants.DIEOPT);
            weap1NumbDiceCho.getItems().addAll(Constants.NUMBDICEOPT);
            weap1TypeCho.getItems().addAll(Constants.WEAPTYPEOPT);
            weap2DieCho.getItems().addAll(Constants.DIEOPT);
            weap2NumbDiceCho.getItems().addAll(Constants.NUMBDICEOPT);
            weap2TypeCho.getItems().addAll(Constants.WEAPTYPEOPT);
            weap3DieCho.getItems().addAll(Constants.DIEOPT);
            weap3NumbDiceCho.getItems().addAll(Constants.NUMBDICEOPT);
            weap3TypeCho.getItems().addAll(Constants.WEAPTYPEOPT);

            //roll stat dice
            ObservableList<Integer> allPosStats = FXCollections.observableArrayList();
            for (int i = 0; i < 6; i++) {
                allPosStats.add(Constants.rollDice(Constants.dice.d6, 4, 3));
            }
            //update stat choice lists
            updaStatChoi(allPosStats);

            //add strength choice
            streCho.getSelectionModel().selectedIndexProperty()
                    .addListener(new ChangeListener<Number>() {
                        @Override
                        public void changed(ObservableValue<? extends Number> observable,
                                            Number oldValue, Number newValue) {

                            try {
                                //edit skills and stats
                                newChar.setStre((Integer) streCho.getItems().get((Integer) newValue));
                                setSkilLabs();
                                setStatLabs();
                            } catch (IndexOutOfBoundsException e) {

                            }
                        }
                    });

            //add inteligence choices
            inteCho.getSelectionModel().selectedIndexProperty()
                    .addListener(new ChangeListener<Number>() {
                        @Override
                        public void changed(ObservableValue<? extends Number> observable,
                                            Number oldValue, Number newValue) {

                            try {
                                //update skills and stats
                                newChar.setInte((Integer) inteCho.getItems().get((Integer) newValue));
                                setSkilLabs();
                                setStatLabs();
                            } catch (IndexOutOfBoundsException e) {

                            }
                        }
                    });

            //add intelligence choice
            dextCho.getSelectionModel().selectedIndexProperty()
                    .addListener(new ChangeListener<Number>() {
                        @Override
                        public void changed(ObservableValue<? extends Number> observable,
                                            Number oldValue, Number newValue) {
                            try {
                                newChar.setDext((Integer) dextCho.getItems().get((Integer) newValue));
                                setSkilLabs();
                                setStatLabs();
                            } catch (IndexOutOfBoundsException e) {

                            }
                        }
                    });

            //add wisdom choice
            wisdCho.getSelectionModel().selectedIndexProperty()
                    .addListener(new ChangeListener<Number>() {
                        @Override
                        public void changed(ObservableValue<? extends Number> observable,
                                            Number oldValue, Number newValue) {
                            try {
                                newChar.setWisd((Integer) wisdCho.getItems().get((Integer) newValue));
                                setSkilLabs();
                                setStatLabs();
                            } catch (IndexOutOfBoundsException e) {

                            }
                        }
                    });

            //add charisma choice
            charCho.getSelectionModel().selectedIndexProperty()
                    .addListener(new ChangeListener<Number>() {
                        @Override
                        public void changed(ObservableValue<? extends Number> observable,
                                            Number oldValue, Number newValue) {
                            try {
                                newChar.setChari((Integer) charCho.getItems().get((Integer) newValue));
                                setSkilLabs();
                                setStatLabs();
                            } catch (IndexOutOfBoundsException e) {

                            }
                        }
                    });

            //add const choice
            consCho.getSelectionModel().selectedIndexProperty()
                    .addListener(new ChangeListener<Number>() {
                        @Override
                        public void changed(ObservableValue<? extends Number> observable,
                                            Number oldValue, Number newValue) {
                            newChar.setCons((Integer) consCho.getItems().get((Integer) newValue));

                            try {
                                setSkilLabs();
                                setStatLabs();
                                hitpLab.setText("     " + ((Integer) newChar.getHitP()).toString());
                            } catch (IndexOutOfBoundsException e) {

                            }
                        }
                    });
        } else {
            //if its the spell tab then init all slot labels
            leve1SlotLab.setText(leve1SlotLab.getText().replace("0", ((Integer)(newChar.spellbook.getSpellSlots(1))).toString()));
            leve2SlotLab.setText(leve2SlotLab.getText().replace("0", ((Integer)(newChar.spellbook.getSpellSlots(2))).toString()));
            leve3SlotLab.setText(leve3SlotLab.getText().replace("0", ((Integer)(newChar.spellbook.getSpellSlots(3))).toString()));
            leve4SlotLab.setText(leve4SlotLab.getText().replace("0", ((Integer)(newChar.spellbook.getSpellSlots(4))).toString()));
            leve5SlotLab.setText(leve5SlotLab.getText().replace("0", ((Integer)(newChar.spellbook.getSpellSlots(5))).toString()));
            leve6SlotLab.setText(leve6SlotLab.getText().replace("0", ((Integer)(newChar.spellbook.getSpellSlots(6))).toString()));
            leve7SlotLab.setText(leve7SlotLab.getText().replace("0", ((Integer)(newChar.spellbook.getSpellSlots(7))).toString()));
            leve8SlotLab.setText(leve8SlotLab.getText().replace("0", ((Integer)(newChar.spellbook.getSpellSlots(8))).toString()));
            leve9SlotLab.setText(leve9SlotLab.getText().replace("0", ((Integer)(newChar.spellbook.getSpellSlots(9))).toString()));
        }
    }

    /*
    Reroll the hit points for the class
     */
    @FXML
    private void handReroHitPBut(){
        newChar.setClas(newChar.getClas());
        hitpLab.setText("     " + ((Integer)newChar.getHitP()).toString());
    }

    //reroll the stats choices and update
    @FXML
    private void handReroStatBut(){
        ObservableList<Integer> allPosStats = FXCollections.observableArrayList();
        for (int i = 0; i < 6; i++){
            allPosStats.add(Constants.rollDice(Constants.dice.d6, 4, 3));
        }
        updaStatChoi(allPosStats);
    }

    /*
    Open the learn spell window if the selected class can learn spells
     */
    @FXML
    private void handLearSpelBut(){
        if (newChar.getSpellbook().isActivated()) {
            Constants.LEARSPELWIN = true;
            openNew(Constants.LEARSPEL);
        }
    }

    //open a new window from the given filename
    private void openNew(String filename){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(filename));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Learn Spells");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
            System.err.println();
            System.err.println(filename + " Error in opening");
        }
    }

    /*
    Update the stat choices that a player can use
     */
    private void updaStatChoi(ObservableList<Integer> posStats){
        streCho.getItems().setAll(posStats);
        inteCho.getItems().setAll(posStats);
        charCho.getItems().setAll(posStats);
        consCho.getItems().setAll(posStats);
        wisdCho.getItems().setAll(posStats);
        dextCho.getItems().setAll(posStats);
    }

    /*
    Set all the given skill labels for a char
     */
    private void setSkilLabs(){
        SkillHolder sh = newChar.getSkills();

        athlBonLab.setText("     +" + sh.getAthlBon() + " (Str)");

        acroBonLab.setText("     +" + sh.getAcroBon() + " (Dex)");
        sleiHandBonLab.setText("     +" + sh.getSleiHandBon() + " (Dex)");
        steaBonLab.setText("     +" + sh.getSteaBon() + " (Dex)");
        arcaBonLab.setText("     +" + sh.getArcaBon() + " (Int)");
        histBonLab.setText("     +" + sh.getHistBon() + " (Int)");
        inveBonLab.setText("     +" + sh.getInveBon() + " (Int)");
        natuBonLab.setText("     +" + sh.getNatuBon() + " (Int)");
        reliBonLab.setText("     +" + sh.getReliBon() + " (Int)");
        animHandBonLab.setText("     +" + sh.getAnimHandBon() + " (Wis)");
        insiBonLab.setText("     +" + sh.getInsiBon() + " (Wis)");
        mediBonLab.setText("     +" + sh.getMediBon() + " (Wis)");
        percBonLab.setText("     +" + sh.getPercBon() + " (Wis)");
        survBonLab.setText("     +" + sh.getSurvBon() + " (Wis)");
        deceBonLab.setText("     +" + sh.getDeceBon() + " (Char)");
        intiBonLab.setText("     +" + sh.getIntiBon() + " (Char)");
        perfBonLab.setText("     +" + sh.getPerfBon() + " (Char)");
        persBonLab.setText("     +" + sh.getPersBon() + " (Char)");
    }

    /*
    Set all given stat labels
     */
    private void setStatLabs(){
        streBonLab.setText("     +" + ((Integer)newChar.getStats().getStreBon()).toString());
        charBonLab.setText("     +" + ((Integer)newChar.getStats().getCharBon()).toString());
        consBonLab.setText("     +" + ((Integer)newChar.getStats().getConsBon()).toString());
        dextBonLab.setText("     +" + ((Integer)newChar.getStats().getDextBon()).toString());
        wisdBonLab.setText("     +" + ((Integer)newChar.getStats().getWisdBon()).toString());
        inteBonLab.setText("     +" + ((Integer)newChar.getStats().getInteBon()).toString());
    }

    /*
    The following are the FXML elements for learnSpells.fxml
     */
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
    private Button learBut;

    /*
    Handle the learn spells button
     */
    @FXML
    private void handLearBut(){
        CommandProcessor cp = new CommandProcessor(newChar, null);
        //process the commands in each of the spell fields
        cp.processCommand(CommandParser.parseCommand(leve0SpelFie.getText()));
        cp.processCommand(CommandParser.parseCommand(leve1SpelFie.getText()));
        cp.processCommand(CommandParser.parseCommand(leve2SpelFie.getText()));
        cp.processCommand(CommandParser.parseCommand(leve3SpelFie.getText()));
        cp.processCommand(CommandParser.parseCommand(leve4SpelFie.getText()));
        cp.processCommand(CommandParser.parseCommand(leve5SpelFie.getText()));
        cp.processCommand(CommandParser.parseCommand(leve6SpelFie.getText()));
        cp.processCommand(CommandParser.parseCommand(leve7SpelFie.getText()));
        cp.processCommand(CommandParser.parseCommand(leve8SpelFie.getText()));
        cp.processCommand(CommandParser.parseCommand(leve9SpelFie.getText()));

        //set learn spells to false and close the stage
        Constants.LEARSPELWIN = false;
        Stage stage = (Stage) learBut.getScene().getWindow();
        stage.close();
    }

    /*
    Handle the create character button
     */
    @FXML
    private void handCreaCharBut(){
        //process all of the commands in each given field
        CommandProcessor cp = new CommandProcessor(newChar, null);
        cp.processCommand(CommandParser.parseCommand(charNameFie.getText()));
        cp.processCommand(CommandParser.parseCommand(playNameFie.getText()));
        cp.processCommands(CommandParser.parseCommands(profLangAre.getText()));
        cp.processCommand(CommandParser.parseCommand(equiAre.getText()));
        cp.processCommand(CommandParser.parseCommand(traiAre.getText()));
        cp.processCommand(CommandParser.parseCommand(backAre.getText()));
        cp.processCommand(CommandParser.parseCommand(featAre.getText()));

        //check each weapon field. If given field is popualted, add weapon
        if (!weap1NameFie.getText().equals("")){
            //build the command for a weapon
            StringBuilder command = new StringBuilder();
            command.append("$give weapon ");
            command.append(weap1NameFie.getText().replace(" ", "_") + " ");
            command.append(0 + " ");
            command.append(weap1TypeCho.getValue() + " ");
            command.append(weap1NumbDiceCho.getValue() + " ");
            command.append(weap1DieCho.getValue() + " ");
            command.append(weap1FineChe.isSelected());
            cp.processCommand(CommandParser.parseCommand(command.toString()));
        }
        if (!weap2NameFie.getText().equals("")){
            StringBuilder command = new StringBuilder();
            command.append("$give weapon ");
            command.append(weap2NameFie.getText().replace(" ", "_") + " ");
            command.append(0 + " ");
            command.append(weap2TypeCho.getValue() + " ");
            command.append(weap2NumbDiceCho.getValue() + " ");
            command.append(weap2DieCho.getValue() + " ");
            command.append(weap2FineChe.isSelected());
            cp.processCommand(CommandParser.parseCommand(command.toString()));
        }
        if (!weap3NameFie.getText().equals("")){
            StringBuilder command = new StringBuilder();
            command.append("$give weapon ");
            command.append(weap3NameFie.getText().replace(" ", "_") + " ");
            command.append(0 + " ");
            command.append(weap3TypeCho.getValue() + " ");
            command.append(weap3NumbDiceCho.getValue() + " ");
            command.append(weap3DieCho.getValue() + " ");
            command.append(weap3FineChe.isSelected());
            cp.processCommand(CommandParser.parseCommand(command.toString()));
        }

        //check armor field, if populate add armor
        if (!armoNameFie.getText().equals("")){
            //populate the given command for armor
            StringBuilder command = new StringBuilder();
            command.append("$give armor ");
            command.append(armoNameFie.getText().replace(" ", "_") + " ");
            command.append(armoACFie.getText() + " ");
            command.append("false");
            cp.processCommand(CommandParser.parseCommand(command.toString()));
        }
        //check if the armor is a sheidl
        if (shieChe.isSelected()){
            StringBuilder command = new StringBuilder();
            command.append("$give armor ");
            command.append("shield ");
            command.append(shieACFie.getText() + " ");
            command.append("true");
            cp.processCommand(CommandParser.parseCommand(command.toString()));
        }

        //save the new character to a dchar fiel to be loaded
        newChar.save();

        //send player to the player controller and navigate to the player window
        PlayerController.player = newChar;
        naviToNew(creaCharBut, Constants.PLAYWIND);
    }

    /*
    Navigate to a new window
     */
    private void naviToNew(Button but, String fileName){
        try {
            Stage stage = (Stage) but.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource(fileName));
            Scene scene = new Scene(root);
            stage.hide();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setOnCloseRequest(event -> System.exit(0));
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
            System.err.println();
            System.err.println(fileName + " Error in navigating to");
        }
    }

    /*
    The following are the FXML elements for newChar.fxml
     */
    @FXML
    private CheckBox shieChe;
    @FXML
    private TextField shieACFie;
    @FXML
    private TextField armoNameFie;
    @FXML
    private TextField armoACFie;
    @FXML
    private Button creaCharBut;
    @FXML
    private ResourceBundle resources;
    @FXML
    private Label hitpLab;
    @FXML
    private TextField charNameFie;
    @FXML
    private ChoiceBox clasCho;
    @FXML
    private ChoiceBox raceCho;
    @FXML
    private ChoiceBox backCho;
    @FXML
    private ChoiceBox aligCho;
    @FXML
    private TextField playNameFie;
    @FXML
    private TextField starLeveFie;
    @FXML
    private Label streBonLab;
    @FXML
    private Label charBonLab;
    @FXML
    private Label consBonLab;
    @FXML
    private Label inteBonLab;
    @FXML
    private Label dextBonLab;
    @FXML
    private Label wisdBonLab;
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
    private TextArea profLangAre;
    @FXML
    private TextArea equiAre;
    @FXML
    private TextArea traiAre;
    @FXML
    private TextArea backAre;
    @FXML
    private TextArea featAre;
    @FXML
    private TextField weap2NameFie;
    @FXML
    private ChoiceBox weap2TypeCho;
    @FXML
    private ChoiceBox weap2NumbDiceCho;
    @FXML
    private ChoiceBox weap2DieCho;
    @FXML
    private CheckBox weap2FineChe;
    @FXML
    private TextField weap3NameFie;
    @FXML
    private ChoiceBox weap3TypeCho;
    @FXML
    private ChoiceBox weap3NumbDiceCho;
    @FXML
    private ChoiceBox weap3DieCho;
    @FXML
    private CheckBox weap3FineChe;
    @FXML
    private TextField weap1NameFie;
    @FXML
    private ChoiceBox weap1TypeCho;
    @FXML
    private ChoiceBox weap1NumbDiceCho;
    @FXML
    private ChoiceBox weap1DieCho;
    @FXML
    private CheckBox weap1FineChe;
    @FXML
    private ChoiceBox streCho;
    @FXML
    private ChoiceBox dextCho;
    @FXML
    private ChoiceBox consCho;
    @FXML
    private ChoiceBox inteCho;
    @FXML
    private ChoiceBox wisdCho;
    @FXML
    private ChoiceBox charCho;
}
