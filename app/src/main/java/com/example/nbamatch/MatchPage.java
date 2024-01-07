package com.example.nbamatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MatchPage extends AppCompatActivity {

    //Создание переменных для эл. разметки
    TextView homeTeamName, visitorTeamName,
    homeTeamScore, visitorTeamScore, status, date, tire;
    ImageView homeTeamLogo, visitorTeamLogo;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_page);

        //Инициализация переменных для эл. разметки
        homeTeamName = findViewById(R.id.home_team_name);
        visitorTeamName = findViewById(R.id.visitor_team_name);
        homeTeamScore = findViewById(R.id.home_team_score);
        visitorTeamScore = findViewById(R.id.visitor_team_score);
        status = findViewById(R.id.status);
        date = findViewById(R.id.date);
        homeTeamLogo = findViewById(R.id.home_team_logo);
        visitorTeamLogo = findViewById(R.id.visitor_team_logo);
        progressBar = findViewById(R.id.progressBar2);
        tire = findViewById(R.id.tire);

        //Запускаем поток для получения данных о матче
        int id = getIntent().getIntExtra("id", 1);
        LoadMatchData loadMatchData = new LoadMatchData(id, homeTeamName, visitorTeamName,
                homeTeamScore, visitorTeamScore, status, date, progressBar, tire, MatchPage.this);
        loadMatchData.start();




    }
}