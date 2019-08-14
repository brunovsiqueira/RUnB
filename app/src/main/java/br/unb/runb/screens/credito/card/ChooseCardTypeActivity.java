package br.unb.runb.screens.credito.card;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import br.unb.runb.R;
import br.unb.runb.basic.BasicActvity;
import br.unb.runb.util.Mask;

public class ChooseCardTypeActivity extends BasicActvity {

    private TextView toolbarTitle;
    private Button creditButton;
    private Button debitButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_card);

        findViewItems();
    }

    private void findViewItems() {

        toolbarTitle = findViewById(R.id.toolbar_container_title);
        creditButton = findViewById(R.id.choose_card_credit);
        debitButton = findViewById(R.id.choose_card_debit);

        toolbarTitle.setText("Escolher cartão");

        creditButton.setOnClickListener(creditClickListener);
        debitButton.setOnClickListener(debitClickListener);

    }

    View.OnClickListener creditClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(ChooseCardTypeActivity.this, PaymentActivity.class);
            intent.putExtra("card_type", "CreditCard");
            startActivity(intent);

        }
    };

    View.OnClickListener debitClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(ChooseCardTypeActivity.this, PaymentActivity.class);
            intent.putExtra("card_type", "DebitCard");
            startActivity(intent);

        }
    };

}
