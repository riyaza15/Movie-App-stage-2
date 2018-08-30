package com.example.riyaza.movieappstage2;

import android.text.TextUtils;
import android.util.Log;

import com.example.riyaza.movieappstage2.module.Movie;
import com.example.riyaza.movieappstage2.module.Review;
import com.example.riyaza.movieappstage2.module.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.riyaza.movieappstage2.MainActivity.LOG_TAG;

public class QueryUtils {
    //Tutorial followed Android Basic Networking course
    static final String BASE_URL = "https://image.tmdb.org/t/p/";
    static final String POSTER_SIZE = "w500";

    private QueryUtils(){
    }
    public static List<Movie> fetchMovieData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem creating the HTTP request.", e);
        }
        List<Movie> movies = extractFeatureFromJson(jsonResponse);
        return movies;
    }

    private static List<Movie> extractFeatureFromJson(String movieJSON) {
        if (TextUtils.isEmpty(movieJSON)) {
            return null;
        }
        List<Movie> movies = new ArrayList<>();
        try {
            JSONObject baseJson = new JSONObject(movieJSON);
            JSONArray movieArray = baseJson.getJSONArray("results");
            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject currentMovie = movieArray.getJSONObject(i);
                int movieId   = currentMovie.getInt("id");
                String title  = currentMovie.getString("original_title");
                String releaseDate = currentMovie.getString("release_date");
                String overView = currentMovie.getString("overview");
                String posterUrl = currentMovie.getString("poster_path");
                Double voteAverage = currentMovie.getDouble("vote_average");
                String imageUrl= BASE_URL + POSTER_SIZE+ posterUrl;
                Movie movie = new Movie(movieId,title, releaseDate, overView, imageUrl,voteAverage);
                movies.add(movie);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem in the movie JSON results", e);
        }
        return movies;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        Log.v("URL", String.valueOf(url));
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);

            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
                Log.i("Detail", "connected "+ line);

            }
        }
        return output.toString();
    }

    public static List<Review> fetchReviewData(URL url) {
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem creating the HTTP request.", e);
        }
        List<Review> reviews = extractReviewFromJson(jsonResponse);
        return reviews;
    }

    private static ArrayList<Review> extractReviewFromJson(String MovieReviewsJSON) {
        if (TextUtils.isEmpty(MovieReviewsJSON)) {
            return null;
        }

        ArrayList<Review> reviewList = new ArrayList<>();
                try {
                    JSONObject baseJsonResponse = new JSONObject(MovieReviewsJSON);
                    JSONArray reviewsArray = baseJsonResponse.getJSONArray("results");
                    for (int i = 0; i < reviewsArray.length(); i++) {
                        JSONObject currentReview = reviewsArray.getJSONObject(i);
                        String author = currentReview.getString("author");
                        String content = currentReview.getString("content");
                        Review review = new Review(author, content);
                        reviewList.add(review);
                        Log.i("Detail", "Done ");
                    }
                    }
                catch (JSONException e) {
                    Log.e("Review Details", "Problem in the Movie Review JSON results", e);

                    }
        return reviewList;
        }

    public static List<Trailer> fetchTrailerData(URL url) {
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem creating the HTTP request.", e);
        }
        List<Trailer> trailers = extractTrailerFromJson(jsonResponse);
        return trailers;
    }

    private static ArrayList<Trailer> extractTrailerFromJson(String MovieReviewsJSON) {
        if (TextUtils.isEmpty(MovieReviewsJSON)) {
            return null;
        }

        ArrayList<Trailer> trailerList = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(MovieReviewsJSON);
            JSONArray trailerArray = baseJsonResponse.getJSONArray("results");
            for (int i = 0; i < trailerArray.length(); i++) {
                JSONObject currentTrailer = trailerArray.getJSONObject(i);
                String moviid= currentTrailer.getString("id");
                String name = currentTrailer.getString("name");
                String key = currentTrailer.getString("key");
                Trailer trailer = new Trailer(moviid,name, key);
                Log.i("Trailer",name );
                trailerList.add(trailer);
            }
        }
        catch (JSONException e) {
            Log.e("Review Details", "Problem in the Movie Review JSON results", e);
        }
        return trailerList;
    }




}
