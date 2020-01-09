package br.unb.runb.screens.credito.extrato;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import br.unb.runb.R;
import br.unb.runb.models.Extrato;
import br.unb.runb.models.User;
import info.hoang8f.android.segmented.SegmentedGroup;

public class ExtratoActivity extends AppCompatActivity {

    private TextView toolbarTitle;
    private RecyclerView recyclerView;
    private RadioButton rbUmAno;
    private RadioButton rbDoisAnos;
    private RadioButton rbTresAnos;
    private SegmentedGroup segmentedGroup;
    private TextView textSaldo;
    private ContentLoadingProgressBar contentLoadingProgressBar;
    private ArrayList<Extrato> extratoArrayList = new ArrayList<>();

    private Date date;
    private Date dateMinusTime;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extrato);

        //TODO: progress bar while loading
        date = new Date();
        dateMinusTime = new Date();
        dateMinusTime.setYear(date.getYear() - 1);

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
        rbUmAno = findViewById(R.id.rb_um_ano);
        rbDoisAnos = findViewById(R.id.rb_dois_anos);
        rbTresAnos = findViewById(R.id.rb_tres_anos);
        segmentedGroup = findViewById(R.id.segmented_time);
        contentLoadingProgressBar = findViewById(R.id.progress_bar);

        toolbarTitle.setText("Extrato");
        segmentedGroup.check(R.id.rb_um_ano);
        segmentedGroup.setOnCheckedChangeListener(onCheckedChangeListener);
        contentLoadingProgressBar.show();

    }

    RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.rb_dois_anos:
                    dateMinusTime.setYear(date.getYear() - 2);
                    getExtrato();
                    break;
                case R.id.rb_um_ano:
                    dateMinusTime.setYear(date.getYear() - 1);
                    getExtrato();
                    break;
                case R.id.rb_tres_anos:
                    dateMinusTime.setYear(date.getYear() - 3);
                    getExtrato();
                    break;
            }
        }
    };

    private void getExtrato() {

        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        AndroidNetworking.get("https://homologaservicos.unb.br/dados/administrativo/ru/pessoa/{id}/extrato?access_token={access_token}&filter={filter}")
                .addPathParameter("id", User.getInstance().getMatricula())
                .addPathParameter("access_token", User.getInstance().getAccessToken())
                .addPathParameter("filter","\"{\"dataInicio\":" + formatter.format(dateMinusTime) + ", \"dataFim\":" + formatter.format(date) + "}\"") //TODO: user escolhe a data, ou colocar nos últimos 3 anos (?)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        contentLoadingProgressBar.hide();
                        extratoArrayList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
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
                        contentLoadingProgressBar.hide();
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
