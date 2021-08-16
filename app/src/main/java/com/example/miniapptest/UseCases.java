package com.example.miniapptest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UseCases {
    private TestRepository testRepository = new TestRepository();
    private List<Question> listOfQuestions = new ArrayList<>();
    private final String APP_PREFERENCES = "mysettings";
    private final String APP_PREFERENCES_QUESTION_NUMBER = "QuestionNumber";
    private SharedPreferences sharedPreferences;
    private boolean firstLaunch = false;
    private int questionNumber = -1;
    private Context context;
    private final String FILE_NAME = "DATA";
    private String DATA;

    public UseCases(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public Question getData() {
        if (isDataFileFound()) {
            if (!firstLaunch) getSavedQuestionNumber();
            else {
                firstLaunch = false;
                questionNumber++;
            }
            listOfQuestions.clear();
            listOfQuestions.addAll(getDataFromFile());

        } else {
            listOfQuestions.clear();
            listOfQuestions.addAll(testRepository.getDataFromJSON());
            questionNumber++;
            saveQuestionNumber();
        }
        return listOfQuestions.get(questionNumber);
    }

    public Question getQuestion() {
        return listOfQuestions.get(questionNumber);
    }

    public boolean checkSelectedAnswer(String checkAnswer, int indexOfAnswer) {
        listOfQuestions.get(questionNumber).setResolved(true);
        listOfQuestions.get(questionNumber).setIndexOfAnswer(indexOfAnswer);
        if (listOfQuestions.get(questionNumber).getTrueAnswer().equals(checkAnswer)) {
            listOfQuestions.get(questionNumber).setSolvedCorrectly(true);
            listOfQuestions.get(questionNumber).setNameResolvedAnswer(checkAnswer);
            saveData(listOfQuestions);
            return true;
        } else {
            listOfQuestions.get(questionNumber).setNameResolvedAnswer(checkAnswer);
            listOfQuestions.get(questionNumber).setSolvedCorrectly(false);
            saveData(listOfQuestions);
            return false;
        }
    }

    public boolean checkAnswerIsResolved() {
        if (questionNumber == -1) return false;
        else return listOfQuestions.get(questionNumber).isResolved();

    }

    public boolean isResolved() {
        return listOfQuestions.get(questionNumber).isResolved();
    }

    public boolean isLastQuestion() {
        return questionNumber + 1 == listOfQuestions.size();
    }

    public boolean isFirstLaunchApp() {
        return questionNumber == -1;
    }

    public boolean isFirstQuestion() {
        return questionNumber == 0;
    }

    public int numberOfCorrectAnswer() {
        int score = 0;
        for (Question q : listOfQuestions) {
            int count;
            if (q.isSolvedCorrectly()) count = 1;
            else count = 0;
            score = score + count;
        }
        return score;
    }

    public String getPercentCorrectAnswers() {
        String s = String.valueOf((double) numberOfCorrectAnswer() / listOfQuestions.size() * 100);
        return s;
    }

    public void increaseQuestionNumber() {
        if (questionNumber + 1 < listOfQuestions.size()) {
            questionNumber++;
            saveQuestionNumber();
        }
    }

    public void decreaseQuestionNumber() {
        if (questionNumber != 0) {
            questionNumber--;
            saveQuestionNumber();
        }
    }

    public boolean answerIsTrue() {
        Question q = listOfQuestions.get(questionNumber);
        return q.getNameResolvedAnswer().equals(q.getTrueAnswer());
    }

    private void saveData(List<Question> listOfQuestions) {
        Runnable runnable = () -> {
            FileOutputStream fileOutputStream;
            try {
                fileOutputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(listOfQuestions);
                objectOutputStream.close();
                Log.i("DATA", "Data uploaded successfully");
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private boolean isDataFileFound() {
        String[] fileDir = context.fileList();
        boolean fileIsFound = false;
        for (String s : fileDir) {
            if (s.equals(FILE_NAME)) {
                fileIsFound = true;
                break;
            }
        }
        return fileIsFound;
    }

    private List<Question> getDataFromFile() {
        FileInputStream fileInputStream;
        List<Question> list = null;
        try {
            fileInputStream = context.openFileInput(FILE_NAME);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            list = (List<Question>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void startNewTest() {
        if (isDataFileFound()) context.deleteFile(FILE_NAME);
        questionNumber = -1;
        Log.i("DATA", "the file was successfully deleted");
    }


    public int[] getAmountTrueAnswers() {
        int[] m = new int[2];
        int i = numberOfCorrectAnswer();
        m[0] = i;
        m[1] = listOfQuestions.size() - i;
        return m;
    }

    public int getIndexOfAnswer() {
        return listOfQuestions.get(questionNumber).getIndexOfAnswer();
    }

    private void saveQuestionNumber() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(APP_PREFERENCES_QUESTION_NUMBER, questionNumber);
        editor.apply();
    }

    public void getSavedQuestionNumber() {
        if (sharedPreferences.contains(APP_PREFERENCES_QUESTION_NUMBER)) {
            questionNumber = sharedPreferences.getInt(APP_PREFERENCES_QUESTION_NUMBER, -1);
        }
    }

    public List<Question> getListQuestion() {
        return listOfQuestions;
    }

    public void firstLaunchApp() {
        questionNumber = -1;
        firstLaunch = true;
    }
}
