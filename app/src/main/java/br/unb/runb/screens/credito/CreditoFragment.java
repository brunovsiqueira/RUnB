package br.unb.runb.screens.credito;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;


import org.json.JSONException;
import org.json.JSONObject;

import br.unb.runb.R;
import br.unb.runb.models.User;
import br.unb.runb.util.UiFunctions;
import im.delight.android.webview.AdvancedWebView;


public class CreditoFragment extends Fragment {

    private View view;
    private TextView toolbarTitle;
    private EditText editUsername;
    private EditText editPassword;
    private Button loginButton;
    private AdvancedWebView webView;
    boolean loadFinished = false;
    private final String AUTHORIZE_URL = "https://homologaservicos.unb.br/dados/authorize";
    private ProgressBar progressBar;
    private boolean isSuccessful = false;

    private final String MY_CLIENT_SECRET = "CPD", MY_CLIENT_ID = "110";
    private Uri MY_REDIRECT_URI = Uri.parse("/ruapp/index.html");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_credito, container, false);

        //SE TIVER LOGADO, IR PARA TELA DE CRÉDITOS

        //SE NAO, AGUARDAR LOGIN
        findViewItems(view);
        isSuccessful = false;
        //verifyIfUserIsLoggedIn();

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

        //verify if token has expired. If yes, request a new one

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

            //makeAuth();

            progressBar.setVisibility(View.VISIBLE);
            //TODO: Save? username and password (encrypted and secret) to implement a fake autologin
            AndroidNetworking
                    .post("https://homologaservicos.unb.br/dados/authorize?grant_type={grant_type}&client_secret={client_secret}&client_id={client_id}&response_type=password&username={username}&password={password}")
                    .addPathParameter("grant_type", "password")
                    .addPathParameter("client_secret", MY_CLIENT_SECRET)
                    .addPathParameter("client_id", MY_CLIENT_ID)
                    .addPathParameter("username", editUsername.getText().toString())
                    .addPathParameter("password", editPassword.getText().toString())
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //"client": "public",
                            //    "access_token": "1noq6aHg1vTmmVGoJTSpP2dqkq0Tshj2",
                            //    "expires_in": 3600,
                            //    "resource_owner": {
                            //        "id": 201210118,
                            //        "remap_user_id": 58674,
                            //        "codigo": 586653,
                            //        "login": "160122791",
                            //        "name": "Giuliana da Cunha Facciolli",
                            //        "email": "giulianafacciolli@hotmail.com",
                            //        "type": 3,
                            //        "subtype": 2,
                            //        "active": true,
                            //        "cpf": "05604588199",
                            //        "lista_perfil": [],
                            //        "lista_permission": [],
                            //        "lista_perfil_permission": []
                            //    }
                            try {
                                JSONObject jsonObject = response.getJSONObject("resource_owner");
                                if (jsonObject.getBoolean("active")) {
                                    User.getInstance().setAccessToken(response.getString("access_token"));
                                    User.getInstance().setId(jsonObject.getString("id"));
                                    User.getInstance().setCodigo(jsonObject.getString("codigo"));
                                    User.getInstance().setMatricula(jsonObject.getString("login"));
                                    User.getInstance().setName(jsonObject.getString("name"));
                                    User.getInstance().setEmail(jsonObject.getString("email"));
                                    User.getInstance().setActive(jsonObject.getBoolean("active"));
                                    User.getInstance().setCpf(jsonObject.getString("cpf"));
                                } else {
                                    Dialog dialog = UiFunctions.showDilalog("Usuário sem vínculo ativo com a UnB", getContext());
                                    dialog.show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //TODO: testar com os 3 grupos de usuário e com usuário não ativo

                            progressBar.setVisibility(View.GONE);

                            if (User.getInstance().isActive()) {
                                startActivity(new Intent(getActivity(), CreditoActivity.class));
                                Toast.makeText(getContext(), "Bem vindo(a), " + User.getInstance().getName(), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            progressBar.setVisibility(View.GONE);
                            Dialog dialog = UiFunctions.showDilalog("Matrícula e/ou senha incorretas", getContext());
                            dialog.show();
                            Log.d("ERRO", anError.getErrorBody());
                        }
                    });

        }
    };


}
