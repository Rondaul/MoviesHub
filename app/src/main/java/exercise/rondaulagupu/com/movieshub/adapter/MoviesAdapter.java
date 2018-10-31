package exercise.rondaulagupu.com.movieshub.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import exercise.rondaulagupu.com.movieshub.R;
import exercise.rondaulagupu.com.movieshub.activity.DetailActivity;
import exercise.rondaulagupu.com.movieshub.model.Movie;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    //static final variables
    public static final String POSTER_BASE_PATH = "http://image.tmdb.org/t/p/w342";
    public static final String MOVIE_TITLE = "movie_title";
    public static final String ID = "id";
    public static final String MOVIE_RELEASE_DATE = "movie_release_date";
    public static final String MOVIE_RATING = "movie_rating";
    public static final String MOVIE_PLOT_SUMMARY = "movie_plot_summary";
    public static final String MOVIE_POSTER_PATH = "movie_poster_path";

    private final List<Movie> movies;
    private final Context mContext;

    public MoviesAdapter(List<Movie> movies, Context context) {
        this.movies = movies;
        mContext = context;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate the list item layout here.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bind(position);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        //ButterKnife view binding variables
        @BindView(R.id.tv_movie_thumbnail)
        ImageView movieImage;
        @BindView(R.id.tv_movie_title)
        TextView movieTitle;
        @BindView(R.id.tv_movie_year)
        TextView movieYear;
        @BindView(R.id.tv_movie_rating)
        TextView movieRating;
        @BindView(R.id.card_view)
        CardView cardView;

        private MovieViewHolder(View itemView) {
            super(itemView);
            //Butterknife view binding done here
            ButterKnife.bind(this, itemView);

            //onclick listener for each card or movie item
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //setting shared transition options
                    ActivityOptionsCompat options = ActivityOptionsCompat
                            .makeSceneTransitionAnimation((Activity) mContext,
                                    movieImage,
                                    ViewCompat.getTransitionName(movieImage));
                    int position = getAdapterPosition();
                    Movie movie = movies.get(position);
                    //create bundle and send the data in it.
                    Bundle bundle = new Bundle();
                    bundle.putInt(ID, movie.getId());
                    bundle.putString(MOVIE_TITLE, movie.getTitle());
                    bundle.putString(MOVIE_RELEASE_DATE, movie.getReleaseDate());
                    bundle.putDouble(MOVIE_RATING, movie.getVoteAverage());
                    bundle.putString(MOVIE_PLOT_SUMMARY, movie.getOverview());
                    bundle.putString(MOVIE_POSTER_PATH, movie.getPosterPath());
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtras(bundle);
                    //start activity with intent and also pass shared element transitions
                    mContext.startActivity(intent, options.toBundle());
                }
            });
        }

        //set the data to the views here.
        private void bind(int position) {
            final Movie movie = movies.get(position);
            Log.d("Image Url", movie.getPosterPath());
            Picasso
                    .get()
                    .load(POSTER_BASE_PATH + movie.getPosterPath())
                    .error(R.drawable.image_not_available)
                    .placeholder(R.drawable.image_not_available)
                    .into(movieImage);

            movieTitle.setText(movie.getTitle());
            movieYear.setText(movie.getReleaseDate().substring(0, 4));
            movieRating.setText(movie.getVoteAverage().toString());
        }
    }
}
