package com.example.miniapptest.screens.viewmodel;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.miniapptest.screens.question.Question;
import com.example.miniapptest.usecase.UseCases;
import java.util.List;


public class ViewModel extends androidx.lifecycle.ViewModel implements LifecycleObserver {
    private UseCases useCases;
    private MutableLiveData<Question> mutableLiveData;
    public static boolean questionIsLoaded;

    public ViewModel(UseCases useCases) {
        this.useCases = useCases;
        if (mutableLiveData == null) {
            mutableLiveData = new MutableLiveData<>();
        }
    }

    public LiveData<Question> loadData() {
        if (mutableLiveData == null) {
            mutableLiveData = new MutableLiveData<>();
        }
        questionIsLoaded = false;
        mutableLiveData.postValue(useCases.getQuestion());
        questionIsLoaded = true;
        return mutableLiveData;
    }


    public int getResolvedAnswer() {
        return useCases.getIndexOfAnswer();
    }

    public int getIndexTrueAnswer() {
        return useCases.getIndexTrueAnswer();
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

    public void setNumberOfQuestionForOverview(int number) {
        useCases.setNumberOfQuestionForOverview(number);
    }

    public void returnNumberOfQuestion() {
        useCases.returnNumberOfQuestion();
    }
}
