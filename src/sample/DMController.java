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

    //server for the dm for players to connect to while hosting
    private DMServer server;
    //update time to check if it should be updated every few minutes
    private Timer update;
    //true if it should be updated
    public boolean updated;
    //the current stage the dungoen is in
    public Stage currentStage;

    /*
    Empty controller fr the dm
     */
    public DMController(){

    }

    /*
    Initialization of the dm page
     */
    @FXML
    private void initialize(){

        //start the update timer
        update = new Timer();
        //settup update timer
        update.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //settup the run event with 100 ms timer
                Platform.runLater(new Runnable(){
                    public void run(){
                        //check if it is updated
                        if (updated){
                            //check if server has been iniatlized
                            if (server != null) {
                                //if it has make sure tabs are set if players are updated
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

                            //set the stage if the current stage is not null
                            if (currentStage != null){
                                setStage();
                            }

                            //turn update off
                            updated = false;
                        }
                    }
                });
            }
        }, 0, 100);
    }

    /*
    Alter the displayed stage info on the dm page
     */
    private void setStage() {
        //build the string of the info window
        StringBuilder sb = new StringBuilder();
        sb.append("Info Window" + '\n');
        sb.append('\n');
        sb.append("ID : " + currentStage.getId() + '\n' + '\n');
        sb.append("Name : " + currentStage.getName() + '\n' + '\n');
        sb.append("Description : " + currentStage.getDescription());
        infoAre.setText(sb.toString());

        //build object window string
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

        //build link window string
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

        //build the substage window string
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

        /*
        settupd the encounter window
         */
        sb = new StringBuilder();
        sb.append("Encounter Window" + '\n' + '\n');
        if (currentStage.getEncounter() != null)
            sb.append(currentStage.getEncounter().toString());
        encoAre.setText(sb.toString());

        /*
        set relevant commands
         */
        sb = new StringBuilder();
        sb.append("Commands");
        sb.append('\n');
        sb.append("@reveal subStageID" + '\n');
        sb.append("@transfer linkedStageID" + '\n');
        sb.append("@encounter" + '\n');
        sb.append("@encounterfront" + '\n');
        sb.append("@encounterback");

        commandAre.setText(sb.toString());
    }

    /*
    Handle the load dungeon button
    load a dcrawl dungeon into
     */
    @FXML
    public void handLoadDungBut(){
        //filter to only dcrawl documents
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Dungeon Documents","dcrawl");
        JFileChooser fc = new JFileChooser();
        //set current directory
        fc.setCurrentDirectory(new File("."));
        fc.setFileFilter(filter);
        int returnVal = fc.showOpenDialog(null);

        //if smething was picked load a dungeon
        if (returnVal == JFileChooser.APPROVE_OPTION) {
                File f = (fc.getSelectedFile());
                loadDung(f);
        }
    }

    //objects that can be used when a dungeon is loaded
    private ArrayList<Obj> objects;
    //stages that can be used in the dungeon when loaded
    private ArrayList<Stage> stages;
    //attacks that are loaded into the dungeon
    private ArrayList<Attack> attacks;
    //monster definitions that are defined
    private ArrayList<MonsterDefinition> monsterDefinitions;
    //encounters that are defined
    private ArrayList<Encounter> encounters;

    /*
    Load a given dungeon from a select file
     */
    private void loadDung(File f){
        //initialize the lists as blank
        objects = new ArrayList<>();
        stages = new ArrayList<>();
        attacks = new ArrayList<>();
        monsterDefinitions = new ArrayList<>();
        encounters = new ArrayList<>();

        //clean the file and return as a string
        String file = clean(f);
        //parse the file and populate all lists
        parse(file);

        //if the current stage is null, set the stage to the first one loaded
        if (currentStage == null)
            currentStage = stages.get(0);

        //if the server is not null, send init commands to all for the current stage
        //also send the current description of the stage
        if (server != null){
            server.sendInitCommand(currentStage.sendStage());
            server.sendToAll(currentStage.getDescription());
        }

        //set update to true so screen will update
        updated = true;
    }

    /*
    Handles a given inputted command in a CRAWL file
    populates necessary lists
     */
    private void handle(String line){
        //get the tag of the string
        switch(getTag(line)){
            //if the tag is import
            case "IMPORT":
                //get the file name
                String fileName = getParameter(line);
                //clean and parse the file
                File file = new File(fileName);
                String f = clean(file);
                parse(f);
                break;
            //if the tag is stages
            case "STAGES":
                //get the parameter of the line
                String params = getParameter(line);
                //get all ids of stages in the CRAWL file
                ArrayList<String> elements = getElements(params);
                //initiatlize all stages
                for (String s : elements){
                    stages.add(new Stage(s));
                }
                break;
            //if the tag is start
            case "START":
                //get the start stage id and then set current stage
                String param = getParameter(line);
                currentStage = getStage(param);
                break;
            //handle an undefined tag by reporting an error
            default:
                System.err.println("Tag Undefined : " + getTag(line));
                return;
        }
    }

    /*
    Define a specific object, monster, etc
    Inputs it into the character array
     */
    private void define(String line, String type){
        //init all fields to null
        String def = null;
        String id = null;
        String name = null;
        String description = null;

        //check if there is a definition character
        if (line.charAt(0) != '?'){
            System.err.println("No definition character ? : " + line);
            return;
        }

        //switch based on the type
        switch (type){

            //handle encounters
            case "ENCS":
                //get the id of the encounter
                id = getTag(line);
                //get the whole definition to parse
                def = getDefinition(line);

                //set undefined name and description
                name = id;
                description = "You enter an encounter.";
                int eIndex;

                //get the name if defined for the encounter, make sure not def to something else
                if (def.contains("!NAME") && notInBrackets((eIndex = def.indexOf("!NAME")), def)){
                    String s = def.substring(eIndex, def.length());
                    name = getParameter(s);
                }

                //get the description if defined
                if (def.contains("!DES") && notInBrackets((eIndex = def.indexOf("!DES")), def)){
                    String s = def.substring(eIndex, def.length());
                    description = getParameter(s);
                }

                //create monster list
                ArrayList<Monster> monsters = new ArrayList<>();
                //check if monsters are defined
                if (def.contains("!MONS") && notInBrackets((eIndex = def.indexOf("!MONS")), def)){
                    //get list of all monster
                    String s = def.substring(eIndex, def.length());
                    String params = getParameter(s);
                    //get each as an individual element
                    ArrayList<String> elements = getElements(params);
                    for (String str : elements){
                        //if a monster is undefined, define it
                        //add it either way
                        if (str.charAt(0) == '?'){
                            define(str, "MONS");
                            monsters.add(monsterDefinitions.get(monsterDefinitions.size()-1).createMonster(monsters));
                        } else {
                            monsters.add(getMonsterDefinition(str).createMonster(monsters));
                        }
                    }
                }

                //add the encounter to the list
                encounters.add(new Encounter(id, name, description, monsters));

                break;

            //handle object descriptions
            case "OBJS":
                //get the tag and definition
                id = getTag(line);
                def = getDefinition(line);

                //set default
                name = id;
                description = "There does not appear to be anything important about this object.";

                //check if name is defined
                if (def.contains("!NAME")){
                    int index = def.indexOf("!NAME");
                    String s1 = def.substring(index, def.length());
                    name = getParameter(s1);
                }

                //check if description is defined
                if (def.contains("!DES")){
                    int index = def.indexOf("!DES");
                    String s1 = def.substring(index, def.length());
                    description = getParameter(s1);
                }

                //add object
                objects.add(new Obj(id, name, description));
                break;

            //define an attack
            case "ATTS":
                //get efinitions and set default
                id = getTag(line);
                def = getDefinition(line);
                name = id;

                //check if name is defined
                if (def.contains("!NAME")){
                    int index = def.indexOf("!NAME");
                    String s = def.substring(index, def.length());
                    name = getParameter(s);
                }

                //check if hit is defined
                String hit = "0";
                if (def.contains("!HIT")){
                    int index = def.indexOf("!HIT");
                    String s = def.substring(index, def.length());
                    hit = getParameter(s);
                }

                //check if dice is defined
                String dd = "0d0";
                if (def.contains("!DD")){
                    int index = def.indexOf("!DD");
                    String s = def.substring(index, def.length());
                    dd = getParameter(s);
                }

                //check if damage is defined
                String dam = "0";
                if (def.contains("!DAM")){
                    int index = def.indexOf("!DAM");
                    String s = def.substring(index, def.length());
                    dam = getParameter(s);
                }

                //add attacks to list of attacks
                attacks.add(new Attack(id, name, hit, dd, dam));

                break;

            //define a monster
            case "MONS":
                //get id and definition, set to default
                id = getTag(line);
                def = getDefinition(line);
                name = id;
                description = "A terrifying monster that wants to kill you.";

                int mIndex;
                //check for name
                if (def.contains("!NAME") && notInBrackets(mIndex = def.indexOf("!NAME"), def)){
                    String s = def.substring(mIndex, def.length());
                    name = getParameter(s);
                }

                //check for description
                if (def.contains("!DES") && notInBrackets(mIndex = def.indexOf("!DES"), def)){
                    String s = def.substring(mIndex, def.length());
                    description = getParameter(s);
                }

                //check if ac is defined
                String ac = "0";
                if (def.contains("!AC")){
                    mIndex = def.indexOf("!AC");
                    String s = def.substring(mIndex, def.length());
                    ac = getParameter(s);
                }

                //check if hit dice was defined
                String hd = "0d0";
                if (def.contains("!HD")){
                    mIndex = def.indexOf("!HD");
                    String s = def.substring(mIndex, def.length());
                    hd = getParameter(s);
                }

                //check if hit points boost is defined
                String hp = "0";
                if (def.contains("!HP")){
                    mIndex = def.indexOf("!HP");
                    String s = def.substring(mIndex, def.length());
                    hp = getParameter(s);
                }

                //check if level is defined
                String lev = "0";
                if (def.contains("!LEV")){
                    mIndex = def.indexOf("!LEV");
                    String s = def.substring(mIndex, def.length());
                    lev = getParameter(s);
                }

                //check if attacks are defined
                ArrayList<Attack> att = new ArrayList<>();
                if (def.contains("!ATTS")){
                    //get the parameters
                    mIndex = def.indexOf("!ATTS");
                    String s = def.substring(mIndex, def.length());
                    String params = getParameter(s);

                    //get all elements
                    ArrayList<String> elements = getElements(params);
                    //for each one check if the attack is defined or needs to be
                    for (String str : elements){
                        //if its not define it
                        if (str.charAt(0) == '?'){
                            define(str, "ATTS");
                            att.add(attacks.get(attacks.size()-1));
                        } else {
                            att.add(getAttack(str));
                        }
                    }
                }

                //add to the monster definitions
                monsterDefinitions.add(new MonsterDefinition(id, name, description, ac, hd, hp, lev, att));

                break;

            //define a stage
            case "STAGES":
                //get correct stage from predefined stages
                Stage stage = getStage(getTag(line));
                //get the definition and set defaults
                def = getDefinition(line);
                name = getTag(line);
                stage.setName(name);
                description = "There is nothing of importance in this area.";
                stage.setDescription(description);

                int index;
                //check if name is defined
                if (def.contains("!NAME") && notInBrackets(index = def.indexOf("!NAME"), def)){
                    String s = def.substring(index, def.length());
                    name = getParameter(s);
                    stage.setName(name);
                }

                //check if description is defined
                if (def.contains("!DES") && notInBrackets(index = def.indexOf("!DES"), def)){
                    String s = def.substring(index, def.length());
                    description = getParameter(s);
                    stage.setDescription(description);
                }

                //init link list
                ArrayList<Link> links = new ArrayList<>();

                //check if links are defined
                if (def.contains("!LINKS") && notInBrackets(index = def.indexOf("!LINKS"), def)){
                    String s = def.substring(index, def.length());
                    String params = getParameter(s);
                    //get all elements and define each as a link
                    ArrayList<String> elements = getElements(params);
                    for (String str : elements){
                        links.add(new Link(stage, getStage(str)));
                    }
                    stage.setLinks(links);
                }

                //init object list
                ArrayList<Obj> objs = new ArrayList<>();

                //check if objects are defined
                if (def.contains("!OBJS") && notInBrackets(index = def.indexOf("!OBJS"), def)){
                    //get all elements
                    String s = def.substring(index, def.length());
                    String params = getParameter(s);
                    ArrayList<String> elements = getElements(params);
                    //for each one either add predefined or define it
                    for (String str : elements){
                        if (str.charAt(0) == '?'){
                            define(str, "OBJS");
                            objs.add(objects.get(objects.size()-1));
                        } else {
                            objs.add(getObj(str));
                        }
                    }
                    //set the objects in the stage
                    stage.setObjects(objs);
                }

                //define an empty encounter
                Encounter enc = new Encounter("", "", "", new ArrayList<Monster>());
                //check if encounter is defined
                if (def.contains("!ENCS") && notInBrackets(index = def.indexOf("!ENCS"), def)){
                    //get all paremeters
                    String s = def.substring(index, def.length());
                    String param = getParameter(s);
                    //add predefined or define encounter and add
                    if (param.charAt(0) == '?'){
                        define(param, "ENCS");
                        enc = encounters.get(encounters.size()-1);
                    } else {
                        enc = getEncounter(param);
                    }
                    stage.setEncounter(enc);
                }

                //set substages and check defined
                ArrayList<Stage> subStages = new ArrayList<>();
                if (def.contains("!SUBSTAGES") && notInBrackets(index = def.indexOf("!SUBSTAGES"), def)){
                    //get definition and all elements
                    String s = def.substring(index, def.length());
                    String params = getParameter(s);
                    ArrayList<String> elements = getElements(params);
                    //add to the substage to the stages and define it
                    for (String str : elements){
                        String tag = getTag(str);
                        stages.add(new Stage(tag));
                        define(str, "STAGES");
                        //add to substage list
                        subStages.add(stages.get(stages.size()-1));
                    }
                    //set substage list
                    stage.setSubstages(subStages);
                }
                break;

            default:
                System.err.println("Do not recognize type : " + type);
        }
    }

    /*
    Get an encounter based on an entered id
     */
    private Encounter getEncounter(String param) {
        for (int i = encounters.size()-1; i >= 0; i--){
            if (encounters.get(i).getId().equals(param)){
                return encounters.get(i);
            }
        }
        System.err.println("Could not find specified encounter : " + param);
        return null;
    }

    /*
    get a monster definition based on an entered id
     */
    private MonsterDefinition getMonsterDefinition(String str) {
        for (int i = monsterDefinitions.size()-1; i>= 0; i--){
            if (monsterDefinitions.get(i).getId().equals(str)){
                return monsterDefinitions.get(i);
            }
        }
        System.err.println("Could not find specified monster definition : " + str);
        return null;
    }

    /*
    get attack that matches the id
     */
    private Attack getAttack(String str) {
        for (int i = attacks.size()-1; i >= 0; i--){
            if (attacks.get(i).getId().equals(str)){
                return attacks.get(i);
            }
        }
        System.err.println("Could not find specified attack : " + str);
        return null;
    }

    //check if an index in a string is inside other brackets
    private boolean notInBrackets(int i, String str) {
        int indexClosed = str.indexOf('}', i);
        if (indexClosed == -1) return true;
        int indexOpen = str.indexOf('{', i);
        if (indexOpen != -1 && indexOpen < indexClosed){
            return true;
        }
        return false;
    }

    /*
    get object that matches the string
     */
    private Obj getObj(String str) {
        for (int i = objects.size()-1; i >= 0; i--){
            if (objects.get(i).getId().equals(str)){
                return objects.get(i);
            }
        }
        System.err.println("Could not find specified object : " + str);
        return null;
    }

    /*
    get the stage that matches the id
     */
    private Stage getStage(String tag) {
        for (int i = stages.size()-1; i >= 0; i--){
            if (stages.get(i).getId().equals(tag)){
                return stages.get(i);
            }
        }
        System.err.println("Could not find stage : " + tag);
        return null;
    }

    /*
    Get all elements defined within a [] in a string
     */
    private ArrayList<String> getElements(String line){
        ArrayList<String> res = new ArrayList<>();
        while(line.contains("[")){
            //get everything enclosed by that
            res.add(getEnclosed(line, "[", "]"));
            //build string and delete excess charars
            StringBuilder sb = new StringBuilder(line);
            sb.delete(0, line.indexOf("]",res.get(res.size()-1).length()) + 1);
            line = sb.toString();
        }
        //return the result
        return res;
    }

    //get a param enclosed by <>
    private String getParameter(String line){
        return getEnclosed(line, "<", ">");
    }

    //get a definitions enclosed in {}
    private String getDefinition(String line){
        return getEnclosed(line, "{", "}");
    }

    //get something enclosed by a start and end character in the given line
    private String getEnclosed(String line, String startChar, String endChar){
        //find start and end character
        int start = line.indexOf(startChar)+1;
        int end = line.indexOf(endChar);
        if (start == 0){
            System.err.println("Cannot find start character " + startChar + " : " + line);
        }
        if (end == 0){
            System.err.println("Cannot find end character " + endChar + " : " + line);
        }

        //get the first substring
        String attempt = line.substring(start, end).trim();
        //check the number of starts and ends in the given substring
        int numStarts = attempt.length() - attempt.replace(startChar, "").length();
        int numEnds = attempt.length() - attempt.replace(endChar, "").length();
        //while they are not eqal
        while (numStarts != numEnds){
            //advance to the next enclosed bracket
            end = line.indexOf(endChar, end+1);
            if (end == -1){
                System.err.println("Unbalanced Expression : " + line + " " + numStarts + "/" + numEnds);
            }

            //reset and continue loop
            attempt = line.substring(start, end).trim();
            numStarts = attempt.length() - attempt.replace(startChar, "").length();
            numEnds = attempt.length() - attempt.replace(endChar, "").length();
        }
        return attempt;
    }

    //get the given tag of a line
    private String getTag(String line){
        //check what the start char is based on def or declare
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

    /*
    parse a given cleaned file adds to all defined files
     */
    private void parse(String file){
        //checks if what is being parsed is a datafile
        boolean dataFile = file.startsWith("!TYPE");
        String dataType = null;
        //if its a data file
        if (dataFile){
            //get what type of file it is
            int start = file.indexOf('<')+1;
            int end = file.indexOf('>');
            if (start == 0 || end == -1){
                System.err.println("Cannot find type declaration for data file");
                return;
            }
            //get the data type and the rest of the file
            dataType = file.substring(start, end).trim();
            file = file.substring(end+1, file.length()).trim();
        }

        //while there is still more file to be parsed
        while(file.length() != 0){
            //get the current definitin character
            char defChar = file.charAt(0);
            String startChar = null;
            String endChar = null;
            switch (defChar){
                //check the start and end char
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
            //runs the get enclosed method using the start and end char
            int end = file.indexOf(endChar);
            String attempt = file.substring(0, end+1).trim();
            int numStarts = attempt.length() - attempt.replace(startChar, "").length();
            int numEnds = attempt.length() - attempt.replace(endChar, "").length();
            //loop until the start and end are equal
            while (numStarts != numEnds){
                end = file.indexOf(endChar, end+1);
                if (end == -1){
                    System.err.println("Unbalanced Expression : " + file + " " + numStarts + "/" + numEnds);
                }
                attempt = file.substring(0, end+1).trim();
                numStarts = attempt.length() - attempt.replace(startChar, "").length();
                numEnds = attempt.length() - attempt.replace(endChar, "").length();
            }
            //reduce side of the file
            file = file.substring(end+1, file.length()).trim();

            //if its not a datafile, it is made of stages.
            //then either hand the current declaration
            //or it is a new stage and must be defined
            if (!dataFile) {
                if (defChar == '!') {
                    handle(attempt);
                } else if (defChar == '?') {
                    define(attempt, "STAGES");
                }

            //else define it as the data type it was said to be
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
        //create empty string builder
        StringBuilder res = new StringBuilder();
        Scanner s = null;
        //define scanner
        try {
            s = new Scanner(f);
        } catch (FileNotFoundException e) {
            System.err.println("File to clean (Dungeon File) could not be found : " + f.toString());
        }
        if (s == null){
            System.err.println("Failed cleaning of file : " + f.toString());
            return null;
        }

        //while there is still a line
        while(s.hasNext()){
            //unless its a comment add to the string build
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

    /*
    Initatlized the server once the host button is pressed
     */
    @FXML
    public void handHostBut(){
        if (server == null){
            server = new DMServer(5000, commAre, this);
        }
    }

    /*
    Set the first tab with all information
    that a character has
     */
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

    /*
    set second tab
     */
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

    /*
    set third tab
     */
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

    /*
    set fourth tab
     */
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

    /*
    All FXML objects delcared
     */
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

    /*
    Event for handling the enter button
     */
    @FXML
    public void onEnterSend(ActionEvent ae){
        //get the text from the enter field
        String s = enteFie.getText();
        //clear field
        enteFie.clear();
        if (s.length() == 0) return;
        //if it is a stage command, handle it
        if (s.trim().startsWith("@")){
            handleStageCommand(s);
            return;
        }
        //if its clear just clear the screen
        if (s == "$clear") commAre.clear();

        //otherwise if there is a server, send it out
        else {
            if (server != null) {
                server.handle("DM: " + s, 0);
            }
        }
    }

    /*
    Handle a command given by the dm for the stage
     */
    private void handleStageCommand(String s) {
        //get the actual command and params
        Scanner scan = new Scanner(s);
        String actualCommand = CommandInterpreter.interpret(scan.next());
        if (actualCommand == null)return;
        while(scan.hasNext()){
            actualCommand = actualCommand + " " + scan.next();
        }

        //parse the actual command string into a command
        Command com = CommandParser.parseCommand(actualCommand);
        String action = null;

        //based on what the command is switch to different actions
        switch (com.getCommand()){
            //if it is a transfer
            case TRANSFER:
                //get the link it refers to
                Link link = CommandInterpreter.interpretLinks(com.getArgs().get(0), currentStage.getLinks());
                //handle the linked action
                action = new String("You enter the " + link.end.getName() + ".");
                currentStage = link.end;
                updated = true;

                //send the command to update all in server
                if (server != null){
                    server.sendToAll(action);
                    server.sendInitCommand(currentStage.sendStage());
                }
                break;

            //reveal reveals a given substage
            case REVEAL:
                //get the substage that is being revealed
                Stage subStage = CommandInterpreter.interpretSubStage(com.getArgs().get(0), currentStage.getSubstages());
                if (subStage == null) return;
                action = subStage.getDescription();
                //action2 is not null if there is currently an encounter
                String action2 = currentStage.openSubStage(subStage);
                updated = true;

                //send substage description as well as the init command
                if (server != null){
                    server.sendToAll(action);
                    server.sendInitCommand(currentStage.sendStage());
                    if (action2 != null){
                        server.sendToAll(action2);
                        //TODO send enounter init command
                    }
                }
                break;

            //if it is an encounter handle
            //TODO ALL ENCOUNTER COMMANDS ARE NOT FULLY IMPLEMENTED YET
            case ENCOUNTER:
                action = currentStage.getEncounter().getDescription();
                if (server != null){
                    server.sendToAll(action);
                    action = currentStage.getEncounter().enter(server.players);
                    server.sendToAll(action);
                }
                break;
            //add players to the back of encounter initative
            case ENCOUNTER_BACK:
                action = currentStage.getEncounter().getDescription();
                if (server != null){
                    server.sendToAll(action);
                    action = currentStage.getEncounter().enterBack(server.players);
                    server.sendToAll(action);
                }
                break;
            //add players to the front of the initiative
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


    /*
    Handles a given command by a player
     */
    public void handleCommand(String c) {
        Scanner s = new Scanner(c);
        //get the player name
        String playerName = s.next().trim();
        playerName = playerName.substring(0, playerName.length()-1);

        //get the actual command
        String attemptCommand = s.next().trim();
        String actualCommand = CommandInterpreter.interpret(attemptCommand);

        //add the rest
        StringBuilder sb = new StringBuilder();
        while(s.hasNext()){
            sb.append(" ");
            sb.append(s.next());
        }

        //see if command can be interpreetted
        if (actualCommand == null){
            server.sendToAll("Could not Interpret command : " + c);
            return;
        } else {
            //add params and parse the command
            actualCommand = actualCommand + sb.toString();
            Command com = CommandParser.parseCommand(actualCommand);

            //process the command in the given stage
            CommandProcessor proc = new CommandProcessor(getCharacter(playerName), currentStage);
            ArrayList<String> res = proc.processCommand(com);

            //send the result to all players in the game
            for (String str : res){
                server.sendToAll(str);
            }
        }
    }

    /*
    Get the character with the given player name
     */
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
