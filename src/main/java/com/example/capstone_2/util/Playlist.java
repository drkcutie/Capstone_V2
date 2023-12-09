package com.example.capstone_2.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Playlist {

    public static Map<String, ArrayList<String>> playlistMap = new HashMap<>();

    public static String [] getAllPlaylist()
    {
        return playlistMap.keySet().toArray(new String[0]);
    }
    public static ArrayList<String> getSongsfromPlaylist(String name)
    {
        return playlistMap.get(name);
    }



}