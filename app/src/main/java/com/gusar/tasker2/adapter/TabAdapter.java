package com.gusar.tasker2.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.gusar.tasker2.fragment.CurrentTaskFragment;
import com.gusar.tasker2.fragment.DoneTaskFragment;

public class TabAdapter extends FragmentStatePagerAdapter {
    public static final int CURRENT_TASK_FRAGMENT_POSITION = 0;
    public static final int DONE_TASK_FRAGMENT_POSITION = 1;
    private CurrentTaskFragment currentTaskFragment = new CurrentTaskFragment();
    private DoneTaskFragment doneTaskFragment = new DoneTaskFragment();
    private int numberOfTabs;

    public TabAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    public Fragment getItem(int i) {
        switch (i) {
            case CURRENT_TASK_FRAGMENT_POSITION /*0*/:
                return this.currentTaskFragment;
            case DONE_TASK_FRAGMENT_POSITION /*1*/:
                return this.doneTaskFragment;
            default:
                return null;
        }
    }

    public int getCount() {
        return this.numberOfTabs;
    }
}
