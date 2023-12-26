package com.cs;

public class MapInfo {

    public Place[] places;//库所集合
    public Transition[] transitions;//变迁集合

    public MapInfo(Place[] places, Transition[] transitions) {
        this.places = places;
        this.transitions = transitions;

    }

    public MapInfo() {
        this.places = null;
        this.transitions = null;
    }

    public MapInfo getMap() {
        Place[] places = new PetriNet().creatPlace();
        Transition[] transitions = new PetriNet().creatTransition();
        MapInfo info = new MapInfo(places, transitions);
        return info;
    }
}
