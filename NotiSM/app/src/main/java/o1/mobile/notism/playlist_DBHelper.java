package o1.mobile.notism;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class playlist_DBHelper extends SQLiteOpenHelper {
    String sql;

    public playlist_DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //sName , sLength  , sSinger, sAlbum, priority, sTimes
        sql = "CREATE TABLE playlistDB (_id INTEGER PRIMARY KEY AUTOINCREMENT," + " sName TEXT, sLength TEXT, sSinger TEXT, sAlbum TEXT/*, priority INTEGER*/, sTimes INTEGER);";
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //버전이 업그레이드 됐을 경우 작업할 내용을 작성합니다.
    }
}
