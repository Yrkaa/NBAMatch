package com.example.nbamatch;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class LoadMatchesList extends Thread {

    JSONArray data;

    Activity a;
    ListView list;
    ProgressBar progressBar;
    TextView title, err;

    ConnectivityManager cm;


    public LoadMatchesList(Activity a, ListView list, ProgressBar progressBar, TextView title, TextView err, ConnectivityManager cm){
        this.a = a;
        this.list = list;
        this.progressBar = progressBar;
        this.title = title;
        this.err = err;
        this.cm = cm;
    }

    @Override
    public void run() {
        super.run();

        //Проверка на наличие интернета
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
//Url api с матчами
            String url = "https://www.balldontlie.io/api/v1/games";

            try {
                //Подключаемся к серверу
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.connect();

                if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    //Получаем данные с сервера
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    //Преобразуем данные с сервера в JSONObject
                    JSONObject json = new JSONObject(reader.readLine());
                    data = json.getJSONArray("data");


                    //Заполняем список матчей
                    a.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<String> matchesMassive = new ArrayList<>();
                            //Проходимся по каждому матчу и вытягиваем названия команд
                            for(int i = 0; i < data.length(); i++){
                                try {
                                    String homeTeam = data.getJSONObject(i).getJSONObject("home_team").getString("full_name");
                                    String visitorTeam = data.getJSONObject(i).getJSONObject("visitor_team").getString("full_name");
                                    matchesMassive.add(homeTeam + " - " + visitorTeam);
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                            //Скрываем прогресс бар, когда все загрузили
                            progressBar.setVisibility(View.INVISIBLE);

                            //Создаем адаптор и заполняем список
                            ArrayAdapter adapter = new ArrayAdapter(a, android.R.layout.simple_list_item_1, matchesMassive);
                            list.setAdapter(adapter);
                        }
                    });
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        else{
            a.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    list.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    title.setVisibility(View.INVISIBLE);
                    err.setVisibility(View.VISIBLE);
                }
            });
        }


    }

    public int getId(int l){
        try {
            int id = data.getJSONObject(l).getInt("id");
            return id;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
