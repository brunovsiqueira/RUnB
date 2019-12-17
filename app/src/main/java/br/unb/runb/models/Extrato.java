package br.unb.runb.models;

import java.util.Calendar;

public class Extrato implements Comparable<Extrato> {

    private String tipoTransacao; //V= VENDA (+), C = CONSUMO (-)
    private double valor;
    private String descricao;
    private Calendar calendar;
    private String data;

    public Extrato(String tipoTransacao, double valor, String descricao, String data, Calendar calendar) {
        this.tipoTransacao = tipoTransacao;
        this.valor = valor;
        this.descricao = descricao;
        this.data = data;
        this.calendar = calendar;
    }

    public Extrato(double valor, String descricao, String data) {

        this.valor = valor;
        this.descricao = descricao;
        this.data = data;

    }

    public String getTipoTransacao() {
        return tipoTransacao;
    }

    public double getValor() {
        return valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public String getData() {
        return data;
    }

    @Override
    public int compareTo(Extrato o) {
        return o.getCalendar().compareTo(this.calendar);
    }
}
