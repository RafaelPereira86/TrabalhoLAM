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
    public static final int DATABASE_VERSION = 9;
    public static final String DATABASE_NAME = "mydatabase";


    //NOME DAS TABELAS
    public static final String DB_AL_TABLE = "AL";
    public static final String DB_UC_TABLE = "UC";
    public static final String DB_AL_UC_NOTAS_TABLE = "NOTA";
    public static final String DB_AL_UC_HORARIO_TABLE = "HR";
    public static final String DB_AL_UC_INS = "UC_INS";

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

    //TABELA INCRIÇÃO
    public static final String UC_INSCRICAO_ID = "UC_INS_ID";
    public static final String AL_UCINCRICAO_NUM = "NUM_ALUNO";
    public static final String UC_UCINSCRICAO_COD = "COD_UC";


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
        String queryUCInscritaTable = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s INTEGER,%s INTEGER, FOREIGN KEY(%s) REFERENCES %s(%s), FOREIGN KEY(%s) REFERENCES %s(%s))",DB_AL_UC_INS, UC_INSCRICAO_ID, AL_UCINCRICAO_NUM, UC_UCINSCRICAO_COD, AL_UCINCRICAO_NUM, DB_AL_TABLE, ALNUM, UC_UCINSCRICAO_COD, DB_UC_TABLE, UCCOD);
        db.execSQL(queryTabelaAl);
        db.execSQL(queryTabelaUc);
        db.execSQL(queryTabelaHorario);
        db.execSQL(queryTabelaNota);
        db.execSQL(queryUCInscritaTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_UC_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DB_AL_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DB_AL_UC_INS);
        db.execSQL("DROP TABLE IF EXISTS " + DB_AL_UC_HORARIO_TABLE);
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", DB_AL_UC_NOTAS_TABLE));
        onCreate(db);
    }

    public ArrayList<String> getInscr(String token){
        ArrayList<String>insc = new ArrayList<>();
        ArrayList<Integer>ucCod = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        int al = checkToken(token);
        String query = String.format("SELECT %s FROM %s WHERE %s = %s", UC_UCINSCRICAO_COD, DB_AL_UC_INS,AL_UCINCRICAO_NUM, al);
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                ucCod.add(cursor.getInt(0));
            }while (cursor.moveToNext());
        }
        cursor.close();
        for (int i = 0; i < ucCod.size(); i++) {
            if(insc.contains(getUc(ucCod.get(i)).getUc())){
            }else{
                insc.add(getUc(ucCod.get(i)).getUc());
            }
        }
        return insc;
    }


    public String checkUser(String num, String pass) {
        SQLiteDatabase db = getReadableDatabase();
        String queryCheck = "SELECT * FROM " + DB_AL_TABLE;
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(queryCheck,null);
        }
        if(cursor.getCount() != 0){
            while(cursor.moveToNext()){
                if(num.equalsIgnoreCase(String.valueOf(cursor.getInt(0))) && pass.equalsIgnoreCase(cursor.getString(1))){
                    return cursor.getString(2);
                }
            }
        }
        return "";
    }

    public void addAluno(Aluno a)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryAdd = String.format("INSERT INTO %s(%s,%s,%s) VALUES ('%s','%s','%s'); ", DB_AL_TABLE, ALNUM, ALPASSWORD, ALTOKEN, a.getNumber(), a.getPassword(), a.getToken());
        db.execSQL(queryAdd);
    }

    public void addNota(Nota n) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = String.format("INSERT INTO %s(%s,%s,%s) VALUES('%s','%s','%s');", DB_AL_UC_NOTAS_TABLE, AL_NOTA_NUM, UC_NOTA_COD, NOTA, n.getnAluno(), n.getCodUc(), n.getNota());
        db.execSQL(query);
    }

    public void addInscr(InscricaoAl inscricaoAl) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = String.format("INSERT INTO %s(%s,%s) VALUES(%s,%s)", DB_AL_UC_INS, AL_UCINCRICAO_NUM, UC_UCINSCRICAO_COD, inscricaoAl.getAlunoID(), inscricaoAl.getUcID());
        db.execSQL(query);
        db.close();
    }

    public void addUc(Disciplina c) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (checkPorUmaUc(c.getCodUc())) {
            String query = String.format("INSERT INTO %s(%s,%s) VALUES('%s','%s');", DB_UC_TABLE, UCCOD, UCNOME, c.getCodUc(), c.getUc());
            db.execSQL(query);
        }
    }

    public void addHorario(Horario h) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = String.format("INSERT INTO %s(%s,%s,%s,%s,%s,%s) VALUES('%s','%s','%s','%s','%s','%s');", DB_AL_UC_HORARIO_TABLE, AL_HORARIO_NUM, UC_HORARIO_COD, HORARIODIA, HORIRIOINI, HORARIOFIN, HORARIOTIPO, h.getNumAluno(), h.getCodigoUC(), h.getDiaSemana(), h.getHoraInicio(), h.getHoraFim(), h.getTipoAula());
        db.execSQL(query);
    }

    public Disciplina getUc(int CodUc){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("SELECT * FROM %s WHERE %s = %s", DB_UC_TABLE,UCCOD,CodUc);
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            Disciplina d = new Disciplina(cursor.getInt(0), cursor.getString(1).replace("\n",""));
            return d;
        }
        return null;
    }
    public ArrayList<Nota> getNotas(String token){
        ArrayList<Nota> n = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        int numAl = checkToken(token);
        String query = String.format("SELECT * FROM %s WHERE %s = %s", DB_AL_UC_NOTAS_TABLE, numAl, AL_NOTA_NUM);
        Cursor c = db.rawQuery(query,null);
        if(c.moveToFirst()){
            do{
                Nota nota = new Nota();
                nota.setnAluno(c.getInt(1));
                nota.setCodUc(c.getInt(2));
                nota.setNota(c.getInt(3));
                n.add(nota);
            }while (c.moveToNext());
        }
        return n;
    }

    public ArrayList<Horario> getHorAl(String tokenAl){
        ArrayList<Horario> hs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        int alNum = checkToken(tokenAl);
        String query = String.format("SELECT * FROM %s WHERE %s = %s ", DB_AL_UC_HORARIO_TABLE, AL_HORARIO_NUM,alNum);
        Cursor c = db.rawQuery(query,null);
        if(c.moveToFirst()){
            do{
                Horario h = new Horario();
                h.setNumAluno((c.getInt(1)));
                h.setCodigoUC(c.getInt(2));
                h.setDiaSemana(c.getInt(3));
                h.setHoraInicio(c.getInt(4));
                h.setHoraFim(c.getInt(5));
                h.setTipoAula(c.getString(6));
                hs.add(h);
            }while(c.moveToNext());
        }
            return hs;
        }

        public int checkToken(String token) {
        SQLiteDatabase db = this.getReadableDatabase();
        String querycheck = String.format("SELECT * FROM %s WHERE %s = '%s'",DB_AL_TABLE,ALTOKEN, token);
        Cursor c = db.rawQuery(querycheck,null);
        if(c.moveToFirst()){
            int alNum = c.getInt(0);
            return alNum;
    }else{
            return -1;
        }
    }

    public boolean checkPorUmaUc(int codUc){
        SQLiteDatabase db = this.getReadableDatabase();
        String querycheck = String.format("SELECT * FROM %s WHERE %s = %s", DB_UC_TABLE,UCCOD,codUc);
        Cursor c = db.rawQuery(querycheck, null);
        return  c.getCount() == 0;
    }
}

