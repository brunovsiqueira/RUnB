package br.unb.runb.screens;

import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;

import br.unb.runb.R;
import br.unb.runb.screens.avaliar.AvaliarFragment;
import br.unb.runb.screens.cardapio.CardapioFragment;
import br.unb.runb.screens.credito.CreditoFragment;
import br.unb.runb.screens.menu.MenuFragment;

public class ContainerActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;

    private CardapioFragment cardapioFragment;
    private CreditoFragment creditoFragment;
    private AvaliarFragment avaliarFragment;
    private MenuFragment menuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        instantiateFragments();
        addFragments();
        findViewItems();

        if (getIntent().getBooleanExtra("token_expired", false)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("A sua sessão expirou");
            //builder.setMessage("Por favor, realize o login novamente");
            builder.setMessage("Por favor, realize o login novamente")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                        }
                    });
            builder.show();
        }


    }

    private void findViewItems() {

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        bottomNavigationView.setSelectedItemId(R.id.action_cardapio);

    }

    private void instantiateFragments() {

        cardapioFragment = new CardapioFragment();
        creditoFragment = new CreditoFragment();
        avaliarFragment = new AvaliarFragment();
        menuFragment = new MenuFragment();

    }

    private void addFragments() {
        fragmentManager = getSupportFragmentManager();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.action_cardapio:
                    fragment = cardapioFragment;
                    break;
                case R.id.action_creditos:
                    fragment = creditoFragment;
                    break;
                case R.id.action_avaliacao:
                    fragment = avaliarFragment;
                    break;
                case R.id.action_menu:
                    fragment = menuFragment;
                    break;
            }

            return  loadFragment(fragment);

        }
    };

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            fragmentManager.beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).replace(R.id.fragment_container, fragment).commit();
            return true;
        }

        return false;
    }

}
