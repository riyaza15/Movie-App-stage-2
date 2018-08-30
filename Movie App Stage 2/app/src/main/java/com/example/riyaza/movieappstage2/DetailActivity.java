package com.example.riyaza.movieappstage2;


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.riyaza.movieappstage2.adapter.ReviewAdapter;
import com.example.riyaza.movieappstage2.adapter.TrailerAdapter;
import com.example.riyaza.movieappstage2.data.MovieContact;
import com.example.riyaza.movieappstage2.data.MovieDbHelper;
import com.example.riyaza.movieappstage2.module.Movie;
import com.example.riyaza.movieappstage2.module.Review;
import com.example.riyaza.movieappstage2.module.Trailer;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.ListItemClickListner{

    private final static String BASE_MOVIE_DB_URL = "http://api.themoviedb.org/3/movie/";
    public static final String YOUTUBE_BASE_PATH = "https://www.youtube.com/watch?v=";
    private final static String API_KEY_PARAM = "api_key";
    private final static String API_KEY = "yourkey";
    private final static String REVIEW_PATH = "reviews";
    private final static String VIDEO_PATH = "videos";
    private static final String YOUTUBE_BASE_URL = "http://www.youtube.com";
    private static final String YOUTUBE_WATCH_PATH = "watch";
    private static final String YOUTUBE_VIDEO_KEY = "v";
    ArrayList<Review> reviews= new ArrayList<Review>();
    ArrayList<Trailer> trailers= new ArrayList<Trailer>();
    MovieDbHelper movieDbHelper;
    private RecyclerView mReviewList;
    private ReviewAdapter reviewAdapter;
    private RecyclerView mTrailerList;
    private TrailerAdapter trailerAdapter;
    private boolean isFavorite;
    private final static String MOVIE_SAVED= "movieSaved";
    public static int favorite= 0;
    private String movieID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
        ImageView imageView= (ImageView) findViewById(R.id.image);
        TextView textViewTitle= (TextView)findViewById(R.id.title);
        TextView textViewReleaseDate= (TextView) findViewById(R.id.releaseDate);
        TextView textViewOverView =(TextView) findViewById(R.id.overView);
        TextView textViewVoteAverage= (TextView) findViewById(R.id.voteAverage);

        Intent intent= getIntent();

        movieID= String.valueOf(intent.getIntExtra("Id",0));
        final String imageUrl= intent.getStringExtra("PosterUrl");
        final String title = intent.getStringExtra("Titel");
        final String releaseDate=intent.getStringExtra("ReleaseDate");
        final String overView= intent.getStringExtra("OverView");
        final double voteAverage= intent.getDoubleExtra("VoteAverage",0);

        Picasso.with(getApplicationContext()).load(imageUrl).placeholder(R.drawable.plcimage).into(imageView);
        textViewTitle.setText(title);
        textViewReleaseDate.setText(releaseDate);
        textViewOverView.setText(overView);
        textViewVoteAverage.setText(String.valueOf (voteAverage) );

        reviewAdapter = new ReviewAdapter(reviews);
        mReviewList= (RecyclerView)findViewById(R.id.rvReviewList);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        mReviewList.setLayoutManager(layoutManager);
        mReviewList.setHasFixedSize(true);
        reviewAdapter = new ReviewAdapter(reviews);
        mReviewList.setAdapter(reviewAdapter);

        URL reviewURL = buildReviewUrl(movieID);
        new RevieAsyncwTask().execute(reviewURL);

        mTrailerList= (RecyclerView) findViewById(R.id.rvTrailerList);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        mTrailerList.setLayoutManager(layoutManager2);
        mTrailerList.setHasFixedSize(true);
        trailerAdapter = new TrailerAdapter(trailers, this);
        mTrailerList.setAdapter(trailerAdapter);

        URL trailerURL = buildTrailerUrl(movieID);
        new TrailerAsyncTask().execute(trailerURL);
        Log. i("Favorite ", String.valueOf(favorite));

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavorite) {
                    int moviesRemoved = getContentResolver().delete(MovieContact.MoviesEntry.CONTENT_URI,
                            MovieContact.MoviesEntry.COLUMN_ID + " = ?", new String[]{movieID});
                    if(moviesRemoved >0 ){
                        Toast.makeText(DetailActivity.this, "The Movie removed", Toast.LENGTH_SHORT).show();
                        isFavorite = false;
                        ((ImageButton) v).setImageResource(R.drawable.ic_star_border);
                    }
                    else {
                        Toast.makeText(DetailActivity.this, "The Movie not  removed", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    ContentValues values = new ContentValues();
                    values.put(MovieContact.MoviesEntry.COLUMN_ID, movieID);
                    values.put(MovieContact.MoviesEntry.COLUMN_TITLE, title);
                    values.put(MovieContact.MoviesEntry.COLUMN_RELEASE_DATE, releaseDate);
                    values.put(MovieContact.MoviesEntry.COLUMN_VOTE_AVERAGE, voteAverage);
                    values.put(MovieContact.MoviesEntry.COLUMN_OVERVIEW, String.valueOf(overView));
                    values.put(MovieContact.MoviesEntry.COLUMN_PORTER_URI, imageUrl);

                    Uri newUri = getContentResolver().insert(MovieContact.MoviesEntry.CONTENT_URI, values);
                    if (newUri == null) {
                        Toast.makeText(DetailActivity.this, "Error with saving Movie", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(DetailActivity.this, "Movie  saved : ", Toast.LENGTH_SHORT).show();
                    }

                    ContentValues valuesTrailer = new ContentValues();

                    for (int i = 0; i < trailers.size(); i++) {
                        Trailer currentTraile = trailers.get(i);

                        String name = currentTraile.getname();
                        String key = currentTraile.getkey();

                        Log.i("Current Trailer", "id" + movieID + " Name" + name + key);


                        valuesTrailer.put(MovieContact.VideoEntry.COLUMN_ID, movieID);
                        valuesTrailer.put(MovieContact.VideoEntry.COLUMN_KEY, currentTraile.getkey());
                        valuesTrailer.put(MovieContact.VideoEntry.COLUMN_NAME, currentTraile.getname());
                        Log.i("Name", "name" + currentTraile.getname());
                        Uri newUriVideo = getContentResolver().insert(MovieContact.VideoEntry.CONTENT_URI, valuesTrailer);

                        if (newUriVideo == null) {
                            Toast.makeText(DetailActivity.this, "Error with saving Video", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(DetailActivity.this, "Movie  Video Saved : ", Toast.LENGTH_SHORT).show();
                            isFavorite = true;
                            ((ImageButton) v).setImageResource(R.drawable.ic_star);

                        }
                    }
                }
            }

        });
        isFavorite=isFavoriteM(movieID);
        Log.i("Movie Saved", String.valueOf(isFavorite));
        if (isFavorite) {
            imageButton.setImageResource(R.drawable.ic_star);
        } else {
            imageButton.setImageResource(R.drawable.ic_star_border);
        }
       Log.i("Movie Saved","Detailactivity");

    }

    public static URL buildReviewUrl(String movieID) {
        Uri builtUri = Uri.parse(BASE_MOVIE_DB_URL).buildUpon().appendPath(movieID).appendPath(REVIEW_PATH).appendQueryParameter(API_KEY_PARAM, API_KEY).build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.i("Detail", "Review url "+ url);

        return url;
    }

    public static URL buildTrailerUrl(String moiveID) {
        Uri builtUri = Uri.parse(BASE_MOVIE_DB_URL).buildUpon().appendPath(moiveID).appendPath(VIDEO_PATH).appendQueryParameter(API_KEY_PARAM, API_KEY).build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.i("Detail", "Trailer url "+ url);

        return url;
    }

    @Override
    public void onListItemClick(Trailer trailer) {
        Toast.makeText(this,"trailer clicke", Toast.LENGTH_LONG).show();
        String trailelKey= trailer.getkey();
        Uri uri= buildYoutubeUri(trailelKey);
        Intent intent= new Intent(Intent.ACTION_VIEW,uri);
        Context context = getApplicationContext();

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

    private Uri buildYoutubeUri(String trailerKey){
        Uri builtUri = Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                .appendEncodedPath(YOUTUBE_WATCH_PATH)
                .appendQueryParameter(YOUTUBE_VIDEO_KEY, trailerKey)
                .build();

        return builtUri;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("MOVIE_SAVED", isFavorite);

        Log.i("saved Instence","save");

    }
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        isFavorite=savedInstanceState.getBoolean(MOVIE_SAVED);
        Log.i("saved Instence","restore");
    }

    private boolean isFavoriteM( String movieID) {

        Cursor cursor = getContentResolver().query(MovieContact.MoviesEntry
                        .CONTENT_URI, null,
                MovieContact.MoviesEntry.COLUMN_ID + " = " + movieID, null, null);
        if (cursor != null && cursor.moveToNext()) {
            int movieIdColumnIndex = cursor.getColumnIndex(MovieContact.MoviesEntry.COLUMN_ID);
            if (TextUtils.equals(movieID, cursor.getString(movieIdColumnIndex))) {
                return true;
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    private class RevieAsyncwTask extends AsyncTask<URL, Void, List<Review>> {

        @Override
        protected List<Review> doInBackground(URL... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            List<Review> result = QueryUtils.fetchReviewData(urls[0]);

            return result;
        }

        @Override
        protected void onPostExecute(List<Review> data) {


            if (data != null && !data.isEmpty()) {
                reviewAdapter.setReviewData(data);

            }
        }
        }

    private class TrailerAsyncTask extends AsyncTask<URL, Void, List<Trailer>> {

        @Override
        protected List<Trailer> doInBackground(URL... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            List<Trailer> result = QueryUtils.fetchTrailerData(urls[0]);

            return result;
        }

        @Override
        protected void onPostExecute(List<Trailer> data) {
            if (data != null && !data.isEmpty()) {
                trailerAdapter.setTrailerData(data);
                trailers.addAll(data);

            }
        }
    }

    }





