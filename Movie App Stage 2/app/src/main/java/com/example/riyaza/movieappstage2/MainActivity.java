package com.example.riyaza.movieappstage2;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.riyaza.movieappstage2.adapter.MovieAdapter;
import com.example.riyaza.movieappstage2.data.MovieContact;
import com.example.riyaza.movieappstage2.data.MovieDbHelper;
import com.example.riyaza.movieappstage2.module.Movie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListner {

    public static final String LOG_TAG = MainActivity.class.getName();
    ArrayList<Movie> movies= new ArrayList<Movie>();
    private MovieAdapter mAdapter;
    private RecyclerView mMovieList;
    private static final String POP_MOVIE_REQUEST_URL ="https://api.themoviedb.org/3/movie/popular?api_key=yourkey";
    private static final String TOP_MOVIE_REQUEST_URL ="https://api.themoviedb.org/3/movie/top_rated?api_key=yourkey";
    private Toast mToast;
    private int currentMenuId;
    private int menuId;
    private Parcelable cuurntLayout;
    private static Bundle mAppState;
    protected String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieList = (RecyclerView) findViewById(R.id.rvMovieList);
        GridLayoutManager layoutManager = new GridLayoutManager(this, getGridCol());
        mMovieList.setLayoutManager(layoutManager);
        mMovieList.setHasFixedSize(true);
        mAdapter = new MovieAdapter(movies,this);
        mMovieList.setAdapter(mAdapter);


        if (savedInstanceState != null) {
               movies= savedInstanceState.getParcelableArrayList("Movie Array");
               if(movies.isEmpty()){
                   Toast.makeText(MainActivity.this, "Movie is empty", Toast.LENGTH_LONG).show();
                   MovieAsyncTask task = new MovieAsyncTask();
                   task.execute(POP_MOVIE_REQUEST_URL);
                   setTitle("Populer Movies");
               }
                 else {
                   mAdapter.setMovieData(movies);
                   Toast.makeText(MainActivity.this, "Movie not null", Toast.LENGTH_LONG).show();

               }

            cuurntLayout = savedInstanceState.getParcelable("LAYOUT");
            mMovieList.getLayoutManager().onRestoreInstanceState(cuurntLayout);
            menuId= savedInstanceState.getInt("Menu ID");
            title= savedInstanceState.getString("Title");
            setTitle(title);
           // Toast.makeText(MainActivity.this ,"On Create Layou",Toast.LENGTH_LONG).show();
        }

        else{
            MovieAsyncTask task = new MovieAsyncTask();
            task.execute(POP_MOVIE_REQUEST_URL);
        }
//        if(menuId<2){
//            task.execute(POP_MOVIE_REQUEST_URL);
//            currentMenuId=1;
//            mMovieList.getLayoutManager().onRestoreInstanceState(cuurntLayout);
//        }
//        else if(menuId==2){
//            task.execute(TOP_MOVIE_REQUEST_URL);
//            currentMenuId=2;
//            mMovieList.getLayoutManager().onRestoreInstanceState(cuurntLayout);
//
//        }
//        else{
//            mAdapter.setMovieData(movies);
//            diplayFavoriteList();
//            currentMenuId=3;
//            mMovieList.getLayoutManager().onRestoreInstanceState(cuurntLayout);
//
//        }
    }
    @Override
    protected void onPause()
    {
        super.onPause();

        mAppState = new Bundle();
        Parcelable listState = mMovieList.getLayoutManager().onSaveInstanceState();
        mAppState.putParcelable("KeyAppStae", listState);
    }



    @Override
    protected void onResume() {
        super.onResume();
        DetailActivity.favorite=0;
        if(title == "Favorite Movies" ){
            diplayFavoriteList();
        }

        // mMovieList.getLayoutManager().onRestoreInstanceState(cuurntLayout);
        if (mAppState != null) {
            Parcelable listState = mAppState.getParcelable("KeyAppStae");
            mMovieList.getLayoutManager().onRestoreInstanceState(listState);


        }


    }

    private int getGridCol() {
        int size = 1;
        switch(getResources().getConfiguration().orientation){
            case Configuration.ORIENTATION_PORTRAIT:
                size= 3;
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                size= 5;
                break;
        }
        return size;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int Id = item.getItemId();
        movies.clear();
        if (Id== (R.id.popular)){
//            mAdapter.setMovieData(movies);
            MovieAsyncTask task = new MovieAsyncTask();
            task.execute(POP_MOVIE_REQUEST_URL);
            title="Populer Movies";
            setTitle(title);
            currentMenuId=1;
        }
        if(Id== (R.id.top_rated)){
//            mAdapter.setMovieData(movies);
            MovieAsyncTask task = new MovieAsyncTask();
            task.execute(TOP_MOVIE_REQUEST_URL);
            title="Top rated Movies";
            setTitle(title);
            currentMenuId=2;
        }
        if (Id== R.id.favorite){
            diplayFavoriteList();
            DetailActivity.favorite=1;
            title="Favorite Movies";
            setTitle(title);
            currentMenuId=3;
        }

        return super.onOptionsItemSelected(item);
    }


    public void onRestoreInstanceState(Bundle savedInstanceState) {
        menuId=savedInstanceState.getInt("Menu ID");
        setTitle(savedInstanceState.getString("Title"));
        movies=savedInstanceState.getParcelableArrayList("Movie Array");
        cuurntLayout = savedInstanceState.getParcelable("LAYOUT");
        mMovieList.getLayoutManager().onRestoreInstanceState(cuurntLayout);


        Log.i("saved Instence","restore"+ menuId);
    }


       public void diplayFavoriteList(){
           String[] projection = {
                   MovieContact.MoviesEntry.COLUMN_ID,
                   MovieContact.MoviesEntry.COLUMN_TITLE  ,
                   MovieContact.MoviesEntry.COLUMN_OVERVIEW,
                   MovieContact.MoviesEntry.COLUMN_RELEASE_DATE,
                   MovieContact.MoviesEntry.COLUMN_VOTE_AVERAGE,
                   MovieContact.MoviesEntry.COLUMN_PORTER_URI
           };

              Cursor cursor=getContentResolver().query(
                      MovieContact.MoviesEntry.CONTENT_URI,
                      projection,
                      null,
                      null,
                      null);

           try{
               int idColumnIndex = cursor.getColumnIndex(MovieContact.MoviesEntry.COLUMN_ID);
               int titleColumnIndex = cursor.getColumnIndex(MovieContact.MoviesEntry.COLUMN_TITLE);
               int releaseDateColumnIndex = cursor.getColumnIndex(MovieContact.MoviesEntry.COLUMN_RELEASE_DATE);
               int overViewColumnIndex = cursor.getColumnIndex(MovieContact.MoviesEntry.COLUMN_OVERVIEW);
               int voteAverageColumnIndex = cursor.getColumnIndex(MovieContact.MoviesEntry.COLUMN_VOTE_AVERAGE);
               int posterURIColumnIndex = cursor.getColumnIndex(MovieContact.MoviesEntry.COLUMN_PORTER_URI);
               movies.clear();
               while (cursor.moveToNext()) {
                   int id = cursor.getInt(idColumnIndex);
                   String title = cursor.getString(titleColumnIndex);
                   String date = cursor.getString(releaseDateColumnIndex);
                   String overView = cursor.getString(overViewColumnIndex);
                   Double voteAverage = cursor.getDouble(voteAverageColumnIndex);
                   String posterURI = cursor.getString(posterURIColumnIndex);
                   Movie movie = new Movie( id ,title, date, overView, posterURI,voteAverage);
                   movies.add(movie);

               }
           }
           finally {
               cursor.close();
           }

           mAdapter.setMovieData(movies);

           }

    @Override
    public void onListItemClick(Movie mMovie) {

        Intent intent= new Intent(MainActivity.this,DetailActivity.class);
        int id=mMovie.getId();
        String title=mMovie.getTitle();
        String releaseDate=mMovie.getReleaseDate();
        String overView=mMovie.getOverView();
        String posterUrl=mMovie.getPosterUrl();
        double voteAverage=mMovie.getVoteAverage();

        intent.putExtra("Id",id);
        intent.putExtra("Titel",title);
        intent.putExtra("ReleaseDate",releaseDate);
        intent.putExtra("OverView",overView);
        intent.putExtra("PosterUrl",posterUrl);
        intent.putExtra("VoteAverage",voteAverage);
        startActivity(intent);
    }

    private class MovieAsyncTask extends AsyncTask<String, Void, List<Movie>> {
        @Override
        protected List<Movie> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            List<Movie> result = QueryUtils.fetchMovieData(urls[0]);
            return result;
        }
        @Override
        protected void onPostExecute(List<Movie> data) {
//            mAdapter = new MovieAdapter(movies,MainActivity.this);
//            mMovieList.setAdapter(mAdapter);

            if (data != null && !data.isEmpty()) {
                mAdapter.setMovieData(data);
               // movies= data;
                movies.addAll(data);
            }

        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("Menu ID",currentMenuId);
        if(movies !=null) {
            outState.putParcelableArrayList("Movie Array", movies);
            Toast.makeText(this,"movie not  null",Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this,"movie is null",Toast.LENGTH_LONG).show();

        }

        if (mMovieList != null) {
            outState.putParcelable("LAYOUT" ,mMovieList.getLayoutManager().onSaveInstanceState());
        }

        outState.putString("Title",title);

        Log.i("saved Instence","save"+currentMenuId);

    }
}

