package com.example.omar.moviedb;

/**
 * Created by omar on 10/15/17.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.FontsContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shuo on 6/7/2017.
 */

public class DatabaseManager {
    private SQLiteOpenHelper dbOpenHelper;
    private SQLiteDatabase database;

    public DatabaseManager(Context context) {
        dbOpenHelper = new DBOpenHelper(context);
    }

    public void open() {
        database = dbOpenHelper.getWritableDatabase();
    }

    public void close() {
        dbOpenHelper.close();
    }

    public void insertMovieInfo(String title, String release, String vote) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COLUMN_NAME_TITLE, title);
        values.put(DBOpenHelper.COLUMN_NAME_RELEASE_DATE, release);
        values.put(DBOpenHelper.COLUMN_NAME_VOTE, vote);
        database.insert(DBOpenHelper.TABLE_NAME, null, values);
    }

    public List<String[]> getAllRecordsOrderedBy(String key) {
        //return the list ordered by the key
        //The key should be one of the column keys defined in DBOpenHelper.
        return null;
    }

    public List<Movie> getAllRecords() {
        Cursor cursor = database.query(DBOpenHelper.TABLE_NAME,
                new String[]{
                        DBOpenHelper.COLUMN_ID,
                        DBOpenHelper.COLUMN_NAME_TITLE,
                        DBOpenHelper.COLUMN_NAME_RELEASE_DATE,
                        DBOpenHelper.COLUMN_NAME_VOTE
                }, null, null, null, null, null, null);
        cursor.moveToFirst();
        Movie movie;
        List<Movie> result =new  ArrayList<Movie>();
        while (!cursor.isAfterLast()) {
            movie = new Movie();
            movie.setTitle(
                    cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_NAME_TITLE)));

            movie.setDate(
                    cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_NAME_RELEASE_DATE)));
            movie.setRating(
                    cursor.getString(cursor.getColumnIndex(DBOpenHelper.COLUMN_NAME_VOTE)));
            cursor.moveToNext();
            result.add(movie);
        }
        return result;
    }

    public void deleteAll() {
        if (database.isOpen()) {
            database.execSQL("DELETE FROM " + DBOpenHelper.TABLE_NAME);
        }
    }
}
