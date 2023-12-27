package com.cs.test;

import com.cs.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class PetriTest {
    public static void main(String[] args) {
        //手动创建path
//        HashMap <Integer,Place> path0 = new HashMap<>();
//        path0.put(0,new MapInfo().getMap().places[26]);
//        path0.put(1,new MapInfo().getMap().places[7]);
//        path0.put(2,new MapInfo().getMap().places[6]);
//        path0.put(3,new MapInfo().getMap().places[13]);
//        path0.put(4,new MapInfo().getMap().places[14]);
//        path0.put(5,new MapInfo().getMap().places[15]);
//        path0.put(6,new MapInfo().getMap().places[10]);
//        path0.put(7,new MapInfo().getMap().places[11]);
//        path0.put(8,new MapInfo().getMap().places[28]);
//
//        HashMap <Integer,Place> path1 = new HashMap<>();
//        path1.put(0,new MapInfo().getMap().places[13]);
//        path1.put(1,new MapInfo().getMap().places[14]);
//        path1.put(2,new MapInfo().getMap().places[20]);
//        path1.put(3,new MapInfo().getMap().places[17]);
//        path1.put(4,new MapInfo().getMap().places[18]);
//        path1.put(5,new MapInfo().getMap().places[12]);
//        path1.put(6,new MapInfo().getMap().places[11]);
//        path1.put(7,new MapInfo().getMap().places[10]);
//        path1.put(8,new MapInfo().getMap().places[9]);




        //初始化穿梭车状态
        Token[] tokens = new Token[4];
        tokens[0] = new Token(0, "穿梭车0_红色", true, 0, 1, 3, 3, 26, null);
        tokens[1] = new Token(1, "穿梭车1_绿色", false, 0, 1, 1, 1, 28, null);
        tokens[2] = new Token(2, "穿梭车2_蓝色", false, Math.PI / 2, 1, 9, 9, 11, null);
        tokens[3] = new Token(3, "穿梭车3_青色", false, Math.PI / 2, 1, 7, 7, 5, null);

        //初始化货位数组
        int[] storageInfo = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0};

        //调用A*算法，获得各托肯的路径

        tokens[0].path = new PetriAstar().start(new MapInfo().getMap(), storageInfo, tokens, 0);
        tokens[1].path = new PetriAstar().start(new MapInfo().getMap(), storageInfo, tokens, 1);
//        tokens[0].path = path0;
//        tokens[1].path = path1;
        tokens[2].path = new PetriAstar().start(new MapInfo().getMap(), storageInfo, tokens, 2);
        tokens[3].path = new PetriAstar().start(new MapInfo().getMap(), storageInfo, tokens, 3);


        //打印路径
        for (int i = 0; i < 4; i++) {
            if (tokens[i].path != null) {
                for (HashMap.Entry<Integer, Place> entry : tokens[i].path.entrySet()) {
                    System.out.println(tokens[i].name + "___序号: " + entry.getKey() + ", 库所: " + entry.getValue().name);
                }
            } else {
                System.out.println(tokens[i].name + "___路径为空");
            }

        }
        // 打印冲突库所集合

        ArrayList<conflictPlaceSet> AllConflictPlaceSets = getAllConflictPlaceSets(tokens);
        for (conflictPlaceSet conflictSet : AllConflictPlaceSets) {
            System.out.println("冲突库所集合：");
            for (Place place : conflictSet.placeSet) {
                System.out.println(place.name);
            }
            System.out.println("冲突类型：" + conflictSet.conflictType);
            System.out.println("冲突的托肯A：" + conflictSet.conflictTokenA.name);
            System.out.println("冲突的托肯B：" + conflictSet.conflictTokenB.name);
            System.out.println("--------------------");
        }

        // 打印死锁库所集合
        ArrayList<conflictPlaceSet> deadlockSets = detectDeadlocks(AllConflictPlaceSets);
        for (conflictPlaceSet conflictSet : deadlockSets) {
            System.out.println("死锁库所集合：");
            for (Place place : conflictSet.placeSet) {
                System.out.println(place.name); // 假设 Place 类有一个 name 属性
            }
            System.out.println("死锁类型：" + conflictSet.conflictType);
            System.out.println("冲突的托肯A：" + conflictSet.conflictTokenA.name); // 假设 Token 类有一个 name 属性
            System.out.println("冲突的托肯B：" + conflictSet.conflictTokenB.name); // 假设 Token 类有一个 name 属性
            System.out.println("--------------------");
        }


    }

    /**
     * 获取两token间的冲突库所集合
     * @param tokens
     * @param tokenIndexA
     * @param tokenIndexB
     * @return
     */
    public static ArrayList <conflictPlaceSet>  getConflictPlaceSet (Token[] tokens, int tokenIndexA, int tokenIndexB){

        Map<Integer, Place> pathA = tokens[tokenIndexA].path;
        Map<Integer, Place> pathB = tokens[tokenIndexB].path;
        ArrayList<conflictPlaceSet> conflictPlaceSets = new ArrayList<>(); // 存储所有冲突库所集合的列表
        ArrayList<Place> currentConflictSet = new ArrayList<>(); // 当前正在处理的冲突库所集合
        String currentConflictType = ""; // 当前冲突库所集合的类型

        // 遍历路径A中的每个库所
        for (Map.Entry<Integer, Place> entryA : pathA.entrySet()) {
            // 如果路径B中也存在该库所，则存在冲突
            if (pathB.containsValue(entryA.getValue())) {
                // 如果当前冲突库所集合为空，则直接添加该库所
                if (currentConflictSet.isEmpty()) {
                    currentConflictSet.add(entryA.getValue());
                } else {
                    // 否则，需要判断当前库所与上一个冲突库所是否连续
                    Place lastPlaceInConflictSet = currentConflictSet.get(currentConflictSet.size() - 1);
                    int indexInPathAForLastPlace = getKeyByValue(pathA, lastPlaceInConflictSet);
                    int indexInPathAForCurrentPlace = entryA.getKey();

                    int indexInPathBForLastPlace = getKeyByValue(pathB, lastPlaceInConflictSet);
                    int indexInPathBForCurrentPlace = getKeyByValue(pathB, entryA.getValue());

                    // 如果连续，则添加到当前冲突库所集合中，并更新冲突类型
                    if (Math.abs(indexInPathAForLastPlace - indexInPathAForCurrentPlace) == 1 &&
                            Math.abs(indexInPathBForLastPlace - indexInPathBForCurrentPlace) == 1) {
                        currentConflictSet.add(entryA.getValue());
                        if (indexInPathAForLastPlace < indexInPathAForCurrentPlace && indexInPathBForLastPlace < indexInPathBForCurrentPlace ||
                                indexInPathAForLastPlace > indexInPathAForCurrentPlace && indexInPathBForLastPlace > indexInPathBForCurrentPlace) {
                            currentConflictType = "同向冲突库所集合";
                        } else {
                            currentConflictType = "反向冲突库所集合";
                        }
                    } else {
                        // 如果不连续，则将当前冲突库所集合添加到列表中，并开始新的冲突库所集合
                        conflictPlaceSets.add(new conflictPlaceSet(new ArrayList<>(currentConflictSet), currentConflictType,tokens[tokenIndexA],tokens[tokenIndexB]));
                        currentConflictSet.clear();
                        currentConflictSet.add(entryA.getValue());
                        currentConflictType = "单冲突库所集合";
                    }
                }
            }
        }

        // 如果最后一个冲突库所集合非空，则添加到列表中
        if (!currentConflictSet.isEmpty()) {
            conflictPlaceSets.add(new conflictPlaceSet(currentConflictSet, currentConflictType,tokens[tokenIndexA],tokens[tokenIndexB]));
        }

        return conflictPlaceSets;

    }

    /**
     * 根据值获取键的函数
     * @param map
     * @param value
     * @return
     * @param <K>
     * @param <V>
     */
    public static <K, V> K getKeyByValue(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * 获取所有token间的冲突库所集合
     * @param tokens
     * @return
     */
    public static ArrayList<conflictPlaceSet> getAllConflictPlaceSets(Token[] tokens) {
    ArrayList<conflictPlaceSet> AllConflictPlaceSets = new ArrayList<>();

    // 遍历所有的Token对
    for (int i = 0; i < tokens.length; i++) {
        for (int j = i + 1; j < tokens.length; j++) {
            // 获取Token对之间的冲突库所集合
            ArrayList<conflictPlaceSet> conflictPlaceSets = getConflictPlaceSet(tokens, i, j);
            // 将冲突库所集合添加到AllConflictPlaceSets中
            AllConflictPlaceSets.addAll(conflictPlaceSets);
        }
    }

    return AllConflictPlaceSets;
}

    public static ArrayList<conflictPlaceSet> detectDeadlocks(ArrayList<conflictPlaceSet> AllConflictPlaceSets) {
        ArrayList<conflictPlaceSet> deadlockSets = new ArrayList<>();

        for (conflictPlaceSet conflictSet : AllConflictPlaceSets) {
            Place startPlace = conflictSet.placeSet.get(0);
            Place endPlace = conflictSet.placeSet.get(conflictSet.placeSet.size() - 1);

            if (conflictSet.conflictType.equals("同向冲突库所集合") &&
                ((startPlace.index == conflictSet.conflictTokenA.start && endPlace.index == conflictSet.conflictTokenA.end) ||
            (startPlace.index == conflictSet.conflictTokenB.start && endPlace.index == conflictSet.conflictTokenB.end) || (endPlace.index == conflictSet.conflictTokenA.start && startPlace.index == conflictSet.conflictTokenA.end) ||
                        (endPlace.index == conflictSet.conflictTokenB.start && startPlace.index == conflictSet.conflictTokenB.end))) {
                conflictSet.conflictType = "同向包含";
                deadlockSets.add(conflictSet);
            } else if (conflictSet.conflictType.equals("反向冲突库所集合")) {
                if ((startPlace.index == conflictSet.conflictTokenA.start && endPlace.index == conflictSet.conflictTokenA.end) ||
                        (startPlace.index == conflictSet.conflictTokenB.start && endPlace.index == conflictSet.conflictTokenB.end) || (endPlace.index == conflictSet.conflictTokenA.start && startPlace.index == conflictSet.conflictTokenA.end) ||
                        (endPlace.index == conflictSet.conflictTokenB.start && startPlace.index == conflictSet.conflictTokenB.end)) {
                    conflictSet.conflictType = "反向包含";
                    deadlockSets.add(conflictSet);
                } else if ((startPlace.index == conflictSet.conflictTokenA.start && endPlace.index == conflictSet.conflictTokenB.start) ||
                        (startPlace.index == conflictSet.conflictTokenB.start && endPlace.index == conflictSet.conflictTokenA.start)) {
                    conflictSet.conflictType = "反向起始冲撞";
                    deadlockSets.add(conflictSet);
                } else if ((startPlace.index == conflictSet.conflictTokenA.end && endPlace.index == conflictSet.conflictTokenB.end) ||
                        (startPlace.index == conflictSet.conflictTokenB.end && endPlace.index == conflictSet.conflictTokenA.end)) {
                    conflictSet.conflictType = "反向终点冲撞";
                    deadlockSets.add(conflictSet);
                }
            }
        }

        return deadlockSets;
    }
}
