package com.cursoandroid.myopinion.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.cursoandroid.myopinion.BitmapUtil;
import com.cursoandroid.myopinion.domain.Usuario;

import java.util.ArrayList;

/**
 * Created by igor on 22/10/16.
 */

public class UsuarioDAO {

    SQLiteDatabase db;
    DBHelper mDbHelper;

    private Usuario usuario;

    public UsuarioDAO(Context context)
    {
        mDbHelper = new DBHelper(context);
        usuario = new Usuario();
    }

    private void open(String mode)
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
            this.usuario.setFoto( c.getBlob(c.getColumnIndex("foto")) );
            this.usuario.setId( c.getLong(c.getColumnIndex("_id")) );
            this.usuario.setNome( c.getString(c.getColumnIndex("nome")) );
            this.usuario.setEmail( c.getString(c.getColumnIndex("email")) );
            this.usuario.setCep( c.getString(c.getColumnIndex("cep")) );
            this.usuario.setDtNasc( c.getString(c.getColumnIndex("dataNasc")) );
            this.usuario.setSenha( c.getString(c.getColumnIndex("senha")) );
//            this.foto = BitmapFactory.decodeByteArray(img,0,img.length);
        }
        c.close();
        this.close();
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

            this.usuario.setFoto( c.getBlob(c.getColumnIndex("foto")) );
            this.usuario.setId( c.getLong(c.getColumnIndex("_id")) );
            this.usuario.setNome( c.getString(c.getColumnIndex("nome")) );
            this.usuario.setEmail( c.getString(c.getColumnIndex("email")) );
            this.usuario.setCep( c.getString(c.getColumnIndex("cep")) );
            this.usuario.setDtNasc( c.getString(c.getColumnIndex("dataNasc")) );
            this.usuario.setSenha( c.getString(c.getColumnIndex("senha")) );
        }
        c.close();
        this.close();
    }


    public void put(String nome, String email, String cep, String dtNasc, String senha, Bitmap foto){
        // Create a new map of values, where column names are the keys
        this.open("write");
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

        this.close();
    }

    public ArrayList<Usuario> getUsuarios(){
        this.open("read");
        ArrayList<Usuario> listUsuarios = new ArrayList<>();
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
            Usuario novo = new Usuario();
            novo.setFoto( c.getBlob(c.getColumnIndex("foto")) );
            novo.setId( c.getLong(c.getColumnIndex("_id")) );
            novo.setNome( c.getString(c.getColumnIndex("nome")) );
            novo.setEmail( c.getString(c.getColumnIndex("email")) );
            novo.setCep( c.getString(c.getColumnIndex("cep")) );
            novo.setDtNasc( c.getString(c.getColumnIndex("dataNasc")) );
            novo.setSenha( c.getString(c.getColumnIndex("senha")) );
            listUsuarios.add(novo);
        }
        c.close();
        return listUsuarios;
    }

    public void update(){
        this.open("write");
        ContentValues values = new ContentValues();
        values.put(DBContract.EntdUsuario.COLUMN_NAME_NOME, this.usuario.getNome());
        values.put(DBContract.EntdUsuario.COLUMN_NAME_EMAIL, this.usuario.getEmail());
        values.put(DBContract.EntdUsuario.COLUMN_NAME_CEP, this.usuario.getCep());
        values.put(DBContract.EntdUsuario.COLUMN_NAME_DATA_NASC,this.usuario.getDtNasc());
        values.put(DBContract.EntdUsuario.COLUMN_NAME_SENHA,this.usuario.getSenha());
        values.put(DBContract.EntdUsuario.COLUMN_NAME_FOTO, this.usuario.getFoto());

        String selection = DBContract.EntdUsuario._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(this.usuario.getId()) };

        int count = db.update(
                DBContract.EntdUsuario.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        this.close();
    }

    private void close(){ db.close();}

    public void delete(){
        db.execSQL("delete from "+ DBContract.EntdUsuario.TABLE_NAME);
    }

    public ArrayList<String> getCredenciais()
    {
        ArrayList<String> credenciais = new ArrayList<>();
        this.open("read");
        ArrayList<Usuario> listUsuarios = this.getUsuarios();
        for (Usuario user: listUsuarios){credenciais.add(user.getEmail()+":"+user.getSenha()+":"+user.getId());}
        this.close();
        return credenciais;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}
