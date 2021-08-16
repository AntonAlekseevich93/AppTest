package com.example.miniapptest.screens;

import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.miniapptest.Question;
import com.example.miniapptest.UseCases;

import java.util.List;


public class ViewModel extends androidx.lifecycle.ViewModel {
    private UseCases useCases;
    private MutableLiveData<Question> mutableLiveData;
    static boolean questionIsLoaded;

    public ViewModel(UseCases useCases) {
        this.useCases = useCases;
    }

    public LiveData<Question> loadData() {
        if (mutableLiveData == null) {
            mutableLiveData = new MutableLiveData<>();
        }
        getData();
        return mutableLiveData;
    }

    private void getData() {
        if (useCases.isFirstLaunchApp()) {
            Runnable runnable = () -> {
                mutableLiveData.postValue(useCases.getData());
                questionIsLoaded = true;
            };
            new Thread(runnable).start();

        } else {
            Runnable runnable2 = () -> {
                questionIsLoaded = false;
                mutableLiveData.postValue(useCases.getQuestion());
                questionIsLoaded = true;

            };
            new Thread(runnable2).start();

        }


    }

    public int getResolvedAnswer() {
        return useCases.getIndexOfAnswer();
//        return mutableLiveData.getValue().getIndexOfAnswer();
    }


    public boolean checkSelectedAnswer(String selectedAnswer, int indexOfSelectedAnswer) {
        return useCases.checkSelectedAnswer(selectedAnswer, indexOfSelectedAnswer);

    }

    public boolean checkAnswerIsResolved() {
        return useCases.checkAnswerIsResolved();
    }

    public boolean isLastQuestion() {
        return useCases.isLastQuestion();
    }

    public boolean isFirstQuestion() {
        return useCases.isFirstQuestion();
    }


    public void increaseQuestionNumber() {
        useCases.increaseQuestionNumber();
    }

    public void decreaseQuestionNumber() {
        useCases.decreaseQuestionNumber();
    }

    public boolean answerIsTrue() {
        return useCases.answerIsTrue();
    }

    public void startNewTest() {
        useCases.startNewTest();
    }

    public void clearData() {
        if (mutableLiveData != null) {
            mutableLiveData = null;
            System.out.println("Клеар дата во вью моделе");
        }
    }

    public String getPercentTrueAnswers() {
        return useCases.getPercentCorrectAnswers();
    }


    public int[] getAmountTrueAnswers() {
        return useCases.getAmountTrueAnswers();
    }

    public List<Question> getListQuestion() {
        return useCases.getListQuestion();
    }

    public void firstLaunchApp() {
        useCases.firstLaunchApp();
    }

}
