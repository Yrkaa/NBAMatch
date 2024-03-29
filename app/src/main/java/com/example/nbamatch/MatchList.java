package com.example.nbamatch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MatchList extends AppCompatActivity {

    //Создание переменных для эл. разметки
    ListView matchList;
    ProgressBar progressBar;
    TextView title, err;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_list_layout);

        //Инициализация переменных для эл. разметки
        matchList = findViewById(R.id.match_list);
        progressBar = findViewById(R.id.progressBar);
        title = findViewById(R.id.title_tv);
        err = findViewById(R.id.err_tv);

        //Получаем менеджера подключений
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        //Запускаем поток для заполнения списка матчей
        LoadMatchesList loadMatchesList = new LoadMatchesList(MatchList.this, matchList, progressBar, title, err, cm);
        loadMatchesList.start();

        matchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int id = (int) loadMatchesList.getId((int) l);
                Intent toMatchPage = new Intent(MatchList.this, MatchPage.class);
                toMatchPage.putExtra("id", id);
                startActivity(toMatchPage);
            }
        });

    }
}