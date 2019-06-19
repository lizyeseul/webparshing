package o1.mobile.notism;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button button;
    Elements contents;
    Document doc = null;
    String Top10="null";

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
                            doc = Jsoup.connect("http://www.naver.com").get();
                            contents = doc.select("ul.ah_l > li > a[href=#]");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        Top10 = "";
                        int count = 0;
                        for (Element element : contents) {
                            count++;
                            Top10 += count + ". " + element.select(".ah_k").text() + "\n";

                            if (count == 10)
                                break;
                        }
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
