package com.example.nbamatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

public class MatchList extends AppCompatActivity {

    //Создание переменных для эл. разметки
    ListView matchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.match_list_layout);

        //Инициализация переменных для эл. разметки
        matchList = findViewById(R.id.match_list);

        //Запускаем поток для заполнения списка матчей
        LoadMatchesList loadMatchesList = new LoadMatchesList(MatchList.this, matchList);
        loadMatchesList.start();
    }
}