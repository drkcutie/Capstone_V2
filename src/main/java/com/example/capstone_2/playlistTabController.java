package com.example.capstone_2;

import com.example.capstone_2.util.Albums;
import com.example.capstone_2.util.Artist;
import com.example.capstone_2.util.Playlist;
import com.example.capstone_2.util.Functions;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;


import java.awt.Button;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class playlistTabController implements Initializable {

    @FXML
    private BorderPane albumContent;

    @FXML
    private ListView<String> albumContentList;

    @FXML
    private Tab albumTab;

    @FXML
    private BorderPane artistContent;

    @FXML
    private ListView<String> artistContentList;

    @FXML
    private Tab artistTab;

    @FXML
    private BorderPane playListContent;

    @FXML
    private Tab playListTab;

    @FXML
    private ListView<String> playlistContentList;
    @FXML
    private ListView<String> previewSongs;

    @FXML
    private BorderPane sideTab;

    @FXML
    private TabPane tabContainers;
    private Set<String> playlists;
    private Set<String> artists;
    private Set<String> albums;
    private  File[] files;

    private File playlistDirectory;
    private String currentSongs;
    private Parent root;
    SelectionController selectionController;

    File defaultFolderImagePath = new File("src/img/default/folder.png");
    File defaultArtistImagePath = new File("src/img/default/artist.png");
    File defaultAlbumImagePath = new File("src/img/default/Music_album.png");

    private Image defaultPlaylistImage;
    private Image defaultArtistImage;
    private Image defaultAlbumImage;










    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String absolutePathPlaylist = defaultFolderImagePath.getAbsolutePath();
        absolutePathPlaylist = absolutePathPlaylist.replace("\\", "/");
        String absolutePathArtist = defaultArtistImagePath.getAbsolutePath();
        absolutePathArtist = absolutePathArtist.replace("\\", "/");
        String absolutePathAlbum = defaultAlbumImagePath.getAbsolutePath();
        absolutePathAlbum = absolutePathAlbum.replace("\\", "/");



        FXMLLoader loader = new FXMLLoader(getClass().getResource("selection.fxml"));
        try {
             root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        SelectionController selectionController = loader.getController();



        try {

            defaultPlaylistImage = new Image(new File(absolutePathPlaylist).toURI().toString());
            System.out.println("LEGIT PICTURE MAN");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            defaultArtistImage = new Image(new File(absolutePathArtist).toURI().toString());
            System.out.println("LEGIT PICTURE MAN");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            defaultAlbumImage = new Image(new File(absolutePathAlbum).toURI().toString());
            System.out.println("LEGIT PICTURE MAN");
        } catch (Exception e) {
            e.printStackTrace();
        }



        try {
            playlistDirectory = new File("src/Music");

        } catch (Exception e) {
            System.out.println("File not found!!!!!!!!!!!!!!!!!!!");
        }

        files = playlistDirectory.listFiles();


        artists = new HashSet<>();
        albums = new HashSet<>();
        playlists = new HashSet<>();

        if(files != null){
            for (File folder : files) {
                if (folder.isDirectory()) {
                    String playlist = folder.getName();
                    File[]  playListFiles =  folder.listFiles();
                    for (File file : playListFiles) {
                        if (Functions.checkFile(file)) {
                            String filepath = file.getPath();

                            playlists.add(playlist);

                            if (!Playlist.playlistMap.containsKey(playlist)) {
                                Playlist.playlistMap.put(playlist, new ArrayList<>());
                            }
                            Playlist.playlistMap.get(playlist).add(filepath);
                        }

                    }
                }else{
                    System.out.println("Not a folder");
                }
            }

            for (File playlist : files) {
                File[] playListFiles = playlist.listFiles(); // playlist folders

                if(playListFiles != null){
                    for(File file : playListFiles){
                        if (Functions.checkFile(file)) {
                            Map<String, String> map = Functions.extractMetadata(file.getPath()); // check audio file foreach playlist
                           String artist = map.get("Artist");
                           String album = map.get("Album");
                           String filepath = file.getPath();


                           artists.add(artist);
                           albums.add(album);

                            if (!Artist.artistMap.containsKey(artist)) {
                                Artist.artistMap.put(artist, new ArrayList<>());
                            }
                            Artist.artistMap.get(artist).add(filepath);

                            if (!Albums.albumMap.containsKey(album)) {
                                Albums.albumMap.put(album, new ArrayList<>());
                            }
                            Albums.albumMap.get(album).add(filepath);

                        } else {
                            System.out.println("Not an audio file");
                        }



                    }
                }// end of metadata

            }
        }








        playlistContentList.getItems().addAll(Playlist.getAllPlaylist());
        playlistContentList.setCellFactory(param -> new ListCell<String>() {
            ImageView img = new ImageView();

            @Override
            protected void updateItem(String name, boolean empty) {

                super.updateItem(name, empty);
                if (empty) {
                    img.setImage(null);
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(name);
                    img.setFitWidth(50);
                    img.setFitHeight(50);
                    img.setPreserveRatio(true);
                    img.setImage(defaultPlaylistImage);
                    setGraphic(img);
                }
            }
        });


        artistContentList.getItems().addAll(Artist.getAllArtists());
        artistContentList.setCellFactory(param -> new ListCell<String>() {
            ImageView img = new ImageView();

            @Override
            protected void updateItem(String name, boolean empty) {

                super.updateItem(name, empty);
                if (empty) {
                    img.setImage(null);
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(name);
                    img.setFitWidth(50);
                    img.setFitHeight(50);
                    img.setPreserveRatio(true);
                    img.setImage(defaultArtistImage);
                    setGraphic(img);
                }
            }
        });
        albumContentList.getItems().addAll(Albums.getAllAlbums());
        albumContentList.setCellFactory(param -> new ListCell<String>() {
            ImageView img = new ImageView();

            @Override
            protected void updateItem(String name, boolean empty) {

                super.updateItem(name, empty);
                if (empty) {
                    img.setImage(null);
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(name);
                    img.setFitWidth(40);
                    img.setFitHeight(40);
                    img.setPreserveRatio(true);
                    img.setImage(defaultAlbumImage);
                    setGraphic(img);
                }
            }
        });

        playlistContentList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) { // You can adjust the click count as needed
                currentSongs = playlistContentList.getSelectionModel().getSelectedItem();
                selectionController.setFiles(currentSongs, SelectionController.MediaType.PLAYLIST);
            }
        });

        artistContentList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                currentSongs = artistContentList.getSelectionModel().getSelectedItem();
                selectionController.setFiles(currentSongs, SelectionController.MediaType.ARTIST);
            }
        });

        albumContentList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                currentSongs = albumContentList.getSelectionModel().getSelectedItem();
                System.out.println(currentSongs);
                selectionController.setFiles(currentSongs, SelectionController.MediaType.ALBUM);
            }
        });



    }
}
