package exercise.rondaulagupu.com.movieshub.activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import exercise.rondaulagupu.com.movieshub.R;
import exercise.rondaulagupu.com.movieshub.adapter.MoviesAdapter;
import exercise.rondaulagupu.com.movieshub.broadcast_receiver.NetworkBroadCastReceiver;
import exercise.rondaulagupu.com.movieshub.database.MovieDatabase;
import exercise.rondaulagupu.com.movieshub.model.Movie;
import exercise.rondaulagupu.com.movieshub.model.MoviesResponse;
import exercise.rondaulagupu.com.movieshub.rest.RetrofitClient;
import exercise.rondaulagupu.com.movieshub.rest.RetrofitInterface;
import exercise.rondaulagupu.com.movieshub.view_model.MainViewModel;
import exercise.rondaulagupu.com.movieshub.view_model.ViewModelFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = MainActivity.class.getSimpleName();

    // insert your themoviedb.org API KEY here
    public final static String API_KEY = "98c885575a2306f1fc392adc1dc8bbcf";

    //Butterknife view binding variables
    @BindView(R.id.moviesRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.bottom_sheet)
    LinearLayout mBottomSheetLayout;
    @BindView(R.id.rg_sort)
    RadioGroup sortRadioGroup;
    @BindView(R.id.rb_popular)
    RadioButton popularRadioButton;
    @BindView(R.id.rb_top_rated)
    RadioButton topRatedRadioButton;
    @BindView(R.id.favorites)
    RadioButton favoritesRadioButton;

    //BottomSheetBehavior variable for bottomsheet animation
    private BottomSheetBehavior sheetBehavior;

    //BroadcastReceiver for internet connection
    private NetworkBroadCastReceiver networkBroadCastReceiver;

    private MovieDatabase mMovieDatabase;
    private MainViewModel mMainViewModel;

    //Adapter for recyclerview
    MoviesAdapter moviesAdapter;

    @Override
    protected void onResume() {
        super.onResume();
        //Create an broadcast receiver object and register the receiver here
        networkBroadCastReceiver = new NetworkBroadCastReceiver();

        //intentfilter is added for the connectivity_ action
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        //register the receiver and pass the intentFilter along with it.
        registerReceiver(networkBroadCastReceiver,intentFilter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Butterknife viewbinding done here.
        ButterKnife.bind(this);

        if (API_KEY.length() == 0) {
            Toast.makeText(getApplicationContext(), "Please obtain your API KEY from themoviedb.org first!", Toast.LENGTH_LONG).show();
            return;
        }

        mMovieDatabase = MovieDatabase.getInstance(MainActivity.this);
        mMainViewModel = ViewModelProviders.of(this, new ViewModelFactory(mMovieDatabase)).get(MainViewModel.class);

        //Initialize the sheetBehavior for bottom sheet animation here.
        sheetBehavior = BottomSheetBehavior.from(mBottomSheetLayout);

        //callbacks for bottom sheet behavior
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        //if orientation is portrait set grid = 2. If orientation is landscape set grid = 4
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }
        else{
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        //Retrofit object initialization
        final RetrofitInterface retrofitInterface =
                RetrofitClient.getRetrofitClient().create(RetrofitInterface.class);

        //retrofit call to get Most popular movies.
        Call<MoviesResponse> call = retrofitInterface.getMostPopularMovies(API_KEY);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                List<Movie> movies = response.body().getResults();
                Log.d(TAG, "Number of movies received: " + movies.size());
                moviesAdapter = new MoviesAdapter(movies, MainActivity.this);
                mRecyclerView.setAdapter(moviesAdapter);
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });

        //radio button for selecting between most popular and highest rated movies
        sortRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_popular) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    popularRadioButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check, 0);
                    topRatedRadioButton.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    favoritesRadioButton.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    Call<MoviesResponse> call = retrofitInterface.getMostPopularMovies(API_KEY);
                    call.enqueue(new Callback<MoviesResponse>() {
                        @Override
                        public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                            List<Movie> movies = response.body().getResults();
                            Log.d(TAG, "Number of movies received: " + movies.size());
                            moviesAdapter = new MoviesAdapter(movies, MainActivity.this);
                            moviesAdapter.notifyDataSetChanged();
                            mRecyclerView.setAdapter(moviesAdapter);
                        }

                        @Override
                        public void onFailure(Call<MoviesResponse> call, Throwable t) {
                            Log.e(TAG, t.toString());
                        }
                    });
                } else if (checkedId == R.id.rb_top_rated) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    topRatedRadioButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check, 0);
                    popularRadioButton.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    favoritesRadioButton.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    Call<MoviesResponse> call = retrofitInterface.getTopRatedMovies(API_KEY);
                    call.enqueue(new Callback<MoviesResponse>() {
                        @Override
                        public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                            List<Movie> movies = response.body().getResults();
                            Log.d(TAG, "Number of movies received: " + movies.size());
                            moviesAdapter = new MoviesAdapter(movies, MainActivity.this);
                            moviesAdapter.notifyDataSetChanged();
                            mRecyclerView.setAdapter(moviesAdapter);
                        }

                        @Override
                        public void onFailure(Call<MoviesResponse> call, Throwable t) {
                            Log.e(TAG, t.toString());
                        }
                    });
                } else if(checkedId == R.id.favorites) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    favoritesRadioButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check, 0);
                    popularRadioButton.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    topRatedRadioButton.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    LiveData<List<Movie>> movies = mMainViewModel.getMovies();
                    movies.observe(MainActivity.this, new Observer<List<Movie>>() {
                        @Override
                        public void onChanged(@Nullable List<Movie> movies) {
                            if(movies.size() == 0) {
                                Toast.makeText(getApplicationContext(), "No items added to favorites!", Toast.LENGTH_SHORT).show();
                            }
                            moviesAdapter = new MoviesAdapter(movies, MainActivity.this);
                            moviesAdapter.notifyDataSetChanged();
                            mRecyclerView.setAdapter(moviesAdapter);
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedId = item.getItemId();
        switch (selectedId) {
            case R.id.sort:
                //toggle the bottomsheet to expanded and hidden.
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //unregister broadcast receiver for network connectivity here.
        unregisterReceiver(networkBroadCastReceiver);
    }
}
