package com.example.capstone_2;

import com.example.capstone_2.util.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import java.io.ObjectInputStream;
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
    private static ArrayList<File> songs = new ArrayList<>();
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
    private  TableColumn<example, Integer> Number;

    @FXML
    private  TableColumn<example, String> album;

    @FXML
    private  TableView<example> tableMusic;

    @FXML
    private  TableColumn<example, String> timeDuration;

    @FXML
    private  TableColumn<example, String> title;
    @FXML
    private TableColumn<example,Image> SongImg;

    public String key;
    public enum Type {
        PLAYLIST,
        ALBUM
        ,ARTIST
    }
    static ObservableList<example> data = FXCollections.observableArrayList();



    @FXML
    void initialize() {
        assert Number != null : "fx:id=\"Number\" was not injected: check your FXML file 'selection.fxml'.";
        assert album != null : "fx:id=\"album\" was     not injected: check your FXML file 'selection.fxml'.";
        assert playPicture != null : "fx:id=\"playPicture\" was not injected: check your FXML file 'selection.fxml'.";
        assert tableMusic != null : "fx:id=\"tableMusic\" was not injected: check your FXML file 'selection.fxml'.";
        assert timeDuration != null : "fx:id=\"timeDuration\" was not injected: check your FXML file 'selection.fxml'.";
        assert title != null : "fx:id=\"title\" was not injected: check your FXML file 'selection.fxml'.";
        assert PlaylistName != null : "fx:id=\"PlaylistName\" was not injected: check your FXML file 'selection.fxml'.";

        directory = new File("src/Music/Playlist1");
        files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (Functions.isImageFile(file)) {
                    System.out.println("Skipping image file " + file.getName());
                } else {
                    songs.add(file);
                }
            }
        }
        setPlaylist();
        Number.setCellValueFactory(new PropertyValueFactory<>("number"));  // Use "number" instead of "Number"
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        album.setCellValueFactory(new PropertyValueFactory<>("album"));
        timeDuration.setCellValueFactory(new PropertyValueFactory<>("timeDuration"));
        SongImg.setCellValueFactory(new PropertyValueFactory<>("Image"));
        tableMusic.setItems(data);

    }

    public void setFiles(String key, Type type) {
        songs.clear();
        data.clear();
        PlaylistName.setText(key);
        this.key = key;
        ArrayList<String> temp = new ArrayList<>();
        switch(type)
        {
            case PLAYLIST:
                temp = Playlist.getSongsfromPlaylist(key);
                break;
            case ARTIST:
                temp = Artist.getSongsfromArtist(key);
                break;
            case ALBUM:
                temp = Albums.getSongsfromAlbum(key);
                break;
        }
        for(String path : temp)
        {
            File file = new File(path);
            songs.add(file);
        }
        setPlaylist();
    }


    public static void setPlaylist()
    {

        for (int i = 0; i < songs.size(); i++) {
            final int index = i + 1;  // Create a final variable to capture the correct value of i
            File song = songs.get(i);

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
                Image img = new Image(new File("src/img/default/no-image-icon.jpg").toURI().toString());
                // Create example object and add it to the data list
                example songExample = new example(index, Functions.nameWithoutExtension(song.getName()), "Unknown", formattedDuration,img);
                data.add(songExample);

                // Dispose of the MediaPlayer after obtaining the duration
                songPlayer.dispose();
            });
        }

    }



}
