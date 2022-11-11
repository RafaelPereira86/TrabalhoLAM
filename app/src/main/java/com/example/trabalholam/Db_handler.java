package com.example.trabalholam;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Db_handler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "UTILIZADORES";
    private static final String TABLE_NAME = "utilizadores";

    private static final String LOGIN = "number";
    private static final String PASSWORD = "password";
    private static final String Funcao = "func";

    private Context context;

    public Db_handler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE" + " TABLE_NAME" + "(" + LOGIN + "INTEGER PRIMARY KEY," + PASSWORD + "TEXT" + Funcao + "TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    String listarNotas() {
        String query;
        Cursor cursor;
        String resultado;

        SQLiteDatabase db = this.getReadableDatabase();
        query = "select * FROM " + DATABASE_NAME;
        resultado = "";

        cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                resultado = String.format("%s %20s %20s\n", resultado, cursor.getString(1), cursor.getString(2));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return resultado;
    }

}
