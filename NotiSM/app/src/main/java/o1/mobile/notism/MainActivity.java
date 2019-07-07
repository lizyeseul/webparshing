package o1.mobile.notism;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    playlist_DBHelper dbHelper;
    SQLiteDatabase db;
    String sql;
    Cursor cursor;

    final static String dbName = "Playlist.db";
    final static int dbVersion = 1;

    ListView playlistView;
    Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playlistView = findViewById(R.id.playlist);
        addBtn = findViewById(R.id.addBtn);

        dbHelper = new playlist_DBHelper(this, dbName, null, dbVersion);

        selectDB();

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
    }

    private void selectDB(){
        db = dbHelper.getWritableDatabase();
        sql = "SELECT * FROM playlistDB;";

        cursor = db.rawQuery(sql, null);
        if(cursor.getCount() > 0){
            startManagingCursor(cursor);
            playlist_DBAdapter dbAdapter = new playlist_DBAdapter(this, cursor);
            playlistView.setAdapter(dbAdapter);
        }
    }
}
