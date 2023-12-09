package com.example.capstone_2.util;


import javafx.scene.image.Image;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.datatype.Artwork;
import org.apache.commons.io.FilenameUtils;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Functions {

    public static boolean isImageFile(File file) {
        // Check if the file is an image file based on its extension.
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png")
                || fileName.endsWith(".gif") || fileName.endsWith(".bmp");
        // You can extend this list based on the image formats you want to exclude.
    }

    public static Map<String, String> extractMetadata(String filePath) {
        Map<String, String> metadata = new HashMap<>();

        try {
            // Read the audio file
            AudioFile audioFile = AudioFileIO.read(new File(filePath));

            // Get the tag (metadata) from the audio file
            Tag tag = audioFile.getTag();
            Artwork artwork = tag.getFirstArtwork();
            // Extract specific metadata fields
            metadata.put("Title", tag.getFirst(FieldKey.TITLE));
            metadata.put("Artist", tag.getFirst(FieldKey.ARTIST));
            metadata.put("Album", tag.getFirst(FieldKey.ALBUM));
//            metadata.put("Genre", tag.getFirst(FieldKey.GENRE));
//            metadata.put("Year", tag.getFirst(FieldKey.YEAR));

        } catch (Exception e) {
            e.printStackTrace();
        }
        if(Objects.equals(metadata.get("Title"), ""))
        {
            File file = new File(filePath);
            String title =  nameWithoutExtension(file);
            metadata.put("Title",title);
            metadata.put("Artist", "Unknown Artist");
        }
        if(Objects.equals(metadata.get("Album"),""))
        {
            metadata.put("Album","Unknown Album");
        }
        return metadata;
    }

    public static Image extractAndDisplayAlbumCover(String filePath) {
        Image image = null;
        try {
            // Read the audio file
            AudioFile audioFile = AudioFileIO.read(new File(filePath));

            // Get the tag (metadata) from the audio file
            Tag tag = audioFile.getTag();

            // Extract album cover (if available)
            Artwork artwork = tag.getFirstArtwork();
            if (artwork != null) {
                // Get the image data as a byte array
                byte[] imageData = artwork.getBinaryData();
                image = getImageFromByteArray(imageData);

                // Perform actions with the image data (e.g., display or save)
                // ...

                System.out.println("Album Cover found and processed.");
            } else {
                File file = new File("src/img/default/no-image-icon.jpg");
                image = new Image(file.toURI().toString());
                System.out.println("No Album Cover found.");
            }

        } catch (CannotReadException | InvalidAudioFrameException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }
    private static Image getImageFromByteArray(byte[] imageData) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);
        return new Image(inputStream);
    }
    public static String nameWithoutExtension(File file)
    {
        String name = file.getName();
        name = FilenameUtils.removeExtension(name);
        return name;
    }


    public static boolean checkFile(File file){
        if(file.isFile() && file.exists()){
            String filename = file.getName().toLowerCase();
            if(filename.endsWith(".mp3") || filename.endsWith(".m4a") || filename.endsWith(".m4v") || filename.endsWith(".wav")){
                return true;
            }
        }

        return false;
    }
    public static String nameWithoutExtension(String filename) {
        String cleanedName = filename.replaceAll("^[0-9]+[.\\s]+", "").trim();

        // Remove file extension
        int extensionIndex = cleanedName.lastIndexOf(".");
        if (extensionIndex != -1) {
            return cleanedName.substring(0, extensionIndex);
        }
        return cleanedName;
    }


    

}