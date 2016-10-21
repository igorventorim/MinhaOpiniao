package com.cursoandroid.myopinion.database;

/**
 * Created by igor on 19/10/16.
 */

public class SqlStrings {

    static final String TEXT_TYPE = " TEXT";

    static final String BLOB = " BLOB";

    static final String BOOLEAN_TYPE = " BOOLEAN";

    static final String COMMA_SEP = ",";

    static final String SQL_CREATE_TABLE_USUARIO =
            "CREATE TABLE " + DBContract.EntdUsuario.TABLE_NAME+ "("+
            DBContract.EntdUsuario._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+
            DBContract.EntdUsuario.COLUMN_NAME_NOME+ TEXT_TYPE+COMMA_SEP+
            DBContract.EntdUsuario.COLUMN_NAME_EMAIL+ TEXT_TYPE+COMMA_SEP+
            DBContract.EntdUsuario.COLUMN_NAME_CEP+TEXT_TYPE+COMMA_SEP+
            DBContract.EntdUsuario.COLUMN_NAME_DATA_NASC+TEXT_TYPE+COMMA_SEP+
            DBContract.EntdUsuario.COLUMN_NAME_SENHA+TEXT_TYPE+COMMA_SEP+
            DBContract.EntdUsuario.COLUMN_NAME_FOTO + BLOB+ " )";


    public static final String SQL_DELETE_TABLE_USUARIO =
            "DROP TABLE IF EXISTS " + DBContract.EntdUsuario.TABLE_NAME;
}
