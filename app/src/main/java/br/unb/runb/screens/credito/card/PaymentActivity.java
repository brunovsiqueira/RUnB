package br.unb.runb.screens.credito.card;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.text.NumberFormat;

import br.unb.runb.R;
import br.unb.runb.basic.BasicActvity;
import br.unb.runb.util.FormatterString;
import br.unb.runb.util.Mask;
import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

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

    }

    private void HardcodedFillView() {
        cardNumber.setText("0000000000000001");
        //cardName.setText("Nome Teste");
        securityNumber.setText("123");
        cardDate.setText("12/2018");
    }

    private JsonObject createPaymentJson() {

        JsonObject customer = new JsonObject();
        JsonObject payment = new JsonObject();
        JsonObject completeJson = new JsonObject();

        customer.addProperty("Name", "Comprador teste"); //TODO: futuramente nome do usuario

        payment.addProperty("Type", cardType);
        payment.addProperty("Amount", Integer.valueOf(textAmount.getText().toString()));
        payment.addProperty("Provider", "Simulado"); //TODO: Simulado apenas no Sandbox
        payment.addProperty("ReturnUrl", "https://www.cielo.com.br");
        payment.addProperty("Installments", 0);
        if (cardType.equalsIgnoreCase("debitCard")) {
            payment.addProperty("Authenticate", true);
        } else if  (cardType.equalsIgnoreCase("creditCard")) {
            //payment.addProperty("Authenticate", false);
        }
        JsonObject card = new JsonObject();
        card.addProperty("CardNumber", Integer.valueOf(cardNumber.getText().toString()));
        payment.addProperty("Holder", cardName.getText().toString());
        payment.addProperty("ExpirationDate", 12/2018);
        payment.addProperty("SecurityCode", securityNumber.getText().toString());
        payment.addProperty("Brand", "Visa"); //TODO: Add correct brand (spinner)
        if (cardType.equalsIgnoreCase("debitCard")) {
            payment.add("debitCard", card);
        } else if  (cardType.equalsIgnoreCase("creditCard")) {
            payment.add("creditCard", card);
        }

        completeJson.addProperty("MerchantOrderId", 2014111903); //TODO: generate correctly
        completeJson.add("Customer", customer);
        completeJson.add("Payment", payment);

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

                //make request
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
