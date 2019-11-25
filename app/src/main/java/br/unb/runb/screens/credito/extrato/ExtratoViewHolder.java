package br.unb.runb.screens.credito.extrato;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import br.unb.runb.R;

public class ExtratoViewHolder extends RecyclerView.ViewHolder {

    TextView extratoData;
    TextView extratoDescricao;
    TextView extratoValor;

    public ExtratoViewHolder(@NonNull View itemView) {
        super(itemView);

        extratoData = itemView.findViewById(R.id.extrato_data);
        extratoDescricao = itemView.findViewById(R.id.extrato_desc);
        extratoValor = itemView.findViewById(R.id.extrato_valor);

    }

}
