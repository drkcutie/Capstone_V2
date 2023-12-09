package com.example.capstone_2.util;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class example {
    private SimpleIntegerProperty number;
    private SimpleStringProperty title;
    private SimpleStringProperty album;
    private SimpleStringProperty timeDuration;


    public example(int number, String title, String album, String timeDuration){
        this.number = new SimpleIntegerProperty(number);
        this.title =  new SimpleStringProperty(title);
        this.album =  new SimpleStringProperty(album);
        this.timeDuration =  new SimpleStringProperty(timeDuration);

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
