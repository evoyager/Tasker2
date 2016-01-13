package com.gusar.tasker2;

import android.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.gusar.tasker2.adapter.TabAdapter;
import com.gusar.tasker2.database.DBHelper;
import com.gusar.tasker2.dialog.AddingTaskDialogFragment;
import com.gusar.tasker2.dialog.AddingTaskDialogFragment.AddingTaskListener;
import com.gusar.tasker2.dialog.EditTaskDialogFragment.EditingTaskListener;
import com.gusar.tasker2.dialog.NewTaskDialogFragment;
import com.gusar.tasker2.fragment.CurrentTaskFragment;
import com.gusar.tasker2.fragment.DoneTaskFragment;
import com.gusar.tasker2.fragment.OldTaskFragment;
import com.gusar.tasker2.fragment.TaskFragment;
import com.gusar.tasker2.model.ModelTask;

public class MainActivity extends ActionBarActivity implements View.OnClickListener, AddingTaskListener, EditingTaskListener, CurrentTaskFragment.OnTaskDoneListener, DoneTaskFragment.OnTaskRestoreListener {

    Toolbar toolbar;
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Дела","Сделано"};
    int Numboftabs=2;
    FragmentManager fragmentManager;

    DialogFragment dlg;

    OldTaskFragment currentTaskFragment;
    OldTaskFragment doneTaskFragment;
    public DBHelper dbHelper;
    TabAdapter tabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_old);

        this.dbHelper = new DBHelper(getApplicationContext());
        this.fragmentManager = getSupportFragmentManager();

        dlg = new NewTaskDialogFragment();

        setUI();

//        adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs);
//
//        viewPager.setAdapter(adapter);
//
//        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
//        tabs.setDistributeEvenly(true);
//        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
//            @Override
//            public int getIndicatorColor(int position) {
//                return getResources().getColor(R.color.tabsScrollColor);
//            }
//        });
//
//        tabs.setViewPager(viewPager);

        FloatingActionButton fb = (FloatingActionButton)findViewById(R.id.fab);
        fb.setOnClickListener(this);
    }

    private void setUI() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.current_task));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.done_task));
        viewPager = (ViewPager) findViewById(R.id.pager);
        this.tabAdapter = new TabAdapter(this.fragmentManager, 2);
        viewPager.setAdapter(this.tabAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        this.currentTaskFragment = (CurrentTaskFragment) this.tabAdapter.getItem(0);
        this.doneTaskFragment = (DoneTaskFragment) this.tabAdapter.getItem(1);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        new AddingTaskDialogFragment().show(MainActivity.this.getFragmentManager(), "AddingTaskDialogFragment");
//        dlg.show(getFragmentManager(), "New Task Dialog");
    }

    @Override
    public void onTaskAdded(ModelTask newTask) {
        this.currentTaskFragment.addTask(newTask, true);
    }

    @Override
    public void onTaskAddingCancel() {

    }

    @Override
    public void onTaskDone(ModelTask task) {
        this.doneTaskFragment.addTask(task, false);
    }

    @Override
    public void onTaskRestore(ModelTask task) {
        this.currentTaskFragment.addTask(task, false);
    }

    public void onTaskEdited(ModelTask newTask) {
        this.currentTaskFragment.updateTask(newTask);
        this.dbHelper.update().task(newTask);
    }
}
