package com.example.nbamatch;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

    Activity a;
    ListView list;

    public LoadMatchesList(Activity a, ListView list){
        this.a = a;
        this.list = list;
    }

    @Override
    public void run() {
        super.run();

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
                JSONArray data = json.getJSONArray("data");

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
}
