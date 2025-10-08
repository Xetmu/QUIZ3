package com.example.quiz;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Vector;

public class TokenManager {

    private static final String KEY_STRING_VECTOR = "string_vector";
    private static final String PREF_NAME = "auth_prefs";

    // Ключи для викторины
    private static final String KEY_QUESTIONS = "questions";
    private static final String KEY_CHOICES = "choices";
    private static final String KEY_CORRECT = "correctAnswers";
    private static final String KEY_AMOUNT = "amount";

    private SharedPreferences prefs;

    public TokenManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // --- Сохранение Vector<String> как JSON ---
    public void saveStringVector(Vector<String> vector) {
        JSONArray jsonArray = new JSONArray();
        for (String item : vector) {
            jsonArray.put(item);
        }
        prefs.edit().putString(KEY_STRING_VECTOR, jsonArray.toString()).apply();
    }

    public Vector<String> getStringVector() {
        Vector<String> vector = new Vector<>();
        String json = prefs.getString(KEY_STRING_VECTOR, null);
        if (json != null) {
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    vector.add(jsonArray.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return vector;
    }

    public void clearStringVector() {
        prefs.edit().remove(KEY_STRING_VECTOR).apply();
    }

    // --- Сохранение данных викторины ---
    public void saveQuizData(String[] questions, String[][] choices, String[] correctAnswers) {
        SharedPreferences.Editor editor = prefs.edit();

        // Сохраняем количество
        editor.putInt(KEY_AMOUNT, questions.length);

        // Вопросы
        JSONArray qArray = new JSONArray();
        for (String q : questions) {
            qArray.put(q);
        }
        editor.putString(KEY_QUESTIONS, qArray.toString());

        // Правильные ответы
        JSONArray aArray = new JSONArray();
        for (String a : correctAnswers) {
            aArray.put(a);
        }
        editor.putString(KEY_CORRECT, aArray.toString());

        // Варианты
        JSONArray cArray = new JSONArray();
        for (String[] choiceSet : choices) {
            JSONArray inner = new JSONArray();
            for (String option : choiceSet) {
                inner.put(option);
            }
            cArray.put(inner);
        }
        editor.putString(KEY_CHOICES, cArray.toString());

        editor.apply();
    }

    // --- Загрузка данных викторины ---
    public QuizData loadQuizData() {
        QuizData data = new QuizData();

        data.amount = prefs.getInt(KEY_AMOUNT, 0);

        try {
            // Вопросы
            JSONArray qArray = new JSONArray(prefs.getString(KEY_QUESTIONS, "[]"));
            data.questions = new String[qArray.length()];
            for (int i = 0; i < qArray.length(); i++) {
                data.questions[i] = qArray.getString(i);
            }

            // Ответы
            JSONArray aArray = new JSONArray(prefs.getString(KEY_CORRECT, "[]"));
            data.correctAnswers = new String[aArray.length()];
            for (int i = 0; i < aArray.length(); i++) {
                data.correctAnswers[i] = aArray.getString(i);
            }

            // Варианты
            JSONArray cArray = new JSONArray(prefs.getString(KEY_CHOICES, "[]"));
            data.choices = new String[cArray.length()][4];
            for (int i = 0; i < cArray.length(); i++) {
                JSONArray inner = cArray.getJSONArray(i);
                for (int j = 0; j < inner.length(); j++) {
                    data.choices[i][j] = inner.getString(j);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }

    // --- Очистка викторины ---
    public void clearQuizData() {
        prefs.edit()
                .remove(KEY_QUESTIONS)
                .remove(KEY_CORRECT)
                .remove(KEY_CHOICES)
                .remove(KEY_AMOUNT)
                .apply();
    }

    // Обёртка для хранения загруженных данных викторины
    public static class QuizData {
        public int amount;
        public String[] questions;
        public String[][] choices;
        public String[] correctAnswers;
    }
}
