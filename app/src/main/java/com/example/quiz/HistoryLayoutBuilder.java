package com.example.quiz;

import static androidx.core.content.ContextCompat.startActivity;
import static androidx.core.content.ContextCompat.startActivity;

import static com.example.quiz.App.getContext;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HistoryLayoutBuilder {

    public static void createHistoryLayouts(Context context, LinearLayout container, int count, Activity activity) {

        for (int i = 0; i < count; i++) {

            // === Корневой layout (как кнопка) ===
            LinearLayout parentLayout = new LinearLayout(context);
            parentLayout.setOrientation(LinearLayout.VERTICAL);
            parentLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.shape));
            parentLayout.setPadding(0, dpToPx(context, 15), 0, dpToPx(context, 5));
            parentLayout.setClickable(true);
            parentLayout.setFocusable(true);
            parentLayout.setForeground(getSelectableItemBackground(context)); // Ripple эффект

            LinearLayout.LayoutParams parentParams = new LinearLayout.LayoutParams(
                    dpToPx(context, 330),
                    dpToPx(context, 100)
            );
            parentParams.setMargins(0, 0, 0, dpToPx(context, 20));
            parentLayout.setLayoutParams(parentParams);

            // === Верхний горизонтальный layout (название + звезды) ===
            LinearLayout topLayout = new LinearLayout(context);
            topLayout.setOrientation(LinearLayout.HORIZONTAL);
            topLayout.setGravity(Gravity.CENTER_VERTICAL);

            LinearLayout.LayoutParams topParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            topParams.setMargins(dpToPx(context, 8), dpToPx(context, 0), 0, 0);
            topLayout.setLayoutParams(topParams);

            // Текст: "Quiz X"
            TextView titleTV = new TextView(context);
            titleTV.setText("Quiz " + (i + 1));
            titleTV.setTextSize(20);
            titleTV.setTextColor(ContextCompat.getColor(context, R.color.purle));
            topLayout.addView(titleTV);

            // Пробел
            View space = new View(context);
            space.setLayoutParams(new LinearLayout.LayoutParams(dpToPx(context, 140), 0));
            topLayout.addView(space);

            // 5 звёзд
            int count_star=0;
            for (int j = 0+i*5; j <= 4+i*5; j++) {
                if (QuestionAnswer.numbers[j]==QuestionAnswer.extraNumbers[j]) {
                    count_star++;
                }
            }

            for (int j = 0+i*5; j <= 4+i*5; j++) {




                ImageView star = new ImageView(context);
                LinearLayout.LayoutParams starParams = new LinearLayout.LayoutParams(dpToPx(context, 16), dpToPx(context, 16));
                starParams.setMargins(dpToPx(context, 4), dpToPx(context, 4), dpToPx(context, 4), dpToPx(context, 4));
                star.setLayoutParams(starParams);
                if (count_star>j-i*5) {
                    star.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.active));
                } else {
                    star.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.inactive));
                }


                /* [хотелось оставить так, но ладно
                if (QuestionAnswer.numbers[j]==QuestionAnswer.extraNumbers[j]) {
                    star.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.active));
                } else {
                    star.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.inactive));
                }*/


                topLayout.addView(star);
            }

            // === Нижний layout (дата и время) ===
            LinearLayout bottomLayout = new LinearLayout(context);
            bottomLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams bottomParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            bottomParams.setMargins(dpToPx(context, 8), dpToPx(context, 8), 0, 0);
            bottomLayout.setLayoutParams(bottomParams);

            // Дата
            TextView dateTV = new TextView(context);
            dateTV.setText(QuestionAnswer.saveDates[i]);
            dateTV.setTextSize(20);
            bottomLayout.addView(dateTV);

            // Пробел
            View spacer = new View(context);
            LinearLayout.LayoutParams spacerParams = new LinearLayout.LayoutParams(
                    0, 0, 1
            );
            spacer.setLayoutParams(spacerParams);
            bottomLayout.addView(spacer);

            // Время
            TextView timeTV = new TextView(context);
            timeTV.setText(QuestionAnswer.saveTimes[i]);
            timeTV.setTextSize(20);
            LinearLayout.LayoutParams timeParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            timeParams.setMargins(0, 0, dpToPx(context, 8), 0);
            timeTV.setLayoutParams(timeParams);
            bottomLayout.addView(timeTV);

            // === Добавляем всё в родительский ===
            parentLayout.addView(topLayout);
            parentLayout.addView(bottomLayout);

            // === Установка действия при клике ===
            int finalI = i;
            parentLayout.setOnClickListener(view -> {
                // Здесь можно обработать нажатие на "карточку"
                // Например: открытие подробной информации
                // Toast.makeText(context, "Clicked on Quiz " + (i + 1), Toast.LENGTH_SHORT).show();
                QuestionAnswer.index = finalI *5;
                Intent intent = new Intent(activity, End.class);
                intent.putExtra("caller", "history");

                activity.startActivity(intent);
            });
                parentLayout.setOnLongClickListener(v -> {
                    PopupMenu popup = new PopupMenu(activity, v, Gravity.END);
                    popup.getMenuInflater().inflate(R.menu.my_context_menu, popup.getMenu());






                    popup.setOnMenuItemClickListener(item -> {
                        Menu menu = popup.getMenu();
                        menu.setHeaderIcon(R.drawable.trash_icon);
                        item.setIcon(R.drawable.trash_icon);
                        if (item.getItemId() == R.id.delete) {
                            // Обработка удаления
                            return true;
                        }
                        return false;
                    });

                    popup.show();


                    return true;
            });






            // === Добавляем layout в основной контейнер ===
            container.addView(parentLayout);
        }
    }

    private static int dpToPx(Context context, int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.getResources().getDisplayMetrics()
        );
    }


    private static Drawable getSelectableItemBackground(Context context) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);
        return ContextCompat.getDrawable(context, typedValue.resourceId);
    }
}
