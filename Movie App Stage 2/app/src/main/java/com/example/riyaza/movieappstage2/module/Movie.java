package com.example.riyaza.movieappstage2.module;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private  int id;
    private String title;
    private String releaseDate;
    private  String overView;
    private String posterUrl;
    private double voteAverage;

    public Movie(){
    }

    public Movie(int id,String title, String releaseDate, String overView, String posterUrl, double voteAverage){
        this.id=id;
        this.title= title;
        this.releaseDate=releaseDate;
        this.overView= overView;
        this.posterUrl=posterUrl;
        this.voteAverage=voteAverage;
    }

    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        releaseDate = in.readString();
        overView = in.readString();
        posterUrl = in.readString();
        voteAverage = in.readDouble();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOverView() {
        return overView;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public int getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(releaseDate);
        dest.writeString(overView);
        dest.writeString(posterUrl);
        dest.writeDouble(voteAverage);
    }
}
