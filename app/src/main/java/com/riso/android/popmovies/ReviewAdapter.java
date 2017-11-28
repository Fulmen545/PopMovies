package com.riso.android.popmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by richard.janitor on 28-Nov-17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private static final String TAG = ReviewAdapter.class.getSimpleName();

    private List<ReviewUrl> mReviewItems;
    final private TrailerAdapter.ListItemClickListener mOnClickListener;

    public ReviewAdapter(List<ReviewUrl> mReviewItems, TrailerAdapter.ListItemClickListener mOnClickListener) {
        this.mReviewItems = mReviewItems;
        this.mOnClickListener = mOnClickListener;
    }


    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        ReviewAdapter.ReviewViewHolder viewHolder = new ReviewAdapter.ReviewViewHolder(view);
        Log.d(TAG, "onCreateViewHolder: number of ViewHolders created: "
                + mReviewItems.size());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ReviewViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mReviewItems.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView contentItemView;
        TextView authorItemView;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            contentItemView = (TextView) itemView.findViewById(R.id.tv_review);
            authorItemView = (TextView) itemView.findViewById(R.id.author);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            contentItemView.setText(mReviewItems.get(listIndex).content);
            authorItemView.setText(mReviewItems.get(listIndex).author);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
