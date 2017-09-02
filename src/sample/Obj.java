package sample;

/**
 * An object in a room
 * Created by Tanner on 5/1/2017.
 */
public class Obj {

    private String id;
    private String name;
    private String description;

    public Obj(){

    }

    public Obj(String id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("ID : " + id + '\n');
        sb.append("Name : " + name + '\n');
        sb.append("Description : " + description);
        return sb.toString();
    }
}
