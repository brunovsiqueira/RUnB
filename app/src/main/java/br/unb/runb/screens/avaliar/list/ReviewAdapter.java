package br.unb.runb.screens.avaliar.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import br.unb.runb.R;
import br.unb.runb.models.Review;

public class ReviewAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<Review> reviewList;

    public ReviewAdapter(Context context, ArrayList<Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_item, viewGroup, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Review review = reviewList.get(i);
        ReviewViewHolder reviewViewHolder = (ReviewViewHolder) viewHolder;

        if (review != null) {
            reviewViewHolder.textName.setText(review.getUser());
            Date date = review.getDate();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String minutes = String.valueOf(date.getMinutes());
            if (minutes.length() == 1) {
                minutes = "0" + minutes;
            }
            reviewViewHolder.textDate.setText(sdf.format(date) + " as " + date.getHours() + ":" + minutes);
            reviewViewHolder.ratingBar.setRating((float)review.getRate());
            reviewViewHolder.textReview.setText(review.getComentario());
            reviewViewHolder.textRefeicao.setText("("+review.getRefeicao()+")");

        }
    }

    @Override
    public int getItemCount() {
        return this.reviewList.size();
    }
}
