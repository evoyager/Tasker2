package com.gusar.tasker2.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.gusar.tasker2.model.ModelTask;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "reminder_database";
    public static final int DATABASE_VERSION = 1;
    public static final String SELECTION_LIKE_TITLE = "task_title LIKE ?";
    public static final String SELECTION_STATUS = "task_status = ?";
    public static final String SELECTION_TIME_STAMP = "task_time_stamp = ?";
    public static final String TASKS_TABLE = "tasks_table";
    private static final String TASKS_TABLE_CREATE_SCRIPT = "CREATE TABLE tasks_table (_id INTEGER PRIMARY KEY AUTOINCREMENT, task_title TEXT NOT NULL, task_date LONG, task_priority INTEGER, task_status INTEGER, task_time_stamp LONG);";
    public static final String TASK_DATE_COLUMN = "task_date";
    public static final String TASK_PRIORITY_COLUMN = "task_priority";
    public static final String TASK_STATUS_COLUMN = "task_status";
    public static final String TASK_TIME_STAMP_COLUMN = "task_time_stamp";
    public static final String TASK_TITLE_COLUMN = "task_title";
    private DBQueryManager queryManager = new DBQueryManager(getReadableDatabase());
    private DBUpdateManager updateManager = new DBUpdateManager(getWritableDatabase());

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TASKS_TABLE_CREATE_SCRIPT);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE tasks_table");
        onCreate(db);
    }

    public void saveTask(ModelTask task) {
        ContentValues newValues = new ContentValues();
        newValues.put(TASK_TITLE_COLUMN, task.getTitle());
        newValues.put(TASK_DATE_COLUMN, Long.valueOf(task.getDate()));
        newValues.put(TASK_STATUS_COLUMN, Integer.valueOf(task.getStatus()));
        newValues.put(TASK_PRIORITY_COLUMN, Integer.valueOf(task.getPriority()));
        newValues.put(TASK_TIME_STAMP_COLUMN, Long.valueOf(task.getTimeStamp()));
        getWritableDatabase().insert(TASKS_TABLE, null, newValues);
    }

    public DBQueryManager query() {
        return this.queryManager;
    }

    public DBUpdateManager update() {
        return this.updateManager;
    }

    public void removeTask(long timeStamp) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        String str = TASKS_TABLE;
        String str2 = SELECTION_TIME_STAMP;
        String[] strArr = new String[DATABASE_VERSION];
        strArr[0] = Long.toString(timeStamp);
        writableDatabase.delete(str, str2, strArr);
    }
}
