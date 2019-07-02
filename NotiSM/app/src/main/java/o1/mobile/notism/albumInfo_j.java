package o1.mobile.notism;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class albumInfo_j extends AppCompatActivity {

    ListView trackList;
    Button button;
    Elements contents;
    Elements contentTrack;
    Document doc = null;
    Document docTrack = null;
    ArrayList<String> times = new ArrayList<>();
    ArrayAdapter adapter;
    String url = "https://www.genie.co.kr/detail/albumInfo?axnm=";
    String trackUrlDefault = "https://www.genie.co.kr/detail/songInfo?xgnm=";
    String trackUrl = "https://www.genie.co.kr/detail/songInfo?xgnm=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.albuminfo_x);

        button = findViewById(R.id.back);
        trackList = findViewById(R.id.trackListView);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,times);
        trackList.setAdapter(adapter);

        Intent intent = getIntent();
        if(intent != null){
            url += intent.getIntExtra("albumNum", 1);
        }

        getTrack gettrack = new getTrack();
        gettrack.execute();
        adapter.notifyDataSetChanged();

        button.setOnClickListener(new View.OnClickListener(){
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v){
                finish();
            }
        });
    }


    public class getTrack extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                doc = Jsoup.connect(url).get();
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
                    Log.d("logg-","url: "+trackUrl);
                    contentTrack = docTrack.select(".info-data > li");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                times.add(contentTrack.get(3).text());
                Log.d("logg-","times: "+contentTrack.get(3).text());
            }

            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
