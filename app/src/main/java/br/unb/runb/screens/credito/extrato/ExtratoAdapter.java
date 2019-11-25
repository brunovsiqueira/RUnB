package br.unb.runb.screens.credito.extrato;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import br.unb.runb.R;
import br.unb.runb.models.Extrato;

public class ExtratoAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<Extrato> extratoArrayList;

    ExtratoViewHolder extratoViewHolder;

    public ExtratoAdapter(ArrayList<Extrato>  extratoList, Context context){
        this.extratoArrayList = extratoList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.extrato_item, parent, false);
        return new ExtratoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        extratoViewHolder = (ExtratoViewHolder) holder;
        Extrato extrato = extratoArrayList.get(position);

        extratoViewHolder.extratoData.setText(extratoArrayList.get(position).getData());

        if (extrato.getTipoTransacao().equalsIgnoreCase("V")) {
            extratoViewHolder.extratoDescricao.setText("Compra");
            extratoViewHolder.extratoValor.setText(String.valueOf(extrato.getValor()));
        } else {
            extratoViewHolder.extratoDescricao.setText(extrato.getDescricao());
            extratoViewHolder.extratoValor.setText(String.valueOf(extrato.getValor()));
        }

    }

    @Override
    public int getItemCount() {
        return extratoArrayList.size();
    }
}
