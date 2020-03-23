package o1.mobile.notism;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class playlist_DBAdapter extends CursorAdapter {
    public playlist_DBAdapter(Context context, Cursor c){
        super(context, c);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor){
        //bookNum integer, title string, author string, priority integer
        final TextView sNameTV = view.findViewById(R.id.sNameTV);
        final TextView sLengthTV = view.findViewById(R.id.sLengthTV);
        final TextView sSingerTV = view.findViewById(R.id.sSingerTV);
        //final TextView sPriorityTV = view.findViewById(R.id.sPri);
        final TextView sTimesTV = view.findViewById(R.id.sTimes);

        sNameTV.setText(cursor.getString(cursor.getColumnIndex("sName")));
        sLengthTV.setText(cursor.getString(cursor.getColumnIndex("sLength")));
        sSingerTV.setText(cursor.getString(cursor.getColumnIndex("sSinger")));
        //sPriorityTV.setText(cursor.getString(cursor.getColumnIndex("priority")));
        sTimesTV.setText(cursor.getString(cursor.getColumnIndex("sTimes")));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.playlist_layout_x, parent, false);
        return v;
    }
}
