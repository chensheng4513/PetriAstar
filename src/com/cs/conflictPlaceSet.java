package com.cs;

import java.util.ArrayList;

public class conflictPlaceSet {
    ArrayList<Place> placeSet ;//库所集合
    String conflictType;//冲突类型
    Token conflictTokenA;//冲突的两个托肯之一，tokenA
    Token conflictTokenB;//冲突的两个托肯之一，tokenB

    public conflictPlaceSet(ArrayList<Place> placeSet, String conflictType, Token conflictTokenA, Token conflictTokenB) {
        this.placeSet = placeSet;
        this.conflictType = conflictType;
        this.conflictTokenA = conflictTokenA;
        this.conflictTokenB = conflictTokenB;
    }


}
