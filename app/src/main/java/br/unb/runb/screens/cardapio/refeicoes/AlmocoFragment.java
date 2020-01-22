package br.unb.runb.screens.cardapio.refeicoes;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileNotFoundException;

import br.unb.runb.R;
import br.unb.runb.util.PDFUtil;

public class AlmocoFragment extends Fragment {

    private TextView nomeRefeicao;
    private LinearLayout shareContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_refeicao, container, false);

        findViewItems(rootView);

        //TODO: call service and populate view


        return rootView;
    }

    private void findViewItems(final View v) {

        nomeRefeicao = v.findViewById(R.id.refeicao_nome);
        shareContainer = v.findViewById(R.id.share_container);

        nomeRefeicao.setText("Almoço");
        shareContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScrollView z = ((Activity) getContext()).findViewById(R.id.scroll);
                int totalHeight = z.getChildAt(0).getHeight();
                int totalWidth = z.getChildAt(0).getWidth();
                Bitmap bitmap = PDFUtil.getBitmapFromView(v, totalWidth, totalHeight);
                PDFUtil.saveBitmap(bitmap, getContext());
                PDFUtil.shareImage(getContext());
            }
        });

    }
}
