module com.example.munch_map {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;
    requires com.zaxxer.hikari;


    opens com.example.munch_map to javafx.fxml;
    exports com.example.munch_map;
}