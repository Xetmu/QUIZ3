package com.example.quiz;

import static android.view.View.GONE;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;


import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button load_btn,history_btn;
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QuestionAnswer.loadFromPreferences(App.getContext());
        //QuestionAnswer.clearPreferences(App.getContext());
        setContentView(R.layout.activity_main);
        load_btn = findViewById(R.id.load_btn);
        LinearLayout LL = findViewById(R.id.popup);
        history_btn = findViewById(R.id.history_btn);


        LL.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Получаем текущие размеры (уже вычисленные после layout)
                int width = LL.getWidth();
                int height = LL.getHeight();

                // Получаем LayoutParams
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) LL.getLayoutParams();

                // Устанавливаем фиксированные размеры
                params.width = width;
                params.height = height;

                // Применяем новые параметры
                LL.setLayoutParams(params);

                // Удаляем слушатель, чтобы не вызывался повторно
                LL.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        history_btn.setOnClickListener(this);


        load_btn.setOnClickListener(this);


    }

    @Override
    public void onClick(View view){
        Button clickedButton = (Button) view;


        if(clickedButton.getId()==R.id.load_btn) {
            load_btn.setVisibility(GONE);
            LinearLayout LL = findViewById(R.id.popup);
            LL.setBackgroundColor(ContextCompat.getColor(App.getContext(), R.color.purle));


            TextView titleTextView = findViewById(R.id.titleTextView);
            TextView subtitleTextView = findViewById(R.id.subtitleTextView);
            ProgressBar progressBar = findViewById(R.id.custom_progress);
            titleTextView.setVisibility(GONE);
            subtitleTextView.setVisibility(GONE);
            progressBar.setVisibility(View.VISIBLE);
            QuizLoader.loadQuiz(
                    MainActivity.this,
                    progressBar,
                    load_btn,
                    titleTextView,
                    subtitleTextView,
                    LL
            );
            //Log.d("HTTP-GET", "ddsadsavdsvdsdvs");


        }


        if(clickedButton.getId()==R.id.history_btn) {

            Intent intent = new Intent(MainActivity.this, history.class);
            startActivity(intent);
        }





    }











}






