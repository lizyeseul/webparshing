package o1.mobile.notism;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    playlist_DBHelper dbHelper;
    SQLiteDatabase db;
    String sql;
    Cursor cursor;
    playlist_DBAdapter dbAdapter;

    final static String dbName = "Playlist.db";
    final static int dbVersion = 1;

    static ListView playlistView;
    Button addBtn, resetBtn;
    TextView totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playlistView = findViewById(R.id.playlist);
        addBtn = findViewById(R.id.addBtn);
        resetBtn = findViewById(R.id.resetBtn);
        totalTime = findViewById(R.id.totalTime);

        dbHelper = new playlist_DBHelper(this, dbName, null, dbVersion);
        dbAdapter = new playlist_DBAdapter(this, cursor);
        playlistView.setAdapter(dbAdapter);

        selectDB();
        calTime();

        Intent intent = getIntent();
        if(intent.getStringExtra("first") != null){
            //intent 내용 있을 때
        }

        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), search_j.class);
                startActivity(intent);
            }
        });
        resetBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                resetDB();
                selectDB();
                calTime();
            }
        });
    }

    private void resetDB(){
        db = dbHelper.getReadableDatabase();
        db.execSQL("delete from playlistDB");
    }

    private void selectDB(){
        db = dbHelper.getWritableDatabase();
        sql = "SELECT * FROM playlistDB;";

        cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            startManagingCursor(cursor);
        }
        dbAdapter.changeCursor(cursor);
        dbAdapter.notifyDataSetChanged();
    }

    private void calTime(){
        db = dbHelper.getReadableDatabase();
        sql = "SELECT sLength, sTimes FROM playlistDB;";

        cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        int totalH=0, totalM=0;
        while(!cursor.isAfterLast()){
            String time = cursor.getString(cursor.getColumnIndex("sLength"));
            int times = cursor.getInt(cursor.getColumnIndex("sTimes"));
            for(int i=0; i<times; i++){
                totalH += Integer.parseInt(time.substring(0,time.indexOf(":")));
                totalM += Integer.parseInt(time.substring(time.indexOf(":")+1));
            }
            cursor.moveToNext();
        }
        totalH += totalM/60;
        totalM = totalM%60;
        totalTime.setText(totalH+":"+totalM);
    }
}
