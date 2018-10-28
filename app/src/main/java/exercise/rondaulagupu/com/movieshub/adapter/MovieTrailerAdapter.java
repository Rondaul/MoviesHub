package exercise.rondaulagupu.com.movieshub.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
import exercise.rondaulagupu.com.movieshub.model.Trailer;

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.MovieTrailerViewHolder> {

    public static final String YOUTUBE_THUMBNAIL = "https://img.youtube.com/vi/%s/mqdefault.jpg";
    public static final String YOUTUBE_URL = "http://www.youtube.com/watch?v=";

    private List<Trailer> mTrailers;
    private Context mContext;

    public MovieTrailerAdapter(List<Trailer> trailers, Context context) {
        mTrailers = trailers;
        mContext = context;
    }

    @NonNull
    @Override
    public MovieTrailerAdapter.MovieTrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_trailers, parent, false);
        return new MovieTrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieTrailerAdapter.MovieTrailerViewHolder holder, int position) {
        holder.bind(position);

    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    public class MovieTrailerViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_trailer_title)
        TextView mTrailerTitleTextView;
        @BindView(R.id.iv_movie_trailer)
        ImageView mTrailerImageView;

        public MovieTrailerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(int position) {
            final Trailer trailer = mTrailers.get(position);
            Picasso
                    .get()
                    .load(String.format(YOUTUBE_THUMBNAIL, trailer.getKey()))
                    .error(R.drawable.image_not_available)
                    .into(mTrailerImageView);

            mTrailerTitleTextView.setText(trailer.getName());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_URL + trailer.getKey()));
                    mContext.startActivity(intent);
                }
            });

        }
    }
}
