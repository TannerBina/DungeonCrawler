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
 * Created by Tanner on 3/14/2017.
 */
public class DMServer implements Runnable{

    public Vector<Player> players;

    private TextArea output;
    private ServerSocket server;
    private Thread thread;

    private DMController controller;

    public class Player extends Combatant{
        public Client client;
        public Character character;
        public int ID;
        public CommandProcessor cp;

        public Player(){
            character = new Character();
            cp = new CommandProcessor(character, null);
            ID = -1;
        }

    }

    public DMServer(int port, TextArea output, DMController controller){
        this.output = output;
        this.controller = controller;
        players = new Vector<>();
        try{
            println("Binding to port, please wait...");
            server = new ServerSocket(port);
            println("Server name: " + InetAddress.getLocalHost().getHostName());
            println("Server port: " + server.getLocalPort());
            println("Command to join: " + "$connect " + InetAddress.getLocalHost().getHostAddress() + " " + server.getLocalPort());
            println("Waiting for players to join.");
            start();
        } catch (IOException e){
            println("Can not bind to port " + port);
        }
    }

    public synchronized void handle(String s, int ID){
        if (s.trim().charAt(0) == '#'){
            handleInitCommand(s, ID);
        } else if (s.contains(" $")) {
            controller.handleCommand(s.trim());
        } else {
            sendToAll(s);
        }
    }

    private void handleInitCommand(String s, int ID){
        Player inputter = find(ID);
        Scanner scan = new Scanner(s);
        switch(scan.next()){
            case "#setchar":
                inputter.cp.processCommands(CommandParser.parseCommands(scan.nextLine()));
                for (Player p : players){
                    p.client.send(s);
                }
                controller.updated = true;
                break;
        }
    }

    public synchronized void sendInitCommand(String s){
        for (Player p : players){
            p.client.send(s);
        }
    }

    public synchronized void sendToAll(String s){
        println(s);
        for (Player p : players){
            p.client.send(s);
        }
    }

    public synchronized void remove(Client client){
        try{
            client.close();
            players.remove(client);
        } catch (IOException e){
            println("Error closing thread.");
        }
    }

    public void run(){
        while (thread != null){
            try{
                addClient(server.accept());
            } catch (IOException e){
                println("Server accept error.");
                stop();
            }
        }
    }

    private void addClient(Socket socket){
        sendToAll("Player joined.");
        players.add(new Player());
        players.lastElement().ID = socket.getPort();
        players.lastElement().client = new Client(this, socket);
        for (Player p: players){
            if (p != players.lastElement()){
                players.lastElement().client.send(p.character.sendChar());
            }
        }
        players.lastElement().client.send("#sendchar");

        if (controller.currentStage != null){
            players.lastElement().client.send(controller.currentStage.sendStage());
            players.lastElement().client.send(controller.currentStage.getDescription());
        }

        sendToAll("Players in game: " + players.size());
    }

    public void start(){
        if (thread == null){
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stop(){
        thread = null;
    }

    private class Client extends Thread{

        private DMServer server;
        private Socket socket;
        private BufferedReader reader;
        private PrintWriter sender;
        private int ID;

        public Client(DMServer server, Socket socket){
            super();
            this.server = server;
            this.socket = socket;
            ID = socket.getPort();
            try{
                open();
                start();
            } catch (IOException e){
                println("Failed to open client streams.");
            }
        }

        public void send(String s){
            sender.write(s + '\n');
            sender.flush();
        }

        public void run(){
            while (true) {
                try{
                    server.handle(reader.readLine(), ID);
                } catch (IOException e){
                    println("Error reading, removing player.");
                    server.remove(this);
                }
            }
        }

        public void open() throws IOException{
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            sender = new PrintWriter(socket.getOutputStream());
        }

        public void close() throws IOException {
            reader.close();
            sender.close();
            socket.close();
        }
    }

    private Player find(int ID){
        for (Player p: players){
            if (p.ID == ID){
                return p;
            }
        }
        return null;
    }

    private void println(String s){
        output.appendText(s + '\n');
    }
}
