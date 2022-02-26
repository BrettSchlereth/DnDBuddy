package com.example.dndbuddy;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HelloController {
    @FXML
    private Label welcomeText;
    private TabPane ClassTabs;

    @FXML
    protected void tabSelected(Event ev) {
        System.out.println("tabSelected");
        System.out.println("ev: " + ev.getSource());
    }

    @FXML
    protected void raceSelected(ActionEvent event) {
        Node node = (Node) event.getSource();
        String race = (String) node.getUserData();
        System.out.println("race selected: " + race);
        Scene scene = node.getScene();
        Text t = (Text) scene.lookup("#CurrentRace");
        t.setText("Current Race: " + race);
        try {
            setContent(race, "Race", scene);
        }
        catch (IOException e) {
            System.out.println("IOException: " + e);
        }
    }

    public void setContent(String item, String category, Scene scene) throws IOException {
        try {
            System.out.println("setting content");
            String fileText = "";
            fileText = new String(Files.readAllBytes(Paths.get("src/main/resources/" + category + "/" + item)));
            Text t = (Text) scene.lookup("Current" + category);
            t.setText(fileText);
        }
        catch (IOException e){
            System.out.println("Exception caught: File read error" + e);
        }
        }

}