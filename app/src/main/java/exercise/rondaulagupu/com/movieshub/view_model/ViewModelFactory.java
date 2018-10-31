package exercise.rondaulagupu.com.movieshub.view_model;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import exercise.rondaulagupu.com.movieshub.database.MovieDatabase;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final MovieDatabase mMovieDatabase;
    private int mMovieId = -1;

    public ViewModelFactory(MovieDatabase movieDatabase, int movieId) {
        this.mMovieDatabase = movieDatabase;
        this.mMovieId = movieId;
    }

    public ViewModelFactory(MovieDatabase movieDatabase) {
        this.mMovieDatabase = movieDatabase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(mMovieId == -1) {
            return (T) new MainViewModel(mMovieDatabase);
        } else {
            return (T) new MainViewModel(mMovieDatabase, mMovieId);
        }
    }
}
