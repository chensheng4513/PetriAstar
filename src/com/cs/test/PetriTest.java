package com.cs.test;

import com.cs.*;

import java.util.HashMap;


public class PetriTest {
    public static void main(String[] args) {


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


    }


}
