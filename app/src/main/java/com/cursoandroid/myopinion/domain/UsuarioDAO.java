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

public class UsuarioDAO{

    SQLiteDatabase db;
    DBHelper mDbHelper;

    private long id;
    private String nome;
    private String email;
    private String dtNasc;
    private String cep;
    private String senha;

    private Bitmap foto;

    public UsuarioDAO(Context context)
    {
        mDbHelper = new DBHelper(context);
    }

    private UsuarioDAO(){}

    public void open(String mode)
    {
        if(mode.equals("write"))
        {
            db = mDbHelper.getWritableDatabase();
        }else
        {
            db = mDbHelper.getReadableDatabase();
        }
    }

    public void read(String nome)
    {
        String[] projection = {
                DBContract.EntdUsuario._ID,
                DBContract.EntdUsuario.COLUMN_NAME_NOME,
                DBContract.EntdUsuario.COLUMN_NAME_EMAIL,
                DBContract.EntdUsuario.COLUMN_NAME_CEP,
                DBContract.EntdUsuario.COLUMN_NAME_DATA_NASC,
                DBContract.EntdUsuario.COLUMN_NAME_SENHA,
                DBContract.EntdUsuario.COLUMN_NAME_FOTO
        };
        String selection = DBContract.EntdUsuario.COLUMN_NAME_NOME+"=?";
        String[] selectionArgs = { nome };
        Cursor c = db.query(
                DBContract.EntdUsuario.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        boolean findEntd;

        if(findEntd = c.moveToNext())
        {
            byte[] img = c.getBlob(c.getColumnIndex("foto"));
            this.id = c.getLong(c.getColumnIndex("_id"));
            this.nome = c.getString(c.getColumnIndex("nome"));
            this.email = c.getString(c.getColumnIndex("email"));
            this.cep = c.getString(c.getColumnIndex("cep"));
            this.dtNasc = c.getString(c.getColumnIndex("dataNasc"));
            this.senha = c.getString(c.getColumnIndex("senha"));
            this.foto = BitmapFactory.decodeByteArray(img,0,img.length);
        }
        c.close();

    }


    public void read(int id)
    {
        this.open("read");
        String[] projection = {
                DBContract.EntdUsuario._ID,
                DBContract.EntdUsuario.COLUMN_NAME_NOME,
                DBContract.EntdUsuario.COLUMN_NAME_EMAIL,
                DBContract.EntdUsuario.COLUMN_NAME_CEP,
                DBContract.EntdUsuario.COLUMN_NAME_DATA_NASC,
                DBContract.EntdUsuario.COLUMN_NAME_SENHA,
                DBContract.EntdUsuario.COLUMN_NAME_FOTO

        };
        String selection = DBContract.EntdUsuario._ID+"=?";
        String[] selectionArgs = { String.valueOf(id) };
        Cursor c = db.query(
                DBContract.EntdUsuario.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        boolean findEntd;

        if(findEntd = c.moveToNext())
        {

            this.id = c.getLong(c.getColumnIndex("_id"));
            this.nome = c.getString(c.getColumnIndex("nome"));
            this.email = c.getString(c.getColumnIndex("email"));
            this.cep = c.getString(c.getColumnIndex("cep"));
            this.dtNasc = c.getString(c.getColumnIndex("dataNasc"));
            this.senha = c.getString(c.getColumnIndex("senha"));
            byte[] img = c.getBlob(c.getColumnIndex("foto"));
            this.foto = BitmapFactory.decodeByteArray(img,0,img.length);

        }
        c.close();
        this.close();
    }


    public void put(String nome, String email, String cep, String dtNasc, String senha, Bitmap foto){
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DBContract.EntdUsuario.COLUMN_NAME_NOME, nome);
        values.put(DBContract.EntdUsuario.COLUMN_NAME_EMAIL,email);
        values.put(DBContract.EntdUsuario.COLUMN_NAME_CEP,cep);
        values.put(DBContract.EntdUsuario.COLUMN_NAME_DATA_NASC,dtNasc);
        values.put(DBContract.EntdUsuario.COLUMN_NAME_SENHA,senha);
        values.put(DBContract.EntdUsuario.COLUMN_NAME_FOTO, BitmapUtil.getBitmapAsByteArray(foto));

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                DBContract.EntdUsuario.TABLE_NAME,
                null,
                values);
    }

    public ArrayList<UsuarioDAO> getUsuarios(){
        ArrayList<UsuarioDAO> listUsuarios = new ArrayList<>();
        String[] projection = {
                DBContract.EntdUsuario._ID,
                DBContract.EntdUsuario.COLUMN_NAME_NOME,
                DBContract.EntdUsuario.COLUMN_NAME_EMAIL,
                DBContract.EntdUsuario.COLUMN_NAME_CEP,
                DBContract.EntdUsuario.COLUMN_NAME_DATA_NASC,
                DBContract.EntdUsuario.COLUMN_NAME_SENHA,
                DBContract.EntdUsuario.COLUMN_NAME_FOTO

        };

        String sortOrder =
                DBContract.EntdUsuario.COLUMN_NAME_NOME+ " ASC";

        Cursor c = db.query(
                DBContract.EntdUsuario.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        boolean findEntd;
        while((findEntd = c.moveToNext()))
        {
            UsuarioDAO novo = new UsuarioDAO();
            novo.id = c.getLong(c.getColumnIndex("_id"));
            novo.nome = c.getString(c.getColumnIndex("nome"));
            novo.email = c.getString(c.getColumnIndex("email"));
            novo.cep = c.getString(c.getColumnIndex("cep"));
            novo.dtNasc = c.getString(c.getColumnIndex("dataNasc"));
            novo.senha = c.getString(c.getColumnIndex("senha"));
            byte[] img = c.getBlob(c.getColumnIndex("foto"));
            novo.foto = BitmapFactory.decodeByteArray(img,0,img.length);
            listUsuarios.add(novo);
        }
        c.close();
        return listUsuarios;
    }

    public void update(){
        ContentValues values = new ContentValues();
        values.put(DBContract.EntdUsuario.COLUMN_NAME_NOME, nome);
        values.put(DBContract.EntdUsuario.COLUMN_NAME_EMAIL, email);
        values.put(DBContract.EntdUsuario.COLUMN_NAME_CEP,cep);
        values.put(DBContract.EntdUsuario.COLUMN_NAME_DATA_NASC,dtNasc);
        values.put(DBContract.EntdUsuario.COLUMN_NAME_SENHA,senha);
        values.put(DBContract.EntdUsuario.COLUMN_NAME_FOTO, BitmapUtil.getBitmapAsByteArray(foto));

        String selection = DBContract.EntdUsuario._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };

        int count = db.update(
                DBContract.EntdUsuario.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public void close(){ db.close();}

    public void delete(){
        db.execSQL("delete from "+ DBContract.EntdUsuario.TABLE_NAME);
    }


    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getDtNasc() {
        return dtNasc;
    }

    public String getCep() {
        return cep;
    }

    public String getSenha() {
        return senha;
    }

    public Bitmap getFoto(){ return foto;}

    public static ArrayList<String> getCredenciais(Context c)
    {
        ArrayList<String> credenciais = new ArrayList<>();
        UsuarioDAO dao = new UsuarioDAO(c);
        dao.open("read");
        ArrayList<UsuarioDAO> listUsuarios = dao.getUsuarios();
        for (UsuarioDAO user: listUsuarios){credenciais.add(user.getEmail()+":"+user.getSenha()+":"+user.getId());}
        dao.close();
        return credenciais;
    }



//    public String getFoto() {
//        return foto;
//    }

//    public void setEmail(String email){ this.email = email;}

}
