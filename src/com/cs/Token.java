package com.cs;

import java.util.HashMap;

public class Token {
    public int id;//穿梭车ID
    public String name;//穿梭车名称，即颜色
    public boolean isLoad;//是否载货
    public double direction;//行进的方向,0表示x方向，Math.PI/2表示y方向
    public float speed;//速度
    public int position;//当前位置
    public int start;
    public int end;

    public HashMap<Integer, Place> path;//当前的路径


    public Token(int id, String name, boolean isLoad, double direction, float speed, int position, int start, int end, HashMap<Integer, Place> path) {
        this.id = id;
        this.name = name;
        this.isLoad = isLoad;
        this.direction = direction;
        this.speed = speed;
        this.position = position;
        this.start = start;
        this.end = end;
        this.path = path;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isLoad() {
        return isLoad;
    }

    public void setLoad(boolean load) {
        isLoad = load;
    }

    public double getDirection() {
        return direction;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public HashMap<Integer, Place> getPath() {
        return path;
    }

    public void setPath(HashMap<Integer, Place> path) {
        this.path = path;
    }
}
