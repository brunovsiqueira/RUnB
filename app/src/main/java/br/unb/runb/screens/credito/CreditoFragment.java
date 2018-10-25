package br.unb.runb.screens.credito;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import br.unb.runb.R;

public class CreditoFragment extends Fragment {

    private View view;
    private TextView toolbarTitle;
    private Button loginButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_credito, container, false);

        //SE TIVER LOGADO, IR PARA TELA DE CRÉDITOS

        //SE NAO, AGUARDAR LOGIN
        findViewItems(view);

        return view;

    }

    private void findViewItems(View v) {

        toolbarTitle = v.findViewById(R.id.toolbar_container_title);
        loginButton = v.findViewById(R.id.login_button);

        loginButton.setOnClickListener(loginClickListener);
        toolbarTitle.setText("Login");

    }

    View.OnClickListener loginClickListener =  new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getActivity(), CardActivity.class));
        }
    };

}
