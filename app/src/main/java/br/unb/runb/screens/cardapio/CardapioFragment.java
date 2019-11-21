package br.unb.runb.screens.cardapio;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import br.unb.runb.R;

public class CardapioFragment extends Fragment {

    private View view;
    private TextView toolbarTitle;
    private Spinner spinnerDia;
    private Spinner spinnerRefeicao;

    String[] dias = new String[]{"Seg - 8 OUT","Ter - 9 OUT","Qua - 10 OUT","Qui - 11 OUT","Sex - 12 OUT"};
    String[] refeicoes = new String[]{"Café da manhã", "Almoço", "Janta"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cardapio, container, false);

        findViewItems(view);

        return view;

    }

    private void findViewItems(View v) {

        toolbarTitle = v.findViewById(R.id.toolbar_container_title);
        spinnerDia = v.findViewById(R.id.cardapio_spinner_dia);
        spinnerRefeicao = v.findViewById(R.id.cardapio_spinner_refeicao);

        toolbarTitle.setText("Cardápio");

        ArrayAdapter<String> diasArray= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, dias);
        diasArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDia.setAdapter(diasArray);

        ArrayAdapter<String> refeicaoArray= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, refeicoes);
        refeicaoArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRefeicao.setAdapter(refeicaoArray);

    }

}