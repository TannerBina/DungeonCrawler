package sample;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;
import java.util.Vector;

/**
 * handles interactions between players and the dm
 * connection through sockets to the server
 * Created by Tanner on 3/14/2017.
 */
public class DMServer implements Runnable{

    //the vector of all players in the game
    public Vector<Player> players;

    //the output area
    private TextArea output;
    //server and server thread
    private ServerSocket server;
    private Thread thread;

    //the reference to the controller
    private DMController controller;

    //the player class
    public class Player extends Combatant{
        //link to client, their character, id, and personal processor
        public Client client;
        public Character character;
        public int ID;
        public CommandProcessor cp;

        //creates a blank player
        public Player(){
            character = new Character();
            cp = new CommandProcessor(character, null);
            ID = -1;
        }

    }

    /*
    Creates a dm server on a specified port, connected to output and controller
     */
    public DMServer(int port, TextArea output, DMController controller){
        //initalize all values
        this.output = output;
        this.controller = controller;
        players = new Vector<>();
        //try to bind to port
        try{
            println("Binding to port, please wait...");
            server = new ServerSocket(port);
            println("Server name: " + InetAddress.getLocalHost().getHostName());
            println("Server port: " + server.getLocalPort());
            //give command to join and start server to accept sockets
            println("Command to join: " + "$connect " + InetAddress.getLocalHost().getHostAddress() + " " + server.getLocalPort());
            println("Waiting for players to join.");
            start();
        } catch (IOException e){
            println("Can not bind to port " + port);
        }
    }

    /*
    handle a incoming string of text
     */
    public synchronized void handle(String s, int ID){
        //if the incoming string is an init command
        if (s.trim().charAt(0) == '#'){
            //handle it
            handleInitCommand(s, ID);

            //if its a play command send it to the controller
        } else if (s.contains(" $")) {
            controller.handleCommand(s.trim());

            //if its just a message, send it to everyone
        } else {
            sendToAll(s);
        }
    }

    /*
    Handles a given init command
     */
    private void handleInitCommand(String s, int ID){
        //find the player who input the command
        Player inputter = find(ID);
        Scanner scan = new Scanner(s);
        //get the command tag
        switch(scan.next()){
            //if its setchar, send the char to everyone
            case "#setchar":
                //update that players char
                inputter.cp.processCommands(CommandParser.parseCommands(scan.nextLine()));
                for (Player p : players){
                    p.client.send(s);
                }
                //set update to true
                controller.updated = true;
                break;
        }
    }

    /*
    sends an init command to everyone
     */
    public synchronized void sendInitCommand(String s){
        for (Player p : players){
            p.client.send(s);
        }
    }

    /*
    sends a command to everyone
     */
    public synchronized void sendToAll(String s){
        println(s);
        for (Player p : players){
            p.client.send(s);
        }
    }

    /*
    removed a client from the server
     */
    public synchronized void remove(Client client){
        try{
            client.close();
            players.remove(client);
        } catch (IOException e){
            println("Error closing thread.");
        }
    }

    /*
    the run command for the servers thread
     */
    public void run(){
        while (thread != null){
            try{
                //try to add a client
                addClient(server.accept());
            } catch (IOException e){
                println("Server accept error.");
                stop();
            }
        }
    }

    /*
    adds clients given a socket attempting to connect
     */
    private void addClient(Socket socket){
        //send a player joined message to all
        sendToAll("Player joined.");
        //send player count to all
        sendToAll("Players in game: " + players.size());
        //add the player, his id and create a client for him
        players.add(new Player());
        players.lastElement().ID = socket.getPort();
        players.lastElement().client = new Client(this, socket);
        //for every player
        for (Player p: players){
            //if p was not the player that just joined
            if (p != players.lastElement()){
                //send p's char to the player that just joined
                players.lastElement().client.send(p.character.sendChar());
            }
        }
        //request player from the last joined player
        players.lastElement().client.send("#sendchar");

        //send stage if current stage
        if (controller.currentStage != null){
            players.lastElement().client.send(controller.currentStage.sendStage());
            players.lastElement().client.send(controller.currentStage.getDescription());
        }
    }

    /*
    start this thread to check for connections
     */
    public void start(){
        if (thread == null){
            thread = new Thread(this);
            thread.start();
        }
    }

    //stop this thread
    public void stop(){
        thread = null;
    }

    /*
    Client class of a player that is in the game
    used for sending message to that player and
    recieving messages
     */
    private class Client extends Thread{

        //server, socket, reader, writer refrences
        private DMServer server;
        private Socket socket;
        private BufferedReader reader;
        private PrintWriter sender;
        private int ID;

        /*
        A client, input a dmserver they linked to
        as well as a socket for them to use
         */
        public Client(DMServer server, Socket socket){
            super();
            this.server = server;
            this.socket = socket;
            ID = socket.getPort();
            try{
                //attempt to open the socket connection
                open();
                start();
            } catch (IOException e){
                println("Failed to open client streams.");
            }
        }

        /*
        Send a message to the client
         */
        public void send(String s){
            sender.write(s + '\n');
            sender.flush();
        }

        /*
        Runs the client thread.
         */
        public void run(){
            while (true) {
                try{
                    //handle the input from the client
                    server.handle(reader.readLine(), ID);
                } catch (IOException e){
                    //if there is an error reading remove the player
                    println("Error reading, removing player.");
                    server.remove(this);
                }
            }
        }

        /*
        open the reader and sender with in and out streams from socket
         */
        public void open() throws IOException{
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            sender = new PrintWriter(socket.getOutputStream());
        }

        /*
        close the reader and sender streams
         */
        public void close() throws IOException {
            reader.close();
            sender.close();
            socket.close();
        }
    }

    /*
    Returns the player whose
    ID matches the inputted id
     */
    private Player find(int ID){
        for (Player p: players){
            if (p.ID == ID){
                return p;
            }
        }
        return null;
    }

    /*
    print a line of text on the output
     */
    private void println(String s){
        output.appendText(s + '\n');
    }
}
