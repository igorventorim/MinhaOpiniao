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
        public static final String COLUMN_NAME_FOTO = "foto";
    }

    public static abstract class EntdEstabelecimento implements BaseColumns
    {
        public static final String TABLE_NAME = "estabelecimento";
        public static final String COLUMN_NAME_NOME = "nome";
        public static final String COLUMN_NAME_TIPO = "tipo";
        public static final String COLUMN_NAME_ESTADO = "estado";
        public static final String COLUMN_NAME_CIDADE = "cidade";
        public static final String COLUMN_NAME_BAIRRO = "bairro";
        public static final String COLUMN_NAME_FOTO = "foto";
        public static final String COLUMN_NAME_RESPONSAVEL = "responsavel";
        public static final String COLUMN_NAME_NOTIFICACAO = "notificacao";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_GPS = "gps";
        public static final String COLUMN_NAME_RATE = "rate";
        public static final String COLUMN_NAME_NUM_AVALIATION = "numAvaliacao";
    }

//    public static abstract class EntdAvaliacao implements BaseColumns
//    {
//        public static final String TABLE_NAME = "avaliacao";
//        public static final String COLUMN_NAME_ID_ESTABELECIMENTO = "idEstabelecimento";
////        public static final String COLUMN_NAME_NUM_AVALIACOES
//
//    }



}
