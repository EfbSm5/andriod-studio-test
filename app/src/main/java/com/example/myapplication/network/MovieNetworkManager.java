package com.example.myapplication.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieNetworkManager {
    public String movieData;
    String responseData;

    public String sendRequestWithOkhttp(String receiveData, int currentPage) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("https://www.omdbapi.com/?apikey=7852f5f&s=" + receiveData + "&page=" + currentPage).build();
                    Response response = client.newCall(request).execute();
                    responseData = response.body().string();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return responseData;
    }

}

