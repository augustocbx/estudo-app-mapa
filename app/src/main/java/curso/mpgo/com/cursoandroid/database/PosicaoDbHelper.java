package curso.mpgo.com.cursoandroid.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import curso.mpgo.com.cursoandroid.Posicao;

/**
 * Created by augustocbx on 5/11/16.
 */
public class PosicaoDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public PosicaoDbHelper(Context ctx) {
        super(ctx, PosicaoContract.TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Passa na primeira vez que o aplicativo for executado
        db.execSQL(PosicaoContract.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Quando mudar a versão para cima
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Quando mudar a versão para baixo
    }

    public void create(Posicao posicao){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(PosicaoContract.COLUMN_NAME_NAME, posicao.name);
        cv.put(PosicaoContract.COLUMN_NAME_LAT, posicao.latitude);
        cv.put(PosicaoContract.COLUMN_NAME_LNG, posicao.longitude);


        db.insert(PosicaoContract.TABLE_NAME, null, cv);
        db.close();
    }

    public void update(Posicao posicao){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(PosicaoContract.COLUMN_NAME_NAME, posicao.name);
        cv.put(PosicaoContract.COLUMN_NAME_LAT, posicao.latitude);
        cv.put(PosicaoContract.COLUMN_NAME_LNG, posicao.longitude);


        db.update(
                PosicaoContract.TABLE_NAME,
                cv,
                PosicaoContract.COLUMN_NAME_ENTRY_ID + " = ?",
                new String[]{""+ posicao.id});
        db.close();

    }

    public void delete(Posicao posicao){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(
                PosicaoContract.TABLE_NAME,
                PosicaoContract.COLUMN_NAME_ENTRY_ID + " = ?",
                new String[]{""+ posicao.id}
        );
        db.close();
    }

    public void clear(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(
                PosicaoContract.TABLE_NAME,
                null,
                null
        );
        db.close();
    }

    public List<Posicao> read(){
        SQLiteDatabase db = getReadableDatabase();
        List<Posicao> posicaos = new ArrayList<>();

//        String[] campos = new String[]{
//                PosicaoContract.COLUMN_NAME_NAME,
//                PosicaoContract.COLUMN_NAME_LAT,
//                PosicaoContract.COLUMN_NAME_LNG
//        };

        Cursor cursor = db.query(
                PosicaoContract.TABLE_NAME,
                null, // campos a retornar
                null, // where
                null, // argumentos do where
                null, // group by
                null, // having
                PosicaoContract.COLUMN_NAME_NAME
        );

        while (cursor.moveToNext()){
            Posicao posicao = new Posicao(cursor.getDouble(cursor.getColumnIndex(PosicaoContract.COLUMN_NAME_LAT)), cursor.getDouble(cursor.getColumnIndex(PosicaoContract.COLUMN_NAME_LNG)));
            posicao.name = cursor.getString(cursor.getColumnIndex(PosicaoContract.COLUMN_NAME_NAME));
            posicaos.add(posicao);
        }

        db.close();

        return posicaos;
    }
}
