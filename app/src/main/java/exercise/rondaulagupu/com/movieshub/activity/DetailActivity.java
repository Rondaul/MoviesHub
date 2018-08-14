package exercise.rondaulagupu.com.movieshub.activity;

import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import exercise.rondaulagupu.com.movieshub.R;
import exercise.rondaulagupu.com.movieshub.adapter.MoviesAdapter;

public class DetailActivity extends AppCompatActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //Butterknife view binding happens here.
        ButterKnife.bind(this);

        //reduce the alpha or transparency for background image.
        mMovieImageBackgroundImageView.setImageAlpha(128);

        //set toolbar as actionbar
        setSupportActionBar(mToolbar);

        //display the up button for back feature
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int id = getIntent().getExtras().getInt(MoviesAdapter.ID);
        String movieTitle = getIntent().getExtras().getString(MoviesAdapter.MOVIE_TITLE);
        String movieReleaseDate = getIntent().getExtras().getString(MoviesAdapter.MOVIE_RELEASE_DATE);
        String movieRating = getIntent().getExtras().getString(MoviesAdapter.MOVIE_RATING);
        String moviePlotSummary = getIntent().getExtras().getString(MoviesAdapter.MOVIE_PLOT_SUMMARY);
        String moviePosterPath = getIntent().getExtras().getString(MoviesAdapter.MOVIE_POSTER_PATH);

        //set the title in toolbar here.
        setTitle(movieTitle);

        //call methods showMovieDetails to set the data to the views.
        showMovieDetails(movieReleaseDate, movieRating, moviePlotSummary, moviePosterPath, movieTitle);
    }

    //method to set data to view in detail activity layout.
    private void showMovieDetails(String movieReleaseDate, String movieRating, String moviePlotSummary, String moviePosterPath, String movieTitle) {
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

        mMovieYearTextView.setText(movieReleaseDate);

        mMovieRatingTextView.setText(movieRating);

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
