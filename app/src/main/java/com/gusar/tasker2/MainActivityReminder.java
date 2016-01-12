package com.gusar.tasker2;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.design.widget.TabLayout.Tab;
import android.support.design.widget.TabLayout.TabLayoutOnPageChangeListener;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
//import com.gusar.tasker2.adapter.TabAdapter;
import com.gusar.tasker2.database.DBHelper;
import com.gusar.tasker2.dialog.AddingTaskDialogFragment;
import com.gusar.tasker2.dialog.AddingTaskDialogFragment.AddingTaskListener;
import com.gusar.tasker2.dialog.EditTaskDialogFragment.EditingTaskListener;
import com.gusar.tasker2.fragment.CurrentTaskFragment;
import com.gusar.tasker2.fragment.CurrentTaskFragment.OnTaskDoneListener;
import com.gusar.tasker2.fragment.DoneTaskFragment;
import com.gusar.tasker2.fragment.DoneTaskFragment.OnTaskRestoreListener;
//import com.gusar.tasker2.fragment.SplashFragment;
import com.gusar.tasker2.fragment.OldTaskFragment;
import com.gusar.tasker2.fragment.TaskFragment;
import com.gusar.tasker2.model.ModelTask;

public class MainActivityReminder extends AppCompatActivity implements AddingTaskListener, EditingTaskListener, OnTaskDoneListener, OnTaskRestoreListener {
//    AlarmHelper alarmHelper;
    OldTaskFragment currentTaskFragment;
    public DBHelper dbHelper;
    OldTaskFragment doneTaskFragment;
    FragmentManager fragmentManager;
//    PreferenceHelper preferenceHelper;
    SearchView searchView;
//    TabAdapter tabAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_old);
//        Ads.showBottomBanner(this);
//        PreferenceHelper.getInstance().init(getApplicationContext());
//        this.preferenceHelper = PreferenceHelper.getInstance();
//        AlarmHelper.getInstance().init(getApplicationContext());
//        this.alarmHelper = AlarmHelper.getInstance();
        this.dbHelper = new DBHelper(getApplicationContext());
        this.fragmentManager = getFragmentManager();
//        runSplash();
//        setUI();
    }

    protected void onResume() {
        super.onResume();
        MyApplication.activityResumed();
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("hello")) {
            Log.d("really?", getIntent().getStringExtra("hello"));
        }
    }

    protected void onPause() {
        super.onPause();
        MyApplication.activityPaused();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
//        menu.findItem(R.id.action_splash).setChecked(this.preferenceHelper.getBoolean(PreferenceHelper.SPLASH_IS_INVISIBLE));
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_splash /*2131624075*/:
//                item.setChecked(!item.isChecked());
//                this.preferenceHelper.putBoolean(PreferenceHelper.SPLASH_IS_INVISIBLE, item.isChecked());
//                return true;
//            default:
                return super.onOptionsItemSelected(item);
//        }
    }

    protected void onSaveInstanceState(Bundle outState) {
    }

//    public void runSplash() {
//        if (!this.preferenceHelper.getBoolean(PreferenceHelper.SPLASH_IS_INVISIBLE)) {
//            this.fragmentManager.beginTransaction().replace(R.id.content_frame, new SplashFragment()).addToBackStack(null).commit();
//        }
//    }

//    private void setUI() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        if (toolbar != null) {
//            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
//            setSupportActionBar(toolbar);
//        }
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
//        tabLayout.addTab(tabLayout.newTab().setText(R.string.current_task));
//        tabLayout.addTab(tabLayout.newTab().setText(R.string.done_task));
//        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
//        this.tabAdapter = new TabAdapter(this.fragmentManager, 2);
//        viewPager.setAdapter(this.tabAdapter);
//        viewPager.addOnPageChangeListener(new TabLayoutOnPageChangeListener(tabLayout));
//        this.currentTaskFragment = (CurrentTaskFragment) this.tabAdapter.getItem(0);
//        this.doneTaskFragment = (DoneTaskFragment) this.tabAdapter.getItem(1);
//        this.searchView = (SearchView) findViewById(R.id.searchView);
//        tabLayout.setOnTabSelectedListener(new OnTabSelectedListener() {
//            public void onTabSelected(Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//            }
//
//            public void onTabUnselected(Tab tab) {
//            }
//
//            public void onTabReselected(Tab tab) {
//            }
//        });
//        this.searchView.setOnQueryTextListener(new OnQueryTextListener() {
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            public boolean onQueryTextChange(String newText) {
//                MainActivityReminder.this.currentTaskFragment.findTasks(newText);
//                MainActivityReminder.this.doneTaskFragment.findTasks(newText);
//                return false;
//            }
//        });
//        ((FloatingActionButton) findViewById(R.id.fab)).setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                new AddingTaskDialogFragment().show(MainActivityReminder.this.fragmentManager, "AddingTaskDialogFragment");
//            }
//        });
//    }

    public void onTaskAdded(ModelTask newTask) {
        this.currentTaskFragment.addTask(newTask, true);
    }

    public void onTaskAddingCancel() {
    }

    public void onTaskDone(ModelTask task) {
        this.doneTaskFragment.addTask(task, false);
    }

    public void onTaskRestore(ModelTask task) {
        this.currentTaskFragment.addTask(task, false);
    }

    public void onBackPressed() {
        if (this.searchView.isIconified()) {
            super.onBackPressed();
        } else {
            this.searchView.setIconified(true);
        }
    }

    public void onTaskEdited(ModelTask newTask) {
        this.currentTaskFragment.updateTask(newTask);
        this.dbHelper.update().task(newTask);
    }
}
