package br.unb.runb.screens.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import br.unb.runb.R;

public class MenuFragment extends Fragment {

    private View view;
    private RelativeLayout relativeLayout;
    private TextView toolbarTitle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu, container, false);

        findViewItems();

        return view;

    }

    private void findViewItems() {
        relativeLayout = view.findViewById(R.id.clickable_news);
        toolbarTitle = view.findViewById(R.id.toolbar_container_title);
        toolbarTitle.setText("Menu");

        relativeLayout.setOnClickListener(relativeClickListener);
    }

    View.OnClickListener relativeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getContext(), NewsActivity.class));
        }
    };

}
