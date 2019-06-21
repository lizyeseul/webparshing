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


               new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] params) {
                        try {
                            //doc = Jsoup.connect("http://exo.smtown.com/Board/List/10724").get();
                            doc = Jsoup.connect("http://exo.smtown.com/Board/List/?kind=1").timeout(30000)
                                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                                    .referrer("http://www.google.com").get();
                            //doc = Jsoup.connect("http%3A%2F%2Fexo.smtown.com%2FBoard%2FList%2F10724").get();
                            //doc = Jsoup.connect("http://exo.smtown.com/Main").get();
                            //doc = Jsoup.connect("http://www.naver.com").get();
                            contents = doc.select("div");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        String temp = contents.text();
                        Log.d("logg0","temp: "+temp);
                        Top10 = "";
                        Top10 += contents.size() + "\n";
                        Log.d("logg1","top: "+Top10);

                        for (Element element : contents) {
                            Log.d("logg2","top: "+Top10);
                            /*Iterator<Element> iterElem = element.getElementsByTag("td").iterator();
                            for(String item : items){
                                Log.d("logg3","top: "+Top10);
                                Top10 += item+": "+iterElem.next().text() + "\n";
                            }*/
                        }
                        Log.d("logg4","top: "+Top10);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        textView.setText(Top10);
                    }

                }.execute();
            }
        });
    }
}
