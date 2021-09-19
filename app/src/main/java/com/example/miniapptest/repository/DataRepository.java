package com.example.miniapptest.repository;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import com.example.miniapptest.screens.question.Question;
import com.example.miniapptest.support.EnumEvent;
import com.example.miniapptest.support.ParserJSON;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class DataRepository implements LifecycleObserver {
    private List<Question> listOfQuestions;
    private Context context;

    private ParserJSON parserJSON;
    private SharedPreferences sharedPreferences;
    private final String APP_PREFERENCES_IS_TEST_STARTED = "isTestStarted";
    private final String APP_PREFERENCES = "mysettings";
    private final String NAME_FILE_FROM_ASSETS_FOLDER = "questions.json";
//    private final String APP_PREFERENCES_QUESTION_NUMBER_FOR_OVERVIEW = "QuestionNumberOverview";
//    private final String APP_PREFERENCES_IS_OVERVIEW_RESPONSE = "isOverviewResponse";

    private final String FILE_NAME = "DATA";

//    private boolean isFirstQuestion;

    public DataRepository(Context context) {
        this.context = context;
        parserJSON = new ParserJSON();
        sharedPreferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public Question getData(EnumEvent enumEvent, int index) {
        switch (enumEvent) {
            case CONFIGURATION_CHANGED:
                if(!isFileFound()) saveData(); //на случай когда запущен новый тест, открыт первый вопрос, но данные еще не сохранены, при перевороте мы сохраним данные
                else listOfQuestions = getDataFromFile();
                    break;
            case NEW_TEST:
                if (isFileFound()) {
                    listOfQuestions = getDataFromFile();
                    Log.i("DATA", "File is found");
                } else {
                    listOfQuestions = parserJSON.getListOfQuestionsFromJson(getStringFromFileFromAssetsFolder());
                    Log.i("DATA", "File is not found");
                }
                break;
            case START_NEW_TEST:
                listOfQuestions = parserJSON.getListOfQuestionsFromJson(getStringFromFileFromAssetsFolder());
                break;
            case OVERVIEW_QUESTION:

        }
        return listOfQuestions.get(index);
    }

    public int sizeOfList() {
        return listOfQuestions.size();
    }

    /**
     * Метод вызывается при первом запуске приложения, когда тест запускается впервый раз
     *
     * @return возвращает распарсенную строку полученную из файла в котором хранится JSON
     */
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

    /**
     * Если тест уже начинали проходить, через этот метод получим список пройденных вопросов.
     *
     * @return Возвращает ранее сохраненный список Question
     */
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

    /**
     * Метод сохраняет список объектов Question.
     * Привязан к жизненному циклу Активности. Вызывается в методе активности onPause()
     */
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

    /**
     * Ищет ранее сохраненный файл пройденного теста. Если файл с таким именем найден в системе, возвращает true
     * Если тест ранее не проходился и данные не были сохранены, вернет false
     *
     * @return boolean file is found
     */
    private boolean isFileFound() {
        String[] savedFilesArray = context.fileList();
        boolean fileIsFound = false;
        for (String fileName : savedFilesArray) {
            if (fileName.equals(FILE_NAME)) fileIsFound = true;
        }
        return fileIsFound;
    }

    /**
     * Метод удаляет файл с ответами пользователя для начала нового теста
     */
    public void startNewTest() {
        if (isFileFound()) context.deleteFile(FILE_NAME);
        testStarted(false);
        Log.i("DATA", "the file was successfully deleted");
    }

    public void testStarted(boolean testStarted) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(APP_PREFERENCES_IS_TEST_STARTED, testStarted);
        editor.apply();
    }

    public boolean isTestStarted() {
        boolean testStarted = false;
        if (sharedPreferences.contains(APP_PREFERENCES_IS_TEST_STARTED)) {
            testStarted = sharedPreferences.getBoolean(APP_PREFERENCES_IS_TEST_STARTED, false);
        }
        return testStarted;
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

    public int getSizeOfQuestionsList(){
        return listOfQuestions.size();
    }

    public List<Question> getListQuestions() {
        return listOfQuestions;
    }

//    public int getPercentCorrectAnswers() {
//       return  numberOfCorrectAnswer() / listOfQuestions.size() * 100;
//    }




//    /**
//     * Сохраняет номер вопроса
//     * @param questionNumber - номер вопроса
//     */
//    private void saveQuestionNumber(int questionNumber) {
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt(APP_PREFERENCES_QUESTION_NUMBER, questionNumber);
//        editor.apply();
//    }
//
//    /**
//     * Получает сохраненный ранее номер вопроса
//     * @return int - номер вопроса
//     */
//    public int getSavedQuestionNumber() {
//        if (sharedPreferences.contains(APP_PREFERENCES_QUESTION_NUMBER)) {
//            return sharedPreferences.getInt(APP_PREFERENCES_QUESTION_NUMBER, -1);
//        } else return 0;
//    }

//    public EnumEvent getEventForNextQuestion() {
//        if (sizeOfListIsCorrect()) {
//            increaseQuestionNumber();
//            return EnumEvent.NEXT_QUESTION;
//        } else return EnumEvent.FINISH_TEST;
//    }

//    public Question setAnswerIsSelected(String checkAnswer, int indexOfAnswer) {
//        listOfQuestions.get(questionNumber).setIndexOfAnswer(indexOfAnswer);
//        listOfQuestions.get(questionNumber).setNameResolvedAnswer(checkAnswer);
//        return listOfQuestions.get(questionNumber);
//
//    }

//    public boolean isFirstQuestion(){
//        return isFirstQuestion;
//    }
//
//    private boolean sizeOfListIsCorrect() {
//        return questionNumber + 1 < listOfQuestions.size();
//    }

}
