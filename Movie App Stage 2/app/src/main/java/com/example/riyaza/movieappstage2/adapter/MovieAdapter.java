package com.example.riyaza.movieappstage2.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.riyaza.movieappstage2.module.Movie;
import com.example.riyaza.movieappstage2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private static final String TAG = MovieAdapter.class.getSimpleName();
    final private ListItemClickListner mOnclickListner;
    ArrayList<Movie> mMovies = new ArrayList<Movie>();

    public MovieAdapter(ArrayList<Movie> movies, ListItemClickListner listner) {
        mMovies = movies;
        mOnclickListner=listner;
    }
    public interface ListItemClickListner{
        void onListItemClick(Movie movie);
    }

    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        MovieAdapter.MovieViewHolder viewHolder = new MovieAdapter.MovieViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieAdapter.MovieViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }


    @Override
    public int getItemCount() {
        return mMovies.size();
    }


    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView listItemNumberView;
        ImageView imageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.image);
            itemView.setOnClickListener(this);
        }
        void bind( int listIndex) {
            Movie currentMovie = mMovies.get(listIndex);
            Picasso.with(imageView.getContext())
                    .load(currentMovie.getPosterUrl())
                    .placeholder(R.drawable.plcimage)
                    .into(imageView);
        }

        @Override
        public void onClick(View v) {
          int clickPosition= getAdapterPosition();
          mOnclickListner.onListItemClick(mMovies.get(clickPosition));
        }
    }

    public void setMovieData(List<Movie> movieData) {
        mMovies = (ArrayList<Movie>) movieData;
        notifyDataSetChanged();
    }
}
