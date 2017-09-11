package sample;

import javafx.scene.control.TextArea;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;

/**
 * Handles communication between the player(client) and the dm(server)
 * Created by Tanner on 3/2/2017.
 */
public class PlayerClient {

    /*
    Class for a given player,
    sorta replication of dm player class
     */
    public class Player{
        //the players id, command processor and character
        public int ID;
        public CommandProcessor cp;
        public Character character;

        public Player(){
            character = new Character();
            cp = new CommandProcessor(character, null);
            ID = -1;
        }
    }

    //socket for communicating with dm
    private Socket socket;
    //output text area
    private TextArea output;
    //sender and listener for communication
    private PrintWriter sender;
    private ReadingThread listen;
    //whether it is active
    private boolean active;

    //the controller and the player
    private Character player;
    private PlayerController controller;

    //vector of all other players in the game
    public Vector<Player> players;

    /*
    Create a player client connecting to the given server on the given port,
    with the other inputted params
     */
    public PlayerClient(String server, int port, TextArea output, Character player, PlayerController controller){
        //init fields
        this.player = player;
        this.output = output;
        this.controller = controller;
        active = false;
        players = new Vector<>();
        //connect to the specified server with port
        connect(server, port);
    }

    /*
    Connect to a server with the given port
     */
    public void connect(String serverName, int serverPort){
        println("Establishing connection to DM. Please wait...");
        try {
            //create the socket
            socket = new Socket(serverName, serverPort);
            println("Connected Successfully.");

            //start reading thread and sender
            listen = new ReadingThread(this, socket);
            sender = new PrintWriter(socket.getOutputStream());
            active = true;
        } catch (IOException e){
            println("Could not connect to DM.");
        }
    }

    /*
    Send a message to the server
     */
    public void send(String s){
        sender.print(s + '\n');
        sender.flush();
    }

    /*
    Called whenever a message is recieved to handle the message.
     */
    public void recieve(String s){
        //if it is an init command handle it
        if (s.charAt(0) == '#'){
            handleInitCommand(s);
            //otherwise just print out the contents
        } else {
            println(s);
        }
    }

    /*
    Handles a given init command given as a string
     */
    private void handleInitCommand(String s){
        Scanner scan = new Scanner(s);
        //scan for init command tag
        switch(scan.next()){
            case "#sendchar":
                //if its a send char request send the character to server
                send(player.sendChar());
                break;
            case "#setchar":
                //if its set char, create a new player and make the controller update
                Player p = new Player();
                p.cp.processCommands(CommandParser.parseCommands(scan.nextLine()));
                controller.updated = true;
                players.add(p);
                break;
            case "#setstage":
                //set the stage
                setStage(s);
                break;
        }
    }

    /*
    Takes in an init command for setting the stage
     */
    private void setStage(String s) {
        //get the init command from the first definer
        s = s.substring(s.indexOf('@'), s.length());
        //scan through using @ for each definition
        Scanner scan = new Scanner(s);
        Stage current = new Stage();
        scan.useDelimiter("@");

        //get the next scanned in, it is the name
        String next = scan.next();
        String name = next.substring(5, next.length());
        //get description
        next = scan.next();
        String description = next.substring(12, next.length());

        //get string of all objects in the room
        next = scan.next();
        String objects = next.substring(7, next.length());

        //scan through all objects and add to list
        Scanner scan2 = new Scanner(objects);
        scan2.useDelimiter(",");
        ArrayList<Obj> objs = new ArrayList<>();

        //add objects with only name to list of objs
        while(scan2.hasNext()){
            Obj newObj = new Obj();
            newObj.setName(scan2.next().trim());
            objs.add(newObj);
        }

        //scan and get the encounter string
        next = scan.next();
        String encounter = next.substring(9, next.length());

        //scan through list of monsters
        scan2 = new Scanner(encounter);
        scan2.useDelimiter(",");
        ArrayList<Monster> mons = new ArrayList<>();

        //for each of the monsters, get the name
        while(scan2.hasNext()){
            Monster newMonster = new Monster();
            newMonster.setName(scan2.next().trim());
            mons.add(newMonster);
        }

        //set all data fields for the room
        current.setName(name);
        current.setDescription(description);
        current.setObjects(objs);
        current.setEncounter(new Encounter("", "", "", mons));

        //TODO add active encounter thing

        //set the current stage and mark update needed
        controller.currentStage = current;
        controller.updated = true;
    }

    /*
    Close the thread
     */
    private void close(){
        //close the sending thread
        sender.close();

        //attempt to close the socket
        try {
            socket.close();
        } catch (IOException e){
            println("Error closing.");
        }

        //if listener is open, close it
        if (listen != null)
            listen.close();
        listen = null;
        active = false;
    }

    /*
    Prints a line of text in the output
     */
    private void println(String s){
        output.appendText(s + '\n');
    }

    public boolean isActive(){
        return active;
    }

    /*
    Reading thread for reading in messages from server
     */
    private class ReadingThread extends Thread{
        //socket for sending and recieving messages
        private Socket socket;
        //reference to client
        private PlayerClient client;
        //reader for inputted messages
        private BufferedReader reader;

        /*
        Creates the reading thread
         */
        public ReadingThread(PlayerClient client, Socket socket){
            this.client = client;
            this.socket = socket;

            //open and start the thread
            open();
            start();
        }

        /*
        Settup the input reader
         */
        public void open(){
            try{
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e){
                println("Error getting input stream.");
                client.close();
            }
        }

        /*
        Close the reader
         */
        public void close(){
            try{
                reader.close();
            } catch (IOException e){
                println("Error closing input stream.");
            }
        }

        /*
        Run the message reciever
         */
        public void run(){
            while(true){
                try {
                    //attempt to read a line and send to client code to handle
                    client.recieve(reader.readLine());
                } catch (IOException e){
                    println("Listening error.");
                    client.close();
                }
            }
        }
    }
}
