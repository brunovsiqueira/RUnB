package br.unb.runb.screens.credito;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import br.unb.runb.R;
import br.unb.runb.basic.BasicActvity;
import br.unb.runb.models.User;
import br.unb.runb.screens.credito.card.PaymentActivity;
import br.unb.runb.util.UiFunctions;

public class CreditoActivity extends BasicActvity {

    private Button cardButton;
    private Button generateBill;
    private EditText billAmount;
    private TextView valorSaldo;
    private TextView nomeRefeicao;
    private TextView valorRefeicao;
    private ProgressBar progressSaldo;

    private String current = "";
    private String saldoUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credito);

        findViewItems();
        requestSaldo();
        requestGrupo();
    }

    private void findViewItems() {

        cardButton = findViewById(R.id.credito_button_card);
        //generateBill = findViewById(R.id.credito_generate_bill);
        valorSaldo = findViewById(R.id.credito_saldo);
        progressSaldo = findViewById(R.id.progress_saldo);
        nomeRefeicao = findViewById(R.id.nome_refeicao);
        valorRefeicao = findViewById(R.id.valor_refeicao);

        cardButton.setOnClickListener(cardClickListener);
        //generateBill.setOnClickListener(billClickListener);

    }

    private void requestSaldo() {

        AndroidNetworking.get("https://homologaservicos.unb.br/dados/administrativo/ru/pessoa/{id}/saldo")
                .addPathParameter("id", User.getInstance().getId())
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        valorSaldo.setVisibility(View.VISIBLE);
                        progressSaldo.setVisibility(View.GONE);
                        try {
                            if (response.has("valor")) {
                                valorSaldo.setText("R$ " + String.valueOf(response.getDouble("valor")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        //{"error":"access_denied"} STATUS 400
                        //saldo qualquer um pode ver
//                        if (anError.getErrorCode() == 400) {
//                            //token expirou
//                            //TODO: testar
//                            Intent intent = new Intent(CreditoActivity.this, ContainerActivity.class); //TODO: get intent at container and set to login tab
//                            intent.putExtra("token_expired", true);
//                            startActivity(intent);
//                            //dialog
//                            AlertDialog alertDialog = new AlertDialog.Builder(CreditoActivity.this).create();
//                            alertDialog.setTitle("A sua sessãso expirou");
//                            alertDialog.setMessage("Por favor, realize o login novamente");
//                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            dialog.dismiss();
//                                        }
//                                    });
//                            alertDialog.show();
//                            //finish
//                        } else {
//
//                            valorSaldo.setVisibility(View.VISIBLE);
//                            valorSaldo.setText("ERRO");
//                            //TODO: see how to deal to access token expired response
//                        }
                        valorSaldo.setVisibility(View.VISIBLE);
                        valorSaldo.setText("ERRO");
                        progressSaldo.setVisibility(View.GONE);
                    }
                });

    }

    private void requestGrupo() {
        AndroidNetworking.get("https://homologaservicos.unb.br/dados/administrativo/ru/pessoa/{id}/grupo?access_token={access_token}")
                .addPathParameter("id", User.getInstance().getMatricula())
                .addPathParameter("access_token", User.getInstance().getAccessToken())
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String[] descricao = response.getString("descricao").split(" ");
                            nomeRefeicao.setText("O valor da refeição para o " + descricao[2] + " é de");
                            valorRefeicao.setText("R$ " + response.getString("valor"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        int statusCode = anError.getErrorCode();
                        try {
                            JSONObject jsonObject = new JSONObject(anError.getErrorBody());
                            if (jsonObject.getString("error").equalsIgnoreCase("access_denied")) {
                                //                            //token expirou
                                //TODO: testar
                                UiFunctions.tokenExpired(CreditoActivity.this);
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        //retorna 400 = TOKEN EXPIROU
        //relogar automaticamente? (salvar usuario e senha)

    }

    View.OnClickListener cardClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(CreditoActivity.this, PaymentActivity.class);
            intent.putExtra("type", "CreditCard");
            //startActivity(new Intent(CreditoActivity.this, ChooseCardTypeActivity.class));

        }
    };

//    View.OnClickListener billClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            //onCreateDialog();
//            final Dialog dialog = new Dialog(CreditoActivity.this);
//            dialog.setContentView(R.layout.dialog_bill);
//            dialog.show();
//            //dialog.setTitle("Title");
//
//            billAmount = dialog.findViewById(R.id.bill_amount);
//
//            billAmount.addTextChangedListener(amountChangedListener);
//        }
//    };
//
//
//    public void onCreateDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(CreditoActivity.this);
//        // Get the layout inflater
//        LayoutInflater inflater = CreditoActivity.this.getLayoutInflater();
//
//        // Inflate and set the layout for the dialog
//        // Pass null as the parent view because its going in the dialog layout
//        builder.setView(inflater.inflate(R.layout.dialog_bill, null));
//        builder.create();
//        builder.show();
//
//        billAmount = findViewById(R.id.bill_amount);
//
//        billAmount.addTextChangedListener(amountChangedListener);
//    }
//
//    TextWatcher amountChangedListener = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            if(!s.toString().equals(current)){
//                billAmount.removeTextChangedListener(this);
//
//                //String cleanString = s.toString().replaceAll("\\D", "");
//                String cleanString = s.toString().replaceAll("[$,.]", "");
//
//                double parsed = Double.parseDouble(cleanString);
//                String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));
//
//                current = formatted;
//                //textAmount.setText("R" + formatted);
//                billAmount.setText(formatted);
//                billAmount.setSelection(formatted.length());
//
//                billAmount.addTextChangedListener(this);
//            }
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//
//        }
//    };

}
