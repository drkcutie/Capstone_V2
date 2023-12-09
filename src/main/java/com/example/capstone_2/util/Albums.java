package com.example.capstone_2.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Albums {
    public static Map<String, ArrayList<String>> albumMap = new HashMap<>();

    public static String [] getAllAlbums()
    {
        return albumMap.keySet().toArray(new String[0]);
    }
    public static ArrayList<String> getSongsfromAlbum(String name)
    {
        return albumMap.get(name);
    }



}
