package br.unb.runb.models;

public class Extrato {

    private String tipoTransacao; //V= VENDA (+),
    private double valor;
    private String descricao;
    private String data;

    public Extrato(String tipoTransacao, double valor, String descricao, String data) {
        this.tipoTransacao = tipoTransacao;
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

    public String getData() {
        return data;
    }
}
