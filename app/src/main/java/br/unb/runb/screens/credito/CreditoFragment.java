package br.unb.runb.screens.credito;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import br.unb.runb.R;
import im.delight.android.webview.AdvancedWebView;

public class CreditoFragment extends Fragment {

    private View view;
    private TextView toolbarTitle;
    private Button loginButton;
    private AdvancedWebView webView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_credito, container, false);

        //SE TIVER LOGADO, IR PARA TELA DE CRÉDITOS

        //SE NAO, AGUARDAR LOGIN
        findViewItems(view);
        webView.loadUrl("https://homologaservicos.unb.br/dados/login/index.html?response_type=code&client_id=110&redirect_uri=/ruapp/index.html");

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();
        // ...
    }

    @Override
    public void onPause() {
        webView.onPause();
        // ...
        super.onPause();
    }

    @Override
    public void onDestroy() {
        webView.onDestroy();
        // ...
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        webView.onActivityResult(requestCode, resultCode, intent);
        // ...
    }

    public void showLoginScreen() {
//        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
//        webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl("https://homologaservicos.unb.br/dados/login/index.html?response_type=code&client_id=110&redirect_uri=/ruapp/index.html");

    }

    private void findViewItems(View v) {

        toolbarTitle = v.findViewById(R.id.toolbar_container_title);
//        loginButton = v.findViewById(R.id.login_button);
        webView = v.findViewById(R.id.webview_login);

//        loginButton.setOnClickListener(loginClickListener);
        toolbarTitle.setText("Login");

    }

    View.OnClickListener loginClickListener =  new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getActivity(), CreditoActivity.class));
        }
    };


}
