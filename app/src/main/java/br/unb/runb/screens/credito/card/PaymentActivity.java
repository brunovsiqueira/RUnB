package br.unb.runb.screens.credito.card;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;

import br.unb.runb.R;
import br.unb.runb.basic.BasicActvity;
import br.unb.runb.util.FormatterString;
import br.unb.runb.util.Mask;
import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;
import okhttp3.OkHttpClient;

public class PaymentActivity extends BasicActvity {

    private TextView toolbarTitle;
    public EditText cardNumber;
    public EditText cardName;
    private EditText securityNumber;
    private EditText cardDate;
    private View takePicture;
    private Button paymentButton;
    private EditText textAmount;

    public JsonObject cardObject = new JsonObject();

    private String current = "";

    private final int MY_SCAN_REQUEST_CODE = 0;
    private String cardType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        cardType = getIntent().getStringExtra("card_type");
//        OkHttpClient okHttpClient = new OkHttpClient() .newBuilder()
//                .addNetworkInterceptor(new StethoInterceptor())
//                .build();
        AndroidNetworking.initialize(getApplicationContext());

        findViewItems();
    }

    private void findViewItems() {

        toolbarTitle = findViewById(R.id.toolbar_container_title);
        cardName = findViewById(R.id.payment_card_name);
        cardNumber = findViewById(R.id.payment_card_number);
        securityNumber = findViewById(R.id.payment_card_security_code);
        takePicture = findViewById(R.id.payment_card_photo_container);
        paymentButton = findViewById(R.id.payment_card_pay);
        cardDate = findViewById(R.id.payment_card_date);
        textAmount = findViewById(R.id.payment_amount);

        toolbarTitle.setText("Comprar créditos");
        takePicture.setOnClickListener(pictureClickListener);
        paymentButton.setOnClickListener(paymentClickListener);
//        cardNumber.addTextChangedListener(Mask.insert(Mask.MaskType.CARD_NUMBER, cardNumber));
        cardDate.addTextChangedListener(Mask.insert(Mask.MaskType.CARD_DATE, cardDate));
        textAmount.addTextChangedListener(amountChangedListener);

        //HardcodedFillView();

    }

    private void HardcodedFillView() {
        cardNumber.setText("1234123412341231");
        cardName.setText("Nome Teste");
        securityNumber.setText("123");
        cardDate.setText("12/2018");
    }

    private JSONObject createPaymentJson() {

        JSONObject customer = new JSONObject();
        JSONObject payment = new JSONObject();
        JSONObject completeJson = new JSONObject();

        try {
            customer.put("Name", "Comprador teste"); //TODO: futuramente nome do usuario


        payment.put("Type", cardType);
        payment.put("Amount", Integer.parseInt(FormatterString.onlyDigits(textAmount.getText().toString())));
        payment.put("Provider", "Simulado"); //TODO: Simulado apenas no Sandbox
        payment.put("ReturnUrl", "https://www.cielo.com.br");
        payment.put("Installments", 1);
        if (cardType.equalsIgnoreCase("debitCard")) {
            payment.put("Authenticate", true);
        } else if  (cardType.equalsIgnoreCase("creditCard")) {
            //payment.addProperty("Authenticate", false);
        }
        JSONObject card = new JSONObject();
        card.put("CardNumber", cardNumber.getText().toString());
        card.put("Holder", cardName.getText().toString());
        card.put("ExpirationDate", cardDate.getText().toString());
        card.put("SecurityCode", securityNumber.getText().toString());
        card.put("Brand", "Visa"); //TODO: Add correct brand (spinner)
        if (cardType.equalsIgnoreCase("debitCard")) {
            payment.put("DebitCard", card);
        } else if  (cardType.equalsIgnoreCase("CreditCard")) {
            payment.put("CreditCard", card);
        }

        completeJson.put("MerchantOrderId", 2014111903); //TODO: generate correctly
        completeJson.put("Customer", customer);
        completeJson.put("Payment", payment);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return completeJson;
    }

    public void updateView(String cardNumber, String cardName, String cardDate) {
        this.cardNumber.setText(cardNumber);
        this.cardName.setText(cardName);
        if (cardDate.length() > 5) {
            this.cardDate.setText(getMonth(cardDate) + "/" + getYear(cardDate));
        } else {
            this.cardDate.setText(cardDate);
        }
    }

    public boolean validates() {

        boolean isValidated = true;

        if(securityNumber.getText().toString().length() < 3) {
            securityNumber.setError(getResources().getString(R.string.register_card_code_error));
            securityNumber.requestFocus();
            securityNumber.setText("");
            isValidated = false;
        }

        if (cardName.getText().toString().isEmpty()) {
            cardName.setError(getResources().getString(R.string.register_card_name_error));
            cardName.requestFocus();
            cardName.setText("");
            isValidated = false;
        }

        if (cardNumber.getText().toString().length() < 15) {
            cardNumber.setError(getResources().getString(R.string.register_card_number_error));
            cardNumber.requestFocus();
            cardNumber.setText("");
            isValidated = false;
        }

        if (cardDate.getText().toString().length() < 5) {
            cardDate.setError(getResources().getString(R.string.register_card_date_error));
            cardDate.requestFocus();
            cardDate.setText("");
            isValidated = false;
        }

        return isValidated;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_SCAN_REQUEST_CODE) {
            String resultDisplayStr;
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

                // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
                resultDisplayStr = "Card Number: " + scanResult.getRedactedCardNumber() + "\n";

                // Do something with the raw number, e.g.:
                // myService.setCardNumber( scanResult.cardNumber );

                if (scanResult.isExpiryValid()) {
                    resultDisplayStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";
                    updateView(scanResult.getFormattedCardNumber(), scanResult.cardholderName, scanResult.expiryMonth + "/" + scanResult.expiryYear);
                } else {
                    Toast.makeText(PaymentActivity.this, "Seu cartão expirou", Toast.LENGTH_SHORT);
                }

                if (scanResult.cvv != null) {
                    // Never log or display a CVV
                    resultDisplayStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
                }

                if (scanResult.postalCode != null) {
                    resultDisplayStr += "Postal Code: " + scanResult.postalCode + "\n";
                }
            }
            else {
                resultDisplayStr = "Scan was canceled.";
            }
            // do something with resultDisplayStr, maybe display it in a textView
            // resultTextView.setText(resultDisplayStr);
        }
        // else handle other activity results
    }

    private int getMonth(String date) {
        return Integer.valueOf(date.substring(0, 2));
    }

    private int getYearOfFormattedDate(String date) {
        String year = date.substring(3, 5);

        return Integer.valueOf(year);
    }

    private int getYear(String date) {
        String year = date.substring(5, 7);

        return Integer.valueOf(year);
    }

    TextWatcher amountChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(!s.toString().equals(current)){
                textAmount.removeTextChangedListener(this);

                //String cleanString = s.toString().replaceAll("\\D", "");
                String cleanString = s.toString().replaceAll("[$,.]", "");

                double parsed = Double.parseDouble(cleanString);
                String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

                current = formatted;
                //textAmount.setText("R" + formatted);
                textAmount.setText(formatted);
                textAmount.setSelection(formatted.length());

                textAmount.addTextChangedListener(this);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    View.OnClickListener paymentClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (validates()) {
                //createJsonCard();
                JSONObject jsonObject = createPaymentJson();

                final ProgressDialog pd = new ProgressDialog(PaymentActivity.this);
                pd.show(PaymentActivity.this, "Carregando", "Efetuando pagamento...");

                AndroidNetworking.post("https://apisandbox.cieloecommerce.cielo.com.br/1/sales")
                        .addJSONObjectBody(jsonObject)
                        .addHeaders("merchantId", "448fcb7c-6670-4493-995d-1863671899ee")
                        .addHeaders("merchantKey", "ZCMHARNFVOTJNWILJADFAZJUCOQBBBKVRSNRWCQU")
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // do anything with response
                                try {
                                    //Toast.makeText(PaymentActivity.this, response.getJSONObject("Payment").get("ReturnMessage").toString(), Toast.LENGTH_SHORT).show();
                                    if (response.getJSONObject("Payment").get("ReturnMessage").toString().contains("Successful")) {
                                        Toast.makeText(PaymentActivity.this, "Pagamento realizado com sucesso!", Toast.LENGTH_SHORT).show();
//                                        new FancyAlertDialog.Builder(PaymentActivity.this)
//                                                .setTitle("Pagamento realizado com sucesso!")
//                                                .build();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                pd.dismiss();
                                finish();
                            }
                            @Override
                            public void onError(ANError error) {
                                // handle error
                                pd.dismiss();
                                if (error.getErrorCode() != 0) {
                                    // get parsed error object (If ApiError is your class)

                                } else {
                                    // error.getErrorDetail() : connectionError, parseError, requestCancelledError
                                }
                            }
                        });
//                Ion.with(PaymentActivity.this)
//                        .load("https://apisandbox.cieloecommerce.cielo.com.br")
//                        .uploadProgressBar(progressBar)
//                        .setHeader("merchantId", "448fcb7c-6670-4493-995d-1863671899ee")
//                        .setHeader("merchantKey", "ZCMHARNFVOTJNWILJADFAZJUCOQBBBKVRSNRWCQU")
//                        .setJsonObjectBody(jsonObject)
//                        .asJsonObject()
//                        .setCallback(new FutureCallback<JsonObject>() {
//                            @Override
//                            public void onCompleted(Exception e, JsonObject result) {
//                                Toast.makeText(PaymentActivity.this, result.getAsJsonObject("Payment").get("ReturnMessage").toString(), Toast.LENGTH_SHORT).show();
//                            }
//                        });

            }
        }
    };

    View.OnClickListener pictureClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent scanIntent = new Intent( PaymentActivity.this, CardIOActivity.class);


            // customize these values to suit your needs.
            scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
            scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
            scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false

            // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
            //startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
        }
    };


}
