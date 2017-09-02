package sample;

import java.util.ArrayList;

/**
 * An Stage which characters can be in.
 * Created by Tanner on 2/15/2017.
 */
public class Stage {

    private String id;
    private String name;
    private String description;
    private ArrayList<Link> links;
    private ArrayList<Obj> objects;
    private ArrayList<Stage> substages;
    private Encounter encounter;

    public Stage(){
        links = new ArrayList<>();
        objects = new ArrayList<>();
        substages = new ArrayList<>();
    }

    public Stage(String id){
        this();
        this.id = id;
    }

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

    public String openSubStage(Stage subStage) {
        for (Link l : subStage.getLinks()){
            links.add(l);
        }
        for (Obj o : subStage.getObjects()){
            objects.add(o);
        }
        for (Stage s : subStage.getSubstages()){
            substages.add(s);
        }
        String res = null;
        if (encounter == null){
            encounter = subStage.encounter;
        } else {
            res = encounter.merge(subStage.encounter);
        }
        substages.remove(subStage);
        return res;
    }

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

    public boolean equals(Object other){
        Stage o = (Stage)other;
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
     */
    public String sendStage() {
        StringBuilder sb = new StringBuilder();
        sb.append("#setstage");
        sb.append(" @name " + name);
        sb.append(" @description " + description);
        sb.append(" @objects ");
        for (Obj o : objects){
            sb.append(o.getName() + ", ");
        }
        if (objects.size() != 0)sb.delete(sb.length() - 2, sb.length());
        sb.append(" @encounter ");
        for (Monster m : encounter.getMonsters()){
            sb.append(m.getName() + ", ");
        }
        if (encounter.getMonsters().size() != 0) sb.delete(sb.length()-2, sb.length());
        return sb.toString();
    }

    public void setEncounter(Encounter encounter, boolean createMonsters) {
        if (this.encounter != null){
            for (Monster m : this.encounter.getMonsters()){
                removeMonsterObject(m);
            }
        }
        this.encounter = encounter;

        for (Monster m : this.encounter.getMonsters()){
            Obj newObj = new Obj(m.getId(), m.getName(), m.getDescription());
            objects.add(newObj);
        }
    }

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
