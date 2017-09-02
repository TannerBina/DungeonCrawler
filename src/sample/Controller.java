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

    @FXML
    private Button playBut;
    @FXML
    private Button dmBut;
    @FXML
    private Button newCharBut;
    @FXML
    private Button loadCharBut;

    public Controller(){
    }

    @FXML
    private void initialize(){
    }

    @FXML
    private void handPlayBut(){
        naviToNew(playBut, Constants.PLAYFIRS);
    }

    @FXML
    private void handDMBut(){
        naviToNew(dmBut, Constants.DMWIN);
    }

    @FXML
    private void handNewCharBut(){
        naviToNew(newCharBut, Constants.NEWCHAR);
    }

    @FXML
    private void handLoadCharBut(){
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Character Documents","dchar");
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File("."));
        fc.setFileFilter(filter);
        int returnVal = fc.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                Scanner s = new Scanner(fc.getSelectedFile());
                StringBuilder sb = new StringBuilder();
                while(s.hasNext()){
                    sb.append(s.next());
                    sb.append(" ");
                }
                PlayerController.player = new Character();
                CommandProcessor cp = new CommandProcessor(PlayerController.player, null);
                cp.processCommands(CommandParser.parseCommands(sb.toString()));
                naviToNew(loadCharBut, Constants.PLAYWIND);
            } catch (IOException e){
                System.err.println("Cannot open selected file when loading char");
            }
        }
    }

    private void naviToNew(Button but, String fileName){
        try {
            Stage stage = (Stage) but.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource(fileName));
            Scene scene = new Scene(root);
            stage.hide();
            stage.setScene(scene);
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
