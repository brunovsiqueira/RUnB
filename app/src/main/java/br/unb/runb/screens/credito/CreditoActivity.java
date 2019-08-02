package br.unb.runb.screens.credito;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.NumberFormat;

import br.unb.runb.R;
import br.unb.runb.basic.BasicActvity;
import br.unb.runb.screens.credito.card.ChooseCardTypeActivity;
import br.unb.runb.screens.credito.card.PaymentActivity;

public class CreditoActivity extends BasicActvity {

    private TextView toolbarTitle;
    private Button cardButton;
    private Button generateBill;
    private EditText billAmount;

    private String current = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credito);

        findViewItems();
    }

    private void findViewItems() {

        toolbarTitle = findViewById(R.id.toolbar_container_title);
        cardButton = findViewById(R.id.credito_button_card);
        generateBill = findViewById(R.id.credito_generate_bill);

        toolbarTitle.setText("Créditos");
        cardButton.setOnClickListener(cardClickListener);
        generateBill.setOnClickListener(billClickListener);

    }

    View.OnClickListener cardClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            startActivity(new Intent(CreditoActivity.this, ChooseCardTypeActivity.class));

        }
    };

    View.OnClickListener billClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //onCreateDialog();
            final Dialog dialog = new Dialog(CreditoActivity.this);
            dialog.setContentView(R.layout.dialog_bill);
            dialog.show();
            //dialog.setTitle("Title");

            billAmount = dialog.findViewById(R.id.bill_amount);

            billAmount.addTextChangedListener(amountChangedListener);
        }
    };


    public void onCreateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CreditoActivity.this);
        // Get the layout inflater
        LayoutInflater inflater = CreditoActivity.this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_bill, null));
        builder.create();
        builder.show();

        billAmount = findViewById(R.id.bill_amount);

        billAmount.addTextChangedListener(amountChangedListener);
    }

    TextWatcher amountChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(!s.toString().equals(current)){
                billAmount.removeTextChangedListener(this);

                //String cleanString = s.toString().replaceAll("\\D", "");
                String cleanString = s.toString().replaceAll("[$,.]", "");

                double parsed = Double.parseDouble(cleanString);
                String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

                current = formatted;
                //textAmount.setText("R" + formatted);
                billAmount.setText(formatted);
                billAmount.setSelection(formatted.length());

                billAmount.addTextChangedListener(this);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

}
