package br.unb.runb.screens.credito.extrato;

import android.os.Build;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import br.unb.runb.R;
import br.unb.runb.models.Extrato;
import br.unb.runb.models.User;
import br.unb.runb.screens.credito.CreditoActivity;
import br.unb.runb.util.UiFunctions;

public class ExtratoActivity extends AppCompatActivity {

    private TextView toolbarTitle;
    private RecyclerView recyclerView;
    private TextView textSaldo;
    private ArrayList<Extrato> extratoArrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extrato);

        //TODO: progress bar while loading
        findViewItems();
        getExtrato();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setAutoMeasureEnabled(false);

        recyclerView.setLayoutManager(layoutManager);

        if (getIntent().getStringExtra("saldo") != null) {
            textSaldo.setText(getIntent().getStringExtra("saldo"));
        }

    }

    private void findViewItems() {

        toolbarTitle = findViewById(R.id.toolbar_container_title);
        recyclerView = findViewById(R.id.extrato_reyclerview);
        textSaldo = findViewById(R.id.saldo_valor);

        toolbarTitle.setText("Extrato");

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getExtrato() {
        Date date = new Date();
        Date dateMinusYears = new Date();
        dateMinusYears.setYear(dateMinusYears.getYear() - 3);
//        LocalDate localDate = LocalDate.now();
//        LocalDate localDateMinusYear = localDate.minusYears(3);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
//        extratoArrayList.add(new Extrato(Double.valueOf(getIntent().getStringExtra("saldo")), "Seu saldo", "Hoje"));

        AndroidNetworking.get("https://homologaservicos.unb.br/dados/administrativo/ru/pessoa/{id}/extrato?access_token={access_token}&filter={filter}")
                .addPathParameter("id", User.getInstance().getMatricula())
                .addPathParameter("access_token", User.getInstance().getAccessToken())
                .addPathParameter("filter","\"{\"dataInicio\":" + formatter.format(dateMinusYears) + ", \"dataFim\":" + formatter.format(date) + "}\"") //TODO: user escolhe a data, ou colocar nos últimos 3 anos (?)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        extratoArrayList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                //String[] descricao = jsonObject.getJSONObject("grupo").getString("descricao").split(" ");
                                String descricao = "";
                                String[] data =  jsonObject.getString("dataHora").split(" ");

                                int hora = Integer.valueOf(data[1].split(":")[0]);
                                if (hora>=16) {
                                    descricao = "Jantar";
                                } else if (hora >= 10 && hora < 15) {
                                    descricao =  "Almoço";
                                } else if (hora <10) {
                                    descricao = "Café da manhã";
                                }

                                Calendar calendar = getCalendarFromString(data);

                                if (jsonObject.getDouble("valorRecebido") > 0) {
                                    extratoArrayList.add(new Extrato(jsonObject.getString("tipoTransacao"),
                                            jsonObject.getDouble("valorRecebido"),
                                            descricao, data[0], calendar));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        Collections.sort(extratoArrayList);
                        recyclerView.setAdapter(new ExtratoAdapter(extratoArrayList, ExtratoActivity.this));

                    }

                    @Override
                    public void onError(ANError anError) {
                        ANError anError1 = anError;
                    }
                });

    }

    private Calendar getCalendarFromString(String[] data) {
        int day = Integer.valueOf(data[0].split("/")[0]);
        int month = Integer.valueOf(data[0].split("/")[1]);
        int year = Integer.valueOf(data[0].split("/")[2]);

        int hour = Integer.valueOf(data[1].split(":")[0]);
        int minute = Integer.valueOf(data[1].split(":")[1]);

        Calendar calendar = Calendar.getInstance();

        calendar.set(year, month, day, hour, minute);

        return calendar;
    }

}
