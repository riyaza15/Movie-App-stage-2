package com.example.riyaza.movieappstage2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.example.riyaza.movieappstage2.data.MovieContact.MoviesEntry.TABLE_NAME;

public class MovieProvider extends ContentProvider {

    public static final String LOG_TAG = MovieProvider.class.getSimpleName();
    private MovieDbHelper movieDbHelper;
    public static final int MOVIES = 100;
    public static final int MOVIE_ID = 101;
    private static final int VIDEOS = 200;
    private static final int VIDEOS_ITEM = 201;
    private static final int REVIEWS = 300;
    private static final int REVIEWS_ITEM = 301;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(MovieContact.CONTENT_AUTHORITY, MovieContact.PATH_MOVIE, MOVIES);
        sUriMatcher.addURI(MovieContact.CONTENT_AUTHORITY, MovieContact.PATH_MOVIE + "/#", MOVIE_ID);
        sUriMatcher.addURI(MovieContact.CONTENT_AUTHORITY, MovieContact .PATH_VIDEOS ,VIDEOS);
        sUriMatcher.addURI(MovieContact.CONTENT_AUTHORITY, MovieContact.PATH_REVIEWS + "/#",VIDEOS_ITEM );
    }


    @Override
    public boolean onCreate() {
        movieDbHelper= new MovieDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = movieDbHelper.getReadableDatabase();
        Cursor cursor;
        String id;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                cursor = database.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case MOVIE_ID:

                id = uri.getPathSegments().get(1);

                cursor = database.query(TABLE_NAME,
                        projection,
                        MovieContact.MoviesEntry.COLUMN_ID + " = " + id,
                        null,
                        null,
                        null,
                        sortOrder);
                break;
            case VIDEOS: {
                cursor = database.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }
            case VIDEOS_ITEM: {

                selection= MovieContact.VideoEntry.COLUMN_ID +"=?";
                selectionArgs= new String[]{ String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }


            default:
                throw new UnsupportedOperationException("Cannot query the Unknown uri: " + uri);

        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
   return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                return insertMovie(uri, values);

            case VIDEOS:
                return insertVideo(uri,values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }



    private Uri insertMovie(Uri uri, ContentValues values) {

        SQLiteDatabase database = movieDbHelper.getWritableDatabase();
        long id = database.insert(MovieContact.MoviesEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertVideo(Uri uri, ContentValues values) {

        SQLiteDatabase database = movieDbHelper.getWritableDatabase();
        long id = database.insert(MovieContact.VideoEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        Log.i("URI", "uri"+ uri);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = movieDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        int rowsDeleted;
        if (null == selection) selection = "1";
        switch (match) {
            case MOVIES:
                rowsDeleted = database.delete(MovieContact.MoviesEntry.TABLE_NAME, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }


}
