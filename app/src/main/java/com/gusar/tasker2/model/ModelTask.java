package com.gusar.tasker2.model;

import com.gusar.tasker2.R;

import java.util.Date;

/**
 * Created by Evgeniy on 26.12.2015.
 */
public class ModelTask {
    public static final int PRIORITY_HIGH = 2;
    public static final String[] PRIORITY_LEVELS = new String[]{"Low Priority", "Normal Priority", "High Priority"};
    public static final int PRIORITY_LOW = 0;
    public static final int PRIORITY_NORMAL = 1;
    public static final int STATUS_CURRENT = 1;
    public static final int STATUS_DONE = 2;
    public static final int STATUS_OVERDUE = 0;
    private int priority;
    private String title;
    private int status;
    private long date;
    private long timeStamp;

    public ModelTask() {
        this.status = -1;
        this.timeStamp = new Date().getTime();
    }

    public ModelTask(String title, long date, int priority, int i, long timeStamp) {
        this.title = title;
        this.date = date;
        this.priority = priority;
        this.status = status;
        this.timeStamp = timeStamp;
    }

    public int getPriorityColor() {
        switch (getPriority()) {
            case PRIORITY_LOW /*0*/:
                if (getStatus() == STATUS_CURRENT || getStatus() == 0) {
                    return R.color.priority_low;
                }
                return R.color.priority_low_selected;
            case STATUS_CURRENT /*1*/:
                if (getStatus() == STATUS_CURRENT || getStatus() == 0) {
                    return R.color.priority_normal;
                }
                return R.color.priority_normal_selected;
            case STATUS_DONE /*2*/:
                if (getStatus() == STATUS_CURRENT || getStatus() == 0) {
                    return R.color.priority_high;
                }
                return R.color.priority_high_selected;
            default:
                return PRIORITY_LOW;
        }
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public long getDate() {
        return date;
    }

    public int getPriority() {
        return priority;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public int getStatus() {
        return status;
    }
}