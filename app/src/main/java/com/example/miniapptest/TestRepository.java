package com.example.miniapptest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestRepository implements ITestRepository {
    private List<Question> listOfQuestions = new ArrayList<>();
    //    private String json = " {\"questions\":[{\"question\": \"Что является точкой входа в приложении?\",\"trueAnswer\": \"Активность\",\"falseAnswer1\":  \"Фрагмент\",\"falseAnswer2\": \"View\",\"falseAnswer3\": \"ViewModel\"}]}";
    private String json = " {\"questions\":[{\"question\": \"Что является точкой входа в приложении?\",\"trueAnswer\": \"Активность\",\"falseAnswer1\":  \"Фрагмент\",\"falseAnswer2\": \"View\",\"falseAnswer3\": \"ViewModel\"}, \n" +
            "{\"question\": \"Где солнце\",\"trueAnswer\": \"На другое стороне\",\"falseAnswer1\":  \"На луне\",\"falseAnswer2\": \"В космосе\",\"falseAnswer3\": \"Нету\"}]}";
    private final String JSON_NAME_QUESTION = "question";
    private final String JSON_NAME_TRUE_ANSWER = "trueAnswer";
    private final String JSON_NAME_FALSE_ANSWER1 = "falseAnswer1";
    private final String JSON_NAME_FALSE_ANSWER2 = "falseAnswer2";
    private final String JSON_NAME_FALSE_ANSWER3 = "falseAnswer3";

    @Override
    public List<Question> getDataFromJSON() {
        parsingJson();
        return listOfQuestions;
    }

    private void parsingJson() {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("questions");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject js = jsonArray.getJSONObject(i);
                String question = js.getString(JSON_NAME_QUESTION);
                String trueAnswer = js.getString(JSON_NAME_TRUE_ANSWER);
                String answer2 = js.getString(JSON_NAME_FALSE_ANSWER1);
                String answer3 = js.getString(JSON_NAME_FALSE_ANSWER2);
                String answer4 = js.getString(JSON_NAME_FALSE_ANSWER3);
                listOfQuestions.add(generateRandomQuestion(question, trueAnswer, answer2, answer3, answer4, trueAnswer));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Collections.shuffle(listOfQuestions);
    }

    private Question generateRandomQuestion(String Question, String a1, String a2, String a3, String a4, String trueAnswer) {
        List<String> random = Arrays.asList(a1, a2, a3, a4);
        Collections.shuffle(random);
        return new Question(Question, random.get(0), random.get(1), random.get(2), random.get(3), trueAnswer);
    }
}
