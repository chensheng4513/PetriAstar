package com.cs;

import java.util.ArrayList;
import java.util.HashMap;

public class ShareData {
    //任务信息
    public static ArrayList<ShuttleTask> shuttleTasks = new ArrayList<ShuttleTask>() {{
        add(new ShuttleTask(0,26,10,0,1,0));
        add(new ShuttleTask(1,28,24,0,1,0));
        add(new ShuttleTask(2,11,4,0,1,0));
        add(new ShuttleTask(3,5,20,0,1,0));

    }};

    //地图信息
    public static MapInfo mapInfo = new MapInfo().getMap();
    //穿梭车信息
    public static Shuttle[] shuttles = {new Shuttle(0,"穿梭车0",3,new HashMap<>(),new ArrayList<>()),
                                        new Shuttle(1,"穿梭车1",1,new HashMap<>(),new ArrayList<>()),
                                        new Shuttle(2,"穿梭车2",9,new HashMap<>(),new ArrayList<>()),
                                        new Shuttle(3,"穿梭车3",7,new HashMap<>(),new ArrayList<>()) };
    //托肯信息
    public static Token[] tokens = {new Token(0, "托肯0_红色", true, 0, 1, 3,  26, new HashMap<>(),new HashMap<>(),1,2,new HashMap<>(),ShareData.shuttleTasks.remove(0)),
                                    new Token(1, "托肯1_绿色", false, 0, 1, 1,  28, new HashMap<>(),new HashMap<>(),1,1,new HashMap<>(),ShareData.shuttleTasks.remove(0)),
                                    new Token(2, "托肯2_蓝色", false, Math.PI / 2, 1, 9,  11, new HashMap<>(),new HashMap<>(),1,1,new HashMap<>(),ShareData.shuttleTasks.remove(0)),
                                    new Token(3, "托肯3_青色", false, Math.PI / 2, 1, 7, 5, new HashMap<>(),new HashMap<>(),1,1,new HashMap<>(),ShareData.shuttleTasks.remove(0)) };


    //货位信息
    public static int[] storageInfo = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                                0, 0, 0, 0, 0, 0, 0, 0, 0};

    public static Object lock = new Object();

}
