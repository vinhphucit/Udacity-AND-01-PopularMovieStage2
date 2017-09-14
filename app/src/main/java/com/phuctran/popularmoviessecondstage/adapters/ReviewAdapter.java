package com.phuctran.popularmoviessecondstage.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.phuctran.popularmoviessecondstage.R;
import com.phuctran.popularmoviessecondstage.models.ReviewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by phuctran on 9/9/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MovieViewHolder> {
    private static final String TAG = ReviewAdapter.class.getSimpleName();
    private List<ReviewModel> mMovieItems;
    private Context mContext;

    public ReviewAdapter(Context context, List<ReviewModel> mMovieItems) {
        this.mContext = context;
        this.mMovieItems = mMovieItems;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediatelly = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediatelly);
        MovieViewHolder viewHolder = new MovieViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mMovieItems.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvReview)
        TextView tvReview;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(int position) {
            final ReviewModel movieModel = mMovieItems.get(position);

            tvReview.setText(movieModel.getContent());
        }
    }
}
