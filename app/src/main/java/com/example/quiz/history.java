package com.example.quiz;

import static android.view.View.GONE;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

public class history extends AppCompatActivity implements View.OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        QuestionAnswer.loadFromPreferences(App.getContext());
        setContentView(R.layout.history); // <-- ОБЯЗАТЕЛЬНО!
        LinearLayout no_history = findViewById(R.id.no_history);
        Button load_ = findViewById(R.id.load_);
        load_.setOnClickListener(this);

        if(QuestionAnswer.question.length>0) {
            no_history.setVisibility(View.GONE);
            HistoryLayoutBuilder.createHistoryLayouts(App.getContext(), findViewById(R.id.history_container), QuestionAnswer.question.length / 5, this);

        }



    }
/*
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.my_button) {
            getMenuInflater().inflate(R.menu.my_context_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                // Обработка пункта 1
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }*/



    @Override
    public void onClick(View view) {
        Button clickedButton = (Button) view;
        if ( clickedButton.getId()==R.id.load_)
        {
            QuizLoader.loadQuiz(history.this);
        }
    }
}
