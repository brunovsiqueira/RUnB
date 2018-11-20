package br.unb.runb.screens.credito;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import br.unb.runb.R;
import br.unb.runb.basic.BasicActvity;
import br.unb.runb.screens.credito.card.PaymentActivity;

public class CreditoActivity extends BasicActvity {

    private TextView toolbarTitle;
    private Button cardButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credito);

        findViewItems();
    }

    private void findViewItems() {

        toolbarTitle = findViewById(R.id.toolbar_container_title);
        cardButton = findViewById(R.id.credito_button_card);

        toolbarTitle.setText("Créditos");
        cardButton.setOnClickListener(cardClickListener);

    }

    View.OnClickListener cardClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            startActivity(new Intent(CreditoActivity.this, PaymentActivity.class));

        }
    };

}
