package com.example.capstone_2.util;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class example {
    private SimpleIntegerProperty number;
    private SimpleStringProperty title;
    private SimpleStringProperty album;
    private SimpleStringProperty timeDuration;

    private ImageView Image;



    public example(int number, String title, String album, String timeDuration , Image image){
        this.number = new SimpleIntegerProperty(number);
        this.title =  new SimpleStringProperty(title);
        this.album =  new SimpleStringProperty(album);
        this.timeDuration =  new SimpleStringProperty(timeDuration);
        Image = new ImageView(image);
        Image.setFitWidth(30);
        Image.setFitHeight(30);

    }


    public int getNumber() {
        return number.get();
    }

    public SimpleIntegerProperty numberProperty() {
        return number;
    }
    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getAlbum() {
        return album.get();
    }

    public SimpleStringProperty albumProperty() {
        return album;
    }

    public void setAlbum(String album) {
        this.album.set(album);
    }

    public ImageView getImage() {
        return Image;
    }

    // Setter for the Image property (if needed)
    public void setImage(Image image) {
        this.Image = new ImageView(image);
    }

    public String getTimeDuration() {
        return timeDuration.get();
    }

    public SimpleStringProperty timeDurationProperty() {
        return timeDuration;
    }

        public void setTimeDuration(String timeDuration) {
            this.timeDuration.set(timeDuration);
        }
}
