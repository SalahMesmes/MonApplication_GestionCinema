module org.example.monapplication {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens org.example.monapplication to javafx.fxml;
    exports org.example.monapplication;
    exports controllers;
    opens controllers to javafx.fxml;
}