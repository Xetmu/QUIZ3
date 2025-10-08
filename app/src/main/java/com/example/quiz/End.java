package com.example.quiz;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.quiz.R;

public class End extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout container;
    private int Index=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end);

        // Получаем layout контейнер
        container = findViewById(R.id.containerLinearLayout);

        // Массив id звездочек
        int[] starIds = new int[] {
                R.id.star_1,
                R.id.star_2,
                R.id.star_3,
                R.id.star_4,
                R.id.star_5
        };

        int total = 0;
        Index =  QuestionAnswer.index;

        for (int i = 0; i < QuestionAnswer.index; i++) {
            if (QuestionAnswer.numbers[i + Index] == QuestionAnswer.extraNumbers[i + Index]) {


                // Получаем ImageView по id
                ImageView im = findViewById(starIds[total]);
                if (im != null) {
                    // Меняем на активный ресурс
                    im.setImageResource(R.drawable.active);
                }
                total++;
            }
        }

        TextView tv = findViewById(R.id.textView1);
        tv.setText(total + " из " + 5);
        TextView tv2 = findViewById(R.id.textView2);
        TextView tv3 = findViewById(R.id.textView3);
        switch (total) {
            case 0: tv2.setText("Неудача не приговор!"); tv3.setText("старания не гарантируют успех, но кто не старается - обречён"); break;
            case 1: tv2.setText("Старания путь к успеху!"); tv3.setText("1/5 - ошибки созданы чтобы на них учиться"); break;
            case 2: tv2.setText("Почти половина!"); tv3.setText("2/5 - ничего страшного"); break;
            case 3: tv2.setText("Неплохо!"); tv3.setText("3/5 - эрудит!"); break;
            case 4: tv2.setText("Почти идеально!"); tv3.setText("4/5 - очень близко к совершенству!");  break;
            case 5: tv2.setText("Отлично!"); tv3.setText("5/5 - ты ювелир!");  break;
        }



        // Далее создаём остальные блоки вопросов
        for (int i = 1; i <= 5; i++) {
            LinearLayout block = createQuestionBlock(i + Index);
            container.addView(block);

        }
        Button endButton = findViewById(R.id.end_button);
        endButton.setOnClickListener(this);

    }

    private LinearLayout createQuestionBlock(int index) {
        // Родительский вертикальный LinearLayout (как в корне XML)
        LinearLayout parent = new LinearLayout(this);
        LinearLayout.LayoutParams parentParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        parentParams.setMargins(0, dpToPx(20), 0, dpToPx(20));
        parent.setLayoutParams(parentParams);
        parent.setOrientation(LinearLayout.VERTICAL);
        parent.setGravity(Gravity.CENTER);
        parent.setPadding(0, dpToPx(20), 0, dpToPx(5));
        parent.setBackground(getDrawable(R.drawable.shape));

        // Горизонтальный LinearLayout для TextView и ImageView
        LinearLayout horizontalLayout = new LinearLayout(this);
        LinearLayout.LayoutParams horizParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        horizontalLayout.setLayoutParams(horizParams);
        horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
        horizontalLayout.setGravity(Gravity.CENTER_VERTICAL);
        horizontalLayout.setPadding(dpToPx(10), 0, dpToPx(10), 0);

        // TextView "Total Questions"
        TextView totalQuestionsTV = new TextView(this);
        totalQuestionsTV.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));


        totalQuestionsTV.setText(" Вопрос " + index%5 + " из 5");
        totalQuestionsTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        totalQuestionsTV.setTextColor(getColor(R.color.count));
        totalQuestionsTV.setId(View.generateViewId()); // Можно для уникальности

        horizontalLayout.addView(totalQuestionsTV);

        // Заполнитель (View) с весом 1, чтобы сдвинуть ImageView вправо
        View spacer = new View(this);
        LinearLayout.LayoutParams spacerParams = new LinearLayout.LayoutParams(
                0,
                0,
                1f);
        spacer.setLayoutParams(spacerParams);
        horizontalLayout.addView(spacer);

        // ImageView справа
        ImageView icon = new ImageView(this);
        icon.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        icon.setImageResource(R.drawable.right); // или другой drawable
        icon.setId(View.generateViewId());

        horizontalLayout.addView(icon);

        // Добавляем горизонтальный блок в родителя
        parent.addView(horizontalLayout);

        // TextView с вопросом под горизонтальным блоком
        TextView questionTV = new TextView(this);
        LinearLayout.LayoutParams questionParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        questionParams.topMargin = dpToPx(16);
        questionTV.setLayoutParams(questionParams);
        questionTV.setText("This will be question #" + index);
        questionTV.setText(QuestionAnswer.question[index-1]);
        questionTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        questionTV.setTextColor(Color.BLACK);
        questionTV.setGravity(Gravity.CENTER);
        questionTV.setTypeface(null, android.graphics.Typeface.BOLD);
        questionTV.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        questionTV.setId(View.generateViewId());

        parent.addView(questionTV);

        // Кнопки ответов
        String[] answers = QuestionAnswer.choices[index-1];

        int topMarginForButton = dpToPx(24);
        for (int i = 0; i < 4; i++) {
            Button btn = new Button(this);
            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                    dpToPx(250),
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            btnParams.topMargin = (i == 0) ? topMarginForButton : dpToPx(8);
            btn.setLayoutParams(btnParams);
            btn.setText(answers[i]);
            btn.setTextColor(getColor(R.color.black));
            btn.setBackground(getDrawable(R.drawable.button_rectangular));
            btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.circle, 0, 0, 0);
            btn.setBackgroundTintList(null);
            btn.setPadding(dpToPx(16), dpToPx(8), dpToPx(16), dpToPx(8));
            btn.setElevation(0);
            btn.setId(View.generateViewId());
            if (QuestionAnswer.extraNumbers[index-1] == i+1) {
                btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.right, 0, 0, 0);
                btn.setBackgroundColor(getColor(R.color.white));
                btn.setBackground(ContextCompat.getDrawable(this, R.drawable.button_border_right));
            }else
            if(QuestionAnswer.numbers[index-1]==i+1) {

                    btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.wrong, 0, 0, 0);
                    btn.setBackgroundColor(getColor(R.color.white));
                    btn.setBackground(ContextCompat.getDrawable(this, R.drawable.button_border_wrong));
                    icon.setImageResource(R.drawable.wrong);
            }

            parent.addView(btn);
        }

        // Пустой View для отступа
        View spacerBottom = new View(this);
        LinearLayout.LayoutParams spacerBottomParams = new LinearLayout.LayoutParams(
                0, dpToPx(30));
        spacerBottom.setLayoutParams(spacerBottomParams);

        parent.addView(spacerBottom);

        return parent;
    }
    @Override
    public void onBackPressed() {
        if (getIntent().getStringExtra("caller").equals("history"))
            finish();

    }


    @Override
    public void onClick(View view) {
        Button clickedButton = (Button) view;
        if ( clickedButton.getId()==R.id.end_button)
        {
            Intent intent = new Intent(End.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }

    }
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
