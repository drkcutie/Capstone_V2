package com.example.capstone_2;

import com.example.capstone_2.util.Albums;
import com.example.capstone_2.util.Artist;
import com.example.capstone_2.util.Functions;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.*;

public class playlistController implements Initializable {

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
    private BorderPane sideTab;

    @FXML
    private TabPane tabContainers;
    private ArrayList<String> playlists;
    private Set<String> artists;
    private Set<String> albums;
    private  File[] files;
    private File[] files1;

    private File playlistDirectory;
    private String sampleDirectory;
    private int lastArtistIndex;
    private int lastAlbumIndex;





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            playlistDirectory = new File("src/Music");

        } catch (Exception e) {
            System.out.println("File not found!!!!!!!!!!!!!!!!!!!");
        }

        files = playlistDirectory.listFiles();
        playlists = new ArrayList<>();
        if(files != null){
            for (File file : files) {
                if (file.isDirectory()) {
                    playlists.add(file.getName());
                } else {
                    System.out.println("Not a folder");
                }
            }
        }

        files1 = playlistDirectory.listFiles();

        if(files1 != null){
            for (File playlist : files1) {
                File[] playListFiles = playlist.listFiles(); // playlist folders

                if(playListFiles != null){
                    for(File file : playListFiles){
                        if (Functions.checkFile(file)) {
                            Map<String, String> map = Functions.extractMetadata(file.getPath()); // check audio file foreach playlist
                           String artist = map.get("Artist");
                           String album = map.get("Album");
                           String filepath = file.getPath();

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


//        for (Map.Entry<String, ArrayList<String>> entry : artistMap.entrySet()) {
//            String artist = entry.getKey();
//            ArrayList<String> filePaths = entry.getValue();
//
//            System.out.println("Artist: " + artist);
//            System.out.println("File Paths: " + filePaths);
//            System.out.println();
//        }
        playlistContentList.getItems().addAll(playlists);
        artistContentList.getItems().addAll(Artist.getAllArtists());
        albumContentList.getItems().addAll(Albums.getAllAlbums());
    }
}
