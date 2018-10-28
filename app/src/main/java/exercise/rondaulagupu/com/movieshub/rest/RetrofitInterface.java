package exercise.rondaulagupu.com.movieshub.rest;

import exercise.rondaulagupu.com.movieshub.model.CastResponse;
import exercise.rondaulagupu.com.movieshub.model.MoviesResponse;

import exercise.rondaulagupu.com.movieshub.model.ReviewsResponse;
import exercise.rondaulagupu.com.movieshub.model.TrailersResponse;
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

    //method to get the cast of the movie using id
    @GET("movie/{movie_id}/credits")
    Call<CastResponse> getCastOfMovie(@Path(value = "movie_id", encoded = true) int movieId, @Query("api_key") String apiKey);

    //method to get the trailers of the movie
    @GET("movie/{id}/videos")
    Call<TrailersResponse> getMovieTrailers(@Path(value = "id", encoded = true) int id , @Query("api_key") String apiKey);

    //method to get the reviews of the movie
    @GET("movie/{id}/reviews")
    Call<ReviewsResponse> getMovieReviews(@Path(value = "id", encoded = true) int id , @Query("api_key") String apiKey);
}
