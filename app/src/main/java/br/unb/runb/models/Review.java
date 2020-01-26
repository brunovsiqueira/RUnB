package br.unb.runb.models;

import com.google.firebase.firestore.FieldValue;

import java.util.Date;

public class Review implements Comparable<Review>{

    private String comentario;
    private String user;
    private String refeicao;
    private Date date;
    private double rate;

    public Review(String comentario, String user, String refeicao, Date date, double rate) {
        this.comentario = comentario;
        this.user = user;
        this.refeicao = refeicao;
        this.date = date;
        this.rate = rate;
    }

    public String getComentario() {
        return comentario;
    }

    public String getUser() {
        return user;
    }

    public String getRefeicao() {
        return refeicao;
    }

    public Date getDate() {
        return date;
    }

    public double getRate() {
        return rate;
    }


    @Override
    public int compareTo(Review review) {
        return review.getDate().compareTo(this.date);
    }
}
