package br.unb.runb.screens.credito.card;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import br.unb.runb.R;
import br.unb.runb.basic.BasicActvity;

public class PaymentActivity extends BasicActvity {

    private TextView toolbarTitle;

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

        toolbarTitle.setText("Comprar créditos");

    }
}
