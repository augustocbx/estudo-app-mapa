package curso.mpgo.com.cursoandroid.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import curso.mpgo.com.cursoandroid.Ponto;

/**
 * Created by augustocbx on 5/11/16.
 */
public class PontoDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public PontoDbHelper(Context ctx) {
        super(ctx, PontoContract.TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Passa na primeira vez que o aplicativo for executado
        db.execSQL(PontoContract.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Quando mudar a versão para cima
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Quando mudar a versão para baixo
    }

    public void create(Ponto ponto){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(PontoContract.COLUMN_NAME_ENTRY_ID, ponto.id);
        cv.put(PontoContract.COLUMN_NAME_NAME, ponto.name);
        cv.put(PontoContract.COLUMN_NAME_LAT, ponto.latitude);
        cv.put(PontoContract.COLUMN_NAME_LNG, ponto.longitude);


        db.insert(PontoContract.TABLE_NAME, null, cv);
        db.close();
    }

    public void update(Ponto ponto){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(PontoContract.COLUMN_NAME_NAME, ponto.name);
        cv.put(PontoContract.COLUMN_NAME_LAT, ponto.latitude);
        cv.put(PontoContract.COLUMN_NAME_LNG, ponto.longitude);


        db.update(
                PontoContract.TABLE_NAME,
                cv,
                PontoContract.COLUMN_NAME_ENTRY_ID + " = ?",
                new String[]{""+ponto.id});
        db.close();

    }

    public void delete(Ponto ponto){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(
                PontoContract.TABLE_NAME,
                PontoContract.COLUMN_NAME_ENTRY_ID + " = ?",
                new String[]{""+ponto.id}
        );
        db.close();
    }

    public void clear(Ponto ponto){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(
                PontoContract.TABLE_NAME,
                null,
                null
        );
        db.close();
    }

    public List<Ponto> read(){
        SQLiteDatabase db = getReadableDatabase();
        List<Ponto> pontos = new ArrayList<>();

//        String[] campos = new String[]{
//                PontoContract.COLUMN_NAME_NAME,
//                PontoContract.COLUMN_NAME_LAT,
//                PontoContract.COLUMN_NAME_LNG
//        };

        Cursor cursor = db.query(
                PontoContract.TABLE_NAME,
                null, // campos a retornar
                null, // where
                null, // argumentos do where
                null, // group by
                null, // having
                PontoContract.COLUMN_NAME_NAME
        );

        while (cursor.moveToNext()){
            Ponto ponto = new Ponto(cursor.getDouble(cursor.getColumnIndex(PontoContract.COLUMN_NAME_LAT)), cursor.getDouble(cursor.getColumnIndex(PontoContract.COLUMN_NAME_LNG)));
            ponto.name = cursor.getString(cursor.getColumnIndex(PontoContract.COLUMN_NAME_NAME));
            pontos.add(ponto);
        }

        db.close();

        return pontos;
    }
}
