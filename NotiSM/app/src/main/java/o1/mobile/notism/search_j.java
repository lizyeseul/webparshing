package o1.mobile.notism;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.URLEncoder;

public class search_j extends AppCompatActivity {

    TextView searchList;
    Button searchBtn;
    EditText searchWord;
    Elements contents;
    Document doc = null;
    String Top10="null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_x);

        searchList = findViewById(R.id.searchList);
        searchBtn = findViewById(R.id.searchBtn);
        searchWord = findViewById(R.id.searchWord);


        try{
            String word =  URLEncoder.encode(searchWord.getText().toString());
            int i=0;
            while(i<word.length()){
                char c=word.charAt(i);
                if(c == '%'){
                    word = word.substring(0,i+1)+"25"+word.substring(i+1);
                }
                i++;
            }
            String url = "https://www.genie.co.kr/search_x/searchAlbum?query="+ word+"&Coll=";
            searchList.setText(url);
        } catch (Exception e){
            e.printStackTrace();
        }

        searchBtn.setOnClickListener(new View.OnClickListener(){
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v){
                SearchAT searchThread = new SearchAT();
                searchThread.execute();
            }
        });
    }

    public class SearchAT extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {


            try{
                String url = "https://www.genie.co.kr/search_x/searchAlbum?query="+ URLEncoder.encode(searchWord.getText().toString(),"utf-8")+"&Coll=";
                Log.d("loggUrl","url: "+url);
                doc = Jsoup.connect(url).get();
                contents = doc.select("div");
            } catch (Exception e){
                e.printStackTrace();
            }

            String temp = contents.text();
            Log.d("logg0","temp: "+temp);
            Top10 = "";
            Top10 += contents.size() + "\n";
            Top10 += contents.get(3).text() + "\n";
            Log.d("logg1","top: "+Top10);

           /* for (Element element : contents) {
                Log.d("logg2","top: "+Top10);
                Top10 += element.text() + "\n";
            }*/
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
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
