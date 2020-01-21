package br.unb.runb.screens.cardapio.refeicoes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import br.unb.runb.R;

public class JantarFragment extends Fragment {

    private TextView nomeRefeicao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_refeicao, container, false);

        findViewItems(rootView);

        return rootView;
    }

    private void findViewItems(View v) {

        nomeRefeicao = v.findViewById(R.id.refeicao_nome);

        nomeRefeicao.setText("Jantar");

    }

}
