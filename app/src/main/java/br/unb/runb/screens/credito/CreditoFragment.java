package br.unb.runb.screens.credito;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import br.unb.runb.R;
import br.unb.runb.models.User;
import br.unb.runb.util.UiFunctions;
import ca.mimic.oauth2library.OAuth2Client;
import ca.mimic.oauth2library.OAuthError;
import ca.mimic.oauth2library.OAuthResponse;
import ca.mimic.oauth2library.OAuthResponseCallback;
import im.delight.android.webview.AdvancedWebView;
import okhttp3.OkHttpClient;

public class CreditoFragment extends Fragment {

    private View view;
    private TextView toolbarTitle;
    private EditText editUsername;
    private EditText editPassword;
    private Button loginButton;
    private AdvancedWebView webView;
    boolean loadFinished = false;
    private String authorizeUrl = "https://homologaservicos.unb.br/dados/authorize";
    private ProgressBar progressBar;
    private boolean isSuccessful = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_credito, container, false);

        //SE TIVER LOGADO, IR PARA TELA DE CRÉDITOS

        //SE NAO, AGUARDAR LOGIN
        findViewItems(view);
        verifyIfUserIsLoggedIn();

        return view;

    }

    private void findViewItems(View v) {

        toolbarTitle = v.findViewById(R.id.toolbar_container_title);
        loginButton = v.findViewById(R.id.login_button);
        editPassword = v.findViewById(R.id.login_password);
        editUsername = v.findViewById(R.id.login_matricula);
        progressBar = v.findViewById(R.id.determinateBar);

        loginButton.setOnClickListener(loginClickListener);
        toolbarTitle.setText("Login");

    }

    private void verifyIfUserIsLoggedIn() {

        progressBar.setVisibility(View.VISIBLE);
        if (User.getInstance() != null) {
            //if user.getInstance exists, then send user to next activity
            startActivity(new Intent(getContext(), CreditoActivity.class));
        } else {
            //else, wait for login
        }

    }


    View.OnClickListener loginClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            progressBar.setVisibility(View.VISIBLE);
            //TODO: Save? username and password (encrypted and secret) to implement a fake autologin
            OkHttpClient okHttpClient = new OkHttpClient();

            OAuth2Client client = new OAuth2Client.Builder(editUsername.getText().toString(), editPassword.getText().toString(), "92", "CPD", "https://homologaservicos.unb.br/dados/authorize")
                                    .okHttpClient(okHttpClient)
                                    .build();

            client.requestAccessToken(new OAuthResponseCallback() {
                @Override
                public void onResponse(OAuthResponse response) {
                    if (response.isSuccessful()) {
                        isSuccessful = true;
                        String accessToken = response.getAccessToken();
                        User.getInstance().setAccessToken(accessToken);
                        try {
                            JSONObject jsonObject = (new JSONObject(response.getBody())).getJSONObject("resource_owner");
                            User.getInstance().setId(jsonObject.getString("id"));
                            User.getInstance().setCodigo(jsonObject.getString("codigo"));
                            User.getInstance().setMatricula(jsonObject.getString("login"));
                            User.getInstance().setName(jsonObject.getString("name"));
                            User.getInstance().setEmail(jsonObject.getString("email"));
                            User.getInstance().setActive(jsonObject.getBoolean("active"));
                            User.getInstance().setCpf(jsonObject.getString("cpf"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        startActivity(new Intent(getActivity(), CreditoActivity.class));
                        Toast.makeText(getContext(), "Bem vindo(a), " + User.getInstance().getName(), Toast.LENGTH_LONG).show();
                    } else {
                        Integer statusCode = response.getCode();
                        OAuthError error = response.getOAuthError();
                        String errorMsg = error.getError();

                        if (!isSuccessful) {

                            new Handler(Looper.getMainLooper()).post(new Runnable(){
                                @Override
                                public void run() {
                                    Dialog dialog = UiFunctions.showDilalog("Matrícula e/ou senha incorretas", getContext());
                                    dialog.show();
                                }
                            });
                        }
                    }
                    new Handler(Looper.getMainLooper()).post(new Runnable(){
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                        }
                    });

                }
            });

//            AndroidNetworking
//                    .get("https://homologaservicos.unb.br/dados/authorize?response_type=password&username={username}&password={password}")
//                    .addPathParameter("username", editUsername.getText().toString())
//                    .addPathParameter("password", editPassword.getText().toString())
//                    .setPriority(Priority.HIGH)
//                    .build()
//                    .getAsJSONObject(new JSONObjectRequestListener() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            //"client": "public",
//                            //    "access_token": "1noq6aHg1vTmmVGoJTSpP2dqkq0Tshj2",
//                            //    "expires_in": 3600,
//                            //    "resource_owner": {
//                            //        "id": 201210118,
//                            //        "remap_user_id": 58674,
//                            //        "codigo": 586653,
//                            //        "login": "160122791",
//                            //        "name": "Giuliana da Cunha Facciolli",
//                            //        "email": "giulianafacciolli@hotmail.com",
//                            //        "type": 3,
//                            //        "subtype": 2,
//                            //        "active": true,
//                            //        "cpf": "05604588199",
//                            //        "lista_perfil": [],
//                            //        "lista_permission": [],
//                            //        "lista_perfil_permission": []
//                            //    }
//
//                            try {
//                                JSONObject jsonObject = response.getJSONObject("resource_owner");
//                                User.getInstance().setAccessToken(response.getString("access_token"));
//                                User.getInstance().setId(jsonObject.getString("id"));
//                                User.getInstance().setCodigo(jsonObject.getString("codigo"));
//                                User.getInstance().setMatricula(jsonObject.getString("login"));
//                                User.getInstance().setName(jsonObject.getString("name"));
//                                User.getInstance().setEmail(jsonObject.getString("email"));
//                                User.getInstance().setActive(jsonObject.getBoolean("active"));
//                                User.getInstance().setCpf(jsonObject.getString("cpf"));
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                            progressBar.setVisibility(View.GONE);
//                            startActivity(new Intent(getActivity(), CreditoActivity.class));
//                            Toast.makeText(getContext(), "Bem vindo(a), " + User.getInstance().getName(), Toast.LENGTH_LONG).show();
//                        }
//
//                        @Override
//                        public void onError(ANError anError) {
//                            progressBar.setVisibility(View.GONE);
//                            Dialog dialog = UiFunctions.showDilalog("Matrícula e/ou senha incorretas", getContext());
//                            dialog.show();
//                            Log.d("ERRO", anError.getErrorBody());
//                        }
//                    });

            //startActivity(new Intent(getActivity(), CreditoActivity.class));
        }
    };


}
