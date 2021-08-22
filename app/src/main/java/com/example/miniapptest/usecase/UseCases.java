package com.example.miniapptest.usecase;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import com.example.miniapptest.repository.TestRepository;
import com.example.miniapptest.screens.question.Question;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class UseCases implements LifecycleObserver {
    private TestRepository testRepository;
    private List<Question> listOfQuestions = new ArrayList<>();
    private final String APP_PREFERENCES = "mysettings";
    private final String APP_PREFERENCES_QUESTION_NUMBER = "QuestionNumber";
    private final String APP_PREFERENCES_QUESTION_NUMBER_FOR_OVERVIEW = "QuestionNumberOverview";
    private final String APP_PREFERENCES_IS_OVERVIEW_RESPONSE = "isOverviewResponse";
    private int lastNumberForSaved;
    private static int indexCorrectAnswer;
    private SharedPreferences sharedPreferences;
    private static boolean firstLaunch = false;
    private static int questionNumber = -1;
    private Context context;
    private final String FILE_NAME = "DATA";
    public static boolean isOverviewResponse = false;


    public UseCases(Context context) {
        this.context = context;
        testRepository = new TestRepository(context);
        sharedPreferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void getData() {
        Runnable runnable = () -> {
            if (isDataFileFound()) {
                if (!firstLaunch) {
                    getSavedQuestionNumber();
                    getNumberOfQuestionForOverview();
                } else {
                    firstLaunch = false;
                    questionNumber++;
                    saveQuestionNumber();
                }
                listOfQuestions.clear();
                listOfQuestions.addAll(getDataFromFile());


            } else {
                listOfQuestions.clear();
                listOfQuestions.addAll(testRepository.getDataFromJSON());
                questionNumber++;
                saveQuestionNumber();
            }
        };
        new Thread(runnable).start();
    }

    public Question getQuestion() {
        if (questionNumber == -1) questionNumber = 0;
        if (listOfQuestions.size() > 0)
            return listOfQuestions.get(questionNumber);
        else return null;
    }

    public boolean checkSelectedAnswer(String checkAnswer, int indexOfAnswer) {
        listOfQuestions.get(questionNumber).setResolved(true);
        listOfQuestions.get(questionNumber).setIndexOfAnswer(indexOfAnswer);
        if (listOfQuestions.get(questionNumber).getTrueAnswer().equals(checkAnswer)) {
            listOfQuestions.get(questionNumber).setSolvedCorrectly(true);
            listOfQuestions.get(questionNumber).setNameResolvedAnswer(checkAnswer);
            return true;
        } else {
            listOfQuestions.get(questionNumber).setNameResolvedAnswer(checkAnswer);
            listOfQuestions.get(questionNumber).setSolvedCorrectly(false);
            return false;
        }

    }

    public boolean checkAnswerIsResolved() {
        if (questionNumber == -1) return false;
        else return listOfQuestions.get(questionNumber).isResolved();
    }

    public boolean isLastQuestion() {
        return questionNumber + 1 == listOfQuestions.size();
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
        double percent = (double) numberOfCorrectAnswer() / listOfQuestions.size() * 100;
        String format = new DecimalFormat("##").format(percent);
        return format + "%";
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

    public int getIndexOfAnswer() {
        return listOfQuestions.get(questionNumber).getIndexOfAnswer();
    }

    public int getIndexTrueAnswer() {
        String answer = listOfQuestions.get(questionNumber).getTrueAnswer();
        Question question = listOfQuestions.get(questionNumber);

        if (question.getAnswer1().equals(answer)) indexCorrectAnswer = 0;
        else if (question.getAnswer2().equals(answer)) indexCorrectAnswer = 1;
        else if (question.getAnswer3().equals(answer)) indexCorrectAnswer = 2;
        else indexCorrectAnswer = 3;
        return indexCorrectAnswer;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void saveData() {
        if (listOfQuestions.size() != 0) {
            Runnable runnable = () -> {
                FileOutputStream fileOutputStream;
                try {
                    fileOutputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                    objectOutputStream.writeObject(listOfQuestions);
                    objectOutputStream.close();
                    Log.i("DATA", "Data saved successfully");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }
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
        getData();
        Log.i("DATA", "the file was successfully deleted");
    }


    public int[] getAmountTrueAnswers() {
        int[] m = new int[2];
        int i = numberOfCorrectAnswer();
        m[0] = i;
        m[1] = listOfQuestions.size() - i;
        return m;
    }


    private void saveQuestionNumber() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(APP_PREFERENCES_QUESTION_NUMBER, questionNumber);
        editor.apply();
    }

    private void saveNumberOfQuestionForOverview() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(APP_PREFERENCES_QUESTION_NUMBER_FOR_OVERVIEW, lastNumberForSaved);
        editor.putBoolean(APP_PREFERENCES_IS_OVERVIEW_RESPONSE, isOverviewResponse);
        editor.apply();
    }

    public void getSavedQuestionNumber() {
        if (sharedPreferences.contains(APP_PREFERENCES_QUESTION_NUMBER)) {
            questionNumber = sharedPreferences.getInt(APP_PREFERENCES_QUESTION_NUMBER, -1);
        }
    }

    private int getNumberOfQuestionForOverview() {
        if (sharedPreferences.contains(APP_PREFERENCES_QUESTION_NUMBER_FOR_OVERVIEW) && sharedPreferences.contains(APP_PREFERENCES_IS_OVERVIEW_RESPONSE)) {
            lastNumberForSaved = sharedPreferences.getInt(APP_PREFERENCES_QUESTION_NUMBER_FOR_OVERVIEW, -1);
            isOverviewResponse = sharedPreferences.getBoolean(APP_PREFERENCES_IS_OVERVIEW_RESPONSE, false);
        }
        return lastNumberForSaved;
    }

    public List<Question> getListQuestion() {
        return listOfQuestions;
    }

    public void firstLaunchApp() {
        questionNumber = -1;
        firstLaunch = true;
    }


    public void setNumberOfQuestionForOverview(int number) {
        lastNumberForSaved = questionNumber;
        isOverviewResponse = true;
        saveNumberOfQuestionForOverview();
        questionNumber = number;
        saveQuestionNumber();

    }

    public void returnNumberOfQuestion() {
        questionNumber = getNumberOfQuestionForOverview();
        isOverviewResponse = false;
        saveQuestionNumber();
        saveNumberOfQuestionForOverview();
    }


}
