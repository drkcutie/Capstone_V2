package com.example.capstone_2;

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

import  javafx.scene.image.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class musicPlayerController implements Initializable {

    @FXML
    private Label currentDuration;


    @FXML
    private Label maxDurationLabel;

    @FXML
    private Label chooseMusic;
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

    private Media media;
    private Timer timer;
    private TimerTask task;
    private MediaPlayer mediaPlayer;
    private File directory;
    private File[] files;
    private ArrayList<File> songs;

    boolean running;
    private int songNumber;

    private long minutes = 0,seconds = 0;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        songs = new ArrayList<File>();
        try {
            directory = new File("src/Music");

        } catch (Exception e) {
            System.out.println("File not found!!!!!!!!!!!!!!!!!!!");
        }
        files = directory.listFiles();
        if (files != null) {
            Collections.addAll(songs, files);
        }
        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        songLabel.setText(songs.get(songNumber).getName());

//        File file = new File("src/Images/playButton.png");
//        Image img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(String.valueOf(file))));
//        ImageView imageView = new ImageView(img);

//        songImage.setImage(img);

        FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream("src/Images/pauseButton.png");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Image image = new Image(fileInputStream);
        songImage.setImage(image);


        mediaPlayer.setOnReady(this::getDuration);

        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
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
    void playProgress(ActionEvent actionEvent)
    {
        if(mediaPlayer!= null)
        {
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
                    Platform.runLater(()->{
                        currentDuration.setText(String.format("%02d:%02d", minutes, seconds));

                    });

                    if (targetProgress <= progress) {
                        progressSlider.setValue(targetProgress);
                    }
                    if(current/end == 1.0)
                    {
                        mediaPlayer.stop();
                       running = false;
                    }
                }
            };
            timer.scheduleAtFixedRate(task , 1000, 1000);
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
    void reset()
    {
        progressSlider.setValue(0);
    }
    boolean state() {

        boolean temp = running;
        running = !running;
        return temp;
    }

    @FXML
    void chooseMusic(MouseEvent event) {

    }

    @FXML
    void forwardMusic(ActionEvent event) {
        if(songNumber < songs.size() - 1)
            songNumber++;
        else
            songNumber= 0;

            mediaPlayer.stop();
            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            songLabel.setText(songs.get(songNumber).getName());
            running = true;
        mediaPlayer.setOnReady(this::getDuration);
            playMusic(event);
            reset();


    }

    @FXML
    void playMusic(ActionEvent event) {
        if(state())
         mediaPlayer.play();
        else
         mediaPlayer.pause();

        playProgress(event);
    }
    @FXML
    void prevMusic(ActionEvent event) {
        if(songNumber != 0 ) {
            songNumber--;
        }
        else
            songNumber = songs.size() - 1;

             mediaPlayer.stop();
            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            songLabel.setText(songs.get(songNumber).getName());


            running = true;
        mediaPlayer.setOnReady(this::getDuration);
            playMusic(event);
            reset();

    }


}
