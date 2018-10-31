package exercise.rondaulagupu.com.movieshub.view_model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import java.util.List;

import exercise.rondaulagupu.com.movieshub.database.MovieDatabase;
import exercise.rondaulagupu.com.movieshub.model.Movie;

public class MainViewModel extends ViewModel {

    public static final String  TAG = MainViewModel.class.getSimpleName();

    private LiveData<Movie> movie;
    private LiveData<List<Movie>> movies;

    public MainViewModel(@NonNull MovieDatabase movieDatabase) {
        movies = movieDatabase.moviesDao().getAllMovies();
    }

    public MainViewModel(@NonNull MovieDatabase movieDatabase, int id) {
        movie = movieDatabase.moviesDao().getMovieById(id);
        movies = movieDatabase.moviesDao().getAllMovies();
    }

    public LiveData<Movie> getMovie() {
        return movie;
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }
}
