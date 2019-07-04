package o1.mobile.notism;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    ListView playlistView;
    Button addBtn;
    ArrayAdapter adapter;
    ArrayList<String> playList = new ArrayList<>(), songInformation = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playlistView = findViewById(R.id.playlist);
        addBtn = findViewById(R.id.addBtn);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,playList);
        playlistView.setAdapter(adapter);

        Intent intent = getIntent();
        if(intent.getStringExtra("first") != null){
            songInformation=(ArrayList<String>)intent.getSerializableExtra("songInformation");
            Log.d("logg", "non");
            setPlaylist();
        }

        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), search_j.class);
                startActivity(intent);
            }
        });
    }

    private void setPlaylist(){
        for(int i=0; i<songInformation.size(); i++){
            playList.add(songInformation.get(i));
        }
        adapter.notifyDataSetChanged();
    }
}
