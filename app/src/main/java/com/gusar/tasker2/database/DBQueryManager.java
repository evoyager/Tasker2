package com.gusar.tasker2.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.gusar.tasker2.model.ModelTask;
import java.util.ArrayList;
import java.util.List;

public class DBQueryManager {
    private SQLiteDatabase database;

    DBQueryManager(SQLiteDatabase database) {
        this.database = database;
    }

    public ModelTask getTask(long timeStamp) {
        ModelTask modelTask;
        Cursor cursor = this.database.query(DBHelper.TASKS_TABLE, null, DBHelper.SELECTION_TIME_STAMP, new String[]{Long.toString(timeStamp)}, null, null, null);
        if (cursor.moveToFirst()) {
            modelTask = new ModelTask(cursor.getString(cursor.getColumnIndex(DBHelper.TASK_TITLE_COLUMN)), cursor.getLong(cursor.getColumnIndex(DBHelper.TASK_DATE_COLUMN)), cursor.getInt(cursor.getColumnIndex(DBHelper.TASK_PRIORITY_COLUMN)), cursor.getInt(cursor.getColumnIndex(DBHelper.TASK_STATUS_COLUMN)), timeStamp);
        } else {
            modelTask = null;
        }
        cursor.close();
        return modelTask;
    }

    public List<ModelTask> getTasks(String selection, String[] selectionArgs, String orderBy) {
        List<ModelTask> tasks = new ArrayList();
        Cursor c = this.database.query(DBHelper.TASKS_TABLE, null, selection, selectionArgs, null, null, orderBy);
        if (c.moveToFirst()) {
            do {
                String title = c.getString(c.getColumnIndex(DBHelper.TASK_TITLE_COLUMN));
                long date = c.getLong(c.getColumnIndex(DBHelper.TASK_DATE_COLUMN));
                int priority = c.getInt(c.getColumnIndex(DBHelper.TASK_PRIORITY_COLUMN));
                int status = c.getInt(c.getColumnIndex(DBHelper.TASK_STATUS_COLUMN));
                long timeStamp = c.getLong(c.getColumnIndex(DBHelper.TASK_TIME_STAMP_COLUMN));
                Log.d("wtf?", "title = " + title);
                tasks.add(new ModelTask(title, date, priority, status, timeStamp));
            } while (c.moveToNext());
        }
        c.close();
        return tasks;
    }
}
