package com.cursoandroid.myopinion.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.cursoandroid.myopinion.BitmapUtil;
import com.cursoandroid.myopinion.domain.Estabelecimento;

import java.util.ArrayList;

/**
 * Created by igor on 22/10/16.
 */

public class EstabelecimentoDAO {

    Estabelecimento estabelecimento;
    SQLiteDatabase db;
    DBHelper mDbHelper;

    public EstabelecimentoDAO(Context context)
    {
        estabelecimento = new Estabelecimento();
        mDbHelper = new DBHelper(context);
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

    private void close(){ db.close();}


    public void read(int id)
    {
        this.open("read");
        String[] projection = {
                DBContract.EntdEstabelecimento._ID,
                DBContract.EntdEstabelecimento.COLUMN_NAME_NOME,
                DBContract.EntdEstabelecimento.COLUMN_NAME_TIPO,
                DBContract.EntdEstabelecimento.COLUMN_NAME_GPS,
                DBContract.EntdEstabelecimento.COLUMN_NAME_ESTADO,
                DBContract.EntdEstabelecimento.COLUMN_NAME_CIDADE,
                DBContract.EntdEstabelecimento.COLUMN_NAME_BAIRRO,
                DBContract.EntdEstabelecimento.COLUMN_NAME_LATITUDE,
                DBContract.EntdEstabelecimento.COLUMN_NAME_LONGITUDE,
                DBContract.EntdEstabelecimento.COLUMN_NAME_RESPONSAVEL,
                DBContract.EntdEstabelecimento.COLUMN_NAME_NOTIFICACAO,
                DBContract.EntdEstabelecimento.COLUMN_NAME_FOTO,
                DBContract.EntdEstabelecimento.COLUMN_NAME_RATE,
                DBContract.EntdEstabelecimento.COLUMN_NAME_NUM_AVALIATION
        };

        String selection = DBContract.EntdEstabelecimento._ID+"=?";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor c = db.query(
                DBContract.EntdEstabelecimento.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                // The sort order
        );
        boolean findEntd;
        if((findEntd = c.moveToNext()))
        {
            estabelecimento.setId( c.getLong(c.getColumnIndex(DBContract.EntdEstabelecimento._ID)) );
            estabelecimento.setNome( c.getString(c.getColumnIndex(DBContract.EntdEstabelecimento.COLUMN_NAME_NOME)) );
            estabelecimento.setTipoEstabelecimento( c.getString(c.getColumnIndex(DBContract.EntdEstabelecimento.COLUMN_NAME_TIPO)) );
            estabelecimento.setGps( c.getInt(c.getColumnIndex(DBContract.EntdEstabelecimento.COLUMN_NAME_GPS)) > 0 );
            estabelecimento.setNotificacao( c.getInt(c.getColumnIndex(DBContract.EntdEstabelecimento.COLUMN_NAME_NOTIFICACAO)) > 0 );
            estabelecimento.setEstado( c.getString(c.getColumnIndex(DBContract.EntdEstabelecimento.COLUMN_NAME_ESTADO)) );
            estabelecimento.setCidade( c.getString(c.getColumnIndex(DBContract.EntdEstabelecimento.COLUMN_NAME_CIDADE)) );
            estabelecimento.setBairro( c.getString(c.getColumnIndex(DBContract.EntdEstabelecimento.COLUMN_NAME_BAIRRO)) );
            estabelecimento.setResponsavel( c.getString(c.getColumnIndex(DBContract.EntdEstabelecimento.COLUMN_NAME_RESPONSAVEL)) );
            estabelecimento.setLatitude( c.getDouble(c.getColumnIndex(DBContract.EntdEstabelecimento.COLUMN_NAME_LATITUDE)) );
            estabelecimento.setLongitude( c.getDouble(c.getColumnIndex(DBContract.EntdEstabelecimento.COLUMN_NAME_LONGITUDE)) );
            estabelecimento.setRating( c.getFloat(c.getColumnIndex(DBContract.EntdEstabelecimento.COLUMN_NAME_RATE)) );
            estabelecimento.setFoto( c.getBlob(c.getColumnIndex(DBContract.EntdEstabelecimento.COLUMN_NAME_FOTO)) );
            estabelecimento.setNumAvaliacoes( c.getInt(c.getColumnIndex(DBContract.EntdEstabelecimento.COLUMN_NAME_NUM_AVALIATION)));

        }
        c.close();
        this.close();

    }

    public void putGPS(String nome, String tipoEstabelecimento, double latitude, double longitude, boolean notificacao, Bitmap foto, String responsavel){
        // Create a new map of values, where column names are the keys

        this.open("write");
        ContentValues values = new ContentValues();
        values.put(DBContract.EntdEstabelecimento.COLUMN_NAME_NOME, nome);
        values.put(DBContract.EntdEstabelecimento.COLUMN_NAME_TIPO,tipoEstabelecimento);
        values.put(DBContract.EntdEstabelecimento.COLUMN_NAME_GPS,true);
        values.put(DBContract.EntdEstabelecimento.COLUMN_NAME_LATITUDE,latitude);
        values.put(DBContract.EntdEstabelecimento.COLUMN_NAME_LONGITUDE,longitude);
        values.put(DBContract.EntdEstabelecimento.COLUMN_NAME_NOTIFICACAO,notificacao);
        values.put(DBContract.EntdEstabelecimento.COLUMN_NAME_RESPONSAVEL,responsavel);
        values.put(DBContract.EntdEstabelecimento.COLUMN_NAME_FOTO, BitmapUtil.getBitmapAsByteArray(foto));
        values.put(DBContract.EntdEstabelecimento.COLUMN_NAME_RATE,0);
        values.put(DBContract.EntdEstabelecimento.COLUMN_NAME_NUM_AVALIATION,0);

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                DBContract.EntdEstabelecimento.TABLE_NAME,
                null,
                values);

        this.close();
    }

    public void put(String nome, String tipoEstabelecimento, String estado, String cidade, String bairro, boolean notificacao, Bitmap foto, String responsavel){
        // Create a new map of values, where column names are the keys

        this.open("write");
        ContentValues values = new ContentValues();
        values.put(DBContract.EntdEstabelecimento.COLUMN_NAME_NOME, nome);
        values.put(DBContract.EntdEstabelecimento.COLUMN_NAME_TIPO,tipoEstabelecimento);
        values.put(DBContract.EntdEstabelecimento.COLUMN_NAME_GPS,false);
        values.put(DBContract.EntdEstabelecimento.COLUMN_NAME_ESTADO,estado);
        values.put(DBContract.EntdEstabelecimento.COLUMN_NAME_CIDADE,cidade);
        values.put(DBContract.EntdEstabelecimento.COLUMN_NAME_BAIRRO,bairro);
        values.put(DBContract.EntdEstabelecimento.COLUMN_NAME_NOTIFICACAO,notificacao);
        values.put(DBContract.EntdEstabelecimento.COLUMN_NAME_RESPONSAVEL,responsavel);
        values.put(DBContract.EntdEstabelecimento.COLUMN_NAME_FOTO, BitmapUtil.getBitmapAsByteArray(foto));
        values.put(DBContract.EntdEstabelecimento.COLUMN_NAME_RATE,0);
        values.put(DBContract.EntdEstabelecimento.COLUMN_NAME_NUM_AVALIATION,0);

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                DBContract.EntdEstabelecimento.TABLE_NAME,
                null,
                values);

        this.close();
    }



    public  ArrayList<Estabelecimento> getEstabelecimentos(){
        ArrayList<Estabelecimento> listEstabelecimentos = new ArrayList<>();
        this.open("read");
        String[] projection = {
                DBContract.EntdEstabelecimento._ID,
                DBContract.EntdEstabelecimento.COLUMN_NAME_NOME,
                DBContract.EntdEstabelecimento.COLUMN_NAME_TIPO,
                DBContract.EntdEstabelecimento.COLUMN_NAME_GPS,
                DBContract.EntdEstabelecimento.COLUMN_NAME_ESTADO,
                DBContract.EntdEstabelecimento.COLUMN_NAME_CIDADE,
                DBContract.EntdEstabelecimento.COLUMN_NAME_BAIRRO,
                DBContract.EntdEstabelecimento.COLUMN_NAME_LATITUDE,
                DBContract.EntdEstabelecimento.COLUMN_NAME_LONGITUDE,
                DBContract.EntdEstabelecimento.COLUMN_NAME_RESPONSAVEL,
                DBContract.EntdEstabelecimento.COLUMN_NAME_NOTIFICACAO,
                DBContract.EntdEstabelecimento.COLUMN_NAME_FOTO,
                DBContract.EntdEstabelecimento.COLUMN_NAME_RATE,
                DBContract.EntdEstabelecimento.COLUMN_NAME_NUM_AVALIATION
        };

        String sortOrder = DBContract.EntdEstabelecimento.COLUMN_NAME_RATE+ " DESC";

        Cursor c = db.query(
                DBContract.EntdEstabelecimento.TABLE_NAME,  // The table to query
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
            Estabelecimento novo = new Estabelecimento();
            novo.setId( c.getLong(c.getColumnIndex(DBContract.EntdEstabelecimento._ID)) );
            novo.setNome( c.getString(c.getColumnIndex(DBContract.EntdEstabelecimento.COLUMN_NAME_NOME)) );
            novo.setTipoEstabelecimento( c.getString(c.getColumnIndex(DBContract.EntdEstabelecimento.COLUMN_NAME_TIPO)) );
            novo.setGps( c.getInt(c.getColumnIndex(DBContract.EntdEstabelecimento.COLUMN_NAME_GPS)) > 0 );
            novo.setNotificacao( c.getInt(c.getColumnIndex(DBContract.EntdEstabelecimento.COLUMN_NAME_NOTIFICACAO)) > 0 );
            novo.setEstado( c.getString(c.getColumnIndex(DBContract.EntdEstabelecimento.COLUMN_NAME_ESTADO)) );
            novo.setCidade( c.getString(c.getColumnIndex(DBContract.EntdEstabelecimento.COLUMN_NAME_CIDADE)) );
            novo.setBairro( c.getString(c.getColumnIndex(DBContract.EntdEstabelecimento.COLUMN_NAME_BAIRRO)) );
            novo.setResponsavel( c.getString(c.getColumnIndex(DBContract.EntdEstabelecimento.COLUMN_NAME_RESPONSAVEL)) );
            novo.setLatitude( c.getDouble(c.getColumnIndex(DBContract.EntdEstabelecimento.COLUMN_NAME_LATITUDE)) );
            novo.setLongitude( c.getDouble(c.getColumnIndex(DBContract.EntdEstabelecimento.COLUMN_NAME_LONGITUDE)) );
            novo.setRating( c.getFloat(c.getColumnIndex(DBContract.EntdEstabelecimento.COLUMN_NAME_RATE)) );
            novo.setFoto( c.getBlob(c.getColumnIndex(DBContract.EntdEstabelecimento.COLUMN_NAME_FOTO)));
            novo.setNumAvaliacoes( c.getInt(c.getColumnIndex(DBContract.EntdEstabelecimento.COLUMN_NAME_NUM_AVALIATION)));
            listEstabelecimentos.add(novo);
        }
        c.close();
        this.close();
        return listEstabelecimentos;
    }

    public void update()
    {
        this.open("write");
        ContentValues values = new ContentValues();
        values.put(DBContract.EntdEstabelecimento.COLUMN_NAME_NOME, this.estabelecimento.getNome());
        values.put(DBContract.EntdEstabelecimento.COLUMN_NAME_TIPO,this.estabelecimento.getTipoEstabelecimento());
        values.put(DBContract.EntdEstabelecimento.COLUMN_NAME_GPS,this.estabelecimento.isGps());
        values.put(DBContract.EntdEstabelecimento.COLUMN_NAME_ESTADO,this.estabelecimento.getEstado());
        values.put(DBContract.EntdEstabelecimento.COLUMN_NAME_CIDADE,this.estabelecimento.getCidade());
        values.put(DBContract.EntdEstabelecimento.COLUMN_NAME_BAIRRO,this.estabelecimento.getBairro());
        values.put(DBContract.EntdEstabelecimento.COLUMN_NAME_NOTIFICACAO,this.estabelecimento.isNotificacao());
        values.put(DBContract.EntdEstabelecimento.COLUMN_NAME_RESPONSAVEL,this.estabelecimento.getResponsavel());
        values.put(DBContract.EntdEstabelecimento.COLUMN_NAME_LATITUDE,this.estabelecimento.getLatitude());
        values.put(DBContract.EntdEstabelecimento.COLUMN_NAME_LONGITUDE,this.estabelecimento.getLongitude());
        values.put(DBContract.EntdEstabelecimento.COLUMN_NAME_RATE,this.estabelecimento.getRating());
        values.put(DBContract.EntdEstabelecimento.COLUMN_NAME_FOTO, this.estabelecimento.getFoto());
        values.put(DBContract.EntdEstabelecimento.COLUMN_NAME_NUM_AVALIATION, this.estabelecimento.getNumAvaliacoes());

        String selection = DBContract.EntdEstabelecimento._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(this.estabelecimento.getId()) };

        int count = db.update(
                DBContract.EntdEstabelecimento.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        this.close();
    }

    public void delete(){ this.db.execSQL("delete from "+ DBContract.EntdEstabelecimento.TABLE_NAME);}

    public Estabelecimento getEstabelecimento()
    {
        return estabelecimento;
    }

}
