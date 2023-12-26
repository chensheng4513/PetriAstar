package com.cs;

import java.util.ArrayList;

public class Place implements Comparable<Place> {
    public int index;
    public String name;
    public Coord coord; // 坐标
    public int g;
    public int g1;
    public int g2;
    public int g3;
    public int h;
    public Place parent;


    public ArrayList<Integer> backwardPlaceIndexSet;

    public Place(int index){this.index = index;}

    public Place(int x, int y)
    {
        this.coord = new Coord(x, y);
    }

    public Place(int index,String name,Coord coord, int g, int g1, int g2, int g3, int h, Place parent,ArrayList<Integer> backwardPlaceIndexSet) {
        this.name = name;
        this.coord = coord;
        this.g = g;
        this.g1 = g1;
        this.g2 = g2;
        this.g3 = g3;
        this.h = h;
        this.parent = parent;
        this.index = index;
        this.backwardPlaceIndexSet = backwardPlaceIndexSet;

    }

    @Override
    public int compareTo( Place o) {
        if (o == null) return -1;
        if (g + h > o.g + o.h)
            return 1;
        else if (g + h < o.g + o.h) return -1;
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Place) {
            Place node = (Place) obj;
            return coord.x == node.coord.x && coord.y == node.coord.y;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return coord.x * 31 + coord.y;
    }

    @Override
    public String toString() {
        return "Place [名称=" + name + ", x坐标=" + coord.x +" y坐标=" + coord.y + "]";
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getG1() {
        return g1;
    }

    public void setG1(int g1) {
        this.g1 = g1;
    }

    public int getG2() {
        return g2;
    }

    public void setG2(int g2) {
        this.g2 = g2;
    }

    public int getG3() {
        return g3;
    }

    public void setG3(int g3) {
        this.g3 = g3;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public Place getParent() {
        return parent;
    }

    public void setParent(Place parent) {
        this.parent = parent;
    }

    public ArrayList<Integer> getBackwardPlaceIndexSet() {
        return backwardPlaceIndexSet;
    }

    public void setBackwardPlaceIndexSet(ArrayList<Integer> backwardPlaceIndexSet) {
        this.backwardPlaceIndexSet = backwardPlaceIndexSet;
    }
}
