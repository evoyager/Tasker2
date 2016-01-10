package com.gusar.tasker2.model;

public class ModelSeparator implements Item {
    public static final int TYPE_FUTURE = 2131165243;
    public static final int TYPE_OVERDUE = 2131165244;
    public static final int TYPE_TODAY = 2131165245;
    public static final int TYPE_TOMORROW = 2131165246;
    private int type;

    public ModelSeparator(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isTask() {
        return false;
    }
}
