package o1.mobile.notism;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] params) {
                        try {
                            //doc = Jsoup.connect("http://exo.smtown.com/Board/List/10724").get();
                            doc = Jsoup.connect("http://exo.smtown.com/Board/List/?kind=1").get();
                            //doc = Jsoup.connect("http://www.naver.com").get();
                            contents = doc.select("div.wrap > div");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


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
