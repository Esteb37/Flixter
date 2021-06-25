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

    //Current context
    Context context;

    //List of movie objects
    List<Movie> movies;

    //Listener for clicked movie item
    public interface OnClickListener { void onItemClicked(int position);}
    OnClickListener clickListener;

    //Listener for recyclerview scrolling
    public interface OnScrollListener { void onScroll(int position);}
    OnScrollListener scrollListener;

    //Adapter constructor from an activity context, click listener and scroll listener
    public MovieAdapter(Context context, List<Movie> movies, OnClickListener onClickListener, OnScrollListener scrollListener) {

        //Set adapter attributes
        this.context = context;
        this.movies = movies;
        this.clickListener = onClickListener;
        this.scrollListener = scrollListener;
    }

    @NotNull
    @Override
    //Inflates the layout from XML and returns the holder
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        //Inflate the movie item and get the view
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);

        //Return the holder
        return new ViewHolder(movieView);
    }

    //Populates data into the item through the holder
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        //Get the movie item at selected position
        Movie movie = movies.get(position);

        //Bind the movie item to the viewholder
        holder.bind(movie);

        //Send position information to the scroll listener
        if (scrollListener != null)  scrollListener.onScroll(position);
    }

    //Get amount of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //Movie View layout elements
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;
        ImageView ivBackdrop;
        RatingBar rbRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Get the layout items
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            rbRating = itemView.findViewById(R.id.rbRating3);
            ivBackdrop = itemView.findViewById(R.id.ivBackdrop);
        }

        //Set movie information into the movie view
        public void bind(Movie movie) {

            //Set movie information
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            float rating = (float) movie.getRating(5);
            rbRating.setRating(rating);

            //Get movie poster if in portrait, and backdrop poster if in landscape
            String imageURL;
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageURL = movie.getBackdropPath();

            } else {
                imageURL = movie.getPosterPath();
            }

            //Set selected poster into the image view with round corners and a placeholder
            Glide.with(context).load(imageURL)
                    .placeholder(R.mipmap.placeholder)
                    .transform(new FitCenter(), new RoundedCorners(30))
                    .error(R.mipmap.placeholder)
                    .into(ivPoster);

            //Set semitransparent backdrop as background of movie item with
            Glide.with(context).load(movie.getBackdropPath())
                    .placeholder(R.mipmap.placeholder)
                    .error(R.mipmap.placeholder)
                    .into(ivBackdrop);

            //Set an onclick listener on the poster to open Movie Details
            ivPoster.setOnClickListener(v -> clickListener.onItemClicked(getAdapterPosition()));
        }
    }
}
