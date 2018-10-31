package exercise.rondaulagupu.com.movieshub.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import exercise.rondaulagupu.com.movieshub.model.Movie;

@Dao
public interface MoviesDao {

    @Query("SELECT * FROM movie")
    LiveData<List<Movie>> getAllMovies();

    @Query("SELECT * FROM movie WHERE id = :id")
    LiveData<Movie> getMovieById(int id);

    @Insert
    void insertMovie(Movie movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);


}
