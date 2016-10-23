package com.cursoandroid.myopinion.database;

/**
 * Created by igor on 19/10/16.
 */

public class SqlStrings {

    static final String REAL_TYPE = " REAL";

    static final String TEXT_TYPE = " TEXT";

    static final String INTEGER_TYPE = " INTEGER";

    static final String BLOB = " BLOB";

    static final String BOOLEAN_TYPE = " BOOLEAN";

    static final String NOT_NULL = " NOT NULL";

    static final String COMMA_SEP = ",";

    static final String SQL_CREATE_TABLE_USUARIO =
            "CREATE TABLE IF NOT EXISTS " + DBContract.EntdUsuario.TABLE_NAME+ "("+
            DBContract.EntdUsuario._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+
            DBContract.EntdUsuario.COLUMN_NAME_NOME + TEXT_TYPE+COMMA_SEP +
            DBContract.EntdUsuario.COLUMN_NAME_EMAIL + TEXT_TYPE+COMMA_SEP +
            DBContract.EntdUsuario.COLUMN_NAME_CEP + TEXT_TYPE+COMMA_SEP +
            DBContract.EntdUsuario.COLUMN_NAME_DATA_NASC + TEXT_TYPE+COMMA_SEP +
            DBContract.EntdUsuario.COLUMN_NAME_SENHA + TEXT_TYPE+COMMA_SEP +
            DBContract.EntdUsuario.COLUMN_NAME_FOTO + BLOB+" )";


    public static final String SQL_DELETE_TABLE_USUARIO = "DROP TABLE IF EXISTS " + DBContract.EntdUsuario.TABLE_NAME;

    static final String SQL_CREATE_TABLE_ESTABELECIMENTO =
            "CREATE TABLE IF NOT EXISTS " + DBContract.EntdEstabelecimento.TABLE_NAME+ "("+
            DBContract.EntdEstabelecimento._ID +" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+
            DBContract.EntdEstabelecimento.COLUMN_NAME_NOME+ TEXT_TYPE + COMMA_SEP+
            DBContract.EntdEstabelecimento.COLUMN_NAME_TIPO+ TEXT_TYPE + COMMA_SEP+
            DBContract.EntdEstabelecimento.COLUMN_NAME_ESTADO+ TEXT_TYPE + COMMA_SEP+
            DBContract.EntdEstabelecimento.COLUMN_NAME_CIDADE+ TEXT_TYPE + COMMA_SEP+
            DBContract.EntdEstabelecimento.COLUMN_NAME_BAIRRO+ TEXT_TYPE + COMMA_SEP+
            DBContract.EntdEstabelecimento.COLUMN_NAME_FOTO + BLOB+ COMMA_SEP+
            DBContract.EntdEstabelecimento.COLUMN_NAME_RESPONSAVEL + TEXT_TYPE + COMMA_SEP+
            DBContract.EntdEstabelecimento.COLUMN_NAME_NOTIFICACAO + BOOLEAN_TYPE + COMMA_SEP+
            DBContract.EntdEstabelecimento.COLUMN_NAME_GPS + BOOLEAN_TYPE + COMMA_SEP+
            DBContract.EntdEstabelecimento.COLUMN_NAME_RATE+ REAL_TYPE + COMMA_SEP+
            DBContract.EntdEstabelecimento.COLUMN_NAME_LATITUDE+ REAL_TYPE + COMMA_SEP+
            DBContract.EntdEstabelecimento.COLUMN_NAME_LONGITUDE+ REAL_TYPE + COMMA_SEP +
            DBContract.EntdEstabelecimento.COLUMN_NAME_NUM_AVALIATION + INTEGER_TYPE +" )";

    static final  String SQL_DELETE_TABLE_ESTABELECIMENTO = "DROP TABLE IF EXISTS " + DBContract.EntdEstabelecimento.TABLE_NAME;
}
