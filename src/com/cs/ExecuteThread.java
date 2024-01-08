package com.cs;

public class ExecuteThread extends Thread{
    public void run(){
        while(true){

            //遍历托肯，获得currentTask不为2的托肯的数量
            int num = 0;
            for (int i = 0; i < ShareData.tokens.length; i++) {
                if (ShareData.tokens[i].currentTask.taskType != 2) {
                    num++;
                }
            }
            if( num == 0 ){
                for (int i = 0; i < ShareData.shuttles.length; i++) {
                    if (!ShareData.shuttles[i].unObstructedPath.isEmpty()) {
                        ShareData.mapInfo.places[ShareData.shuttles[i].currentPosition].occupiedToken = null;//将shuttle走过的place的occupiedToken置空
                        ShareData.shuttles[i].currentPosition = ShareData.shuttles[i].unObstructedPath.remove(0).index;
                    }
                }
                System.out.println("走了一步，"+"  穿梭车0："+ShareData.shuttles[0].currentPosition+"  穿梭车1："+ShareData.shuttles[1].currentPosition+"  穿梭车2："+ShareData.shuttles[2].currentPosition+"   穿梭车3："+ShareData.shuttles[3].currentPosition);
                break;
            }else {
                for (int i = 0; i < ShareData.shuttles.length; i++) {
                    if (!ShareData.shuttles[i].unObstructedPath.isEmpty()) {
                        ShareData.mapInfo.places[ShareData.shuttles[i].currentPosition].occupiedToken = null;//将shuttle走过的place的occupiedToken置空
                        ShareData.shuttles[i].currentPosition = ShareData.shuttles[i].unObstructedPath.remove(0).index;
                    }
                }

                try {
                    System.out.println("走了一步，"+"  穿梭车0："+ShareData.shuttles[0].currentPosition+"  穿梭车1："+ShareData.shuttles[1].currentPosition+"  穿梭车2："+ShareData.shuttles[2].currentPosition+"   穿梭车3："+ShareData.shuttles[3].currentPosition);
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }





        }
    }
}
