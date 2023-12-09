package com.example.capstone_2;

import com.example.capstone_2.util.example;
import javafx.scene.media.Media;
import javafx.util.Duration;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import javafx.scene.media.MediaPlayer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.control.cell.PropertyValueFactory;

public class SelectionController {
    private File directory;
    private File[] files;
    private ArrayList<File> songs;
    private MediaPlayer mediaPlayer;
    private Media media;

    @FXML
    private ResourceBundle resources;

    @FXML
    private Text PlaylistName;

    @FXML
    private URL location;
    @FXML
    private Pane playPicture;


    @FXML
    private TableColumn<example, Integer> Number;

    @FXML
    private TableColumn<example, String> album;

    @FXML
    private TableView<example> tableMusic;

    @FXML
    private TableColumn<example, String> timeDuration;

    @FXML
    private TableColumn<example, String> title;



    @FXML
    void initialize() {
        assert Number != null : "fx:id=\"Number\" was not injected: check your FXML file 'selection.fxml'.";
        assert album != null : "fx:id=\"album\" was     not injected: check your FXML file 'selection.fxml'.";
        assert playPicture != null : "fx:id=\"playPicture\" was not injected: check your FXML file 'selection.fxml'.";
        assert tableMusic != null : "fx:id=\"tableMusic\" was not injected: check your FXML file 'selection.fxml'.";
        assert timeDuration != null : "fx:id=\"timeDuration\" was not injected: check your FXML file 'selection.fxml'.";
        assert title != null : "fx:id=\"title\" was not injected: check your FXML file 'selection.fxml'.";
        assert PlaylistName != null : "fx:id=\"PlaylistName\" was not injected: check your FXML file 'selection.fxml'.";

        songs = new ArrayList<>();
        try {
            directory = new File("src/Music/Playlist1");
        } catch (Exception e) {
            System.out.println("File not found!!!!!!!!!!!!!!!!!!!");
        }
        files = directory.listFiles();
        if (files != null) {
            Collections.addAll(songs, files);
        }
        if (files != null && files.length > 0) {
            File parentDirectory = files[0].getParentFile();
            if (parentDirectory != null) {
                String playlistName = parentDirectory.getName();
                PlaylistName.setText(playlistName);
            }
        }else{
            PlaylistName.setText("PLAYLIST");
        }
        ObservableList<example> data = FXCollections.observableArrayList();


        for (int i = 1; i <= songs.size(); i++) {
            final int index = i;  // Create a final variable to capture the correct value of i
            File song = songs.get(i - 1);

            // Create a Media object for each file
            Media songMedia = new Media(song.toURI().toString());

            // Create a MediaPlayer for the Media object
            MediaPlayer songPlayer = new MediaPlayer(songMedia);

            // Set the onReady event
            songPlayer.setOnReady(() -> {
                // Get the duration of the media
                Duration duration = songMedia.getDuration();

                // Convert duration to minutes and seconds
                long totalSeconds = (long) duration.toSeconds();
                long minutes = totalSeconds / 60;
                long seconds = totalSeconds % 60;
                String formattedDuration = String.format("%02d:%02d", minutes, seconds);

                // Create example object and add it to the data list
                example songExample = new example(index, song.getName(), "Unknown", formattedDuration);
                data.add(songExample);

                // Dispose of the MediaPlayer after obtaining the duration
                songPlayer.dispose();
            });
        }
        Number.setCellValueFactory(new PropertyValueFactory<>("number"));  // Use "number" instead of "Number"
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        album.setCellValueFactory(new PropertyValueFactory<>("album"));
        timeDuration.setCellValueFactory(new PropertyValueFactory<>("timeDuration"));

        tableMusic.setItems(data);
    }



}
