package com.cs;

import java.util.HashMap;

public class Token {
    public int id;//穿梭车ID
    public String name;//穿梭车名称，即颜色
    public boolean isLoad;//是否载货
    public double direction;//行进的方向,0表示x方向，Math.PI/2表示y方向
    public float speed;//速度
    public int position;//当前位置
    public int start;//起始位置
    public int end;//终点
    public HashMap<Integer, Place> path;//当前规划好的完整路径
    public HashMap<Integer, Place> remainPath;//剩余路径，从当前位置position开始的后续未到达的路径
    public int taskPriority;//任务的优先级
    public int priority;//总的优先级


    public Token(int id, String name, boolean isLoad, double direction, float speed, int position, int start, int end, HashMap<Integer, Place> path, HashMap<Integer, Place> remainPath, int taskPriority, int priority) {
        this.id = id;
        this.name = name;
        this.isLoad = isLoad;
        this.direction = direction;
        this.speed = speed;
        this.position = position;
        this.start = start;
        this.end = end;
        this.path = path;
        this.remainPath = remainPath;
        this.taskPriority = taskPriority;
        this.priority = priority;
    }

    @Override
public boolean equals(Object obj) {
    if (this == obj) {
        return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
        return false;
    }
    Token token = (Token) obj;
    return id == token.id;
}

}
