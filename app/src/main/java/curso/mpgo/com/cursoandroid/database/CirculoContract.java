package curso.mpgo.com.cursoandroid.database;

/**
 * Created by augustocbx on 5/11/16.
 */
public interface CirculoContract {
    String TABLE_NAME = "circleentry";
    String COLUMN_NAME_ENTRY_ID = "entryid";
    String COLUMN_NAME_RAIO = "raio";
    String COLUMN_NAME_LAT = "latitude";
    String COLUMN_NAME_LNG = "longitude";
    String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_NAME_ENTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_NAME_RAIO + " INTEGER," +
                    COLUMN_NAME_LAT + " REAL," +
                    COLUMN_NAME_LNG + " REAL)";

}
