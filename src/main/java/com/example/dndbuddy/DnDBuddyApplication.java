package com.example.dndbuddy;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Objects;
import java.util.Random;

import com.mysql.jdbc.Driver;

import static java.lang.String.valueOf;
import static javafx.application.Platform.exit;

public class DnDBuddyApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, SQLException {

        Connection con = connectToSQLServer();
        ScreenController screenController = new ScreenController();

        HBox mainHBox = new HBox();
        mainHBox.setMinSize(800, 500);
        mainHBox.setAlignment(Pos.CENTER);
        VBox mainVBox = new VBox();
        mainVBox.setAlignment(Pos.CENTER);
        mainVBox.setSpacing(20);
        Text titleText = new Text("DnD Buddy");
        Font titleFont = new Font("Poor Richard", 60);
        titleText.setFont(titleFont);
        Button newButton = new Button("New");
        newButton.setAlignment(Pos.CENTER);
        Button loadButton = new Button("Load");
        mainVBox.getChildren().addAll(titleText, newButton, loadButton);
        mainHBox.getChildren().add(mainVBox);
        Group loadOrNewRoot = new Group();
        loadOrNewRoot.getChildren().add(mainHBox);
        Scene loadOrNewScene = new Scene(loadOrNewRoot, 800, 500, Color.web("d3bb8d"));
        loadOrNewScene.getStylesheets().add(DnDBuddyApplication.class.getResource("Style.css").toExternalForm());


        //Load screen
        HBox loadHBox = new HBox();
        loadHBox.setMinSize(800, 500);
        loadHBox.setAlignment(Pos.CENTER);
        VBox loadVBox = new VBox();
        loadVBox.setMinWidth(800);
        loadVBox.setAlignment(Pos.TOP_CENTER);
        loadVBox.setSpacing(20);
        Text loadTitleText = new Text("DnD Buddy");
        loadTitleText.setFont(titleFont);

        TableView characterTable = new TableView();
        characterTable.minWidth(800);
        characterTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TableColumn<tableCharacter, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<tableCharacter, String> colBackground = new TableColumn<>("Background");
        colBackground.setCellValueFactory(new PropertyValueFactory<>("background"));
        TableColumn<tableCharacter, String> colLevel = new TableColumn<>("Level");
        colLevel.setCellValueFactory(new PropertyValueFactory<>("level"));
        TableColumn<tableCharacter, String> colAlignment = new TableColumn<>("Alignment");
        colAlignment.setCellValueFactory(new PropertyValueFactory<>("alignment"));
        TableColumn<tableCharacter, String> colRace = new TableColumn<>("Race");
        colRace.setCellValueFactory(new PropertyValueFactory<>("race"));
        TableColumn<tableCharacter, String> colClass = new TableColumn<>("Class");
        colClass.setCellValueFactory(new PropertyValueFactory<>("klass"));
        TableColumn<tableCharacter, String> colSkills = new TableColumn<>("Skills");
        colSkills.setCellValueFactory(new PropertyValueFactory<>("something"));
        characterTable.getColumns().addAll(colName, colBackground, colLevel, colAlignment, colRace, colClass, colSkills);
        characterTable = fillCharacterTable(characterTable, con);
        Button loadCharacterButton = new Button("Load Character");
        TableView.TableViewSelectionModel selectionModel = characterTable.getSelectionModel();
        Character character = new Character();
        loadVBox.getChildren().addAll(loadTitleText, characterTable, loadCharacterButton);
        loadHBox.getChildren().addAll(loadVBox);
        Group loadSceneGroup = new Group();
        loadSceneGroup.getChildren().add(loadHBox);
        Scene loadScene = new Scene(loadSceneGroup, 800, 500, Color.web("d3bb8d"));


        //ObservableList selectedRow = selectionModel.getSelectedItem();
        //Declares the race, class, and tab names
        String [] races = {"Dragonborn", "Hill Dwarf", "Mountain Dwarf", "High Elf", "Wood Elf", "Drow", "Deep Gnome", "Forest Gnome", "Rock Gnome", "Half-Elf", "Lightfoot Halfling", "Stout Halfling", "Half Orc", "Human", "Tiefling"};
        String [] classes = {"Barbarian", "Bard", "Cleric", "Druid", "Fighter", "Monk", "Paladin", "Ranger", "Rogue", "Sorcerer", "Warlock", "Wizard"};
        String [] backgrounds = {"Acolyte", "Charlatan", "Criminal or Spy", "Entertainer", "Folk Hero", "Gladiator", "Guild Artisan or Guild Merchant", "Hermit", "Knight", "Noble", "Outlander", "Pirate", "Sage", "Soldier", "Urchin"};
        String [] tabNames = {"Race", "Class", "Background"};


        //Creates a hashtable that relates each category to their corresponding button names
        Hashtable<String, String[]> tabs = new Hashtable<String, String[]>();
        tabs.put("Race", races);
        tabs.put("Class", classes);
        tabs.put("Background", backgrounds);

        //Declares a new tab pane
        TabPane tp = new TabPane();
        tp.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        //Adds the Race, Class, and Background tabs
        for (int j = 0; j < tabNames.length; j++) {
            //Declares a new tab
            Tab tab = new Tab(tabNames[j]);
            //Declares a container for the buttons of this tab
            VBox buttonHolder = new VBox();
            buttonHolder.setSpacing(4);
            //Gets the name of the tab
            String [] buttons = tabs.get(tabNames[j]);

            if (tabNames[j].equals("Race")) {
                HBox levelHBox = new HBox();
                Label levelLabel = new Label("Level: ");
                levelHBox.getChildren().add(levelLabel);
                Spinner<Integer> spinner = new Spinner<Integer>();
                spinner.setPromptText("Level");
                spinner.setMaxWidth(125);
                final int initialValue = 1;
                SpinnerValueFactory<Integer> LevelSpinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, initialValue);
                spinner.setValueFactory(LevelSpinner);
                spinner.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                       System.out.println("clicked");
                       Label levelSheetLabel = (Label) spinner.getScene().lookup("#LevelSheet");
                       levelSheetLabel.setText("Level: " + spinner.getValue());
                    }
                }
                );
                levelHBox.getChildren().add(spinner);
                buttonHolder.getChildren().add(levelHBox);
            }
            else if (tabNames[j].equals("Class")) {
                HBox alignmentHBox = new HBox();
                Label alignmentLabel = new Label("Alignment:");
                alignmentHBox.getChildren().add(alignmentLabel);
                ObservableList<String> alignments = FXCollections.observableArrayList("Lawful Good", "Neutral Good", "Chaotic Good", "Lawful Neutral", "Neutral", "Chaotic Neutral", "Lawful Evil", "Neutral Evil");
                final ComboBox alignmentBox = new ComboBox(alignments);
                alignmentBox.setOnAction(new EventHandler<Event>() {
                    @Override
                    public void handle(Event event) {
                        Node node = (Node) event.getSource();
                        Label alignmentLabel = (Label) node.getScene().lookup("#AlignmentSheet");
                        character.alignment = (String) alignmentBox.getValue();
                        System.out.println("AlignmentLabel: " + alignmentLabel);
                        alignmentLabel.setText("Alignment: " + character.alignment);
                    }
                });
                alignmentHBox.getChildren().add(alignmentBox);
                buttonHolder.getChildren().add(alignmentHBox);
            }

            //Adds each button per tab
            for (int i = 0; i < buttons.length; i++) {
                String buttonName = tabs.get(tabNames[j])[i];
                //Declares a new button
                Button button = new Button(buttonName);
                //attaches the category name and button name to the button to be used
                //in the handle event
                String [] userData = {tabNames[j], buttonName};
                button.setUserData(userData);
                //Triggered when the button is pressed
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Node node = (Node) event.getSource();
                        //Gets the category name and button name from the button
                        String [] userData = (String []) node.getUserData();
                        String category = userData[0];
                        String item = userData[1];
                        character.setItem(category, item);
                        //Gets the scene for setting the content
                        Scene scene = node.getScene();
                        //Sets the content for the item selected
                        try {
                            setContent(item, category, scene);
                        } catch (IOException e) {
                            System.out.println("IOException: " + e);
                        }
                    }
                });
                //Adds the button to the button holder
                buttonHolder.getChildren().add(button);
            }
            //Sets the "Current ______: " text line
            Text current = new Text("Current " + tabNames[j] + ": ");
            current.setId("Current" + tabNames[j]);
            //Creates the content box
            Text content = new Text();
            content.setWrappingWidth(400);
            content.setId(tabNames[j] + "Content");

            //Adds the Current line and content to the content box
            VBox contentVBox = new VBox();
            contentVBox.getChildren().add(current);
            contentVBox.getChildren().add(content);

            //Adds all of the content to the tab
            HBox tabHbox = new HBox();
            tabHbox.getChildren().add(buttonHolder);
            tabHbox.getChildren().add(contentVBox);
            tab.setContent(tabHbox);

            //Adds the tab to the tab pane
            tp.getTabs().add(tab);
        }

        //Creates Ability Score Page
        Tab score = new Tab("Score");
        HBox scoreHBox = new HBox();
        scoreHBox.setSpacing(5);
        String [] abilities = {"Strength", "Dexterity", "Constitution", "Intelligence", "Wisdom", "Charisma"};
        Button rollButton = new Button("Roll");
        rollButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Node node = (Node) event.getSource();
                character.abilityScores = rollRandomNumber(node.getScene(), character);
                recalculateScores(node, abilities);
            }
        });
        scoreHBox.getChildren().add(rollButton);
        for (int i = 0; i < abilities.length; i++) {
            VBox scoreVBox = new VBox();
            Label l = new Label(abilities[i]);
            l.setId(abilities[i]);
            l.setOnDragDetected((MouseEvent event) -> {
                System.out.println("Label " + l.getText() + " is dragged");
                Dragboard db = l.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(l.getText());
                db.setContent(content);
            });
            l.setOnMouseDragged((MouseEvent event) -> {
                event.setDragDetect(true);
            });
            l.setOnDragOver(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent dragEvent) {
                    if (dragEvent.getGestureSource() != l && dragEvent.getDragboard().hasString()) {
                        dragEvent.acceptTransferModes(TransferMode.MOVE);
                    }
                    dragEvent.consume();
                }
            });
            l.setOnDragDropped((DragEvent event) -> {
                Dragboard db = event.getDragboard();
                if (db.hasString()) {
                    Label node = (Label) event.getSource();
                    Label labelNode = (Label) node.getScene().lookup("#" + db.getString());
                    Spinner spinnerNode1 = (Spinner) node.getScene().lookup("#" + db.getString() + "Spinner");
                    Spinner spinnerNode2 = (Spinner) node.getScene().lookup("#" + l.getText() + "Spinner");
                    System.out.println(labelNode + " was dropped");
                    labelNode.setId(l.getText());
                    labelNode.setText(l.getText());
                    spinnerNode1.setId(l.getText() + "Spinner");
                    l.setId(db.getString());
                    l.setText(db.getString());
                    spinnerNode2.setId(db.getString() + "Spinner");
                    recalculateScores(node, abilities);
                    event.setDropCompleted(true);
                } else {
                    event.setDropCompleted(false);
                }
                event.consume();
            });
            scoreVBox.getChildren().add(l);
            Spinner<Integer> spinner = new Spinner<Integer>();
            spinner.setPromptText(abilities[i]);
            spinner.setUserData(abilities[i]);
            spinner.setId(abilities[i] + "Spinner");
            spinner.setMaxWidth(120);
            final int initialValue = 10;
            SpinnerValueFactory<Integer> StrengthSpinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, initialValue);
            spinner.setValueFactory(StrengthSpinner);
            scoreVBox.getChildren().add(spinner);
            scoreHBox.getChildren().add(scoreVBox);
        }
        score.setContent(scoreHBox);
        tp.getTabs().add(score);

        //Creates sheet display tab
        Tab sheet = new Tab("Sheet");
        HBox sheetHBox = new HBox();
        VBox sheetVBox = new VBox();
        TextField nameField = new TextField();
        Label name = new Label("Name: ");
        nameField.setId("NameSheet");
        HBox nameHBox = new HBox();
        nameHBox.getChildren().addAll(name, nameField);
        Label level = new Label("Level: " + character.level);
        level.setId("LevelSheet");
        Label alignment = new Label("Alignment: " + character.alignment);
        alignment.setId("AlignmentSheet");
        Label race = new Label("Race: " + character.race);
        race.setId("RaceSheet");
        Label Class = new Label("Class: " + character.Class);
        Class.setId("ClassSheet");
        Label background = new Label("Background: " + character.background);
        background.setId("BackgroundSheet");
        sheetVBox.getChildren().addAll(nameHBox, alignment, level, race, Class, background);
        for (String abilityName : abilities) {
            Label abilityLabel = new Label(abilityName + ": " + character.abilityScores.get(abilityName));
            abilityLabel.setId("Sheet" + abilityName);
            sheetVBox.getChildren().add(abilityLabel);
        }

        sheetHBox.getChildren().add(sheetVBox);
        sheet.setContent(sheetHBox);
        tp.getTabs().add(sheet);

        Group mainApp = new Group();
        mainApp.getChildren().add(tp);
        //.setId("mainApp");
        Scene mainAppScene = new Scene(mainApp,800, 500, Color.web("d3bb8d"));
        mainAppScene.getStylesheets().add(DnDBuddyApplication.class.getResource("Style.css").toExternalForm());

        newButton.setOnAction(e -> {
            stage.setScene(mainAppScene);
            setCharacterInfo(screenController.getStoredData(), mainAppScene);
        });
        loadButton.setOnAction(e -> {
            stage.setScene(loadScene);
        });
        loadCharacterButton.setOnAction(actionEvent -> {
            tableCharacter selectedRow = (tableCharacter) selectionModel.getSelectedItem();
            System.out.println("Selected row: " + selectedRow);
            System.out.println("Race: " + selectedRow.getName());
            screenController.sendData(selectedRow);
            stage.setScene(mainAppScene);
            setCharacterInfo(screenController.getStoredData(), mainAppScene);
        });
        stage.setTitle("DnD Buddy");

        stage.setScene(loadOrNewScene);
        stage.show();
        System.out.println(Font.getFamilies());
    }

    public Connection connectToSQLServer() {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys?autoReconnect=true&useSSL=false", "root", "mySQLRootPassword!");
            System.out.println("Connecting to SQL Server");
        } catch (Exception e) {
            System.out.println("Could not connect to SQL Server");
            e.printStackTrace();
            exit();
        }
        return con;
    }

    public TableView fillCharacterTable(TableView cTable, Connection con) throws SQLException {
        try {
            String query = "SELECT * FROM characters";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnNumber = rsmd.getColumnCount();
            String characterData [] = new String [7];
            while (rs.next()) {
                for (int i = 1; i <= columnNumber; i++) {
                    characterData[i-1] = rs.getString(i);
                    System.out.println(characterData[i-1]);
                }
                cTable.getItems().add(new tableCharacter(characterData));
            }
        }
        catch (SQLException e) {
            System.out.println("SQL Error");
            e.printStackTrace();
        }
        System.out.println("cTable items: " + cTable.getItems());
        return cTable;
    }

    public void recalculateScores(Node node, String [] abilities) {
        for (String ability : abilities) {
            Label labelNode = (Label) node.getScene().lookup("#Sheet" + ability);
            Spinner spinner = (Spinner) node.getScene().lookup("#" + ability + "Spinner");
            labelNode.setText(ability + ": " + spinner.getValueFactory().getValue());
        }
    }

    public Hashtable rollRandomNumber(Scene scene, Character character) {
        String [] abilities = {"Strength", "Dexterity", "Constitution", "Intelligence", "Wisdom", "Charisma"};
        Random random = new Random();
        int [] as = {0, 0, 0, 0, 0, 0};
        int i = 0;
        for (String ability : abilities) {
            int rand = random.nextInt(16) + 3;
            Spinner spinner = (Spinner) scene.lookup("#" + ability + "Spinner");
            spinner.getValueFactory().setValue(rand);
            as[i] = rand;
            i++;
        }
        return character.addAbilityScores(as);
    }

    public void setCharacterInfo(tableCharacter cData, Scene scene) {
        String sheetNodes [] = {"Name", "Level", "Alignment", "Race", "Class", "Background"};
        System.out.println("StrengthSpinner: " + scene.lookup("#StrengthSpinner"));
        ((TextField) scene.lookup("#NameSheet")).setText(cData.getName());
        ((Label) scene.lookup("#LevelSheet")).setText(valueOf(cData.getLevel()));
        ((Label) scene.lookup("#AlignmentSheet")).setText(cData.getAlignment());
        ((Label) scene.lookup("#RaceSheet")).setText(cData.getAlignment());
        ((Label) scene.lookup("#ClassSheet")).setText(cData.getKlass());
    }

    public void setContent(String item, String category, Scene scene) throws IOException {
        try {
            Text current = (Text) scene.lookup("#Current" + category);
            current.setText("Current " + category + ": " + item);
            Label l = (Label) scene.lookup("#" + category + "Sheet");
            l.setText(category + ": " + item);
            String fileText = new String(Files.readAllBytes(Paths.get("src/main/resources/" + category + "/" + item)));
            Text t = (Text) scene.lookup("#" + category + "Content");
            t.setText(fileText);
        }
        catch (IOException e){
            System.out.println("Exception caught: File read error" + e);
        }
    }

    public int[] stringToArray(String s) {
        int a [] = new int[6];
        System.out.println("s: " + s);
        return a;
    }

    public static void main(String[] args) {
        launch();
    }
}