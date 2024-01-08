package com.cs;

import java.util.ArrayList;
import java.util.HashMap;

public class Shuttle {
    public int id;//穿梭车ID

    public String name;//穿梭车名称
    public int currentPosition;//当前位置
    public HashMap<Integer, Place> path;//当前规划好的完整路径
    public ArrayList<Place> unObstructedPath;//通畅的路径，可以直接按此路径运行

    public Shuttle(int id, String name, int currentPosition, HashMap<Integer, Place> path, ArrayList<Place> unObstructedPath) {
        this.id = id;
        this.name = name;
        this.currentPosition = currentPosition;
        this.path = path;
        this.unObstructedPath = unObstructedPath;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Shuttle shuttle = (Shuttle) obj;
        return id == shuttle.id;
    }
}
