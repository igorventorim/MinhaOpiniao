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
    private String rating;
    private String estado;
    private String cidade;
    private String bairro;
    private double latitude;
    private double longitude;
    private byte[] foto;
    private boolean gps;
    private boolean notificacao;
    private String responsavel;

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

    public String getRating() {
        return rating;
    }

    public Estabelecimento setRating(String rating) {
        this.rating = rating;
        return this;
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

    public String getResponsavel() {
        return responsavel;
    }

    public Estabelecimento setResponsavel(String responsavel) {
        this.responsavel = responsavel;
        return this;
    }

}
