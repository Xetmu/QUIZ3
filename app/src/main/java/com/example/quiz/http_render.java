package com.example.quiz;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Patterns;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class http_render {
    public static void sendRequest(Context context, String url, ResponseCallback callback) {
        // Проверяем корректность URL
        if (url == null || url.isEmpty() || !Patterns.WEB_URL.matcher(url).matches()) {
            new Handler(Looper.getMainLooper()).post(() -> {
                callback.onError("Некорректный URL");
            });
            return;
        }

        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder()
                .url(url);

        Request request = builder.build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(() -> {
                    callback.onError("Ошибка соединения: " + e.getMessage());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                Log.d("BEFOR", String.valueOf(response));
                String body = response.body() != null ? response.body().string() : "";

                if (response.isSuccessful()) {
                    String finalBody = body;

                    new Handler(Looper.getMainLooper()).post(() -> {
                        try {
                            callback.onSuccess(finalBody);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } else {
                    int code = response.code();
                    new Handler(Looper.getMainLooper()).post(() -> {
                        callback.onError("Ошибка запроса: код " + code);
                    });
                }
            }
        });
    }
    public interface ResponseCallback {
        void onSuccess(String responseBody) throws JSONException;
        void onError(String errorMessage);
    }


}
