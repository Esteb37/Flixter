package com.example.flixter.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.flixter.R;
import com.example.flixter.models.Movie;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    Context context;
    List<Movie> movies;

    //Click listener
    public interface OnClickListener {
        void onItemClicked(int position);
    }


    OnClickListener clickListener;

    public interface OnScrollListener {
        void onScroll(int position);
    }

    OnScrollListener scrollListener;

    public MovieAdapter(Context context, List<Movie> movies, OnClickListener onClickListener, OnScrollListener scrollListener) {
        this.context = context;
        this.movies = movies;
        this.clickListener = onClickListener;
        this.scrollListener = scrollListener;
    }


    @NonNull
    @NotNull
    @Override
    //Inflates the layout from XML and returns the holder
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);

        return new ViewHolder(movieView);
    }

    //Populates data into the item through the holder
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        //Get the movie at position
        Movie movie = movies.get(position);
        //
        holder.bind(movie);
        if (scrollListener != null)
            scrollListener.onScroll(position);
    }

    //Get amount of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;
        ImageView ivBackdrop;
        RatingBar rbRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            rbRating = itemView.findViewById(R.id.rbRating3);
            ivBackdrop = itemView.findViewById(R.id.ivBackdrop);
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            String imageURL;
            float rating;
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageURL = movie.getBackdropPath();

            } else {
                imageURL = movie.getPosterPath();
            }
            rating = (float) movie.getRating(5);
            rbRating.setRating(rating);
            Glide.with(context).load(imageURL)
                    .placeholder(R.mipmap.placeholder)
                    .transform(new FitCenter(), new RoundedCorners(30))
                    .error(R.mipmap.placeholder)
                    .into(ivPoster);
            Glide.with(context).load(movie.getBackdropPath())
                    .placeholder(R.mipmap.placeholder)
                    .error(R.mipmap.placeholder)
                    .into(ivBackdrop);

            ivPoster.setOnClickListener(v -> clickListener.onItemClicked(getAdapterPosition()));

        }
    }


}
