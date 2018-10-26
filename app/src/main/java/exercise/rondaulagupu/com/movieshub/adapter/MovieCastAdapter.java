package exercise.rondaulagupu.com.movieshub.adapter;

import android.content.Context;
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
import exercise.rondaulagupu.com.movieshub.model.Cast;

public class MovieCastAdapter extends RecyclerView.Adapter<MovieCastAdapter.MyMovieCastViewHolder> {

    public static final String POSTER_BASE_PATH = "http://image.tmdb.org/t/p/w342";

    List<Cast> mCasts;
    Context mContext;

    public MovieCastAdapter(List<Cast> casts, Context context) {
        mCasts = casts;
        mContext = context;
    }

    @NonNull
    @Override
    public MovieCastAdapter.MyMovieCastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_cast, parent, false);
        return new MyMovieCastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieCastAdapter.MyMovieCastViewHolder holder, int position) {
                holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mCasts.size();
    }

    public class MyMovieCastViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_movie_cast)
        ImageView mMovieCastImageView;
        @BindView(R.id.tv_cast_title)
        TextView mCastTitleTextView;
        @BindView(R.id.tv_cast_character)
        TextView mCastCharacterTextView;
        public MyMovieCastViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(int position) {
            Cast cast = mCasts.get(position);
            Picasso
                    .get()
                    .load(POSTER_BASE_PATH + cast.getProfilePath())
                    .error(R.drawable.image_not_available)
                    .into(mMovieCastImageView);
            mCastTitleTextView.setText(cast.getName());
            mCastCharacterTextView.setText("AS " + cast.getCharacter());
        }
    }
}
