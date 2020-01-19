package br.unb.runb.screens.avaliar;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import br.unb.runb.R;
import br.unb.runb.models.Review;
import br.unb.runb.screens.avaliar.list.ReviewAdapter;

public class AvaliarFragment extends Fragment {

    private View view;
    private Button sendButton;
    private EditText editAvaliar;
    private RecyclerView recyclerAvaliacoes;
    private ReviewAdapter reviewAdapter;
    private TextView toolbarTitle;
    private ContentLoadingProgressBar contentLoadingProgressBar;
    private ArrayList<Review> reviewList = new ArrayList<>();

    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_avaliar, container, false);

        sendButton = view.findViewById(R.id.button_send);
        editAvaliar = view.findViewById(R.id.edit_avaliar);
        recyclerAvaliacoes = view.findViewById(R.id.recyclerview_avaliacoes);
        recyclerAvaliacoes.setLayoutManager(new LinearLayoutManager(getContext()));
        contentLoadingProgressBar = view.findViewById(R.id.progress_bar);
        toolbarTitle = view.findViewById(R.id.toolbar_container_title);

        toolbarTitle.setText("Avaliação");

        contentLoadingProgressBar.show();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editAvaliar.getText().toString().length() > 2) {
                    AvaliarDialog avaliarDialog;
                    avaliarDialog = new AvaliarDialog(editAvaliar.getText().toString());
                    avaliarDialog.show(getFragmentManager(), "");
                } else {
                    Toast.makeText(getContext(), "A avaliação precisa ser mais longa", Toast.LENGTH_SHORT).show();

                }
            }
        });

        getReviews();

        return view;
    }

    private void getReviews() {
        db.collection("reviews")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            reviewList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("CATEGORIA", document.getId() + " => " + document.getData());

                                Review review = new Review(
                                        document.getString("comentario"),
                                        document.getString("user"),
                                        document.getString("refeicao"),
                                        new Date(document.getTimestamp("timestamp").getSeconds()*1000),
                                        document.getDouble("rate"));

                                reviewList.add(review);

                            }

                            Collections.reverse(reviewList);
                            showOnRecyclerView();

                        } else {
                            Log.d("REVIEWS", "Error getting documents: ", task.getException());
                        }
                        contentLoadingProgressBar.hide();
                    }
                });
    }

    private void showOnRecyclerView() {
        reviewAdapter = new ReviewAdapter(getContext(), reviewList);
        recyclerAvaliacoes.setAdapter(reviewAdapter);
        reviewAdapter.notifyDataSetChanged();
    }



}
