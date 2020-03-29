package o1.mobile.notism;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class playlist_DBAdapter extends CursorAdapter {
    public playlist_DBAdapter(Context context, Cursor c){
        super(context, c);
    }

    playlist_DBHelper dbHelper;
    SQLiteDatabase db;
    final static String dbName = "Playlist.db";
    final static int dbVersion = 1;

    @Override
    public void bindView(View view, Context context, Cursor cursor){
        final Cursor tempC = cursor;
        //bookNum integer, title string, author string, priority integer
        final TextView sNameTV = view.findViewById(R.id.sNameTV);
        final TextView sLengthTV = view.findViewById(R.id.sLengthTV);
        final TextView sSingerTV = view.findViewById(R.id.sSingerTV);
        final TextView sTimesTV = view.findViewById(R.id.sTimes);

        final String name = cursor.getString(cursor.getColumnIndex("sName"));
        final String singer = cursor.getString(cursor.getColumnIndex("sSinger"));
        final int times = cursor.getInt(cursor.getColumnIndex("sTimes"));
        Log.d("valuecheck1",Integer.toString(times));

        sNameTV.setText(name);
        sSingerTV.setText(singer);
        sLengthTV.setText(cursor.getString(cursor.getColumnIndex("sLength")));
        sTimesTV.setText(cursor.getString(cursor.getColumnIndex("sTimes")));

        dbHelper = new playlist_DBHelper(context, dbName, null, dbVersion);
        db = dbHelper.getWritableDatabase();

        final Button minusTimes = view.findViewById(R.id.mainMinusTimes);
        minusTimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int time = times;
                if(time>0){
                    time--;
                }
                ContentValues values = new ContentValues();
                values.put("sTimes", time);
                db.update("playlistDB", values, "sName = ? and sSinger = ?",new String[] {name, singer});

                sTimesTV.setText(Integer.toString(time));
                notifyDataSetChanged();
            }
        });

        final Button plusTimes = view.findViewById(R.id.mainPlusTimes);
        plusTimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int time = times;
                if(time<30){
                    time++;
                }
                ContentValues values = new ContentValues();
                values.put("sTimes", time);
                db.update("playlistDB", values, "sName = ? and sSinger = ?",new String[] {name, singer});
                sTimesTV.setText(Integer.toString(time));
                notifyDataSetChanged();
            }
        });
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.playlist_layout_x, parent, false);
        return v;
    }
}
