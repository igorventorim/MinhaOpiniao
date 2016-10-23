package com.cursoandroid.myopinion.domain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.cursoandroid.myopinion.BitmapUtil;
import com.cursoandroid.myopinion.database.DBContract;
import com.cursoandroid.myopinion.database.DBHelper;

import java.util.ArrayList;

/**
 * Created by igor on 19/10/16.
 */

public class Usuario {

    private long id;
    private String nome;
    private String email;
    private String dtNasc;
    private String cep;
    private String senha;
    private byte[] foto;

    public byte[] getFoto() {
        return foto;
    }

    public Bitmap getFotoBitmap()
    {
        return BitmapFactory.decodeByteArray(foto,0,foto.length);
    }

    public Usuario setFoto(byte[] foto) {
        this.foto = foto;
        return this;
    }

    public String getSenha() {
        return senha;
    }

    public Usuario setSenha(String senha) {
        this.senha = senha;
        return this;
    }

    public String getCep() {
        return cep;
    }

    public Usuario setCep(String cep) {
        this.cep = cep;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Usuario setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getNome() {
        return nome;
    }

    public Usuario setNome(String nome) {
        this.nome = nome;
        return this;
    }

    public long getId() {
        return id;
    }

    public Usuario setId(long id) {
        this.id = id;
        return this;
    }

    public String getDtNasc() {
        return dtNasc;
    }

    public Usuario setDtNasc(String dtNasc) {
        this.dtNasc = dtNasc;
        return this;
    }

}
