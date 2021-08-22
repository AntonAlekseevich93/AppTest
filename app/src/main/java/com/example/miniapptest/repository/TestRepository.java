package com.example.miniapptest.repository;

import android.content.Context;
import android.content.res.AssetManager;
import com.example.miniapptest.repository.interfaces.ITestRepository;
import com.example.miniapptest.screens.question.Question;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestRepository implements ITestRepository {
    private List<Question> listOfQuestions = new ArrayList<>();
    private final String JSON_NAME_QUESTION = "question";
    private final String JSON_NAME_TRUE_ANSWER = "trueAnswer";
    private final String JSON_NAME_FALSE_ANSWER1 = "falseAnswer1";
    private final String JSON_NAME_FALSE_ANSWER2 = "falseAnswer2";
    private final String JSON_NAME_FALSE_ANSWER3 = "falseAnswer3";
    private Context context;
    private final String NAME_FILE_FROM_ASSETS_FOLDER = "questions.json";

    public TestRepository(Context context) {
        this.context = context;
    }

    @Override
    public List<Question> getDataFromJSON() {
        parsingJson(getStringFromFileFromAssetsFolder());
        return listOfQuestions;
    }

    private void parsingJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray("questions");
            listOfQuestions.clear();
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

    private String getStringFromFileFromAssetsFolder() {
        AssetManager assetManager = context.getAssets();
        byte[] buffer = null;
        String stringFromAssetsFolder = null;
        try {
            InputStream inputStream = assetManager.open(NAME_FILE_FROM_ASSETS_FOLDER);
            int size = inputStream.available();
            buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stringFromAssetsFolder = new String(buffer);
        return stringFromAssetsFolder;

    }



}
