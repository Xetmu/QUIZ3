package com.example.quiz;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import org.json.JSONException;

public class QuizLoader {

    private static final String url = "https://opentdb.com/api.php?amount=5&type=multiple&category=9&difficulty=easy";

    /**
     * Минимальный вызов — просто загружаем и запускаем BoardQuizActivity
     */
    public static void loadQuiz(Context context) {
        http_render.sendRequest(context, url, new http_render.ResponseCallback() {
            @Override
            public void onSuccess(String responseBody) throws JSONException {
                Log.d("QuizLoader", "Response: " + responseBody);
                Intent intent = new Intent(context, BoardQuizActivity.class);
                intent.putExtra("JSONARRAY", responseBody);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // На случай, если context не Activity
                context.startActivity(intent);
            }

            @Override
            public void onError(String errorMessage) {
                showCustomToast(context);
            }
        });
    }

    /**
     * Расширенный вызов — с отображением прогресса и фона
     */
    public static void loadQuiz(Context context,
                                View progressBar,
                                View loadButton,
                                TextView titleTextView,
                                TextView subtitleTextView,
                                LinearLayout layoutContainer) {

        http_render.sendRequest(context, url, new http_render.ResponseCallback() {
            @Override
            public void onSuccess(String responseBody) throws JSONException {
                Log.d("QuizLoader", "Response: " + responseBody);

                if (layoutContainer != null)
                    layoutContainer.setBackground(ContextCompat.getDrawable(context, R.drawable.shape));

                toggleViews(loadButton, titleTextView, subtitleTextView, progressBar, true);

                Intent intent = new Intent(context, BoardQuizActivity.class);
                intent.putExtra("JSONARRAY", responseBody);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

            @Override
            public void onError(String errorMessage) {
                if (layoutContainer != null)
                    layoutContainer.setBackground(ContextCompat.getDrawable(context, R.drawable.shape));

                toggleViews(loadButton, titleTextView, subtitleTextView, progressBar, true);
                showCustomToast(context);
            }
        });
    }

    // === Вспомогательный метод для переключения видимости ===
    private static void toggleViews(View loadButton,
                                    TextView titleTextView,
                                    TextView subtitleTextView,
                                    View progressBar,
                                    boolean showButtons) {
        if (loadButton != null) loadButton.setVisibility(View.VISIBLE);
        if (titleTextView != null) titleTextView.setVisibility(View.VISIBLE);
        if (subtitleTextView != null) subtitleTextView.setVisibility(View.VISIBLE);
        if (progressBar != null) progressBar.setVisibility(View.GONE);
    }

    // === Тост с оформлением ===
    private static void showCustomToast(Context context) {
        Toast toast = new Toast(context);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        int padding = (int) (16 * context.getResources().getDisplayMetrics().density);
        layout.setPadding(padding * 2, padding, padding * 2, padding);

        Drawable toastBackground = ContextCompat.getDrawable(context, android.R.drawable.toast_frame);
        if (toastBackground != null) {
            toastBackground = toastBackground.mutate();
            toastBackground.setColorFilter(ContextCompat.getColor(context, R.color.next), PorterDuff.Mode.SRC_IN);
            layout.setBackground(toastBackground);
        }

        TextView textView = new TextView(context);
        textView.setText("Ошибка! Попробуйте ещё раз!");
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(16);
        layout.addView(textView);

        toast.setView(layout);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }
}
