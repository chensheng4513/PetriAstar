package com.cs;

public class ShuttleTask {
    public int id;//任务id
    public int start;//起始库所
    public int end;//终止库所
    public int status;//0:未开始，1：正在执行，2：已完成
    public int taskPriority;//任务优先级
    public int taskType;//任务类型，0：入库，1：出库 ,2：移库

    public ShuttleTask(int id, int start, int end, int status,int taskPriority,int taskType) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.status = status;
        this.taskPriority = taskPriority;
        this.taskType = taskType;
    }
}
