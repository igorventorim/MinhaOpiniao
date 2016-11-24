package com.cursoandroid.myopinion.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by igor on 23/11/16.
 */

public class FavoritoDAO {

    SQLiteDatabase db;
    DBHelper mDbHelper;

    public FavoritoDAO(Context context)
    {
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

    public boolean read(int id)
    {
        boolean retorno;
        this.open("read");

        String[] projection = {
                DBContract.EntdFavorito._ID,
                DBContract.EntdFavorito.COLUMN_NAME_ID_FAVORITO
        };

        String selection = DBContract.EntdFavorito.COLUMN_NAME_ID_FAVORITO+"=?";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor c = db.query(
                DBContract.EntdFavorito.TABLE_NAME,  // The table to query
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
            retorno =  true;
        }else
        {
            retorno = false;
        }
        c.close();
        this.close();
        return retorno;
    }

    public void put(long idEstabelecimento)
    {
        this.open("write");
        ContentValues values = new ContentValues();
        values.put(DBContract.EntdFavorito.COLUMN_NAME_ID_FAVORITO,idEstabelecimento);

        long newRowId = db.insert(DBContract.EntdFavorito.TABLE_NAME,null,values);
        this.close();
    }

    public void delete(long id)
    {
        this.open("write");
        String selection = DBContract.EntdFavorito.COLUMN_NAME_ID_FAVORITO + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };
        db.delete(DBContract.EntdFavorito.TABLE_NAME,selection,selectionArgs);
        this.close();
    }

    public ArrayList<Integer> getFavoritos()
    {
        this.open("read");
        ArrayList<Integer> listFavoritos = new ArrayList<>();

        String[] projection = {
                DBContract.EntdFavorito._ID,
                DBContract.EntdFavorito.COLUMN_NAME_ID_FAVORITO
        };

        Cursor c = db.query(
                DBContract.EntdFavorito.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        boolean findEntd;

        while((findEntd = c.moveToNext()))
        {
            listFavoritos.add(c.getInt(c.getColumnIndex(DBContract.EntdFavorito.COLUMN_NAME_ID_FAVORITO)));
        }
        return listFavoritos;
    }



    private void close(){ db.close();}
}
