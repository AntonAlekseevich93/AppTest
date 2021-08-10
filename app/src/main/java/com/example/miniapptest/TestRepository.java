package com.example.miniapptest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
                listOfQuestions.add(new Question(
                        js.getString(JSON_NAME_QUESTION),
                        js.getString(JSON_NAME_TRUE_ANSWER),
                        js.getString(JSON_NAME_FALSE_ANSWER1),
                        js.getString(JSON_NAME_FALSE_ANSWER2),
                        js.getString(JSON_NAME_FALSE_ANSWER3)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
