package sample;

import java.util.ArrayList;

/**
 * A stage in a dungeon, contains description
 * as well as objects, encounters, substages,
 * and links to other stages
 *
 * Created by Tanner on 2/15/2017.
 */
public class Stage {

    //the id name and description of the stage
    private String id;
    private String name;
    private String description;

    //all links to other stages
    private ArrayList<Link> links;
    //list of objects in the stage
    private ArrayList<Obj> objects;
    //all substages that can be revealed
    private ArrayList<Stage> substages;
    //an encounter in the stage
    private Encounter encounter;

    /*
    Create an empty stage
     */
    public Stage(){
        links = new ArrayList<>();
        objects = new ArrayList<>();
        substages = new ArrayList<>();
    }

    /*
    Create a string with a specified id
     */
    public Stage(String id){
        this();
        this.id = id;
    }

    /*
    Create a populated stage with inputted params as values
     */
    public Stage(String id, String name, String description, ArrayList<Link> links,
    ArrayList<Obj> objects, ArrayList<Stage> substages, Encounter encounter){
        this.id = id;
        this.name = name;
        this.description = description;
        this.links = links;
        this.objects = objects;
        this.substages = substages;
        this.encounter = encounter;
    }

    /*
    Open a substage and add it to the current stage
     */
    public String openSubStage(Stage subStage) {
        //for every link in the substage, add it to the list
        for (Link l : subStage.getLinks()){
            links.add(l);
        }

        //add all objects
        for (Obj o : subStage.getObjects()){
            objects.add(o);
        }

        //add all inner substages
        for (Stage s : subStage.getSubstages()){
            substages.add(s);
        }

        //if there isnt an encounter, add it
        String res = null;
        if (encounter == null){
            encounter = subStage.encounter;

            //otherwise merge the two encounters
        } else {
            res = encounter.merge(subStage.encounter);
        }

        //remove the used substage
        substages.remove(subStage);
        return res;
    }

    /*
    Convert the stage to a string
     */
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("ID : ");
        sb.append(id);
        sb.append('\n');
        sb.append("Name : ");
        sb.append(name);
        sb.append('\n');
        sb.append("Description : ");
        sb.append(description);
        sb.append('\n');
        sb.append("Links : ");
        for (Link l : links){
            sb.append(l.start.id + "->" + l.end.id);
            sb.append(", ");
        }
        sb.append('\n');
        sb.append("Objects : ");
        for (Obj o :objects){
            sb.append(o.getId());
            sb.append(", ");
        }
        sb.append('\n');
        sb.append("Substages");
        if (substages.size() != 0) sb.append('\n');
        for (Stage s : substages){
            sb.append(s.toString());
        }
        sb.append('\n');
        sb.append("Encounter");
        if (encounter != null){
            sb.append(encounter.toString());
        }
        return sb.toString();
    }

    /*
    Checks if two stages are equal
     */
    public boolean equals(Object other){
        Stage o = (Stage)other;
        //check matching id
        if (o.id.equals(id)){
            return true;
        }
        return false;
    }

    public ArrayList<Link> getLinks() {
        return links;
    }

    public ArrayList<Obj> getObjects() {
        return objects;
    }

    public ArrayList<Stage> getSubstages() {
        return substages;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLinks(ArrayList<Link> links) {
        this.links = links;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setObjects(ArrayList<Obj> objects) {
        this.objects = objects;
    }

    public void setSubstages(ArrayList<Stage> substages) {
        this.substages = substages;
    }

    /*
    Sends only the current stage, NO SUBSTAGES OR LINKS
    Used to send as an init command to players in a game
     */
    public String sendStage() {
        StringBuilder sb = new StringBuilder();
        //init command
        sb.append("#setstage");

        //add name and description
        sb.append(" @name " + name);
        sb.append(" @description " + description);

        //add all objects after command
        sb.append(" @objects ");
        for (Obj o : objects){
            sb.append(o.getName() + ", ");
        }
        if (objects.size() != 0)sb.delete(sb.length() - 2, sb.length());

        //add encounter
        sb.append(" @encounter ");

        //if there is an encounter, add all monsters to it
        if (encounter != null) {
            for (Monster m : encounter.getMonsters()) {
                sb.append(m.getName() + ", ");
            }
            if (encounter.getMonsters().size() != 0) sb.delete(sb.length() - 2, sb.length());
        }
        return sb.toString();
    }

    /*
    Set the current encounter, overrides any other encounter
     */
    public void setEncounter(Encounter encounter) {
        //if there is a current encounter
        if (this.encounter != null){
            //remove all monster objects
            for (Monster m : this.encounter.getMonsters()){
                removeMonsterObject(m);
            }
        }
        this.encounter = encounter;

        //for every monster in the encounter, add an object to represent
        for (Monster m : this.encounter.getMonsters()){
            Obj newObj = new Obj(m.getId(), m.getName(), m.getDescription());
            objects.add(newObj);
        }
    }

    /*
    Remove a given object that is a monster
     */
    private void removeMonsterObject(Monster m) {
        for (int i = objects.size()-1; i >= 0; i--){
            if (objects.get(i).getId().equals(m.getId())){
                objects.remove(i);
            }
        }
    }

    public Encounter getEncounter() {
        return encounter;
    }
}
