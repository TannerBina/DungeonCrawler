package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Controller {

    //button to select being a player
    @FXML
    private Button playBut;
    //button to select being a dm
    @FXML
    private Button dmBut;
    //create a new char button
    @FXML
    private Button newCharBut;
    //create a load char button
    @FXML
    private Button loadCharBut;

    /*
    Empty constructor
     */
    public Controller(){
    }

    /*
    Empty init
     */
    @FXML
    private void initialize(){
    }

    /*
    Navigates to player page on press
     */
    @FXML
    private void handPlayBut(){
        naviToNew(playBut, Constants.PLAYFIRS);
    }

    /*
    Navigate to dm page on click of button
     */
    @FXML
    private void handDMBut(){
        naviToNew(dmBut, Constants.DMWIN);
    }

    /*
    Brings to new char screen on click
     */
    @FXML
    private void handNewCharBut(){
        naviToNew(newCharBut, Constants.NEWCHAR);
    }

    /*
    Allows player to select and load a dchar file
     */
    @FXML
    private void handLoadCharBut(){
        //set filter to dchar
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Character Documents","dchar");
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File("."));
        fc.setFileFilter(filter);
        int returnVal = fc.showOpenDialog(null);

        //get value
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                //scan in all lines from the given files, add to string builder
                Scanner s = new Scanner(fc.getSelectedFile());
                StringBuilder sb = new StringBuilder();
                while(s.hasNext()){
                    sb.append(s.next());
                    sb.append(" ");
                }

                //create initial blank char and set up processor
                PlayerController.player = new Character();
                CommandProcessor cp = new CommandProcessor(PlayerController.player, null);

                //process all commands, navi to player window
                cp.processCommands(CommandParser.parseCommands(sb.toString()));
                naviToNew(loadCharBut, Constants.PLAYWIND);
            } catch (IOException e){
                System.err.println("Cannot open selected file when loading char");
            }
        }
    }

    /*
    General method to navigate to a new window from current given a file name
     */
    private void naviToNew(Button but, String fileName){
        try {
            //load stage
            Stage stage = (Stage) but.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource(fileName));
            Scene scene = new Scene(root);

            //hide current screen
            stage.hide();
            stage.setScene(scene);

            //set the attributes for the screen
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.sizeToScene();
            stage.setOnCloseRequest(event -> System.exit(0));
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
            System.err.println();
            System.err.println(fileName + " Error in navigating to");
        }
    }
}
