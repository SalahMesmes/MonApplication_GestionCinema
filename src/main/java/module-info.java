module org.example.monapplication {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires junit;

    opens org.example.monapplication to javafx.fxml;
    exports org.example.monapplication;
    exports controllers;
    exports test to junit;
    opens controllers to javafx.fxml;
}