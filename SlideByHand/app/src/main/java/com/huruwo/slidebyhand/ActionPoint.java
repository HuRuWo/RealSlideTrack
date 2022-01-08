package com.huruwo.slidebyhand;

public class ActionPoint {

    private long time;
    private float x;
    private float y;
    private int action;// 0按下 1滑动  2抬起

    public ActionPoint(long time, float x, float y, int action) {
        this.time = time;
        this.x = x;
        this.y = y;
        this.action = action;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
