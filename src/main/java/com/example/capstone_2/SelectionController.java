package com.example.capstone_2;

import com.example.capstone_2.util.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.media.MediaPlayer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.net.URL;
import java.util.Objects;
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
    private Parent root;
    FooterController footerControllerController;

    public String key;
    public enum MediaType {
        PLAYLIST,
        ALBUM
        ,ARTIST
    }
    static ObservableList<example> data = FXCollections.observableArrayList();



    @FXML
    void initialize() {
        assert Number != null : "fx:id=\"Number\" was not injected: check your FXML file 'Selection.fxml'.";
        assert album != null : "fx:id=\"album\" was     not injected: check your FXML file 'Selection.fxml'.";
        assert playPicture != null : "fx:id=\"playPicture\" was not injected: check your FXML file 'Selection.fxml'.";
        assert tableMusic != null : "fx:id=\"tableMusic\" was not injected: check your FXML file 'Selection.fxml'.";
        assert timeDuration != null : "fx:id=\"timeDuration\" was not injected: check your FXML file 'Selection.fxml'.";
        assert title != null : "fx:id=\"title\" was not injected: check your FXML file 'Selection.fxml'.";
        assert PlaylistName != null : "fx:id=\"PlaylistName\" was not injected: check your FXML file 'Selection.fxml'.";

        setCells();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Footer.fxml"));

        try {
            root = loader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

         footerControllerController = loader.getController();

        tableMusic.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                example selectedExample = tableMusic.getSelectionModel().getSelectedItem();
                if (selectedExample != null) {
                    int index = handleDoubleClick(selectedExample);
                    footerControllerController.setSongfromPlaylist(index, songs);

                }
            }
        });

    }

    public void setCells()
    {
        Number.setCellValueFactory(new PropertyValueFactory<>("number"));  // Use "number" instead of "Number"
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        album.setCellValueFactory(new PropertyValueFactory<>("album"));
        timeDuration.setCellValueFactory(new PropertyValueFactory<>("timeDuration"));
        SongImg.setCellValueFactory(new PropertyValueFactory<>("Image"));
        tableMusic.setItems(data);

    }
    private int handleDoubleClick(example selectedExample) {
        // Obtain the file path from the selected example

        // Do something with the file path on double-click, for example, print it
        System.out.println("Double-clicked on row with file path: " + selectedExample.getNumber());
        // Add your logic to handle the file path as needed on double-click

        // Return the index
        return selectedExample.getNumber()-1;
    }
    public void setFiles(String key, MediaType type) {
        if(Objects.equals(this.key, key))
            return;
        songs.clear();
        data.clear();
        Platform.runLater(() -> {
            PlaylistName.setText(key);

        });

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
            System.out.println("File Path = " +path + "\n" );
            songs.add(file);
        }
        setPlaylist();
        setCells();

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
                Image img = Functions.extractAndDisplayAlbumCover(song.getPath());
                // Create example object and add it to the data list
                example songExample = new example(index, Functions.nameWithoutExtension(song.getName()), "Unknown", formattedDuration,img);
                data.add(songExample);

                // Dispose of the MediaPlayer after obtaining the duration
                songPlayer.dispose();
            });

        }
    }



}