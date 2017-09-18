package com.example.birkan.remindme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by birkan on 14.09.2017.
 */

public class SQliteHelper extends SQLiteOpenHelper {
    private static final String dbName = "ReminderDB";
    private static final String tableName = "ReminderItem";
    private static final String reminderId = "id";
    private static final String reminderTitle = "title";

    public SQliteHelper(Context context) {
       super(context, dbName, null, 1);
        //super(context, new File(Environment.getExternalStorageDirectory(),dbName).toString(), null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
       //  sqLiteDatabase.execSQL("CREATE TABLE " + tableName + " (" + reminderId + " INTEGER PRIMARY KEY AUTOINCREMENT, " + reminderTitle + " TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public void AddReminder(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(reminderTitle, title);
        db.insert(tableName, null, contentValues);
        db.close();
    }

    public RemindItem GetReminder(int id) {
        String query = "SELECT * FROM " + tableName + " WHERE id="+ id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        RemindItem remindItem = new RemindItem();
        if (cursor.moveToFirst()) {
            remindItem.setId(Integer.parseInt(cursor.getString(0)));
            remindItem.setTitle(cursor.getString(1));
        }
        return remindItem;
    }

    public List<RemindItem> GetReminderList() {
        String query = "SELECT * FROM " + tableName;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        List<RemindItem> remindItems = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                RemindItem remindItem = new RemindItem();
                remindItem.setId(Integer.parseInt(cursor.getString(0)));
                remindItem.setTitle(cursor.getString(1));
                remindItems.add(remindItem);
            }
            while (cursor.moveToNext());
        }

        return remindItems;
    }

}
