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
import com.example.riyaza.movieappstage2.module.Trailer;

import java.util.ArrayList;
import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerHolder> {
    private static final String TAG = Trailer.class.getSimpleName();
    ArrayList<Trailer> trailers= new ArrayList<Trailer>();
    final private ListItemClickListner mOnclickListner;


    public TrailerAdapter(ArrayList<Trailer> trailers, ListItemClickListner mOnclickListner){
        this.trailers=trailers;
        this.mOnclickListner = mOnclickListner;
    }

    public interface ListItemClickListner{
        void onListItemClick(Trailer trailer);
    }
    @NonNull
    @Override
    public TrailerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        TrailerHolder viewHolder = new TrailerHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);

    }

    @Override
    public int getItemCount() {

        Log.i("Trailer Size  ", String.valueOf(trailers.size()));
        return trailers.size();
    }

    public class TrailerHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView trailerName;

        public TrailerHolder(View itemView) {
            super(itemView);

            trailerName= (TextView) itemView.findViewById(R.id.trailer_tv);
            itemView.setOnClickListener(this);

        }

        public void bind(int position) {
            Trailer currentTrailer= trailers.get(position);
            if (currentTrailer !=null){
//                String name= currentTrailer.getname();
//                Log.i("Name ","Name  "+ name );
                trailerName.setText(currentTrailer.getname());

            }
        }

        @Override
        public void onClick(View v) {
            int clickPosition= getAdapterPosition();
            mOnclickListner.onListItemClick(trailers.get(clickPosition));
        }
    }

    public void setTrailerData(List<Trailer> trailerData) {
        trailers = (ArrayList<Trailer>) trailerData;
        notifyDataSetChanged();
    }
}