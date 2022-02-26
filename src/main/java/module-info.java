module com.example.dndbuddy {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;


    opens com.example.dndbuddy to javafx.fxml;
    exports com.example.dndbuddy;
}