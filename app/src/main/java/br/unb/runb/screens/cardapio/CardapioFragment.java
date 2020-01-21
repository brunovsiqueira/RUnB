package br.unb.runb.screens.cardapio;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import br.unb.runb.R;
import br.unb.runb.screens.cardapio.refeicoes.AlmocoFragment;
import br.unb.runb.screens.cardapio.refeicoes.CafeFragment;
import br.unb.runb.screens.cardapio.refeicoes.JantarFragment;

public class CardapioFragment extends Fragment {

    private View view;
    private TextView toolbarTitle;
    private Spinner spinnerDia;
    private Spinner spinnerRefeicao;
    private Spinner spinnerCampus;
    private ImageView arrowRight;
    private ImageView arrowLeft;
    private int currentItem = 1;

    String[] dias = new String[]{"Seg - 20 JAN","Ter - 21 JAN","Qua - 22 JAN","Qui - 23 JAN","Sex - 24 JAN"};
    String[] refeicoes = new String[]{"Café da manhã", "Almoço", "Janta"};
    String[] campusArray = new String[]{"Darcy Ribeiro", "Gama", "Planaltina", "Ceilândia", "Fazenda"};

    private static final int NUM_PAGES = 3;
    private ViewPager mPager;
    private PagerAdapter pagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cardapio, container, false);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) view.findViewById(R.id.viewpager);
        pagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        mPager.setAdapter(pagerAdapter);
        mPager.setCurrentItem(currentItem);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        currentItem = position;
                        arrowLeft.setVisibility(View.GONE);
                        arrowRight.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        currentItem = position;
                        arrowLeft.setVisibility(View.VISIBLE);
                        arrowRight.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        currentItem = position;
                        arrowLeft.setVisibility(View.VISIBLE);
                        arrowRight.setVisibility(View.GONE);
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        findViewItems(view);

        return view;

    }

    private void findViewItems(View v) {

        toolbarTitle = v.findViewById(R.id.toolbar_container_title);
        spinnerDia = v.findViewById(R.id.cardapio_spinner_dia);
        //spinnerRefeicao = v.findViewById(R.id.cardapio_spinner_refeicao);
        spinnerCampus = v.findViewById(R.id.cardapio_spinner_campus);
        arrowRight = v.findViewById(R.id.arrow_right);
        arrowLeft = v.findViewById(R.id.arrow_left);

        toolbarTitle.setText("Cardápio");

        ArrayAdapter<String> diasArray= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, dias);
        diasArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDia.setAdapter(diasArray);

        ArrayAdapter<String> campusArrayAdapter= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, campusArray);
        campusArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCampus.setAdapter(campusArrayAdapter);

        arrowRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentItem <=2) {
                    mPager.setCurrentItem(currentItem + 1);
                }
            }
        });

        arrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentItem > 0) {
                    mPager.setCurrentItem(currentItem - 1);
                }
            }
        });

    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        AlmocoFragment almocoFragment;
        JantarFragment jantarFragment;
        CafeFragment cafeFragment;

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
            almocoFragment = new AlmocoFragment();
            jantarFragment = new JantarFragment();
            cafeFragment = new CafeFragment();
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return cafeFragment;
                case 1:
                    return almocoFragment;
                case 2:
                    return jantarFragment;

            }
            return new AlmocoFragment();
        }



        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}