package com.example.riyaza.movieappstage2.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.riyaza.movieappstage2.R;
import com.example.riyaza.movieappstage2.module.Movie;
import com.example.riyaza.movieappstage2.module.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {

    private static final String TAG = ReviewAdapter.class.getSimpleName();
    ArrayList<Review> mReview = new ArrayList<Review>();


    public ReviewAdapter( ArrayList<Review> mReview) {
        this.mReview = mReview;
    }


    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        ReviewHolder viewHolder = new ReviewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);

    }

    @Override
    public int getItemCount() {
        return mReview.size();
    }


    public class ReviewHolder extends RecyclerView.ViewHolder {
        private TextView authorName;
        private TextView reviewContent;


        public ReviewHolder(View view) {
            super(view);
            authorName = (TextView) view.findViewById(R.id.author_tv);
            reviewContent = (TextView) view.findViewById(R.id.content_tv);

        }

        public void bind(int position) {
            Review currentReviw = mReview.get(position);
            if(currentReviw != null) {

                authorName.setText(currentReviw.getmAuthor());
                reviewContent.setText(currentReviw.getmContent());
                String name = currentReviw.getmAuthor();
                Log.i(" Name","review Name" + name);
            }

        }
    }
    public void setReviewData(List<Review> reviewData) {
        mReview = (ArrayList<Review>) reviewData;
        notifyDataSetChanged();
    }
}
