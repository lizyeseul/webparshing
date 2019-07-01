package o1.mobile.notism;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

    TextView searchList;
    ListView searchListView;
    Button searchBtn;
    EditText searchWord;
    ArrayAdapter adapter;
    ArrayList<String> items = new ArrayList<String>();

    Elements contents;
    Document doc = null;
    String Top10="null";
    String url;
    String word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_x);

        searchList = findViewById(R.id.searchList);
        searchBtn = findViewById(R.id.searchBtn);
        searchWord = findViewById(R.id.searchWord);
        searchListView = findViewById(R.id.searchListView);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,items);
        searchListView.setAdapter(adapter);

        setUrl();

        searchBtn.setOnClickListener(new View.OnClickListener(){
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v){
                setUrl();
                SearchAT searchThread = new SearchAT();
                searchThread.execute();
            }
        });
    }

    private void setUrl(){
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
            url = "https://www.genie.co.kr/search/searchAlbum?query="+ word+"&Coll=";
            searchList.setText(url);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public class SearchAT extends AsyncTask<Void, Void, Void>{
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
            Log.d("logg0","temp: "+temp);
            Top10 = "";
            Top10 += contents.size() + "\n";
            Log.d("logg1","top: "+Top10);
            items.removeAll(items);

            int count=0;
            for (Element element : contents) {
                count++;
                if(count%3 == 1){
                    Log.d("logg2","top: "+Top10);
                    String albumName = element.text();
                    String albumNum =element.getElementsByAttribute("onclick").toString();
                    albumNum = albumNum.substring(albumNum.indexOf("Layer")+7, albumNum.indexOf("Layer")+8+7);

                    Top10 += albumName + "\n";
                    Top10 += albumNum+ "\n";
                    Top10 += "------------" + "\n";

                    items.add(albumName);
                }
            }

            Log.d("logg4","top: "+Top10);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            searchList.setText(Top10);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
