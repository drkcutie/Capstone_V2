package com.example.capstone_2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class mainController {
    @FXML
    public playlistTabController playlistTabController;
    @FXML
    public  SelectionController selectionController;
    @FXML
    public  FooterController footerController;

    private void initialize() {
        try {
            FXMLLoader playlistLoader = new FXMLLoader(getClass().getResource("Playlist.fxml"));
            Parent playlistRoot = playlistLoader.load();
           playlistTabController playlistTabController = playlistLoader.getController();

            playlistTabController.init(this);

            // Assuming you have similar code for SelectionController and FooterController
            FXMLLoader selectionLoader = new FXMLLoader(getClass().getResource("Selection.fxml"));
            Parent selectionRoot = selectionLoader.load();
            selectionController = selectionLoader.getController();
            selectionController.init(this);

            FXMLLoader footerLoader = new FXMLLoader(getClass().getResource("Footer.fxml"));
            Parent footerRoot = footerLoader.load();
            FooterController footerController = footerLoader.getController();
            footerController.init(this);

            System.out.println("MAIN CONTROLLER IS INITIALIZED");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
