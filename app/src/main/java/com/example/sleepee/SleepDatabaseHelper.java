package com.example.sleepee;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.sleepee.model.Sleep;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SleepDatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "SQLite";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Sleep Manager";
    private static final String TABLE_SLEEP = "Sleep";

    private static final String COLUMN_SLEEP_START_TIME = "Sleep_StartTime";
    private static final String COLUMN_SLEEP_END_TIME = "Sleep_EndTime";
    private static final String COLUMN_SLEEP_DURATION = "Sleep_Duration";
    private static final String COLUMN_SLEEP_CYCLE = "Sleep_Cycle";

    public SleepDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    // Tạo các bảng.
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "SleepDatabaseHelper.onCreate ... ");
        String sql = "CREATE TABLE " + TABLE_SLEEP + "("
                + COLUMN_SLEEP_START_TIME + " LONG PRIMARY KEY,"
                + COLUMN_SLEEP_END_TIME + " LONG,"
                + COLUMN_SLEEP_DURATION + " LONG,"
                + COLUMN_SLEEP_CYCLE + " LONG)";
        db.execSQL(sql);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.i(TAG, "SleepDatabaseHelper.onUpgrade ... ");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SLEEP);
        onCreate(db);
    }

    public void createDefault() {
        int count = this.getSleepCount();
        if (count == 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            Sleep sleep2 = new Sleep(calendar.getTimeInMillis(), 20000000, 25000000, 4);
            calendar.add(Calendar.DATE, -1);
            Sleep sleep1 = new Sleep(calendar.getTimeInMillis(), 20000000, 30000000, 4);

//            Sleep sleep2 = new Sleep(10000000, 20000000, 10000000, 100);
//            Sleep sleep3 = new Sleep(10000000, 20000000, 10000000, 100);
//            Sleep sleep4 = new Sleep(10000000, 20000000, 10000000, 100);
//            Sleep sleep5 = new Sleep(10000000, 20000000, 10000000, 100);
            this.addSleep(sleep1);
            this.addSleep(sleep2);
//            this.addSleep(sleep3);
//            this.addSleep(sleep4);
//            this.addSleep(sleep5);
        }
    }


    public void addSleep(Sleep sleep) {
        Log.i(TAG, "SleepDatabaseHelper.addSleep ... " + sleep.getStartTime());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_SLEEP_START_TIME, sleep.getStartTime());
        values.put(COLUMN_SLEEP_END_TIME, sleep.getEndTime());
        values.put(COLUMN_SLEEP_DURATION, sleep.getDuration());
        values.put(COLUMN_SLEEP_CYCLE, sleep.getCycle());
        db.insert(TABLE_SLEEP, null, values);
        db.close();
    }


    public Sleep getSleep(long startTime) {
        Log.i(TAG, "SleepDatabaseHelper.getSleep ... " + startTime);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SLEEP, new String[]{COLUMN_SLEEP_START_TIME,
                        COLUMN_SLEEP_END_TIME, COLUMN_SLEEP_DURATION, COLUMN_SLEEP_CYCLE}, COLUMN_SLEEP_START_TIME + "=?",
                new String[]{String.valueOf(startTime)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Sleep sleep = new Sleep(Long.parseLong(cursor.getString(0)), Long.parseLong(cursor.getString(1)), Long.parseLong(cursor.getString(2)), Integer.parseInt(cursor.getString(3)));
        // return note
        return sleep;
    }

    public List<Sleep> getAllSleeps() {
        Log.i(TAG, "SleepDatabaseHelper.getAllSleep ... ");

        List<Sleep> listSleep = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_SLEEP;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Sleep sleep = new Sleep(cursor.getLong(0), cursor.getLong(1), cursor.getLong(2), cursor.getInt(3));
                listSleep.add(sleep);
            } while (cursor.moveToNext());
        }
        return listSleep;
    }

    public List<Sleep> getAllSleepsDesc() {
        Log.i(TAG, "SleepDatabaseHelper.getAllSleep ... ");

        List<Sleep> listSleep = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_SLEEP + " ORDER BY " + COLUMN_SLEEP_START_TIME + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Sleep sleep = new Sleep(cursor.getLong(0), cursor.getLong(1), cursor.getLong(2), cursor.getInt(3));
                listSleep.add(sleep);
            } while (cursor.moveToNext());
        }
        return listSleep;
    }

    public List<Sleep> getSleepNearBy(int number) {
        Log.i(TAG, "SleepDatabaseHelper.nearby ... " + number);

        List<Sleep> listSleep = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SLEEP + " DESC LIMIT " + number;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Sleep sleep = new Sleep(Long.parseLong(cursor.getString(0)), Long.parseLong(cursor.getString(1)), Long.parseLong(cursor.getString(2)), Integer.parseInt(cursor.getString(3)));
                listSleep.add(sleep);
            } while (cursor.moveToNext());
        }
        return listSleep;
    }

    public int getSleepCount() {
        Log.i(TAG, "SleepDatabaseHelper.getSleepCount ... ");
        String countQuery = "SELECT  * FROM " + TABLE_SLEEP;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public void deleteSleep(Sleep sleep) {
        Log.i(TAG, "SleepDatabaseHelper.deleteSleep ... " + sleep.getStartTime());
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SLEEP, COLUMN_SLEEP_START_TIME + " = ?", new String[]{String.valueOf(sleep.getStartTime())});
        db.close();
    }

    public static void deleteDatabase(Context mContext) {
        mContext.deleteDatabase(DATABASE_NAME);
    }
}
