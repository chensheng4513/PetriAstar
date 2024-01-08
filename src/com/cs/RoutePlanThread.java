package com.cs;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class RoutePlanThread extends Thread{
    public void run(){
        while(true){

                ArrayList<conflictPlaceSet> allConflictPlaceSets = new ArrayList<>();
                //遍历托肯，获得currentTask不为2的托肯的数量
                int num = 0;
                int num1 = 0;
                for (int i = 0; i < ShareData.tokens.length; i++) {
                    if (ShareData.tokens[i].currentTask.taskType != 2) {
                        num++;
                    }
                    if(ShareData.tokens[i].path.isEmpty()){
                        num1++;
                    }
                }

               if((!ShareData.shuttleTasks.isEmpty()) || num > 0 ){//如果任务列表不为空或者有托肯的当前任务没有完成，则开始规划路径

                   if(num1 > 0){//有托肯的路径为空，需要重新规划路径。如果num1=0，说明所有托肯的路径都不为空，不需要重新规划路径
                       for (int i = 0; i < ShareData.tokens.length; i++) {
                           if(ShareData.tokens[i].currentTask.taskType != 2){//如果当前任务未完成，则规划路径,等于2的托肯，已经没有后续任务了，不需要规划路径
                               //规划路径
                               ShareData.tokens[i].path = new PetriAstar().start(new MapInfo().getMap(), ShareData.storageInfo, ShareData.tokens, i);
                               //计算剩余路径
                               calculateRemainPath(ShareData.tokens[i]);
                               //设置初始占用信息
                               ShareData.mapInfo.places[ ShareData.tokens[i].position].occupiedToken =  ShareData.tokens[i];
                           }

                       }

                       //计算各托肯的allConflictPlaceSets
                       allConflictPlaceSets = getUnDeadlockRoute(ShareData.tokens,ShareData.storageInfo);
                   }
               }
               else {
                   break;
               }

               while (true){


                   //遍历托肯，获得currentTask不为2的托肯的数量
                   int num2 = 0;
                   for (int i = 0; i < ShareData.tokens.length; i++) {
                       if (ShareData.tokens[i].currentTask.taskType != 2) {
                           num2++;
                       }
                   }
                   if( num2 == 0 ){
                       break;
                   }

                   //获得托肯本轮的无碰撞和死锁路径
                   getRoundPathForShuttle(ShareData.tokens, ShareData.shuttles, allConflictPlaceSets, ShareData.mapInfo);
                   //如果任意一个token的position与end相等，则说明此条路径结束，将该token的path置空，更新托肯的起点和终点
                   int count = 0;
                   for (int i = 0; i < ShareData.tokens.length; i++) {
                       if (ShareData.tokens[i].position == ShareData.tokens[i].currentTask.start) {//如果当前到达了当前任务的起点，则将托肯的起点设为当前位置，终点设为任务的终点
                           ShareData.tokens[i].path.clear();
                           ShareData.tokens[i].end = ShareData.tokens[i].currentTask.end;
                            count++;
                       } else if (ShareData.tokens[i].position == ShareData.tokens[i].currentTask.end) {//如果当前到达了当前任务的终点
                           ShareData.tokens[i].path.clear();
                           //判断任务列表是否为空，如果为空，则将托肯的终点设为当前位置,且将最后一个任务的状态设为已完成。否则，弹出任务列表的第一个任务，赋值给托肯的currentTask，token终点设为新任务的起点
                            if (ShareData.shuttleTasks.isEmpty()) {
                                ShareData.tokens[i].end = ShareData.tokens[i].position;
                                ShareData.tokens[i].currentTask.taskType = 2;
                            } else {
                                 count++;
                                 ShareData.tokens[i].currentTask = ShareData.shuttleTasks.remove(0);
                                 ShareData.tokens[i].end = ShareData.tokens[i].currentTask.start;
                            }

                       }
                   }


                   try {
                       System.out.println("完成本轮路径规划");
                       Thread.sleep(10000);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
                   if(count > 0){
                       break;
                   }
               }



        }
    }


    /**
     * 获取两token间的冲突库所集合
     * @param tokens
     * @param tokenIndexA
     * @param tokenIndexB
     * @return 两托肯形成的的所有冲突库所集合
     */
    public static ArrayList<conflictPlaceSet> getConflictPlaceSet (Token @NotNull [] tokens, int tokenIndexA, int tokenIndexB){

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
                    currentConflictType = "单冲突库所集合";
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
                        if(currentConflictSet.size() == 1){
                            currentConflictType = "单冲突库所集合";

                        }
                        conflictPlaceSets.add(new conflictPlaceSet(new ArrayList<>(currentConflictSet), currentConflictType,null,tokens[tokenIndexA],tokens[tokenIndexB]));
                        currentConflictSet.clear();
                        currentConflictSet.add(entryA.getValue());

                    }
                }
            }
        }

        // 如果最后一个冲突库所集合非空，则添加到列表中
        if (!currentConflictSet.isEmpty()) {
            conflictPlaceSets.add(new conflictPlaceSet(currentConflictSet, currentConflictType,null,tokens[tokenIndexA],tokens[tokenIndexB]));
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
     * 获取所有token两两之间形成的的冲突库所集合
     * @param tokens
     * @return
     */
    public static ArrayList<conflictPlaceSet> getAllConflictPlaceSets(Token @NotNull [] tokens) {
        ArrayList<conflictPlaceSet> allConflictPlaceSets = new ArrayList<>();

        // 遍历所有的Token对
        for (int i = 0; i < tokens.length; i++) {
            for (int j = i + 1; j < tokens.length; j++) {
                // 获取Token对之间的冲突库所集合
                ArrayList<conflictPlaceSet> conflictPlaceSets = getConflictPlaceSet(tokens, i, j);
                // 将冲突库所集合添加到AllConflictPlaceSets中
                allConflictPlaceSets.addAll(conflictPlaceSets);
            }
        }

        return allConflictPlaceSets;
    }

    /**
     * 检测死锁
     * @param AllConflictPlaceSets
     * @return 返回所有形成死锁的冲突库所集合
     */
    public static ArrayList<conflictPlaceSet> detectDeadlocks(@NotNull ArrayList<conflictPlaceSet> AllConflictPlaceSets) {
        ArrayList<conflictPlaceSet> deadlockSets = new ArrayList<>();

        for (conflictPlaceSet conflictSet : AllConflictPlaceSets) {
            Place startPlace = conflictSet.placeSet.get(0);
            Place endPlace = conflictSet.placeSet.get(conflictSet.placeSet.size() - 1);

            if (conflictSet.conflictType.equals("同向冲突库所集合") &&
                    ((startPlace.index == conflictSet.conflictTokenA.position && endPlace.index == conflictSet.conflictTokenA.end) ||
                            (startPlace.index == conflictSet.conflictTokenB.position && endPlace.index == conflictSet.conflictTokenB.end) || (endPlace.index == conflictSet.conflictTokenA.position && startPlace.index == conflictSet.conflictTokenA.end) ||
                            (endPlace.index == conflictSet.conflictTokenB.position && startPlace.index == conflictSet.conflictTokenB.end))) {
                conflictSet.deadlockType = "同向包含";
                deadlockSets.add(conflictSet);
            } else if (conflictSet.conflictType.equals("反向冲突库所集合")) {
                if ((startPlace.index == conflictSet.conflictTokenA.position && endPlace.index == conflictSet.conflictTokenA.end) ||
                        (startPlace.index == conflictSet.conflictTokenB.position && endPlace.index == conflictSet.conflictTokenB.end) || (endPlace.index == conflictSet.conflictTokenA.position && startPlace.index == conflictSet.conflictTokenA.end) ||
                        (endPlace.index == conflictSet.conflictTokenB.position && startPlace.index == conflictSet.conflictTokenB.end)) {
                    conflictSet.deadlockType = "反向包含";
                    deadlockSets.add(conflictSet);
                } else if ((startPlace.index == conflictSet.conflictTokenA.position && endPlace.index == conflictSet.conflictTokenB.position) ||
                        (startPlace.index == conflictSet.conflictTokenB.position && endPlace.index == conflictSet.conflictTokenA.position)) {
                    conflictSet.deadlockType = "反向起始冲撞";
                    deadlockSets.add(conflictSet);
                } else if ((startPlace.index == conflictSet.conflictTokenA.end && endPlace.index == conflictSet.conflictTokenB.end) ||
                        (startPlace.index == conflictSet.conflictTokenB.end && endPlace.index == conflictSet.conflictTokenA.end)) {
                    conflictSet.deadlockType = "反向终点冲撞";
                    deadlockSets.add(conflictSet);
                }
            }
        }

        return deadlockSets;
    }

    /**
     * 获取需要重新规划路径的托肯id
     * @param deadlockSets
     * @param AllConflictPlaceSets
     * @return 冲突库所集合个数多的托肯id，如果个数相同，则路径长的托肯id
     */
    public static ArrayList<Integer> getNeedReplanTokensId(@NotNull ArrayList<conflictPlaceSet> deadlockSets, ArrayList<conflictPlaceSet> AllConflictPlaceSets) {
        ArrayList<Integer> needReplanTokens = new ArrayList<>();

        for (conflictPlaceSet deadlockSet : deadlockSets) {
            Token tokenA = deadlockSet.conflictTokenA;
            Token tokenB = deadlockSet.conflictTokenB;

            int numA = 0;
            int numB = 0;

            for (conflictPlaceSet conflictSet : AllConflictPlaceSets) {
                if (conflictSet.conflictTokenA.equals(tokenA) || conflictSet.conflictTokenB.equals(tokenA)) {
                    numA++;
                }
                if (conflictSet.conflictTokenA.equals(tokenB) || conflictSet.conflictTokenB.equals(tokenB)) {
                    numB++;
                }
            }

            Token needReplanToken;
            if (numA > numB) {
                needReplanToken = tokenA;
            } else if (numA < numB) {
                needReplanToken = tokenB;
            } else {
                if (tokenA.path.size() >= tokenB.path.size()) {
                    needReplanToken = tokenA;
                } else {
                    needReplanToken = tokenB;
                }
            }

            if (!needReplanTokens.contains(needReplanToken.id)) {
                needReplanTokens.add(needReplanToken.id);
            }
        }

        return needReplanTokens;
    }

    /**
     * 获取需要重新规划路径的托肯的新地图，将包含此托肯的所有冲突库所集合中的所有库所的前集变迁的cost设为2
     * @param AllConflictPlaceSets
     * @param needReplanToken
     * @return
     */
    public static MapInfo getNewMapForReplanToken(ArrayList<conflictPlaceSet> AllConflictPlaceSets,@NotNull Token needReplanToken) {
        MapInfo info = new MapInfo().getMap();
        PetriNet petriNet = new PetriNet();
        int[][] matrix = petriNet.creatIncidenceMatrix();

        ArrayList<Integer> arrIndexT = new ArrayList<>();
        for (conflictPlaceSet conflictSet : AllConflictPlaceSets) {
            if (conflictSet.conflictTokenA.equals(needReplanToken) || conflictSet.conflictTokenB.equals(needReplanToken)) {
                for (Place place : conflictSet.placeSet) {
                    ArrayList<Integer> forwardTransitionIndexSet = petriNet.getForwardTransitionIndexSetForPlace(place.index, matrix);
                    arrIndexT.addAll(forwardTransitionIndexSet);
                }
            }
        }

        for (Integer indexT : arrIndexT) {
            info.transitions[indexT].cost = 2;
        }

        return info;
    }

    /**
     * 重规划token们的路径
     * @param needReplanTokens
     * @param AllConflictPlaceSets
     * @param storageInfo
     * @param tokens
     */
    public static void replanTokenPaths(@NotNull ArrayList<Integer> needReplanTokens, ArrayList<conflictPlaceSet> AllConflictPlaceSets,@NotNull int[] storageInfo,@NotNull Token[] tokens) {
        for (Integer token : needReplanTokens) {
            MapInfo info = getNewMapForReplanToken(AllConflictPlaceSets, tokens[token]);
            HashMap<Integer,Place> path = new PetriAstar().start(info, storageInfo, tokens, token);
            tokens[token].path = new PetriAstar().start(info, storageInfo, tokens, token);
            calculateRemainPath(tokens[token]);

        }
    }

    /**
     * 计算各各托肯的优先级
     * @param tokens
     * @param AllConflictPlaceSets
     */
    public static void calPriority(Token @NotNull [] tokens,ArrayList<conflictPlaceSet> AllConflictPlaceSets){
        for (int i = 0; i < tokens.length; i++) {
            int numS = 0;
            int numE = 0;
            for (conflictPlaceSet conflictSet : AllConflictPlaceSets){
                if (conflictSet.conflictTokenA.equals(tokens[i]) || conflictSet.conflictTokenB.equals(tokens[i])) {
                    for (Place p : conflictSet.placeSet){
                        if(p.index == tokens[i].position){
                            numS++;
                        }else if(p.index == tokens[i].end){
                            numE++;
                        }
                    }
                }
            }
            int priority = tokens[i].taskPriority + ((tokens[i].isLoad) ? 1 : 0 ) + numS - numE;
            tokens[i].priority = priority;
            System.out.println("托肯"+ i + "的优先级为： " + tokens[i].priority);
        }
    }

    /**
     * 为每个托肯设置无死锁路径和优先级,返回所有的冲突库所集合供轮询算法使用，如果返回null，则表示重规划次数达到上限10次，仍然存在死锁
     * @param tokens
     * @param storageInfo
     * @return
     */
    public static ArrayList<conflictPlaceSet> getUnDeadlockRoute(Token @NotNull [] tokens,int[] storageInfo){
        ArrayList<conflictPlaceSet> allConflictPlaceSets = new ArrayList<>();
        for (int i = 0; i < 10; i++) {

            // 获得冲突库所集合
            allConflictPlaceSets = getAllConflictPlaceSets(tokens);
            if (allConflictPlaceSets.isEmpty()) {
                System.out.println("所有托肯路径均无冲突，各路径如下：");
                //打印所有托肯的路径
                for (int j = 0; j < tokens.length; j++) {
                    System.out.println(tokens[j].name + "的路径为：");
                    StringBuilder sb = new StringBuilder();
                    for (Map.Entry<Integer, Place> entry : tokens[j].path.entrySet()) {
                        sb.append(entry.getValue().name).append(" ");
                    }
                    System.out.println(sb.toString());
                }
                break;
            }
            for (conflictPlaceSet conflictSet : allConflictPlaceSets) {
                System.out.println("冲突库所集合：");
                StringBuilder sb = new StringBuilder();
                for (Place place : conflictSet.placeSet) {
                    sb.append(place.name).append(" ");
                }
                System.out.println(sb.toString());
                System.out.println("冲突类型：" + conflictSet.conflictType);
                System.out.println("冲突的托肯A：" + conflictSet.conflictTokenA.name);
                System.out.println("冲突的托肯B：" + conflictSet.conflictTokenB.name);
                System.out.println("--------------------");
            }

            // 获得死锁库所集合
            ArrayList<conflictPlaceSet> deadlockSets = detectDeadlocks(allConflictPlaceSets);
            if (deadlockSets.isEmpty()) {
                System.out.println("所有托肯路径均无死锁，各路径如下：");
                //打印所有托肯的路径
                for (int j = 0; j < tokens.length; j++) {
                    System.out.println(tokens[j].name + "的路径为：");
                    StringBuilder sb = new StringBuilder();
                    for (Map.Entry<Integer, Place> entry : tokens[j].path.entrySet()) {
                        sb.append(entry.getValue().name).append(" ");
                    }
                    System.out.println(sb.toString());
                }
                break;
            }
            for (conflictPlaceSet conflictSet : deadlockSets) {
                System.out.println("死锁库所集合：");
                StringBuilder sb = new StringBuilder();
                for (Place place : conflictSet.placeSet) {
                    sb.append(place.name).append(" ");
                }
                System.out.println(sb.toString());
                System.out.println("死锁类型：" + conflictSet.deadlockType);
                System.out.println("冲突的托肯A：" + conflictSet.conflictTokenA.name);
                System.out.println("冲突的托肯B：" + conflictSet.conflictTokenB.name);
                System.out.println("--------------------");
            }

            // 获得需要重新规划路径的托肯
            ArrayList<Integer> needReplanTokens = getNeedReplanTokensId(deadlockSets,allConflictPlaceSets);
            for (Integer tokenId : needReplanTokens) {
                System.out.println("需要重新规划路径的托肯：" + tokens[tokenId].name);
            }
            //路径重规划
            replanTokenPaths(needReplanTokens,allConflictPlaceSets,storageInfo,tokens);

            if(i==9){
                // 获得冲突库所集合
                allConflictPlaceSets = getAllConflictPlaceSets(tokens);
                // 获得死锁库所集合
                deadlockSets = detectDeadlocks(allConflictPlaceSets);
                if(!deadlockSets.isEmpty()){
                    System.out.println("路径重规划次数超过10次，仍然存在死锁");
                    return null;
                }

            }

        }
        // 计算优先级
        calPriority(tokens,allConflictPlaceSets);
        return allConflictPlaceSets;
    }

    /**
     * 计算token的remainPath
     * @param token
     */
    public static void calculateRemainPath(Token token) {
        int positionIndex = -1;
        for (Map.Entry<Integer, Place> entry : token.path.entrySet()) {
            if (entry.getValue().index == token.position) {
                positionIndex = entry.getKey();
                break;
            }
        }
        if (positionIndex != -1) {
            HashMap<Integer, Place> remainPath = new HashMap<>();
            for (Map.Entry<Integer, Place> entry : token.path.entrySet()) {
                if (entry.getKey() >= positionIndex) {
                    remainPath.put(entry.getKey() - positionIndex, entry.getValue());
                }
            }
            token.remainPath = remainPath;
        }
    }

    /**
     * 为每个托肯申请库所集合
     * @param i
     * @param tokens
     * @param allConflictPlaceSets
     * @param
     */
    public static void applyPlaceSet(int i,Token[] tokens,ArrayList<conflictPlaceSet> allConflictPlaceSets){
        ArrayList <conflictPlaceSet> includeTokenSet = new ArrayList<>();
        for (conflictPlaceSet conflictSet : allConflictPlaceSets){
            if (conflictSet.conflictTokenA.id == tokens[i].id || conflictSet.conflictTokenB.id == tokens[i].id){//获得包含tokens[i]的所有冲突库所集合
                includeTokenSet.add(conflictSet);
            }
        }
        if (i==0 || includeTokenSet.isEmpty()){//如果该托肯未与其它托肯形成冲突库所序列或者优先级最低，则申请该托肯剩余路径库所序列中的在第一个库所指向第一个未被其占用的库所方向上的所有未被其占用的库所
            if(tokens[i].remainPath.size() > 1){
                int index = tokens[i].remainPath.size() - 1;//如果全是同方向的，那索引设为最后一个
                Place place0 = tokens[i].remainPath.get(0);
                Place place1 = tokens[i].remainPath.get(1);
                double arctan = Math.abs(Math.atan((double) (place1.coord.y - place0.coord.y) / (place1.coord.x - place0.coord.x)));
                for (int j = 0; j < tokens[i].remainPath.size() - 1; j++) {
                    Place placeZero = tokens[i].remainPath.get(j);
                    Place placeOne = tokens[i].remainPath.get(j+1);
                    double arctanI = Math.abs(Math.atan((double) (placeOne.coord.y - placeZero.coord.y) / (placeOne.coord.x - placeZero.coord.x)));
                    if(arctan != arctanI){
                        index = j;
                        break;
                    }
                }
                for (Map.Entry<Integer, Place> entry : tokens[i].remainPath.entrySet()) {
                    if (entry.getKey() > 0 && entry.getKey() <= index) {//第一个库所指向第一个未被其占用的库所方向上的所有未被其占用的库所
                        tokens[i].applyList.put(entry.getKey(), entry.getValue());//tokens[i].applyList的key从1开始到index结束
                    }
                }
            }



        }else{//求出在该托肯与其它更低优先级托肯形成的冲突集合中的且在该托肯剩余路径上的最后一个库所PL

            ArrayList<Integer> keys = new ArrayList<>();
            for (int k = 0; k <= i-1; k++) {//遍历每个更低优先级的托肯
                for (conflictPlaceSet conflictSet : includeTokenSet){
                    if (conflictSet.conflictTokenA.id == tokens[k].id || conflictSet.conflictTokenB.id == tokens[k].id){//获得包含tokens[i]和更低优先级托肯的所有冲突库所集合
                        for (Place place : conflictSet.placeSet){
                            keys.add(getKeyByValue(tokens[i].path,place));//在keys里面找到最大的索引就是PL
                        }
                    }
                }
            }

            if(!keys.isEmpty()){//如果该托肯与其它低优先级的托肯形成冲突库所集合，则申请该托肯路径中的库所PL以及PL前的所有未被占用过的库所。
                int PL = Collections.max(keys);//在path中的索引
                Place place = tokens[i].path.get(PL);//在path中的库所
                int index = getKeyByValue(tokens[i].remainPath,place);//在remainPath中的索引

                if(index > 0){//pl还未申请到
                    for (Map.Entry<Integer, Place> entry : tokens[i].remainPath.entrySet()) {
                        if (entry.getKey() > 0 && entry.getKey() <= index) {
                            tokens[i].applyList.put(entry.getKey(), entry.getValue());//tokens[i].applyList的key从1开始到index结束
                        }
                    }
                }else {//pl已经申请到，申请该托肯剩余路径库所序列中的在第一个库所指向第一个未被其占用的库所方向上的所有未被其占用的库所；

                    int index1 = tokens[i].remainPath.size() - 1;//如果全是同方向的，那索引设为最后一个
                    Place place0 = tokens[i].remainPath.get(0);
                    Place place1 = tokens[i].remainPath.get(1);
                    double arctan = Math.abs(Math.atan((double) (place1.coord.y - place0.coord.y) / (place1.coord.x - place0.coord.x)));
                    for (int j = 0; j < tokens[i].remainPath.size() - 1; j++) {
                        Place placeZero = tokens[i].remainPath.get(j);
                        Place placeOne = tokens[i].remainPath.get(j+1);
                        double arctanI = Math.abs(Math.atan((double) (placeOne.coord.y - placeZero.coord.y) / (placeOne.coord.x - placeZero.coord.x)));
                        if(arctan != arctanI){
                            index1 = j;
                            break;
                        }
                    }
                    for (Map.Entry<Integer, Place> entry : tokens[i].remainPath.entrySet()) {
                        if (entry.getKey() > 0 && entry.getKey() <= index1) {//第一个库所指向第一个未被其占用的库所方向上的所有未被其占用的库所
                            tokens[i].applyList.put(entry.getKey(), entry.getValue());//tokens[i].applyList的key从1开始到index结束
                        }
                    }

                }
            }else{//申请该托肯剩余路径库所序列中的在第一个库所指向第一个未被其占用的库所方向上的所有未被其占用的库所
                int index = tokens[i].remainPath.size() - 1;//如果全是同方向的，那索引设为最后一个
                Place place0 = tokens[i].remainPath.get(0);
                Place place1 = tokens[i].remainPath.get(1);
                double arctan = Math.abs(Math.atan((double) (place1.coord.y - place0.coord.y) / (place1.coord.x - place0.coord.x)));
                for (int j = 0; j < tokens[i].remainPath.size() - 1; j++) {
                    Place placeZero = tokens[i].remainPath.get(j);
                    Place placeOne = tokens[i].remainPath.get(j+1);
                    double arctanI = Math.abs(Math.atan((double) (placeOne.coord.y - placeZero.coord.y) / (placeOne.coord.x - placeZero.coord.x)));
                    if(arctan != arctanI){
                        index = j;
                        break;
                    }
                }
                for (Map.Entry<Integer, Place> entry : tokens[i].remainPath.entrySet()) {
                    if (entry.getKey() > 0 && entry.getKey() <= index) {//第一个库所指向第一个未被其占用的库所方向上的所有未被其占用的库所
                        tokens[i].applyList.put(entry.getKey(), entry.getValue());//tokens[i].applyList的key从1开始到index结束
                    }
                }

            }





        }



    }

    /**
     * 调整每个托肯的申请库所集合，得到最终各托肯的占用库所集合
     * @param i
     * @param tokens
     */
    public static void adjustPlaceSet(int i,Token[] tokens,MapInfo info){
        for (int j = 1; j < tokens[i].remainPath.size(); j++) {
            if (tokens[i].applyList.containsValue(tokens[i].remainPath.get(j))) {//从第二个库所开始遍历remainPath，如果tokens[i]自己的applyList中包含该库所
                if (i < tokens.length-1){
                    for (int k = i+1; k < tokens.length; k++) {//遍历优先级更高的其它托肯
                        if (tokens[k].applyList.containsValue(tokens[i].remainPath.get(j)) || (info.places[tokens[i].remainPath.get(j).index].occupiedToken != tokens[i] && info.places[tokens[i].remainPath.get(j).index].occupiedToken != null)){//如果tokens[k]的applyList中也包含该库所，或者该库所已被其他托肯占用
                            //遍历tokens[i]的applyList，将tokens[i]的applyList中的该库所及之后的所有库所都从tokens[i]的applyList中删除
                            HashMap<Integer, Place> applyListCopy = new HashMap<>(tokens[i].applyList);
                            for (Map.Entry<Integer, Place> entry : tokens[i].applyList.entrySet()) {
                                // 在这里修改applyListCopy
                                if (entry.getKey() >= j) {
                                    applyListCopy.remove(entry.getKey());
                                }
                            }
                            tokens[i].applyList = applyListCopy; // 将修改后的副本赋值回原来的applyList
                            break;
                        }
                    }
                }else{//优先级最高的托肯只要判断applyList中的库所是否被占用
                    if (info.places[tokens[i].remainPath.get(j).index].occupiedToken != tokens[i] && info.places[tokens[i].remainPath.get(j).index].occupiedToken != null){//如果该库所已被其他托肯占用
                        //遍历tokens[i]的applyList，将tokens[i]的applyList中的该库所及之后的所有库所都从tokens[i]的applyList中删除

                        //遍历tokens[i]的applyList，将tokens[i]的applyList中的该库所及之后的所有库所都从tokens[i]的applyList中删除
                        HashMap<Integer, Place> applyListCopy = new HashMap<>(tokens[i].applyList);
                        for (Map.Entry<Integer, Place> entry : tokens[i].applyList.entrySet()) {
                            // 在这里修改applyListCopy
                            if (entry.getKey() >= j) {
                                applyListCopy.remove(entry.getKey());
                            }
                        }
                        tokens[i].applyList = applyListCopy; // 将修改后的副本赋值回原来的applyList
                    }


                }


            }else {
                break;
            }
        }

    }

    /**
     * 得到每个穿梭车每轮的无碰撞、无死锁路径
     * @param tokens
     * @param shuttles
     * @param allConflictPlaceSets
     * @param info
     */
    public static void getRoundPathForShuttle(Token[] tokens,Shuttle[] shuttles,ArrayList<conflictPlaceSet> allConflictPlaceSets,MapInfo info){

        ArrayList<Token> tokenList = new ArrayList<>();
        //去掉任务列表为空时，且已经到达终点的托肯，即去掉tokens中ShareData.tokens[i].currentTask.taskType == 2的token,
        for (int i = 0; i < tokens.length; i++) {
            if(tokens[i].currentTask.taskType != 2){
                tokenList.add(tokens[i]);
            }
        }


        Token[] needSortedTokens = tokenList.toArray(new Token[0]);

        //对tokens数组进行排序，要求按照数组中元素tokens[i]的priority值进行排序，priority值小的元素，索引也小。如果priority值一样大，则索引顺序随意。
        Token[] sortedTokens = Arrays.copyOf(needSortedTokens, needSortedTokens.length);
        Arrays.sort(sortedTokens, new Comparator<Token>() {
            @Override
            public int compare(Token t1, Token t2) {
                return Integer.compare(t1.priority, t2.priority);
            }
        });
        //为每个托肯动态申请若干数量的库所集合
        for (int i = 0; i < sortedTokens.length; i++) {
            if (sortedTokens[i].position == shuttles[sortedTokens[i].id].currentPosition){//如果其对应的四向穿梭车还未到达该托肯的当前所在库所，则本轮不申请，等待下一轮
                applyPlaceSet(i,sortedTokens,allConflictPlaceSets);
                //打印sortedTokens[i].applyList的内容
                System.out.println(sortedTokens[i].name + "申请: ");
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<Integer, Place> entry : sortedTokens[i].applyList.entrySet()) {
                    sb.append(entry.getValue().name).append(" ");
                }
                System.out.println(sb.toString());


            }
        }
        //从优先级最低的托肯开始，调整各托肯的申请库所集合，得到最终各托肯的占用库所集合
        for (int i = 0; i < sortedTokens.length; i++) {
            if (sortedTokens[i].position == shuttles[sortedTokens[i].id].currentPosition){//如果其对应的四向穿梭车还未到达该托肯的当前所在库所，则本轮不申请，等待下一轮
                adjustPlaceSet(i,sortedTokens,info);
                //打印sortedTokens[i].applyList的内容
                System.out.println(sortedTokens[i].name + "占用: ");
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<Integer, Place> entry : sortedTokens[i].applyList.entrySet()) {
                    sb.append(entry.getValue().name).append(" ");
                }
                System.out.println(sb.toString());
            }
        }
        //得到最终各托肯的占用库所集合后，各托肯的position要更新，remainPath也要更新,各托肯的applyList要清空
        for (int i = 0; i < sortedTokens.length; i++) {

            //如果sortedTokens[i].applyList非空
            if (!sortedTokens[i].applyList.isEmpty()) {
                //将sortedTokens[i].applyList中元素的value按照key从小到大的顺序加入到集合shuttles[sortedTokens[i].id].unObstructedPath中
                for (int j = 1; j <= sortedTokens[i].applyList.size(); j++) {
                    shuttles[sortedTokens[i].id].unObstructedPath.add(sortedTokens[i].applyList.get(j));
                }

                //更新info中的各库所的占用托肯
                for (Map.Entry<Integer, Place> entry : sortedTokens[i].applyList.entrySet()) {
                    info.places[entry.getValue().index].occupiedToken = sortedTokens[i];
                }
                //各托肯的position设为对应applyList中的索引最大的库所的index
                sortedTokens[i].position = sortedTokens[i].applyList.get(Collections.max(sortedTokens[i].applyList.keySet())).index ;
                //更新各托肯的remainPath
                calculateRemainPath(sortedTokens[i]);
            }
            //各托肯的applyList要清空
            sortedTokens[i].applyList.clear();

        }


    }

    /**
     * 开始轮询算法
     * @param tokens
     * @param shuttles
     * @param storageInfo
     */
    public static void startRound(Token[] tokens,Shuttle[] shuttles,int[] storageInfo){
        MapInfo info = new MapInfo().getMap();//新建一个图用于轮询算法
        ArrayList<conflictPlaceSet> allConflictPlaceSets = getUnDeadlockRoute(tokens,storageInfo);

        //初始化获得每一个托肯的当前库所，并将各个托肯当前库所的占用托肯设为该托肯
        for (int i = 0; i < tokens.length; i++) {
            info.places[tokens[i].position].occupiedToken = tokens[i];
        }
        //开始循环
        while (true){
            getRoundPathForShuttle(tokens,shuttles,allConflictPlaceSets,info);
            try {
                Thread.sleep(10000); //延迟10s
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
