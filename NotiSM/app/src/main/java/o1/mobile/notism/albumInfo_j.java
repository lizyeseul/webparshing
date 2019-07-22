package o1.mobile.notism;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class albumInfo_j extends AppCompatActivity {

    playlist_DBHelper dbHelper;
    SQLiteDatabase db;
    String sql;
    Cursor cursor;

    final static String dbName = "Playlist.db";
    final static int dbVersion = 1;

    ListView trackList;
    Button back, confirm;
    Elements contents;
    Elements contentTrack;
    Document doc = null;
    Document docTrack = null;

    ArrayList<String> songInformation = new ArrayList<>();
    //ArrayAdapter adapter;
    add_Adapter_j adapter;

    String albumurl = "https://www.genie.co.kr/detail/albumInfo?axnm=";
    String trackUrlDefault = "https://www.genie.co.kr/detail/songInfo?xgnm=";
    String trackUrl = "https://www.genie.co.kr/detail/songInfo?xgnm=";
    String state = "album";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.albuminfo_x);

        back = findViewById(R.id.back);
        confirm = findViewById(R.id.confirm);
        trackList = findViewById(R.id.trackListView);


        //adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, songInformation);
        adapter = new add_Adapter_j();
        trackList.setAdapter(adapter);


        dbHelper = new playlist_DBHelper(this, dbName, null, dbVersion);

        Intent intent = getIntent();
        if(intent != null){
            state = intent.getStringExtra("state");
        }

        if(state.equals("album")){
            albumurl += intent.getIntExtra("numberList", 1);
            getTrackTime getTime = new getTrackTime();
            getTime.execute();
            adapter.notifyDataSetChanged();
        }
        else if(state.equals("sing")){
            trackUrl += intent.getIntExtra("numberList", 1);
            getSingTime getTime = new getSingTime();
            getTime.execute();
            adapter.notifyDataSetChanged();
        }

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener(){
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v){
                if(!songInformation.contains("Loading...")){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    addPlaylist();
                    intent.putExtra("first", "second");
                    startActivity(intent);
                }
            }
        });

        setData();
    }

    private void addPlaylist(){
        for(int i=0; i<songInformation.size(); i++){
            String temp = songInformation.get(i);

            String name = temp.substring(temp.indexOf("노래 : ")+5, temp.indexOf("길이 : ")-1);
            String length = temp.substring(temp.indexOf("길이 : ")+5, temp.indexOf("가수 : ")-1);
            String singer = temp.substring(temp.indexOf("가수 : ")+5, temp.indexOf("앨범 : ")-1);
            String album = temp.substring(temp.indexOf("앨범 : ")+5);

            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            //sName , sLength  , sSinger, sAlbum
            values.put("sName", name);
            values.put("sLength", length);
            values.put("sSinger", singer);
            values.put("sAlbum", album);
            db.insert("playlistDB", null, values);
        }
    }

    private void setData() {
        adapter.deleteAll();
        for (int i = 0; i < songInformation.size(); i++) {
            CustomDTO dto = new CustomDTO();
            dto.setSonginfo(songInformation.get(i));

            adapter.addItem(dto);
        }
    }

    public class CustomDTO{
        private String information;
        public void setSonginfo(String songInfo){
            this.information = songInfo;
        }
        public String getSonginfo(){
            return information;
        }
    }

    public class add_Adapter_j extends BaseAdapter {
        private ArrayList<CustomDTO> listCustom = new ArrayList<CustomDTO>();

        public  void deleteAll(){
            listCustom.removeAll(listCustom);
        }
        public int getCount(){
            return listCustom.size();
        }
        public Object getItem(int position) {
            return listCustom.get(position);
        }
        public long getItemId(int position){
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent){
            CustomViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_layout_x, null, false);

                holder = new CustomViewHolder();
                holder.addInfoTV = (TextView) convertView.findViewById(R.id.addInfoTV);

                convertView.setTag(holder);
            } else {
                holder = (CustomViewHolder) convertView.getTag();
            }

            CustomDTO dto = listCustom.get(position);

            holder.addInfoTV.setText(dto.getSonginfo());

            return convertView;
        }
        class CustomViewHolder {
            TextView addInfoTV;
        }

        public void addItem(CustomDTO dto) {
            listCustom.add(dto);
        }
    }

    public class getTrackTime extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                doc = Jsoup.connect(albumurl).get();
                contents = doc.select(".list-wrap > tbody > tr");
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (Element element : contents) {
                String temp = element.getElementsByAttribute("songid").toString();
                String songId = temp.substring(temp.indexOf("songid=")+8, temp.indexOf("songid=")+8+8);
                trackUrl = trackUrlDefault + songId;
                try {
                    docTrack = Jsoup.connect(trackUrl).get();

                    contentTrack = docTrack.select(".info-zone > .name");
                    String songInfo = "노래 : "+ contentTrack.get(0).text() +"\n";

                    contentTrack = docTrack.select(".info-data > li");
                    songInfo += "길이 : "+ contentTrack.get(3).text() +"\n";
                    songInfo += "가수 : "+ contentTrack.get(0).text() +"\n";
                    songInfo += "앨범 : "+ contentTrack.get(1).text();

                    songInformation.add(songInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            songInformation.add("Loading...");
            adapter.notifyDataSetChanged();
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            songInformation.remove("Loading...");
            adapter.notifyDataSetChanged();
            setData();
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    public class getSingTime extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                docTrack = Jsoup.connect(trackUrl).get();
            } catch (Exception e) {
                e.printStackTrace();
            }

            contentTrack = docTrack.select(".info-zone > .name");
            String songInfo = "노래 : "+ contentTrack.get(0).text() +"\n";

            contentTrack = docTrack.select(".info-data > li");
            songInfo += "길이 : "+ contentTrack.get(3).text()+"\n";
            songInfo += "가수 : "+ contentTrack.get(0).text() +"\n";
            songInfo += "앨범 : "+ contentTrack.get(1).text();

            songInformation.add(songInfo);

            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            songInformation.add("Loading...");
            adapter.notifyDataSetChanged();
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            songInformation.remove("Loading...");
            adapter.notifyDataSetChanged();
            setData();
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
