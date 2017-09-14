package com.phuctran.popularmoviessecondstage.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.phuctran.popularmoviessecondstage.BuildConfig;
import com.phuctran.popularmoviessecondstage.R;
import com.phuctran.popularmoviessecondstage.models.TrailerModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by phuctran on 9/9/17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MovieViewHolder> {
    private static final String TAG = TrailerAdapter.class.getSimpleName();
    private List<TrailerModel> mMovieItems;
    private Context mContext;
    final private TrailerListItemClickListener mOnClickListener;

    public interface TrailerListItemClickListener {
        void onTrailerListItemClick(TrailerModel movieModel);
    }

    public TrailerAdapter(Context context, List<TrailerModel> mMovieItems, TrailerListItemClickListener listener) {
        this.mContext = context;
        this.mMovieItems = mMovieItems;
        this.mOnClickListener = listener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.trailer_list_item;
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

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.ivThumbnail)
        ImageView ivThumbnail;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(int position) {
            final TrailerModel movieModel = mMovieItems.get(position);

            Picasso.with(mContext).load(String.format(BuildConfig.YOUTUBE_THUMBNAIL,movieModel.getKey())).into(ivThumbnail);
        }

        @Override
        public void onClick(View view) {
            int clickPosition = getAdapterPosition();
            mOnClickListener.onTrailerListItemClick(mMovieItems.get(clickPosition));
        }
    }
}
