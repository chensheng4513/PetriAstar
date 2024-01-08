package com.cs;

import com.cs.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.*;

public class PetriTest {
    public static void main(String[] args) {

        //创建RoutePlanThread线程对象
        RoutePlanThread routePlanThread = new RoutePlanThread();
        //创建ExecuteThread线程
        ExecuteThread executeThread = new ExecuteThread();

        //开启线程
        routePlanThread.start();
        executeThread.start();









//        //打印shuttles的unObstructedPath
//        for (int i = 0; i < shuttles.length; i++) {
//
//            System.out.println("穿梭车" + shuttles[i].name + "的通畅路径为：");
//            for (Map.Entry<Integer, Place> entry : shuttles[i].unObstructedPath.entrySet()) {
//                System.out.println(entry.getValue().name);
//            }
//        }
//        //打印tokens的position、remainPath、applyList
//        for (int i = 0; i < tokens.length; i++) {
//            System.out.println("托肯" + tokens[i].name + "的当前位置为：" + tokens[i].position);
//            System.out.println("托肯" + tokens[i].name + "的剩余路径为：");
//            for (Map.Entry<Integer, Place> entry : tokens[i].remainPath.entrySet()) {
//                System.out.println(entry.getValue().name);
//            }
//            System.out.println("托肯" + tokens[i].name + "的申请库所列表为：");
//            for (Map.Entry<Integer, Place> entry : tokens[i].applyList.entrySet()) {
//                System.out.println(entry.getValue().name);
//            }
//        }
//        //打印info的places中的occupiedToken不为空的place
//        for (int i = 0; i < info.places.length; i++) {
//            if (info.places[i].occupiedToken != null) {
//                System.out.println("place" + info.places[i].name + "的occupiedToken为：" + info.places[i].occupiedToken.name);
//            }
//        }



      


    }

}
