package o1.mobile.notism;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.util.ArrayList;

public class search_j extends AppCompatActivity {

    ListView searchListView;
    Button searchSingerBtn, searchAlbumBtn, searchSingBtn, searchCancelBtn;
    EditText searchWord;
    ArrayAdapter adapter;
    ArrayList<String> items = new ArrayList<String>();
    ArrayList<Integer> numberList = new ArrayList<>();

    Elements contents;
    Document doc = null;
    String url;
    String word;
    String state = "album";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_x);

        searchSingerBtn = findViewById(R.id.searchSingerBtn);
        searchAlbumBtn = findViewById(R.id.searchAlbumBtn);
        searchSingBtn = findViewById(R.id.searchSingBtn);
        searchCancelBtn = findViewById(R.id.searchCancelBtn);
        searchWord = findViewById(R.id.searchWord);
        searchListView = findViewById(R.id.searchListView);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,items);
        searchListView.setAdapter(adapter);

        searchAlbumBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                setUrl("singer");
                SearchSingerAT searchThread = new SearchSingerAT();
                searchThread.execute();
                state = "singer";
            }
        });
        searchAlbumBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                setUrl("album");
                SearchAlbumAT searchThread = new SearchAlbumAT();
                searchThread.execute();
                state = "album";
            }
        });
        searchSingBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                setUrl("sing");
                SearchSongAT searchThread = new SearchSongAT();
                searchThread.execute();
                state = "sing";
            }
        });
        searchCancelBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        searchWord.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent event){
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    searchAlbumBtn.performClick();
                    return true;
                }
                return false;
            }
        });

        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), albumInfo_j.class);
                intent.putExtra("numberList", numberList.get(position));
                intent.putExtra("state", state);
                startActivity(intent);
            }
        });
    }

    private void setUrl(String state){
        try{
            word =  URLEncoder.encode(searchWord.getText().toString());
            int i=0;
            while(i<word.length()){
                char c=word.charAt(i);
                if(c == '%'){
                    word = word.substring(0,i+1)+"25"+word.substring(i+1);
                }
                i++;
            }
            if(state.equals("singer")){
                url = "https://www.genie.co.kr/search/searchMain?query="+ word+"&Coll=";
            } else if(state.equals("album")){
                url = "https://www.genie.co.kr/search/searchAlbum?query="+ word+"&Coll=";
            } else if(state.equals("sing")){
                url = "https://www.genie.co.kr/search/searchSong?query="+ word+"&Coll=";
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public class SearchSingerAT extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            try{
                Log.d("loggUrl","url: "+url);
                doc = Jsoup.connect(url).get();
                contents = doc.select(".ellipsis");
            } catch (Exception e){
                e.printStackTrace();
            }

            items.removeAll(items);

            int count=0;
            String albumName ="";
            String albumNum ="";
            numberList.removeAll(numberList);
            for (Element element : contents) {
                count++;
                if(count%3 == 1){
                    albumName = "앨범 : "+element.text()+ "\n";
                    albumNum = element.getElementsByAttribute("onclick").toString();
                    albumNum = albumNum.substring(albumNum.indexOf("Layer")+7, albumNum.indexOf("Layer")+8+7) ;

                    numberList.add(Integer.parseInt(albumNum));
                }
                else if(count%3 == 2){
                    albumName += "title : "+ element.text().substring(5)+ "\n";
                }
                else if(count%3 == 0){
                    albumName += "가수 : "+ element.text();
                    items.add(albumName);
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            items.add("loading...");
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    public class SearchAlbumAT extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            try{
                Log.d("loggUrl","url: "+url);
                doc = Jsoup.connect(url).get();
                contents = doc.select(".ellipsis");
            } catch (Exception e){
                e.printStackTrace();
            }

            String temp = contents.text();
            items.removeAll(items);

            int count=0;
            String albumName ="";
            String albumNum ="";
            numberList.removeAll(numberList);
            for (Element element : contents) {
                count++;
                if(count%3 == 1){
                    albumName = "앨범 : "+element.text()+ "\n";
                    albumNum = element.getElementsByAttribute("onclick").toString();
                    albumNum = albumNum.substring(albumNum.indexOf("Layer")+7, albumNum.indexOf("Layer")+8+7) ;

                    numberList.add(Integer.parseInt(albumNum));
                }
                else if(count%3 == 2){
                    albumName += "title : "+ element.text().substring(5)+ "\n";
                }
                else if(count%3 == 0){
                    albumName += "가수 : "+ element.text();
                    items.add(albumName);
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            items.add("loading...");
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    public class SearchSongAT extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            try{
                Log.d("loggUrl","url: "+url);
                doc = Jsoup.connect(url).get();
                contents = doc.select("tr.list > .info");
            } catch (Exception e){
                e.printStackTrace();
            }

            String temp = contents.text();
            items.removeAll(items);

            numberList.removeAll(numberList);
            for (Element element : contents) {
                String songInfo ="";
                String songNum ="";

                String songName = element.select(".title").text();
                if(songName.contains("TITLE")){
                    songName = songName.substring(6);
                }
                songInfo += "노래 : "+songName+ "\n";

                String songArtist = element.select(".artist").text();
                songInfo += "가수 : "+songArtist+ "\n";

                String songAlbum = element.select(".albumtitle").text();
                songInfo += "앨범 : "+songAlbum;

                songNum = element.getElementsByAttribute("onclick").toString();
                songNum = songNum.substring(songNum.indexOf("fnPlaySong")+12, songNum.indexOf("fnPlaySong")+12+8) ;
                Log.d("loggUrl","num: "+songNum);

                numberList.add(Integer.parseInt(songNum));
                items.add(songInfo);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            items.add("loading...");
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
