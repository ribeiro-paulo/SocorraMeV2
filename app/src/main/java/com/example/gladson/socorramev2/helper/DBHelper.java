package com.example.gladson.socorramev2.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    public static int VERSION = 1;
    public static String DB_NAME = "db_help_me";
    public static String TABLE_CONTACTS = "contacts";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Cria a tabela de Contatos de Emergência.
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_CONTACTS +
                " (name TEXT NOT NULL PRIMARY KEY, " +
                "number TEXT NOT NULL);";

        try {
            db.execSQL(sql);
        } catch (Exception e) {
            Log.i("INFO DB", "Falha na criação da Tabela do Banco de Dados");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
