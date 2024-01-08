package com.cs;

import java.util.*;

public class PetriAstar {

    Queue<Place> openList = new PriorityQueue<Place>(); // 优先队列(升序)
    List<Place> closeList = new ArrayList<Place>();

    /**
     * 开始算法
     */
    public HashMap<Integer, Place> start(MapInfo mapInfo, int[] storageInfo, Token[] tokens, int tokenId) {
        if (mapInfo == null) return null;
        // clean
        openList.clear();
        closeList.clear();
        // 开始搜索
        openList.add(mapInfo.places[tokens[tokenId].position]);
        HashMap<Integer, Place> path = moveNodes(mapInfo, storageInfo, tokens, tokenId);
        return path;
    }

    /**
     * 移动当前结点
     */
    private HashMap<Integer, Place> moveNodes(MapInfo mapInfo, int[] storageInfo, Token[] tokens, int tokenId) {
        while (!openList.isEmpty()) {
            Place current = openList.poll();
//            System.out.println(current.name + ',' + current.g);
            closeList.add(current);
            addNeighborPlaceInOpen(mapInfo, current, storageInfo, tokens, tokenId);
            if (isInClose(mapInfo.places[tokens[tokenId].end])) {
                return drawPath(mapInfo.places[tokens[tokenId].end]);
            }
        }
        return null;
    }

    /**
     * 打印路径
     */
    private HashMap<Integer, Place> drawPath(Place end) {
        if (end == null) return null;
        ArrayList<Place> arr = new ArrayList<>();
        HashMap<Integer, Place> path = new HashMap<>();

        while (end != null) {
            arr.add(end);
//            System.out.println( end.name);
            end = end.parent;
        }
        if (arr.size() != 0) {//调整下顺序放入hashmap中
            for (int i = 0; i < arr.size(); i++) {
                path.put(i, arr.get(arr.size() - i - 1));
            }
        }
        return path;
    }

    /**
     * 添加当前库所的所有后集到open表
     */
    private void addNeighborPlaceInOpen(MapInfo mapInfo, Place current, int[] storageInfo, Token[] tokens, int tokenId) {
        ArrayList<Integer> backwardPlaceIndexSet = current.backwardPlaceIndexSet;

        for (Integer element : backwardPlaceIndexSet) {

            Place backwardPlace = mapInfo.places[element];//获得current的各后集

            if (canAddPlaceToOpen(backwardPlace, storageInfo, tokens, tokenId)) {
                addPlaceToOpen(backwardPlace, current, mapInfo.places[tokens[tokenId].end], tokens, tokenId, mapInfo.transitions);
            }
        }
    }

    /**
     * 添加一个后集库所到open表
     */
    private void addPlaceToOpen(Place backwardPlace, Place current, Place end, Token[] tokens, int tokenId,Transition[] transitions) {
        int indexT =  getTransitionIndex(backwardPlace,current);
        int g1 = current.g1 + transitions[indexT].cost;
        int g2 = current.g2 + getRepeatNum(backwardPlace, tokens, tokenId);
        int g3 = current.g3 + isNeedTurn(current, backwardPlace, tokens, tokenId);
        int g = g1 + g2 + g3; // 计算邻结点的G值

        if (!isInOpen(backwardPlace)) {
            int h = calcH(end.coord, backwardPlace.coord); // 计算H值

            if (isEndPlace(backwardPlace, end)) {
                backwardPlace = end;
                backwardPlace.parent = current;
                backwardPlace.g = g;
                backwardPlace.g1 = g1;
                backwardPlace.g2 = g2;
                backwardPlace.g3 = g3;
                backwardPlace.h = h;
            } else {
                backwardPlace.parent = current;
                backwardPlace.g = g;
                backwardPlace.g1 = g1;
                backwardPlace.g2 = g2;
                backwardPlace.g3 = g3;
                backwardPlace.h = h;
            }
            openList.add(backwardPlace);
        } else if (backwardPlace.g > g) {
            backwardPlace.g = g;
            backwardPlace.g1 = g1;
            backwardPlace.g2 = g2;
            backwardPlace.g3 = g3;
            backwardPlace.parent = current;
            openList.add(backwardPlace);
        }

    }

    /**
     * 判断Place是否在open表中
     */
    private boolean isInOpen(Place place) {
        if (openList.isEmpty()) return false;
        for (Place node : openList) {
            if (node.index == place.index) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断从当前库所current到后集库所backwardPlace是否需要转弯
     *
     * @param current
     * @param backwardPlace
     * @return
     */
    private int isNeedTurn(Place current, Place backwardPlace, Token[] tokens, int tokenId) {
        if (current.parent == null) {//如果current是起点则通过穿梭车当前方向和backwardPlace判断是否要转弯
            double direction = tokens[tokenId].direction;
            double arctan = Math.abs(Math.atan((double) (backwardPlace.coord.y - current.coord.y) / (backwardPlace.coord.x - current.coord.x)));
            if (direction == arctan) {//若方向相同则不用转弯，反之要
                return 0;
            } else {
                return 1;
            }

        } else {//如果current不是起点则通过backwardPlace、current、parent判断是否要转弯
            double arctan1 = Math.abs(Math.atan((double) (backwardPlace.coord.y - current.coord.y) / (backwardPlace.coord.x - current.coord.x)));
            double arctan2 = Math.abs(Math.atan((double) (current.coord.y - current.parent.coord.y) / (current.coord.x - current.parent.coord.x)));
            if (arctan1 == arctan2) {//若方向相同则不用转弯，反之要
                return 0;
            } else {
                return 1;
            }
        }
    }

    /**
     * 获得后集库所backwardPlace在与其他托肯路径冲突的的托肯个数
     *
     * @param backwardPlace
     * @return
     */
    private int getRepeatNum(Place backwardPlace, Token[] tokens, int tokenId) {
        int num = 0;
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].path != null && tokenId != i) {
                if (tokens[i].path.containsValue(backwardPlace)) {
                    num++;
                }

            }

        }
        return num;
    }

    /**
     * 计算H的估值：“曼哈顿”法，坐标分别取差值相加
     */
    private int calcH(Coord end, Coord coord) {
        return (Math.abs(end.x - coord.x) + Math.abs(end.y - coord.y));
    }


    /**
     * 判断库所是否是最终库所
     */
    private boolean isEndPlace(Place end, Place place) {
        return end.index == place.index;
    }

    /**
     * 判断库所能否放入Open列表
     */
    private boolean canAddPlaceToOpen(Place backwardPlace, int[] storageInfo, Token[] tokens, int tokenId) {

        // 判断库所是否存在close表
        if ((storageInfo[backwardPlace.index] == 1 && tokens[tokenId].isLoad) || isInClose(backwardPlace)) {
            return false;
        }

        return true;
    }


    /**
     * 判断库所是否在close表中
     */
    private boolean isInClose(Place place) {
        if (closeList.isEmpty()) return false;
        for (Place node : closeList) {
            if (node.index == place.index) {
                return true;
            }
        }
        return false;
    }
    private int getTransitionIndex(Place backwardPlace, Place current){
        ArrayList<Integer> forwardArr = new PetriNet().getForwardTransitionIndexSetForPlace(backwardPlace.index,new PetriNet().creatIncidenceMatrix());
        ArrayList<Integer> backwardArr = new PetriNet().getbackwardTransitionIndexSetForPlace(current.index,new PetriNet().creatIncidenceMatrix());
        for (Integer numF : forwardArr) {
           for (Integer numB : backwardArr) {
               if(numF == numB){
                   return numB;
               }
           }
        }
        return 0;
    }

}
