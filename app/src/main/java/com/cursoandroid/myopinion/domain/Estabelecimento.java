package com.cursoandroid.myopinion.domain;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.Serializable;

/**
 * Created by igor on 16/10/16.
 */

public class Estabelecimento implements Serializable{

    public Estabelecimento(){}

    private long id;
    private String nome;
    private String tipoEstabelecimento;
    private float rating;
    private String estado;
    private String cidade;
    private String bairro;
    private double latitude;
    private double longitude;
    private byte[] foto;
    private boolean gps;
    private boolean notificacao;
    private String responsavel;
    private int numAvaliacoes;

    public float getNota() {
        return nota;
    }

//    public Estabelecimento setNota(float nota) {
//        this.nota = nota;
//        return this;
//    }

    private float nota;

    public int getNumAvaliacoes() {
        return numAvaliacoes;
    }

    public Estabelecimento setNumAvaliacoes(int numAvaliacoes) {
        this.numAvaliacoes = numAvaliacoes;
        return this;
    }

    public long getId() {
        return id;
    }

    public Estabelecimento setId(long id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return nome;
    }

    public Estabelecimento setNome(String nome) {
        this.nome = nome;
        return this;
    }

    public String getTipoEstabelecimento() {
        return tipoEstabelecimento;
    }

    public Estabelecimento setTipoEstabelecimento(String tipoEstabelecimento) {
        this.tipoEstabelecimento = tipoEstabelecimento;
        return this;
    }

    public String getEstado() {
        return estado;
    }

    public Estabelecimento setEstado(String estado) {
        this.estado = estado;
        return this;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getCidade() {
        return cidade;
    }

    public Estabelecimento setCidade(String cidade) {
        this.cidade = cidade;
        return this;
    }

    public String getBairro() {
        return bairro;
    }

    public Estabelecimento setBairro(String bairro) {
        this.bairro = bairro;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public Estabelecimento setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public Estabelecimento setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public byte[] getFoto() {
        return foto;
    }

    public Bitmap getFotoBitmap()
    {
        return BitmapFactory.decodeByteArray(foto,0,foto.length);
    }

    public Estabelecimento setFoto(byte[] foto) {
        this.foto = foto;
        return this;
    }

    public boolean isGps() {
        return gps;
    }

    public Estabelecimento setGps(boolean gps) {
        this.gps = gps;
        return this;
    }

    public boolean isNotificacao() {
        return notificacao;
    }

    public Estabelecimento setNotificacao(boolean notificacao) {
        this.notificacao = notificacao;
        return this;
    }

    public void setResponsavel(String responsavel){ this.responsavel = responsavel; }

    public String getResponsavel() {
        return responsavel;
    }

//    public Estabelecimento setResponsavel(String responsavel) {
//        this.responsavel = responsavel;
//        return this;
//    }

    public void calculaNota(int atendimento, int conforto, int qualidade, int custo, int retornaria, int indicaria)
    {
        float soma = (5 - atendimento) + (5 - conforto) + (5 - qualidade) + (5 -(custo)*(5/2)) + (5 - retornaria*5) + (5 - indicaria*5);
        soma = (float) (soma/6.0);
        rating *= (float)numAvaliacoes;
        numAvaliacoes++;
        rating += soma;
        rating /= (float)numAvaliacoes;

//        nota = rating/((float)numAvaliacoes);
    }


}
