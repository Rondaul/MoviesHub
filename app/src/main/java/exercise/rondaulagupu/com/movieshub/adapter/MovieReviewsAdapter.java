package exercise.rondaulagupu.com.movieshub.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import exercise.rondaulagupu.com.movieshub.R;
import exercise.rondaulagupu.com.movieshub.model.Review;

public class MovieReviewsAdapter extends RecyclerView.Adapter<MovieReviewsAdapter.ReviewsViewHolder> {

    private List<Review> mReviews;
    private Context mContext;

    public MovieReviewsAdapter(List<Review> reviews, Context context) {
        mReviews = reviews;
        mContext = context;
    }

    @NonNull
    @Override
    public MovieReviewsAdapter.ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_reviews, parent, false);
        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieReviewsAdapter.ReviewsViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_review_image)
        ImageView mReviewImageView;
        @BindView(R.id.tv_review_person)
        TextView mReviewPersionTextView;
        @BindView(R.id.tv_review)
        TextView mReviewTextView;

        public ReviewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(int position) {
            Review review = mReviews.get(position);

            ColorGenerator colorGenerator = ColorGenerator.MATERIAL;

            int color = colorGenerator.getRandomColor();

            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .withBorder(5)
                    .endConfig()
                    .buildRound(String.valueOf(review.getAuthor().charAt(0)), color);

            mReviewImageView.setImageDrawable(drawable);
            mReviewPersionTextView.setText(review.getAuthor());
            mReviewTextView.setText(review.getContent());
        }
    }
}
