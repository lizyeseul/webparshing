package o1.mobile.notism;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button button;
    WebView browser;
    Elements contents;
    Document doc = null;
    String Top10="null";
    String[] items = new String[]{"번호","말머리","제목","이름","날짜","조회수"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text);
        button = findViewById(R.id.button);
        /*browser = findViewById(R.id.webview);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.setWebViewClient(new WebViewClient());
        browser.loadUrl("http://exo.smtown.com/Board/List/?kind=1");*/


        button.setOnClickListener(new View.OnClickListener(){
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v){
                MyAsyncTask getTime = new MyAsyncTask();
                getTime.execute();
            }
        });
    }

    public class MyAsyncTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {try {
            doc = Jsoup.connect("https://www.genie.co.kr/detail/songInfo?xgnm=82145523").get();
            contents = doc.select(".info-data > li");
        } catch (Exception e) {
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
            textView.setText(Top10);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}
