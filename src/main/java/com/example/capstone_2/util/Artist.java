package com.example.capstone_2.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Artist {

    public static Map<String, ArrayList<String>> artistMap = new HashMap<>();

    public static String [] getAllArtists()
    {
        return artistMap.keySet().toArray(new String[0]);
    }
    public static ArrayList<String> getSongsfromArtist(String name)
    {
        return artistMap.get(name);
    }



}
