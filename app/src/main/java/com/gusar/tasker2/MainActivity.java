package com.gusar.tasker2;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.gusar.tasker2.dialog.AddingTaskDialogFragment;
import com.gusar.tasker2.dialog.AddingTaskDialogFragment.AddingTaskListener;
import com.gusar.tasker2.dialog.EditTaskDialogFragment;
import com.gusar.tasker2.dialog.EditTaskDialogFragment.EditingTaskListener;
import com.gusar.tasker2.fragment.TaskFragment;
import com.gusar.tasker2.model.ModelTask;

public class MainActivity extends ActionBarActivity implements View.OnClickListener, EditingTaskListener, AddingTaskListener{

    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Дела","Сделано"};
    int Numboftabs=2;
    FragmentManager fragmentManager;
    TaskFragment currentTaskFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.fragmentManager = getFragmentManager();

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        tabs.setViewPager(pager);

        FloatingActionButton fb = (FloatingActionButton)findViewById(R.id.fab);
        fb.setOnClickListener(this);
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
//        Toast toast = Toast.makeText(getApplicationContext(),
//                "Оля, я тебя люблю! :)",
//                Toast.LENGTH_SHORT);
//        toast.setGravity(Gravity.CENTER, 0, 0);
//        toast.show();
        new AddingTaskDialogFragment().show(MainActivity.this.fragmentManager, "AddingTaskDialogFragment");
    }

    @Override
    public void onTaskEdited(ModelTask modelTask) {
//        this.currentTaskFragment.updateTask(newTask);
    }

    @Override
    public void onTaskAdded(ModelTask modelTask) {
//        this.currentTaskFragment.addTask(newTask, true);
    }

    @Override
    public void onTaskAddingCancel() {

    }
}
