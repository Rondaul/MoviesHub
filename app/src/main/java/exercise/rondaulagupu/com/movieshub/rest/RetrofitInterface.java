package exercise.rondaulagupu.com.movieshub.rest;

import exercise.rondaulagupu.com.movieshub.model.CastResponse;
import exercise.rondaulagupu.com.movieshub.model.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Interface for retrotic to get data through api call.
 */
public interface RetrofitInterface {
    //method to get top_rated movies
    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    //method to get details of individual movies
    @GET("movie/{id}")
    Call<MoviesResponse> getMovieDetails(@Path(value = "id", encoded = true) int id, @Query("api_key") String apiKey);

    //method to get the most popular movies
    @GET("movie/popular")
    Call<MoviesResponse> getMostPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/{movie_id}/credits")
    Call<CastResponse> getCastOfMovie(@Path(value = "movie_id", encoded = true) int movieId, @Query("api_key") String apiKey);
}
