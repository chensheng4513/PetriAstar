package com.cs;

public class Transition {
    public int index;
    public String name;
    public int cost;
    public Boolean isEnabled;
    public Boolean isFired;
    public int backwardPlaceIndex;
    public int forwardPlaceIndex;


    public Transition(int index) {
        this.index = index;
    }

    public Transition(int index,String name,int cost,int forwardPlaceIndex,int backwardPlaceIndex,Boolean isEnabled ,Boolean isFired) {
        this.index = index;
        this.name = name;
        this.cost = cost;
        this.forwardPlaceIndex = forwardPlaceIndex;
        this.backwardPlaceIndex = backwardPlaceIndex;
        this.isEnabled = isEnabled;
        this.isFired = isFired;

    }

    @Override
    public String toString() {
        return "Transition [名称=" + name + ", 代价=" + cost + "]";
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

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public Boolean getFired() {
        return isFired;
    }

    public void setFired(Boolean fired) {
        isFired = fired;
    }

    public int getBackwardPlaceIndex() {
        return backwardPlaceIndex;
    }

    public void setBackwardPlaceIndex(int backwardPlaceIndex) {
        this.backwardPlaceIndex = backwardPlaceIndex;
    }

    public int getForwardPlaceIndex() {
        return forwardPlaceIndex;
    }

    public void setForwardPlaceIndex(int forwardPlaceIndex) {
        this.forwardPlaceIndex = forwardPlaceIndex;
    }
}
