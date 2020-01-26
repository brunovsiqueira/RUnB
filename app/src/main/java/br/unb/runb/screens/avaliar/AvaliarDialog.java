package br.unb.runb.screens.avaliar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.unb.runb.R;
import br.unb.runb.models.Review;
import br.unb.runb.models.User;
import br.unb.runb.util.UiFunctions;

public class AvaliarDialog extends DialogFragment {

    private Spinner spinner;
    RatingBar ratingBar;
    Button sendButton;
    TextView textNome;
    TextView textAvaliacao;
    Context context;
    String refeicao;
    private String userName;
    private String comment;
    private ArrayList<Review> reviewList = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String [] groupArray = {"Café da Manhã", "Almoço", "Jantar"};

    public AvaliarDialog(String comment, ArrayList<Review> reviewList) {
        this.comment = comment;
        this.reviewList = reviewList;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_avaliar, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        userName = sharedPref.getString("user_name", null);

        //TODO: try to fix firebaseAuth: o usuário criado loga

        ratingBar = view.findViewById(R.id.rating_bar);
        spinner = view.findViewById(R.id.spinner_refeicao);
        sendButton = view.findViewById(R.id.button_send);
        textNome = view.findViewById(R.id.text_nome);
        textAvaliacao = view.findViewById(R.id.text_avaliacao);

        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, groupArray);
        spinner.setAdapter(spinnerArrayAdapter);
        spinner.setOnItemSelectedListener(spinnerSelectedListener);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeAtFirestore();
            }
        });

        if (userName != null) {
            textNome.setText(userName);
        } else {
            textNome.setText("Anônimo");
        }
        textAvaliacao.setText(comment);
        spinner.setSelection(1);

        builder.setView(view);
        return builder.create();
    }

    private void storeAtFirestore() {
        Map<String, Object> avaliacaoMap = new HashMap<>();
        avaliacaoMap.put("comentario", textAvaliacao.getText());
        avaliacaoMap.put("user", textNome.getText().toString());
        avaliacaoMap.put("refeicao", refeicao);
        avaliacaoMap.put("timestamp", FieldValue.serverTimestamp());
        avaliacaoMap.put("rate", ratingBar.getRating());


        db.collection("reviews").document()
                .set(avaliacaoMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            UiFunctions.showDilalog("Avaliação realizada com sucesso!", getContext()).show();
                            dismiss();
                            //TODO: add review to the list
                        }
                    }
                });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    AdapterView.OnItemSelectedListener spinnerSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            refeicao  = groupArray[position];
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

}
