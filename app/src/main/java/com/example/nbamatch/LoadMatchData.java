package com.example.nbamatch;

import android.app.Activity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoadMatchData extends Thread{

    //id матча
    int id;

    //Конструктор для получения id матча и эл. разметки
    public LoadMatchData(int id, TextView homeTeamNameTv, TextView visitorTeamNameTv,
                         TextView homeTeamScoreTv, TextView visitorTeamScoreTv,
                         TextView statusTv, TextView dateTv, ProgressBar progressBar,Activity activity){
        this.id = id;
        this.homeTeamNameTv = homeTeamNameTv;
        this.visitorTeamNameTv = visitorTeamNameTv;
        this.homeTeamScoreTv = homeTeamScoreTv;
        this.visitorTeamScoreTv = visitorTeamScoreTv;
        this.statusTv = statusTv;
        this.dateTv = dateTv;
        this.activity = activity;
        this.progressBar = progressBar;
    }

    //Поля, куда запишутся данные об игре
    private String date, status,visitorTeamName, homeTeamName;
    private int visitorTeamScore, homeTeamScore;

    //Поля с эл. разметки
    TextView homeTeamNameTv, visitorTeamNameTv,
            homeTeamScoreTv, visitorTeamScoreTv, statusTv, dateTv;
    ProgressBar progressBar;
    Activity activity;

    @Override
    public void run() {
        super.run();

        //Адрес api с данными о матче
        String url = "https://www.balldontlie.io/api/v1/games/"+id;

        try {
            //Подключаемся к api с данными о матче
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.connect();

            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                //Считываем данные
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                dataFromJson(reader);

                //Заполняем эл. разметки данными
                dataToUi();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    //Функция для переноса информации на ui
    private void dataToUi(){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Скрываем строку загрузки, когда все загружено
                progressBar.setVisibility(View.INVISIBLE);

                //Переносим информацию
                homeTeamNameTv.setText(homeTeamName);
                visitorTeamNameTv.setText(visitorTeamName);
                homeTeamScoreTv.setText(String.valueOf(homeTeamScore));
                visitorTeamScoreTv.setText(String.valueOf(visitorTeamScore));
                statusTv.setText(status);
                dateTv.setText(date);
            }
        });
    }

    private void dataFromJson(BufferedReader reader) throws IOException, JSONException {

        // Преобразуем данные в объект
        JSONObject json = new JSONObject(reader.readLine());

        //Получаю дату...
        String UKDataWithUTC = json.getString("date");
        String UKData = UKDataWithUTC.split(" ")[0];
        String[] yearMonthDay = UKData.split("-");
        date = yearMonthDay[2]+"."+yearMonthDay[1] + "." + yearMonthDay[0];
        //... и остальные данные
        status = json.getString("status");
        homeTeamScore = json.getInt("home_team_score");
        visitorTeamScore = json.getInt("visitor_team_score");
        homeTeamName = json.getJSONObject("home_team").getString("full_name");
        visitorTeamName = json.getJSONObject("visitor_team").getString("full_name");
    }
}
