package com.example.riyaza.movieappstage2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movietest2.db";
    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
         String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE "
                + MovieContact.MoviesEntry.TABLE_NAME
                + " (" + MovieContact.MoviesEntry.COLUMN_ID + " TEXT PRIMARY KEY, "
                + MovieContact.MoviesEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                + MovieContact.MoviesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, "
                + MovieContact.MoviesEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL, "
                + MovieContact.MoviesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, "
                + MovieContact.MoviesEntry.COLUMN_PORTER_URI + " TEXT NOT NULL "
                + " );";

         db.execSQL(SQL_CREATE_MOVIES_TABLE);

        String SQL_CREATE_VIDEO_TABLE ="CREATE TABLE "
                + MovieContact.VideoEntry.TABLE_NAME
                + " (" + MovieContact.VideoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MovieContact.VideoEntry.COLUMN_KEY + " TEXT NOT NULL, "
                + MovieContact.VideoEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + MovieContact.VideoEntry.COLUMN_ID + " TEXT NOT NULL "
                + " );";
        db.execSQL(SQL_CREATE_VIDEO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String SQL_DROP_MOVIES_TABLE = "DROP TABLE IF EXISTS " +
                MovieContact.MoviesEntry.TABLE_NAME;

        db.execSQL(SQL_DROP_MOVIES_TABLE);
    }
}
