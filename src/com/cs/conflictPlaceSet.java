package com.cs;

import java.util.ArrayList;

public class conflictPlaceSet {
    public ArrayList<Place> placeSet ;//库所集合
    public String conflictType;//冲突类型
    public Token conflictTokenA;//冲突的两个托肯之一，tokenA
    public Token conflictTokenB;//冲突的两个托肯之一，tokenB

    public conflictPlaceSet(ArrayList<Place> placeSet, String conflictType, Token conflictTokenA, Token conflictTokenB) {
        this.placeSet = placeSet;
        this.conflictType = conflictType;
        this.conflictTokenA = conflictTokenA;
        this.conflictTokenB = conflictTokenB;
    }


}
