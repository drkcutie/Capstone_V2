package com.example.capstone_2;

import com.example.capstone_2.util.Functions;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class footerController implements Initializable {

    @FXML
    private Label currentDuration;
    @FXML
    private Label maxDurationLabel;
    @FXML
    private Button forwardButton;
    @FXML
    private Button playButton;
    @FXML
    private Button prevButton;
    @FXML
    private Label songLabel;
    @FXML
    private ImageView songImage;
    @FXML
    private Slider progressSlider;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Label artistLabel;
    @FXML
    private ImageView shuffleButton;
    @FXML
    private ImageView repeatButton;
    @FXML
    private  ImageView volumeImage;

    private Media media;
    FileInputStream fileInputStream;
    private Timer timer;
    private TimerTask task;
    private MediaPlayer mediaPlayer;
    private File songs_directory, icon_directory;
    private File[] files;
    private ArrayList<File> songs;


    private Map<String, Image> images = new HashMap<String, Image>();

    public String songName;


    boolean running = false , shuffle = false;
    int repeatState  = 0; //1 for no repeat //2 for repeat whole playlist // 3 for repeat song;
    private int songNumber, prevSong = 0;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        songs = new ArrayList<File>();
        try {
            songs_directory = new File("src/Music/Playlist1");
            icon_directory = new File("src/img/Icons");

        } catch (Exception e) {
            System.out.println("File not found!!!!!!!!!!!!!!!!!!!");
        }
        files = songs_directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (Functions.isImageFile(file)) {
                    System.out.println("Skipping image file " + file.getName());
                } else {
                    songs.add(file);
                }
            }
        }
        File[] temp = icon_directory.listFiles();
        for(File file: temp)
        {
            Image image = new Image(file.toURI().toString());
            String name = Functions.nameWithoutExtension(file);
            images.put(name,image);
        }

        setCurrentSong();

        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
                if(volumeSlider.getValue() > 80)
                {
                    volumeImage.setImage(images.get("volume-max"));
                }
                else if(volumeSlider.getValue() > 40)
                {
                    volumeImage.setImage(images.get("volume-med"));
                }
                else if (volumeSlider.getValue() > 1) {
                    volumeImage.setImage(images.get("volume-min"));
                }
                else
                {
                    volumeImage.setImage(images.get("volume-mute"));
                }
            }
        });

        progressSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                if (progressSlider.isValueChanging()) {
                    double end = mediaPlayer.getTotalDuration().toSeconds();
                    double newPosition = progressSlider.getValue() * 0.01 * end;
                    mediaPlayer.seek(Duration.seconds(newPosition));
                }
            }
        });

        progressSlider.setOnMouseClicked(event -> {
            double end = mediaPlayer.getTotalDuration().toSeconds();
            double newPosition = progressSlider.getValue() * 0.01 * end;
            mediaPlayer.seek(Duration.seconds(newPosition));
        });


    }

    void setSong(int songNumber, ArrayList<File> songs)
    {
        // receives an array of songs from the playlist and plays the music.
        this.songNumber = songNumber;
        this.songs = songs;
        setCurrentSong();
    }


    void playProgress(ActionEvent actionEvent) {
        if (mediaPlayer != null) {
            timer = new Timer();
            task = new TimerTask() {
                @Override
                public void run() {
                    // Duration and Label
                    Duration duration = mediaPlayer.getMedia().getDuration();

                    double current = mediaPlayer.getCurrentTime().toSeconds();
                    double end = mediaPlayer.getTotalDuration().toSeconds();
                    double progress = current / end * 100;
                    double targetProgress = progressSlider.getValue() + 0.1;

                    long minutes = (long) current / 60;
                    long seconds = (long) current % 60;
                    Platform.runLater(() -> {
                        currentDuration.setText(String.format("%02d:%02d", minutes, seconds));

                    });

                    if (targetProgress <= progress) {
                        progressSlider.setValue(targetProgress);
                    }
                    if (current / end == 1.0) {
                        if(repeatState == 2)
                        {
                            setCurrentSong();
                        } else
                        {
                            try {
                                forwardMusic(actionEvent);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            };
            timer.scheduleAtFixedRate(task, 1000, 1000);
        }
    }




    void getDuration() {
        Duration duration = mediaPlayer.getMedia().getDuration();

        if (duration != null && !duration.isUnknown()) {
            long totalSeconds = (long) duration.toSeconds();
            long minutes = totalSeconds / 60;
            long seconds = totalSeconds % 60;

            String formattedDuration = String.format("%02d:%02d", minutes, seconds);
            System.out.println("Duration of the song: " + formattedDuration);
            maxDurationLabel.setText(formattedDuration);
        } else {
            System.out.println("Invalid or null duration");
            maxDurationLabel.setText("Invalid Duration");
        }
    }

    @FXML
    void toggleShuffle(MouseEvent event) {
        shuffle = !shuffle;
        if(shuffle)
        {
            shuffleButton.setImage(images.get("shuffle-toggled"));
        }
        else
        {
            shuffleButton.setImage(images.get("shuffle-untoggled"));
        }
        System.out.println("Shuffle = "+shuffle);
    }
    @FXML
    void toggleRepeat(MouseEvent event) {
        if(repeatState == 0)
        {
            repeatState = 1;
            repeatButton.setImage(images.get("repeat-toggled1"));
        }
        else if (repeatState == 1)
        {
            repeatState = 2;
            repeatButton.setImage(images.get("repeat-toggled2"));
        }
        else
        {
            repeatState = 0;
            repeatButton.setImage(images.get("repeat-untoggled"));
        }
        System.out.println("Repeat = "+repeatState);
    }

    boolean state() {
        boolean temp = running;
        running = !running;
        return temp;
    }
    void setSongMetadata()
    {
        String path = songs.get(songNumber).getPath();
        Map<String, String> map = Functions.extractMetadata(path);
        Platform.runLater(() -> {
            System.out.println(path);
            songLabel.setText(map.get("Title"));
            artistLabel.setText(map.get("Artist"));
            songImage.setImage(Functions.extractAndDisplayAlbumCover(path));
        });
    }
    @FXML
    void forwardMusic(ActionEvent event) throws IOException {
        prevSong = songNumber;

        if (songNumber < songs.size() -1 && !shuffle)
            songNumber++;
        else if(shuffle)
        {
            Random rand = new Random();
            while(songNumber == prevSong){
                songNumber = rand.nextInt(songs.size());
            }
        }
        else if(repeatState == 0)
        {
            songNumber = 0;
            setSongMetadata();
            mediaPlayer.stop();
            progressSlider.setValue(0);
            return;
        }
        else
            songNumber = 0;



       setCurrentSong();
    }

    @FXML
    void playMusic(ActionEvent event) {
        if (state())
            mediaPlayer.play();
        else
            mediaPlayer.pause();

        playProgress(event);
    }
    void setCurrentSong()
    {

        if(media == null || mediaPlayer == null)
        {
            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
        }
        songName = songs.get(songNumber).getName();
        mediaPlayer.stop();
        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        running = true;
        mediaPlayer.setOnReady(this::getDuration);
        mediaPlayer.play();
        playMusic(new ActionEvent());
        progressSlider.setValue(0);
        setSongMetadata();
        System.out.println("Set current song is called");
    }
    @FXML
    void prevMusic(ActionEvent event) throws IOException {
        if(songNumber != prevSong)
        songNumber = prevSong;
        else if(songNumber != 0)
            songNumber--;

        setCurrentSong();

    }

}
