package br.unb.runb.screens.avaliar.list;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import br.unb.runb.R;

class ReviewViewHolder extends RecyclerView.ViewHolder {

    TextView textName;
    RatingBar ratingBar;
    TextView textReview;
    TextView textDate;

    public ReviewViewHolder(@NonNull View itemView) {
        super(itemView);

        textName = itemView.findViewById(R.id.review_name);
        ratingBar = itemView.findViewById(R.id.review_ratingbar);
        textReview = itemView.findViewById(R.id.review_review);
        textDate = itemView.findViewById(R.id.review_data);
    }
}