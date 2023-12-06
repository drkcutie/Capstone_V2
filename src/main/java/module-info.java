module com.example.capstone_2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires org.apache.commons.io;
    requires java.desktop;
    requires jaudiotagger;

    opens com.example.capstone_2 to javafx.fxml;
    exports com.example.capstone_2;
}