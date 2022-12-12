package com.example.trabalholam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class Db_handler extends SQLiteOpenHelper {

    Context context;
    //DEFINICOES BASE DE DADODS
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "mydatabase";


    //NOME DAS TABELAS
    public static final String DB_AL_TABLE = "AL";
    public static final String DB_UC_TABLE = "UC";
    public static final String DB_AL_UC_NOTAS_TABLE = "NOTA";
    public static final String DB_AL_UC_HORARIO_TABLE = "HR";


    //TABELA ALUNO
    public static final String ALNUM = "ALNUM";
    public static final String ALPASSWORD = "ALPASSWORD";
    public static final String ALTOKEN = "ALTOKEN";

    //TABELA UC
    public static final String UCCOD = "UCCOD";
    public static final String UCNOME = "UCNOME";

    //TABELA HORARIO
    public static final String HORARIOID = "HORARIOID";
    public static final String AL_HORARIO_NUM = "NUMALUNO";
    public static final String UC_HORARIO_COD = "UC_HORARIO_COD";
    public static final String HORARIODIA = "HORARIODIA";
    public static final String HORIRIOINI = "HORIRIOINI";
    public static final String HORARIOFIN = "HORARIOFIN";
    public static final String HORARIOTIPO = "HORARIOTIPO";

    //TABELA NOTA
    public static final String NOTAID = "NOTA_ID";
    public static final String AL_NOTA_NUM = "NUMALUNO";
    public static final String UC_NOTA_COD = "COD_UC";
    public static final String NOTA = "NOTA";


    public Db_handler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryTabelaAl = String.format("CREATE TABLE %s(%s INTERGER PRIMARY KEY,%s TEXT,%s TEXT)", DB_AL_TABLE, ALNUM, ALPASSWORD, ALTOKEN);
        String queryTabelaUc = String.format("CREATE TABLE %s(%s INTERGER PRIMARY KEY,%s TEXT)", DB_UC_TABLE, UCCOD, UCNOME);
        String queryTabelaHorario = String.format("CREATE TABLE %s(%s INTERGER PRIMARY KEY,%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT)", DB_AL_UC_HORARIO_TABLE, HORARIOID, AL_HORARIO_NUM, UC_HORARIO_COD, HORARIODIA, HORIRIOINI, HORARIOFIN, HORARIOTIPO);
        String queryTabelaNota = String.format("CREATE TABLE %s(%s INTERGER PRIMARY KEY,%s TEXT,%s TEXT,%s TEXT)", DB_AL_UC_NOTAS_TABLE, NOTAID, AL_NOTA_NUM, UC_NOTA_COD, NOTA);
        db.execSQL(queryTabelaAl);
        db.execSQL(queryTabelaUc);
        db.execSQL(queryTabelaHorario);
        db.execSQL(queryTabelaNota);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_AL_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DB_UC_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DB_AL_UC_HORARIO_TABLE);
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", DB_AL_UC_NOTAS_TABLE));
        onCreate(db);
    }

    public boolean insertNotas(Notas Value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("uc", Integer.parseInt(Value.getUc()));
        cv.put("nota", Integer.parseInt(Value.getNota()));
        return db.insert("Notas", null, cv) > 0;
    }

    public boolean inserthorario(Horario Value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("diaSemana", Integer.parseInt(Value.getDiaSemana()));
        cv.put("horaInicio", Integer.parseInt(Value.getHoraInicio()));
        cv.put("horaFim", Integer.parseInt(Value.getHoraFim()));
        cv.put("codigoUC", Integer.parseInt(Value.getCodigoUC()));
        cv.put("tipoAula", Value.getTipoAula());
        return db.insert("horario", null, cv) > 0;
    }

    public boolean insertdisciplina(Disciplina Value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("uc", Integer.parseInt(Value.getUc()));
        return db.insert("disciplina", null, cv) > 0;
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


    String listarUcs(){
        String query;
        Cursor cursor;
        String resultado;

        SQLiteDatabase db = this.getReadableDatabase();
        query = "select * from " + DATABASE_NAME;
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

    String listarHorario(){
        String query;
        Cursor cursor;
        String resultado;
        SQLiteDatabase db = this.getReadableDatabase();
        query = "select * from " + DATABASE_NAME;
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

