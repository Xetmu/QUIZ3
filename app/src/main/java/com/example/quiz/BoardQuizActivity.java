package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.quiz.End;


public class BoardQuizActivity extends AppCompatActivity implements View.OnClickListener{

    TextView totalQuestionsTextView;
    TextView questionTextView;
    Button ansA,ansB, ansC, ansD ;
    ImageButton back;
    Button submitBtn;
    int[] numbers, extranumbers;
    int score=0;
    int totalQuestion = 5;

    int currentQuestionIndex = 0;
    String selectedAnswer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz);

        Intent intent = getIntent();
        String jsonString = intent.getStringExtra("JSONARRAY");

        try {
            assert jsonString != null;
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            //Log.d("JSON", jsonArray.toString());
            QuestionAnswer.parse(jsonArray.toString());

        } catch (JSONException e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при парсинге JSON: " + e.getMessage());
        }
        numbers = new int[5];
        extranumbers = new int[5];

        totalQuestionsTextView = findViewById(R.id.total_questions);
        questionTextView = findViewById(R.id.question);
        back = findViewById(R.id.back);
        ansA = findViewById(R.id.ans_A);
        ansB = findViewById(R.id.ans_B);
        ansC = findViewById(R.id.ans_C);
        ansD = findViewById(R.id.ans_D);

        submitBtn = findViewById(R.id.submit_btn);
        findViewById(R.id.time_out_button).setOnClickListener(this);

        back.setOnClickListener(this);
        ansA.setOnClickListener(this);
        ansB.setOnClickListener(this);
        ansC.setOnClickListener(this);
        ansD.setOnClickListener(this);
        submitBtn.setOnClickListener(this);

        totalQuestionsTextView.setText("Вопрос 1 из 5");

        ProgressBar timeProgressBar = findViewById(R.id.time_progress_bar);
        TextView timePassedText = findViewById(R.id.time_passed_text);
        TextView timeLeftText = findViewById(R.id.time_left_text);
        RelativeLayout timeBarContainer = findViewById(R.id.time_bar_container);

        final int totalTime = 5 * 60 * 1000; // 5 минут в миллисекундах
        final int tickInterval = 1000; // обновление каждую секунду

        timeBarContainer.post(() -> {

            CountDownTimer timer = new CountDownTimer(totalTime, tickInterval) {
                @Override
                public void onTick(long millisUntilFinished) {
                    int elapsed = (int) (totalTime - millisUntilFinished);
                    int elapsedMinutes = elapsed / 60000;
                    int elapsedSeconds = (elapsed / 1000) % 60;

                    int remainingMinutes = (int) (millisUntilFinished / 60000);
                    int remainingSeconds = (int) ((millisUntilFinished / 1000) % 60);

                    int progress = (int) (elapsed * 100L / totalTime);
                    timeProgressBar.setProgress(progress);

                    timePassedText.setText(String.format("%d:%02d", elapsedMinutes, elapsedSeconds));
                    //timeLeftText.setText(String.format("%d:%02d", remainingMinutes, remainingSeconds));
                    timeLeftText.setText(String.format("%d:%02d", 5 , 0));

                    // Ширина контейнера
                    int containerWidth = timeBarContainer.getWidth();

                    // Позиция X для timeLeftText (начало его расположения в контейнере)
                    float timeLeftX = timeLeftText.getX();

                    // Максимальная позиция для timePassedText:
                    // чтобы не налазил на timeLeftText — left position у timeLeftText минус ширина timePassedText минус небольшой отступ
                    float maxX = timeLeftX - timePassedText.getWidth() - dpToPx(8);

                    // Вычисляем позицию по прогрессу от 0 до maxX
                    float xPosition = (containerWidth - timePassedText.getWidth()) * progress / 100f;

                    // Ограничиваем позицию слева и справа
                    if (xPosition < 0) xPosition = 0;
                    if (xPosition > maxX) xPosition = maxX;

                    //if(xPosition<32)
                        xPosition=32;
                    //функция сделана чтобы перемещать время в соответствии с пройденным над ползунком, но вроде это не надо, поэтому коменчено
                    //if (maxX-xPosition>32)
                    timePassedText.setX(xPosition);
                }

                @Override
                public void onFinish() {
                    timeProgressBar.setProgress(100);
                    timePassedText.setText("5:00");
                    timeLeftText.setText("5:00");
                    LinearLayout time_out = findViewById(R.id.time_out);
                    time_out.setVisibility(View.VISIBLE);
                    ansA.setEnabled(false);
                    ansB.setEnabled(false);
                    ansC.setEnabled(false);
                    ansD.setEnabled(false);
                    submitBtn.setEnabled(false);

                    // Помещаем timePassedText максимально близко к timeLeftText слева с отступом 8dp
                    //float timeLeftX = timeLeftText.getX();
                    //float endPosition = timeLeftX - timePassedText.getWidth() - dpToPx(8);
                    //timePassedText.setX(endPosition);
                }
            };

            timer.start();
        });

// Вспомогательная функция для конвертации dp в px

        loadNewQuestion();

    }
    private float dpToPx(int dp) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }


    @Override
    public void onClick(View view){
        Button clickedButton=null;
        if (view instanceof Button) {
            clickedButton = (Button) view;
            // handle button click
        } else if (view instanceof ImageButton) {
            ImageButton imgBtn = (ImageButton) view;
            finish();
            return;
            // handle image button click
        }
        if(clickedButton.getId()==R.id.time_out_button)
            finish();

        ansA.setCompoundDrawablesWithIntrinsicBounds(R.drawable.circle, 0, 0, 0);
        ansB.setCompoundDrawablesWithIntrinsicBounds(R.drawable.circle, 0, 0, 0);
        ansC.setCompoundDrawablesWithIntrinsicBounds(R.drawable.circle, 0, 0, 0);
        ansD.setCompoundDrawablesWithIntrinsicBounds(R.drawable.circle, 0, 0, 0);
        ansA.setBackgroundTintList(null);
        ansA.setBackground(ContextCompat.getDrawable(this, R.drawable.button_rectangular));
        ansB.setBackgroundTintList(null);
        ansB.setBackground(ContextCompat.getDrawable(this, R.drawable.button_rectangular));
        ansC.setBackgroundTintList(null);
        ansC.setBackground(ContextCompat.getDrawable(this, R.drawable.button_rectangular));
        ansD.setBackgroundTintList(null);
        ansD.setBackground(ContextCompat.getDrawable(this, R.drawable.button_rectangular));


        //ansB.setBackgroundColor(R.drawable.button_rectangular);
        //ansC.setBackgroundColor(R.drawable.button_rectangular);
        //ansD.setBackgroundColor(R.drawable.button_rectangular);

        if(clickedButton.getId()==R.id.ans_A){
            ansA.setCompoundDrawablesWithIntrinsicBounds(R.drawable.selected, 0, 0, 0);
            ansA.setBackgroundColor(getColor(R.color.white));
            ansA.setBackground(ContextCompat.getDrawable(this, R.drawable.button_border));
            numbers[currentQuestionIndex]=1;
        }

        if(clickedButton.getId()==R.id.ans_B){
            ansB.setCompoundDrawablesWithIntrinsicBounds(R.drawable.selected, 0, 0, 0);
            ansB.setBackgroundColor(getColor(R.color.white));
            ansB.setBackground(ContextCompat.getDrawable(this, R.drawable.button_border));
            numbers[currentQuestionIndex]=2;
        }
        if(clickedButton.getId()==R.id.ans_C){
            ansC.setCompoundDrawablesWithIntrinsicBounds(R.drawable.selected, 0, 0, 0);
            ansC.setBackgroundColor(getColor(R.color.white));
            ansC.setBackground(ContextCompat.getDrawable(this, R.drawable.button_border));
            numbers[currentQuestionIndex]=3;
        }
        if(clickedButton.getId()==R.id.ans_D){
            ansD.setCompoundDrawablesWithIntrinsicBounds(R.drawable.selected, 0, 0, 0);
            ansD.setBackgroundColor(getColor(R.color.white));
            ansD.setBackground(ContextCompat.getDrawable(this, R.drawable.button_border));
            numbers[currentQuestionIndex]=4;
        }


        if(clickedButton.getId()==R.id.submit_btn){
            if(selectedAnswer!="null") {
                if(selectedAnswer==QuestionAnswer.correctAnswers[currentQuestionIndex]) {
                    score++;
                }
                if (ansA.getText().toString() == QuestionAnswer.correctAnswers[currentQuestionIndex])
                    extranumbers[currentQuestionIndex]=1;
                if (ansB.getText().toString() == QuestionAnswer.correctAnswers[currentQuestionIndex])
                    extranumbers[currentQuestionIndex]=2;
                if (ansC.getText().toString() == QuestionAnswer.correctAnswers[currentQuestionIndex])
                    extranumbers[currentQuestionIndex]=3;
                if (ansD.getText().toString() == QuestionAnswer.correctAnswers[currentQuestionIndex])
                    extranumbers[currentQuestionIndex]=4;

                currentQuestionIndex++;
                loadNewQuestion();

                selectedAnswer="null";
            }

        }else{
            selectedAnswer = clickedButton.getText().toString();
        }


    }

    void loadNewQuestion(){
        if(currentQuestionIndex==totalQuestion){
            finishQuiz();
            return;
        }
        totalQuestion = QuestionAnswer.question.length;
        totalQuestionsTextView.setText("Вопрос " + (currentQuestionIndex+1) + " из 5");
        questionTextView.setText(QuestionAnswer.question[currentQuestionIndex]);
        //Log.d("HTTP-GET", QuestionAnswer.question[currentQuestionIndex]);
        //questionTextView.setText(QuestionAnswer.getAnswer(currentQuestionIndex));
        //Log.d("HTTP-GET", QuestionAnswer.getAnswer(currentQuestionIndex));
        ansA.setText(QuestionAnswer.choices[currentQuestionIndex][0]);
        ansB.setText(QuestionAnswer.choices[currentQuestionIndex][1]);
        ansC.setText(QuestionAnswer.choices[currentQuestionIndex][2]);
        ansD.setText(QuestionAnswer.choices[currentQuestionIndex][3]);
    }
    void finishQuiz(){
        String passStatus = "";
        if(score > totalQuestion*0.60){
            passStatus = "Passed";
        }
        else{
            passStatus = "Failed";
        }
        /*
        new AlertDialog.Builder(this)
                .setTitle(passStatus)
                .setMessage("Score is " + score + " out of " + totalQuestion)
                .setPositiveButton("Restart", (dialogInerface, i) -> restartQuiz() )
                .setCancelable(false)
                .show();*/


        QuestionAnswer.index = QuestionAnswer.question.length-5;
        restartQuiz();



    }
    void restartQuiz(){

        QuestionAnswer.saveToPreferences(App.getContext(), numbers, extranumbers, score);
        //QuestionAnswer.clearPreferences(App.getContext());
        Intent intent = new Intent(BoardQuizActivity.this, End.class);
        intent.putExtra("caller", "BoardQuizActivity");
        startActivity(intent);
        //finish();
        score = 0;
        currentQuestionIndex = 0;
        //loadNewQuestion();

    }
}