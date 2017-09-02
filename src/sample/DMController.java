package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.soap.Text;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Controller for the dmWindow
 * Created by Tanner on 3/2/2017.
 */
public class DMController {

    private DMServer server;
    private Timer update;
    public boolean updated;
    public Stage currentStage;

    public DMController(){

    }

    @FXML
    private void initialize(){

        update = new Timer();
        //settup update timer
        update.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable(){
                    public void run(){
                        if (updated){
                            if (server != null) {
                                if (server.players.size() > 0) {
                                    setTab1(server.players.get(0).character);
                                }
                                if (server.players.size() > 1) {
                                    setTab2(server.players.get(1).character);
                                }
                                if (server.players.size() > 2) {
                                    setTab3(server.players.get(2).character);
                                }
                                if (server.players.size() > 3) {
                                    setTab4(server.players.get(3).character);
                                }
                            }

                            if (currentStage != null){
                                setStage();
                            }

                            updated = false;
                        }
                    }
                });
            }
        }, 0, 100);
    }

    private void setStage() {
        StringBuilder sb = new StringBuilder();
        sb.append("Info Window" + '\n');
        sb.append('\n');
        sb.append("ID : " + currentStage.getId() + '\n' + '\n');
        sb.append("Name : " + currentStage.getName() + '\n' + '\n');
        sb.append("Description : " + currentStage.getDescription());
        infoAre.setText(sb.toString());

        sb = new StringBuilder();
        sb.append("Object Window" + '\n' + '\n');
        for (int i = 0; i < currentStage.getObjects().size(); i++){
            Obj o = currentStage.getObjects().get(i);
            sb.append("ID : " + o.getId() + '\n');
            sb.append("Name : " + o.getName() + '\n');
            sb.append("Description : " + o.getDescription());
            if (i != currentStage.getObjects().size()-1){
                sb.append('\n');
                sb.append('\n');
            }
        }
        objeAre.setText(sb.toString());

        sb = new StringBuilder();
        sb.append("Link Window" + '\n' + '\n');
        for (int i = 0; i < currentStage.getLinks().size(); i++){
            Link l = currentStage.getLinks().get(i);
            sb.append(l.start.getId() + " -> " + l.end.getId());
            if (i != currentStage.getLinks().size()-1){
                sb.append("\n");
            }
        }
        linkAre.setText(sb.toString());

        sb = new StringBuilder();
        sb.append("SubStage Window" + '\n' + '\n');
        for (int i = 0; i < currentStage.getSubstages().size(); i++){
            Stage subStage = currentStage.getSubstages().get(i);
            sb.append(subStage.toString());
            if (i != currentStage.getSubstages().size()-1){
                sb.append('\n');
                sb.append('\n');
            }
        }
        subsAre.setText(sb.toString());

        sb = new StringBuilder();
        sb.append("Encounter Window" + '\n' + '\n');
        if (currentStage.getEncounter() != null)
            sb.append(currentStage.getEncounter().toString());
        encoAre.setText(sb.toString());

        sb = new StringBuilder();
        sb.append("Commands");
        sb.append('\n');
        sb.append("@reveal subStageID" + '\n');
        sb.append("@transfer linkedStageID" + '\n');
        sb.append("@encounter" + '\n');
        sb.append("@encounterfront" + '\n');
        sb.append("@encounterback");
    }

    @FXML
    public void handLoadDungBut(){
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Dungeon Documents","dcrawl");
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File("."));
        fc.setFileFilter(filter);
        int returnVal = fc.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
                File f = (fc.getSelectedFile());
                loadDung(f);
        }
    }

    //objects that can be used when a dungeon is loaded
    private ArrayList<Obj> objects;
    private ArrayList<Stage> stages;
    private ArrayList<Attack> attacks;
    private ArrayList<MonsterDefinition> monsterDefinitions;
    private ArrayList<Encounter> encounters;

    private void loadDung(File f){
        objects = new ArrayList<>();
        stages = new ArrayList<>();
        attacks = new ArrayList<>();
        monsterDefinitions = new ArrayList<>();
        encounters = new ArrayList<>();

        String file = clean(f);
        parse(file);

        if (currentStage == null)
            currentStage = stages.get(0);

        if (server != null){
            server.sendInitCommand(currentStage.sendStage());
            server.sendToAll(currentStage.getDescription());
        }

        updated = true;
    }

    private void handle(String line){
        switch(getTag(line)){
            case "IMPORT":
                String fileName = getParameter(line);
                File file = new File(fileName);
                String f = clean(file);
                parse(f);
                break;
            case "STAGES":
                String params = getParameter(line);
                ArrayList<String> elements = getElements(params);
                for (String s : elements){
                    stages.add(new Stage(s));
                }
                break;
            case "START":
                String param = getParameter(line);
                currentStage = getStage(param);
                break;
            default:
                System.err.println("Tag Undefined : " + getTag(line));
                return;
        }
    }

    private void define(String line, String type){
        String def = null;
        String id = null;
        String name = null;
        String description = null;

        if (line.charAt(0) != '?'){
            System.err.println("No definition character ? : " + line);
            return;
        }

        switch (type){

            case "ENCS":
                id = getTag(line);
                def = getDefinition(line);

                name = id;
                description = "You enter an encounter.";
                int eIndex;

                if (def.contains("!NAME") && notInBrackets((eIndex = def.indexOf("!NAME")), def)){
                    String s = def.substring(eIndex, def.length());
                    name = getParameter(s);
                }

                if (def.contains("!DES") && notInBrackets((eIndex = def.indexOf("!DES")), def)){
                    String s = def.substring(eIndex, def.length());
                    description = getParameter(s);
                }

                ArrayList<Monster> monsters = new ArrayList<>();
                if (def.contains("!MONS") && notInBrackets((eIndex = def.indexOf("!MONS")), def)){
                    String s = def.substring(eIndex, def.length());
                    String params = getParameter(s);
                    ArrayList<String> elements = getElements(params);
                    for (String str : elements){
                        if (str.charAt(0) == '?'){
                            define(str, "MONS");
                            monsters.add(monsterDefinitions.get(monsterDefinitions.size()-1).createMonster(monsters));
                        } else {
                            monsters.add(getMonsterDefinition(str).createMonster(monsters));
                        }
                    }
                }

                encounters.add(new Encounter(id, name, description, monsters));

                break;

            case "OBJS":
                id = getTag(line);
                def = getDefinition(line);

                name = id;
                description = "There does not appear to be anything important about this object.";

                if (def.contains("!NAME")){
                    int index = def.indexOf("!NAME");
                    String s1 = def.substring(index, def.length());
                    name = getParameter(s1);
                }

                if (def.contains("!DES")){
                    int index = def.indexOf("!DES");
                    String s1 = def.substring(index, def.length());
                    description = getParameter(s1);
                }

                objects.add(new Obj(id, name, description));
                break;

            case "ATTS":
                id = getTag(line);
                def = getDefinition(line);
                name = id;
                if (def.contains("!NAME")){
                    int index = def.indexOf("!NAME");
                    String s = def.substring(index, def.length());
                    name = getParameter(s);
                }

                String hit = "0";
                if (def.contains("!HIT")){
                    int index = def.indexOf("!HIT");
                    String s = def.substring(index, def.length());
                    hit = getParameter(s);
                }

                String dd = "0d0";
                if (def.contains("!DD")){
                    int index = def.indexOf("!DD");
                    String s = def.substring(index, def.length());
                    dd = getParameter(s);
                }

                String dam = "0";
                if (def.contains("!DAM")){
                    int index = def.indexOf("!DAM");
                    String s = def.substring(index, def.length());
                    dam = getParameter(s);
                }

                attacks.add(new Attack(id, name, hit, dd, dam));

                break;

            case "MONS":
                id = getTag(line);
                def = getDefinition(line);
                name = id;
                description = "A terrifying monster that wants to kill you.";

                int mIndex;
                if (def.contains("!NAME") && notInBrackets(mIndex = def.indexOf("!NAME"), def)){
                    String s = def.substring(mIndex, def.length());
                    name = getParameter(s);
                }

                if (def.contains("!DES") && notInBrackets(mIndex = def.indexOf("!DES"), def)){
                    String s = def.substring(mIndex, def.length());
                    description = getParameter(s);
                }

                String ac = "0";
                if (def.contains("!AC")){
                    mIndex = def.indexOf("!AC");
                    String s = def.substring(mIndex, def.length());
                    ac = getParameter(s);
                }

                String hd = "0d0";
                if (def.contains("!HD")){
                    mIndex = def.indexOf("!HD");
                    String s = def.substring(mIndex, def.length());
                    hd = getParameter(s);
                }

                String hp = "0";
                if (def.contains("!HP")){
                    mIndex = def.indexOf("!HP");
                    String s = def.substring(mIndex, def.length());
                    hp = getParameter(s);
                }

                String lev = "0";
                if (def.contains("!LEV")){
                    mIndex = def.indexOf("!LEV");
                    String s = def.substring(mIndex, def.length());
                    lev = getParameter(s);
                }

                ArrayList<Attack> att = new ArrayList<>();
                if (def.contains("!ATTS")){
                    mIndex = def.indexOf("!ATTS");
                    String s = def.substring(mIndex, def.length());
                    String params = getParameter(s);
                    ArrayList<String> elements = getElements(params);
                    for (String str : elements){
                        if (str.charAt(0) == '?'){
                            define(str, "ATTS");
                            att.add(attacks.get(attacks.size()-1));
                        } else {
                            att.add(getAttack(str));
                        }
                    }
                }

                monsterDefinitions.add(new MonsterDefinition(id, name, description, ac, hd, hp, lev, att));

                break;

            case "STAGES":
                Stage stage = getStage(getTag(line));
                def = getDefinition(line);
                name = getTag(line);
                stage.setName(name);
                description = "There is nothing of importance in this area.";
                stage.setDescription(description);

                int index;
                if (def.contains("!NAME") && notInBrackets(index = def.indexOf("!NAME"), def)){
                    String s = def.substring(index, def.length());
                    name = getParameter(s);
                    stage.setName(name);
                }

                if (def.contains("!DES") && notInBrackets(index = def.indexOf("!DES"), def)){
                    String s = def.substring(index, def.length());
                    description = getParameter(s);
                    stage.setDescription(description);
                }

                ArrayList<Link> links = new ArrayList<>();

                if (def.contains("!LINKS") && notInBrackets(index = def.indexOf("!LINKS"), def)){
                    String s = def.substring(index, def.length());
                    String params = getParameter(s);
                    ArrayList<String> elements = getElements(params);
                    for (String str : elements){
                        links.add(new Link(stage, getStage(str)));
                    }
                    stage.setLinks(links);
                }

                ArrayList<Obj> objs = new ArrayList<>();

                if (def.contains("!OBJS") && notInBrackets(index = def.indexOf("!OBJS"), def)){
                    String s = def.substring(index, def.length());
                    String params = getParameter(s);
                    ArrayList<String> elements = getElements(params);
                    for (String str : elements){
                        if (str.charAt(0) == '?'){
                            define(str, "OBJS");
                            objs.add(objects.get(objects.size()-1));
                        } else {
                            objs.add(getObj(str));
                        }
                    }
                    stage.setObjects(objs);
                }

                Encounter enc = new Encounter("", "", "", new ArrayList<Monster>());
                if (def.contains("!ENCS") && notInBrackets(index = def.indexOf("!ENCS"), def)){
                    String s = def.substring(index, def.length());
                    String param = getParameter(s);
                    if (param.charAt(0) == '?'){
                        define(param, "ENCS");
                        enc = encounters.get(encounters.size()-1);
                    } else {
                        enc = getEncounter(param);
                    }
                    stage.setEncounter(enc, true);
                }

                ArrayList<Stage> subStages = new ArrayList<>();
                if (def.contains("!SUBSTAGES") && notInBrackets(index = def.indexOf("!SUBSTAGES"), def)){
                    String s = def.substring(index, def.length());
                    String params = getParameter(s);
                    ArrayList<String> elements = getElements(params);
                    for (String str : elements){
                        String tag = getTag(str);
                        stages.add(new Stage(tag));
                        define(str, "STAGES");
                        subStages.add(stages.get(stages.size()-1));
                    }
                    stage.setSubstages(subStages);
                }
                break;

            default:
                System.err.println("Do not recognize type : " + type);
        }
    }

    private Encounter getEncounter(String param) {
        for (int i = encounters.size()-1; i >= 0; i--){
            if (encounters.get(i).getId().equals(param)){
                return encounters.get(i);
            }
        }
        System.err.println("Could not find specified encounter : " + param);
        return null;
    }

    private MonsterDefinition getMonsterDefinition(String str) {
        for (int i = monsterDefinitions.size()-1; i>= 0; i--){
            if (monsterDefinitions.get(i).getId().equals(str)){
                return monsterDefinitions.get(i);
            }
        }
        System.err.println("Could not find specified monster definition : " + str);
        return null;
    }

    private Attack getAttack(String str) {
        for (int i = attacks.size()-1; i >= 0; i--){
            if (attacks.get(i).getId().equals(str)){
                return attacks.get(i);
            }
        }
        System.err.println("Could not find specified attack : " + str);
        return null;
    }

    private boolean notInBrackets(int i, String str) {
        int indexClosed = str.indexOf('}', i);
        if (indexClosed == -1) return true;
        int indexOpen = str.indexOf('{', i);
        if (indexOpen != -1 && indexOpen < indexClosed){
            return true;
        }
        return false;
    }

    private Obj getObj(String str) {
        for (int i = objects.size()-1; i >= 0; i--){
            if (objects.get(i).getId().equals(str)){
                return objects.get(i);
            }
        }
        System.err.println("Could not find specified object : " + str);
        return null;
    }

    private Stage getStage(String tag) {
        for (int i = stages.size()-1; i >= 0; i--){
            if (stages.get(i).getId().equals(tag)){
                return stages.get(i);
            }
        }
        System.err.println("Could not find stage : " + tag);
        return null;
    }

    private ArrayList<String> getElements(String line){
        ArrayList<String> res = new ArrayList<>();
        while(line.contains("[")){
            res.add(getEnclosed(line, "[", "]"));
            StringBuilder sb = new StringBuilder(line);
            sb.delete(0, line.indexOf("]",res.get(res.size()-1).length()) + 1);
            line = sb.toString();
        }
        return res;
    }

    private String getParameter(String line){
        return getEnclosed(line, "<", ">");
    }

    private String getDefinition(String line){
        return getEnclosed(line, "{", "}");
    }

    private String getEnclosed(String line, String startChar, String endChar){
        int start = line.indexOf(startChar)+1;
        int end = line.indexOf(endChar);
        if (start == 0){
            System.err.println("Cannot find start character " + startChar + " : " + line);
        }
        if (end == 0){
            System.err.println("Cannot find end character " + endChar + " : " + line);
        }

        String attempt = line.substring(start, end).trim();
        int numStarts = attempt.length() - attempt.replace(startChar, "").length();
        int numEnds = attempt.length() - attempt.replace(endChar, "").length();
        while (numStarts != numEnds){
            end = line.indexOf(endChar, end+1);
            if (end == -1){
                System.err.println("Unbalanced Expression : " + line + " " + numStarts + "/" + numEnds);
            }
            attempt = line.substring(start, end).trim();
            numStarts = attempt.length() - attempt.replace(startChar, "").length();
            numEnds = attempt.length() - attempt.replace(endChar, "").length();
        }
        return attempt;
    }

    private String getTag(String line){
        char startChar = 0;
        if (line.charAt(0) == '?'){
            startChar = '{';
        } else if (line.charAt(0) == '!'){
            startChar = '<';
        } else {
            System.err.println("No Tag starter exists : " + line);
            return null;
        }

        int index = line.indexOf(startChar);
        if (index == -1){
            System.err.println("Could not find Tag ending " + startChar + " : " + line);
            return null;
        }
        return line.substring(1, index).trim();
    }

    private void parse(String file){
        boolean dataFile = file.startsWith("!TYPE");
        String dataType = null;
        if (dataFile){
            int start = file.indexOf('<')+1;
            int end = file.indexOf('>');
            if (start == 0 || end == -1){
                System.err.println("Cannot find type declaration for data file");
                return;
            }
            dataType = file.substring(start, end).trim();
            file = file.substring(end+1, file.length()).trim();
        }

        while(file.length() != 0){
            char defChar = file.charAt(0);
            String startChar = null;
            String endChar = null;
            switch (defChar){
                case '?':
                    startChar = "{";
                    endChar = "}";
                    break;
                case '!':
                    startChar = "<";
                    endChar = ">";
                    break;
                default:
                    System.err.println("Defining symbol not recognized : " + defChar);
                    return;
            }
            int end = file.indexOf(endChar);
            String attempt = file.substring(0, end+1).trim();
            int numStarts = attempt.length() - attempt.replace(startChar, "").length();
            int numEnds = attempt.length() - attempt.replace(endChar, "").length();
            while (numStarts != numEnds){
                end = file.indexOf(endChar, end+1);
                if (end == -1){
                    System.err.println("Unbalanced Expression : " + file + " " + numStarts + "/" + numEnds);
                }
                attempt = file.substring(0, end+1).trim();
                numStarts = attempt.length() - attempt.replace(startChar, "").length();
                numEnds = attempt.length() - attempt.replace(endChar, "").length();
            }
            file = file.substring(end+1, file.length()).trim();

            if (!dataFile) {
                if (defChar == '!') {
                    handle(attempt);
                } else if (defChar == '?') {
                    define(attempt, "STAGES");
                }
            } else {
                if (defChar == '!'){
                    handle(attempt);
                } else {
                    define(attempt, dataType);
                }
            }
        }
    }

    /*
    Returns the cleaned file without any comments in it.
     */
    private String clean(File f){
        StringBuilder res = new StringBuilder();
        Scanner s = null;
        try {
            s = new Scanner(f);
        } catch (FileNotFoundException e) {
            System.err.println("File to clean (Dungeon File) could not be found : " + f.toString());
        }
        if (s == null){
            System.err.println("Failed cleaning of file : " + f.toString());
            return null;
        }

        while(s.hasNext()){
            String line = s.nextLine().trim();
            int commentInd = line.indexOf('#');
            if (commentInd != -1){
                line = line.substring(0, commentInd).trim();
            }
            if (line.length() != 0){
                res.append(line);
                res.append(" ");
            }
        }
        s.close();
        return res.toString();
    }

    @FXML
    public void handHostBut(){
        if (server == null){
            server = new DMServer(5000, commAre, this);
        }
    }

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
    private TextArea infoAre;
    @FXML
    private TextArea objeAre;
    @FXML
    private TextArea linkAre;
    @FXML
    private TextArea subsAre;
    @FXML
    private TextArea encoAre;
    @FXML
    private TextField enteFie;
    @FXML
    private TextArea commAre;
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
    @FXML
    private TextArea commandAre;

    @FXML
    public void onEnterSend(ActionEvent ae){
        String s = enteFie.getText();
        enteFie.clear();
        if (s.length() == 0) return;
        if (s.trim().startsWith("@")){
            handleStageCommand(s);
            return;
        }
        if (s == "$clear") commAre.clear();
        else {
            if (server != null) {
                server.handle("DM: " + s, 0);
            }
        }
    }

    private void handleStageCommand(String s) {
        Scanner scan = new Scanner(s);
        String actualCommand = CommandInterpreter.interpret(scan.next());
        if (actualCommand == null)return;
        while(scan.hasNext()){
            actualCommand = actualCommand + " " + scan.next();
        }
        Command com = CommandParser.parseCommand(actualCommand);
        String action = null;
        switch (com.getCommand()){
            case TRANSFER:
                Link link = CommandInterpreter.interpretLinks(com.getArgs().get(0), currentStage.getLinks());
                action = new String("You enter the " + link.end.getName() + ".");
                currentStage = link.end;
                updated = true;
                if (server != null){
                    server.sendToAll(action);
                    server.sendInitCommand(currentStage.sendStage());
                }
                break;

            case REVEAL:
                Stage subStage = CommandInterpreter.interpretSubStage(com.getArgs().get(0), currentStage.getSubstages());
                if (subStage == null) return;
                action = subStage.getDescription();
                //action2 is not null if there is currently an encounter
                String action2 = currentStage.openSubStage(subStage);
                updated = true;
                if (server != null){
                    server.sendToAll(action);
                    server.sendInitCommand(currentStage.sendStage());
                    if (action2 != null){
                        server.sendToAll(action2);
                        //TODO send enounter init command
                    }
                }
                break;

            case ENCOUNTER:
                action = currentStage.getEncounter().getDescription();
                if (server != null){
                    server.sendToAll(action);
                    action = currentStage.getEncounter().enter(server.players);
                    server.sendToAll(action);
                }
                break;
            case ENCOUNTER_BACK:
                action = currentStage.getEncounter().getDescription();
                if (server != null){
                    server.sendToAll(action);
                    action = currentStage.getEncounter().enterBack(server.players);
                    server.sendToAll(action);
                }
                break;
            case ENCOUNTER_FRONT:
                action = currentStage.getEncounter().getDescription();
                if (server != null){
                    server.sendToAll(action);
                    action = currentStage.getEncounter().enterFront(server.players);
                    server.sendToAll(action);
                }
                break;
        }
    }


    public void handleCommand(String c) {
        Scanner s = new Scanner(c);
        String playerName = s.next().trim();
        playerName = playerName.substring(0, playerName.length()-1);
        String attemptCommand = s.next().trim();
        String actualCommand = CommandInterpreter.interpret(attemptCommand);

        StringBuilder sb = new StringBuilder();
        while(s.hasNext()){
            sb.append(" ");
            sb.append(s.next());
        }

        if (actualCommand == null){
            server.sendToAll("Could not Interpret command : " + c);
            return;
        } else {
            actualCommand = actualCommand + sb.toString();
            Command com = CommandParser.parseCommand(actualCommand);
            CommandProcessor proc = new CommandProcessor(getCharacter(playerName), currentStage);
            ArrayList<String> res = proc.processCommand(com);
            for (String str : res){
                server.sendToAll(str);
            }
        }
    }

    private Character getCharacter(String playerName) {
        for (DMServer.Player p : server.players){
            if (p.character.getName().equals(playerName)){
                return p.character;
            }
        }
        System.err.println("Could not find character : " + playerName);
        return null;
    }
}
