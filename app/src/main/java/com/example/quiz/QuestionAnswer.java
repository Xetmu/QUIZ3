package com.example.quiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class QuestionAnswer {

    public static int amount = 0;
    public static int index = 0;
    public static int good = 0;
    public static String[] question;
    public static String[][] choices;
    public static String[] correctAnswers;
    public static int[] numbers;
    public static int[] extraNumbers;

    public static String[] saveDates;
    public static String[] saveTimes;

    public static void parse(String str) {
        try {
            JSONArray jsonArray = new JSONArray(str);

            int len = jsonArray.length();
            amount = len;

            question = new String[len];
            choices = new String[len][4];
            correctAnswers = new String[len];

            for (int i = 0; i < len; i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                question[i] = android.text.Html.fromHtml(obj.getString("question")).toString();
                String correct = obj.getString("correct_answer");
                correctAnswers[i] = android.text.Html.fromHtml(correct).toString();

                JSONArray incorrect = obj.getJSONArray("incorrect_answers");
                ArrayList<String> allChoices = new ArrayList<>();
                for (int j = 0; j < incorrect.length(); j++) {
                    allChoices.add(android.text.Html.fromHtml(incorrect.getString(j)).toString());
                }

                allChoices.add(correctAnswers[i]);
                Collections.shuffle(allChoices);

                for (int j = 0; j < 4; j++) {
                    choices[i][j] = allChoices.get(j);
                }
            }

            Log.d("JSON_PARSE", "Успешно распарсили " + amount + " вопросов.");

        } catch (JSONException e) {
            Log.e("JSON_PARSE", "Ошибка при парсинге JSON: " + e.getMessage());
        }
    }

    public static void saveToPreferences(Context context, int[] inputNumbers, int[] inputExtraNumbers, int gd) {
        SharedPreferences prefs = context.getSharedPreferences("quiz_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        good = gd;

        // Получаем старые значения (если есть)
        int savedAmount = prefs.getInt("amount", 0);
        JSONArray savedQuestions = getJSONArray(prefs, "questions");
        JSONArray savedCorrectAnswers = getJSONArray(prefs, "correctAnswers");
        JSONArray savedChoices = getJSONArray(prefs, "choices");
        JSONArray savedNumberArray = getJSONArray(prefs, "numbers");
        JSONArray savedExtraArray = getJSONArray(prefs, "extraNumbers");
        JSONArray savedDates = getJSONArray(prefs, "saveDates");
        JSONArray savedTimes = getJSONArray(prefs, "saveTimes");

        // Обновляем amount
        amount = savedAmount + 1; // например, увеличиваем количество сохранений
        editor.putInt("amount", amount);

        // Обновляем список вопросов
        for (String q : question) {
            savedQuestions.put(q);
        }
        editor.putString("questions", savedQuestions.toString());

        // Обновляем правильные ответы
        for (String a : correctAnswers) {
            savedCorrectAnswers.put(a);
        }
        editor.putString("correctAnswers", savedCorrectAnswers.toString());

        // Обновляем варианты ответов
        for (int i = 0; i < choices.length; i++) {
            JSONArray innerArray = new JSONArray();
            for (int j = 0; j < choices[i].length; j++) {
                innerArray.put(choices[i][j]);
            }
            savedChoices.put(innerArray);
        }
        editor.putString("choices", savedChoices.toString());

        // Обновляем номера
        numbers = inputNumbers;
        for (int n : numbers) {
            savedNumberArray.put(n);
        }
        editor.putString("numbers", savedNumberArray.toString());

        // Обновляем дополнительные номера
        extraNumbers = inputExtraNumbers;
        for (int n : extraNumbers) {
            savedExtraArray.put(n);
        }
        editor.putString("extraNumbers", savedExtraArray.toString());

        // Добавляем дату и время
        String currentDate = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        savedDates.put(currentDate);
        savedTimes.put(currentTime);

        editor.putString("saveDates", savedDates.toString());
        editor.putString("saveTimes", savedTimes.toString());

        editor.apply();
    }

    public static void loadFromPreferences(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("quiz_data", Context.MODE_PRIVATE);
        amount = prefs.getInt("amount", 0);

        try {
            JSONArray qArray = new JSONArray(prefs.getString("questions", "[]"));
            question = new String[qArray.length()];
            for (int i = 0; i < qArray.length(); i++) {
                question[i] = qArray.getString(i);
            }

            JSONArray aArray = new JSONArray(prefs.getString("correctAnswers", "[]"));
            correctAnswers = new String[aArray.length()];
            for (int i = 0; i < aArray.length(); i++) {
                correctAnswers[i] = aArray.getString(i);
            }

            JSONArray choicesArray = new JSONArray(prefs.getString("choices", "[]"));
            choices = new String[choicesArray.length()][4];
            for (int i = 0; i < choicesArray.length(); i++) {
                JSONArray innerArray = choicesArray.getJSONArray(i);
                for (int j = 0; j < innerArray.length(); j++) {
                    choices[i][j] = innerArray.getString(j);
                }
            }

            JSONArray numberArray = new JSONArray(prefs.getString("numbers", "[]"));
            numbers = new int[numberArray.length()];
            for (int i = 0; i < numberArray.length(); i++) {
                numbers[i] = numberArray.getInt(i);
            }

            JSONArray extraArray = new JSONArray(prefs.getString("extraNumbers", "[]"));
            extraNumbers = new int[extraArray.length()];
            for (int i = 0; i < extraArray.length(); i++) {
                extraNumbers[i] = extraArray.getInt(i);
            }

            // ✅ Загружаем даты и время
            JSONArray dateArray = new JSONArray(prefs.getString("saveDates", "[]"));
            saveDates = new String[dateArray.length()];
            for (int i = 0; i < dateArray.length(); i++) {
                saveDates[i] = dateArray.getString(i);
            }

            JSONArray timeArray = new JSONArray(prefs.getString("saveTimes", "[]"));
            saveTimes = new String[timeArray.length()];
            for (int i = 0; i < timeArray.length(); i++) {
                saveTimes[i] = timeArray.getString(i);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void clearPreferences(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("quiz_data", Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }


    private static JSONArray getJSONArray(SharedPreferences prefs, String key) {
        try {
            return new JSONArray(prefs.getString(key, "[]"));
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray(); // если ошибка — вернуть пустой массив
        }
    }

}
