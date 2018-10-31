package exercise.rondaulagupu.com.movieshub.activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import exercise.rondaulagupu.com.movieshub.R;
import exercise.rondaulagupu.com.movieshub.adapter.MovieCastAdapter;
import exercise.rondaulagupu.com.movieshub.adapter.MovieReviewsAdapter;
import exercise.rondaulagupu.com.movieshub.adapter.MovieTrailerAdapter;
import exercise.rondaulagupu.com.movieshub.adapter.MoviesAdapter;
import exercise.rondaulagupu.com.movieshub.database.MovieDatabase;
import exercise.rondaulagupu.com.movieshub.model.Cast;
import exercise.rondaulagupu.com.movieshub.model.CastResponse;
import exercise.rondaulagupu.com.movieshub.model.Movie;
import exercise.rondaulagupu.com.movieshub.model.Review;
import exercise.rondaulagupu.com.movieshub.model.ReviewsResponse;
import exercise.rondaulagupu.com.movieshub.model.Trailer;
import exercise.rondaulagupu.com.movieshub.model.TrailersResponse;
import exercise.rondaulagupu.com.movieshub.rest.RetrofitClient;
import exercise.rondaulagupu.com.movieshub.rest.RetrofitInterface;
import exercise.rondaulagupu.com.movieshub.view_model.MainViewModel;
import exercise.rondaulagupu.com.movieshub.view_model.ViewModelFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();

    //Butterknife view binding variables
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.tv_movie_title_details)
    TextView mMovieTitleTextView;
    @BindView(R.id.tv_movie_year_details)
    TextView mMovieYearTextView;
    @BindView(R.id.tv_movie_rating_details)
    TextView mMovieRatingTextView;
    @BindView(R.id.tv_plot_summary_details)
    TextView mPlotSummaryTextView;
    @BindView(R.id.movie_image_background_details)
    ImageView mMovieImageBackgroundImageView;
    @BindView(R.id.iv_movie_poster_details)
    ImageView mMoviePosterImageView;
    @BindView(R.id.rv_movie_cast)
    RecyclerView mMovieCastRecyclerView;
    @BindView(R.id.rv_movie_trailers)
    RecyclerView mMovieTrailersRecyclerView;
    @BindView(R.id.rv_movie_reviews)
    RecyclerView mMovieReviewsRecyclerView;
    @BindView(R.id.like_button)
    LikeButton mLikeButton;
    @BindView(R.id.share)
    ImageView mShareImageView;

    private MovieCastAdapter mMovieCastAdapter;
    private MovieTrailerAdapter mMovieTrailerAdapter;
    private MovieReviewsAdapter mMovieReviewsAdapter;

    private MovieDatabase mMovieDatabase;
    private MainViewModel mMainViewModel;

    private RetrofitInterface retrofitInterface;

    private List<Trailer> trailers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //Butterknife view binding happens here.
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        //display the up button for back feature
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int id = getIntent().getExtras().getInt(MoviesAdapter.ID);
        String movieTitle = getIntent().getExtras().getString(MoviesAdapter.MOVIE_TITLE);
        String movieReleaseDate = getIntent().getExtras().getString(MoviesAdapter.MOVIE_RELEASE_DATE);
        Double movieRating = getIntent().getExtras().getDouble(MoviesAdapter.MOVIE_RATING);
        String moviePlotSummary = getIntent().getExtras().getString(MoviesAdapter.MOVIE_PLOT_SUMMARY);
        String moviePosterPath = getIntent().getExtras().getString(MoviesAdapter.MOVIE_POSTER_PATH);

        mMovieDatabase = MovieDatabase.getInstance(DetailActivity.this);
        mMainViewModel = ViewModelProviders.of(this, new ViewModelFactory(mMovieDatabase, id)).get(MainViewModel.class);
        mMovieCastRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mMovieTrailersRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mMovieReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        //reduce the alpha or transparency for background image.
        mMovieImageBackgroundImageView.setImageAlpha(128);

        //set toolbar as actionbar
        setSupportActionBar(mToolbar);

        final Movie movie = new Movie(id, moviePosterPath, moviePlotSummary, movieReleaseDate, movieTitle, movieRating);

        LiveData<Movie> movieLiveData = mMainViewModel.getMovie();
        movieLiveData.observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                if(movie != null) {
                    mLikeButton.setLiked(true);
                } else {
                    mLikeButton.setLiked(false);
                }
            }
        });

        mShareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(trailers.size() != 0) {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");
                    i.putExtra(Intent.EXTRA_TEXT, MovieTrailerAdapter.YOUTUBE_URL + trailers.get(0).getKey());
                    startActivity(Intent.createChooser(i, "Share URL"));
                }
            }
        });

        mLikeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        mMovieDatabase.moviesDao().insertMovie(movie);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(DetailActivity.this, "Added to favorites!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        mMovieDatabase.moviesDao().deleteMovie(movie);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(DetailActivity.this, "Deleted from favorites!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        });

        //set the title in toolbar here.
        setTitle(movieTitle);


        //call methods showMovieDetails to set the data to the views.
        showMovieDetails(movieReleaseDate, movieRating, moviePlotSummary, moviePosterPath, movieTitle);

        retrofitInterface = RetrofitClient.getRetrofitClient().create(RetrofitInterface.class);
        Call<CastResponse> call = retrofitInterface.getCastOfMovie(id, MainActivity.API_KEY);
        call.enqueue(new Callback<CastResponse>() {
            @Override
            public void onResponse(Call<CastResponse> call, Response<CastResponse> response) {
                List<Cast> casts = response.body().getCast();
                mMovieCastAdapter = new MovieCastAdapter(casts, DetailActivity.this);
                mMovieCastRecyclerView.setAdapter(mMovieCastAdapter);

            }

            @Override
            public void onFailure(Call<CastResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });


        Call<TrailersResponse> trailersResponseCall = retrofitInterface.getMovieTrailers(id, MainActivity.API_KEY);
        trailersResponseCall.enqueue(new Callback<TrailersResponse>() {
            @Override
            public void onResponse(Call<TrailersResponse> call, Response<TrailersResponse> response) {
                trailers = response.body().getResults();
                mMovieTrailerAdapter = new MovieTrailerAdapter(trailers, DetailActivity.this);
                mMovieTrailersRecyclerView.setAdapter(mMovieTrailerAdapter);
            }

            @Override
            public void onFailure(Call<TrailersResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
            }
        });

        Call<ReviewsResponse> reviewsResponseCall = retrofitInterface.getMovieReviews(id, MainActivity.API_KEY);
        reviewsResponseCall.enqueue(new Callback<ReviewsResponse>() {
            @Override
            public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {
                List<Review> reviews = response.body().getResults();
                mMovieReviewsAdapter = new MovieReviewsAdapter(reviews, DetailActivity.this);
                mMovieReviewsRecyclerView.setAdapter(mMovieReviewsAdapter);
            }

            @Override
            public void onFailure(Call<ReviewsResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
            }
        });

    }

    //method to set data to view in detail activity layout.
    private void showMovieDetails(String movieReleaseDate, Double movieRating, String moviePlotSummary, String moviePosterPath, String movieTitle) {
        Picasso
                .get()
                .load(MoviesAdapter.POSTER_BASE_PATH + moviePosterPath)
                .error(R.drawable.image_not_available)
                .placeholder(R.drawable.image_not_available)
                .into(mMoviePosterImageView);

        Picasso
                .get()
                .load(MoviesAdapter.POSTER_BASE_PATH + moviePosterPath)
                .error(R.drawable.image_not_available)
                .placeholder(R.drawable.image_not_available)
                .into(mMovieImageBackgroundImageView);

        mMovieTitleTextView.setText(movieTitle);

        mMovieYearTextView.setText(movieReleaseDate.substring(0, 4));

        mMovieRatingTextView.setText(movieRating.toString());

        mPlotSummaryTextView.setText(moviePlotSummary);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
