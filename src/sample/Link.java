package sample;

/**
 * Links two stages together so they can be
 * transfered between
 * Created by Tanner on 3/22/2017.
 */
public class Link {

    Stage start;
    Stage end;

    public Link(Stage s, Stage e){
        this.start = s;
        this.end = e;
    }
}
