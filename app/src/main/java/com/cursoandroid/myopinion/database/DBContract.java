package com.cursoandroid.myopinion.database;

import android.provider.BaseColumns;

/**
 * Created by igor on 19/10/16.
 */

public final class DBContract {

    public DBContract(){}

    public static abstract class EntdUsuario implements BaseColumns
    {
        public static final String TABLE_NAME = "usuario";
        public static final String COLUMN_NAME_NOME = "nome";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_CEP = "cep";
        public static final String COLUMN_NAME_DATA_NASC = "dataNasc";
        public static final String COLUMN_NAME_SENHA = "senha";
    }


}
