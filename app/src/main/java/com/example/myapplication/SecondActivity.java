package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.network.MovieNetworkManager;
import com.example.myapplication.network.parseJSON;
import com.example.myapplication.db.Movie;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    private ArrayList<String> historyInput = new ArrayList<>();
    private int currentPage = 1;
    private ArrayAdapter<String> adapter;
    public Movie movie[];
    private String[] movielist = new String[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String receivedata = getIntent().getStringExtra("movie name");
        MovieNetworkManager movieNetworkManager = new MovieNetworkManager();
        ListView list_view = (ListView) findViewById(R.id.list_view);
        parseJSON parseJSON1 = new parseJSON();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    movieNetworkManager.sendRequestWithOkhttp(receivedata, currentPage);
                    parseJSON1.parseJSONWithGSON(movieNetworkManager.responseData);
                    movie = parseJSON1.movieList.toArray(new Movie[0]);
                    for (int i = 0; i < 10; i++) {
                        movielist[i] = movie[i].getName();
                    }
                    adapter = new ArrayAdapter<String>(SecondActivity.this, android.R.layout.simple_list_item_1, movielist);
                    list_view.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        thread.start();



        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
                intent.putExtra("movie number", position);
                startActivity(intent);
            }
        });
        list_view.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && list_view.getLastVisiblePosition() == adapter.getCount() - 1) {
                    currentPage++;
                    adapter = new ArrayAdapter<String>(SecondActivity.this, android.R.layout.simple_list_item_1, movielist);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

    }

}

