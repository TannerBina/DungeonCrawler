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

    public class Player{
        public int ID;
        public CommandProcessor cp;
        public Character character;

        public Player(){
            character = new Character();
            cp = new CommandProcessor(character, null);
            ID = -1;
        }
    }

    private Socket socket;
    private TextArea output;
    private PrintWriter sender;
    private ReadingThread listen;
    private boolean active;
    private Character player;
    private PlayerController controller;

    public Vector<Player> players;

    public PlayerClient(String server, int port, TextArea output, Character player, PlayerController controller){
        this.player = player;
        this.output = output;
        this.controller = controller;
        active = false;
        players = new Vector<>();
        connect(server, port);
    }

    public void connect(String serverName, int serverPort){
        println("Establishing connection to DM. Please wait...");
        try {
            socket = new Socket(serverName, serverPort);
            println("Connected Successfully.");
            listen = new ReadingThread(this, socket);
            sender = new PrintWriter(socket.getOutputStream());
            active = true;
        } catch (IOException e){
            println("Could not connect to DM.");
        }
    }

    public void send(String s){
        sender.print(s + '\n');
        sender.flush();
    }

    public void recieve(String s){
        if (s.charAt(0) == '#'){
            handleInitCommand(s);
        } else {
            println(s);
        }
    }

    private void handleInitCommand(String s){
        Scanner scan = new Scanner(s);
        switch(scan.next()){
            case "#sendchar":
                send(player.sendChar());
                break;
            case "#setchar":
                Player p = new Player();
                p.cp.processCommands(CommandParser.parseCommands(scan.nextLine()));
                controller.updated = true;
                players.add(p);
                break;
            case "#setstage":
                setStage(s);
                break;
        }
    }

    private void setStage(String s) {
        s = s.substring(s.indexOf('@'), s.length());
        Scanner scan = new Scanner(s);
        Stage current = new Stage();
        scan.useDelimiter("@");

        String next = scan.next();
        String name = next.substring(5, next.length());
        next = scan.next();
        String description = next.substring(12, next.length());

        next = scan.next();
        String objects = next.substring(7, next.length());

        Scanner scan2 = new Scanner(objects);
        scan2.useDelimiter(",");
        ArrayList<Obj> objs = new ArrayList<>();

        while(scan2.hasNext()){
            Obj newObj = new Obj();
            newObj.setName(scan2.next().trim());
            objs.add(newObj);
        }

        next = scan.next();
        String encounter = next.substring(9, next.length());

        scan2 = new Scanner(encounter);
        scan2.useDelimiter(",");
        ArrayList<Monster> mons = new ArrayList<>();

        while(scan2.hasNext()){
            Monster newMonster = new Monster();
            newMonster.setName(scan2.next().trim());
            mons.add(newMonster);
        }

        current.setName(name);
        current.setDescription(description);
        current.setObjects(objs);
        current.setEncounter(new Encounter("", "", "", mons), false);

        //TODO add active encounter thing

        controller.currentStage = current;
        controller.updated = true;
    }

    private void close(){
        sender.close();

        try {
            socket.close();
        } catch (IOException e){
            println("Error closing.");
        }
        if (listen != null)
            listen.close();
        listen = null;
        active = false;
    }

    private void println(String s){
        output.appendText(s + '\n');
    }

    public boolean isActive(){
        return active;
    }

    private class ReadingThread extends Thread{
        private Socket socket;
        private PlayerClient client;
        private BufferedReader reader;

        public ReadingThread(PlayerClient client, Socket socket){
            this.client = client;
            this.socket = socket;
            open();
            start();
        }

        public void open(){
            try{
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e){
                println("Error getting input stream.");
                client.close();
            }
        }

        public void close(){
            try{
                reader.close();
            } catch (IOException e){
                println("Error closing input stream.");
            }
        }

        public void run(){
            while(true){
                try {
                    client.recieve(reader.readLine());
                } catch (IOException e){
                    println("Listening error.");
                    client.close();
                }
            }
        }
    }
}
