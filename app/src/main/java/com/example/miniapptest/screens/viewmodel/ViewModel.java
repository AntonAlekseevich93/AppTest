package com.example.miniapptest.screens.viewmodel;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.miniapptest.screens.question.Question;
import com.example.miniapptest.support.EnumEvent;
import com.example.miniapptest.usecase.UseCases;

import java.util.List;


public class ViewModel extends androidx.lifecycle.ViewModel implements LifecycleObserver {
    private UseCases useCases;
    private MutableLiveData<Question> mutableLiveData;


    public ViewModel(UseCases useCases) {
        this.useCases = useCases;
    }

    public LiveData<Question> loadData(EnumEvent enumEvent, int position) {
        if (mutableLiveData == null) {
            mutableLiveData = new MutableLiveData<>();
        }
        mutableLiveData.postValue((useCases.getQuestion(enumEvent, position)));
        return mutableLiveData;
    }


    public EnumEvent getEventForClickNextQuestion() {
        return useCases.getEventForClickNextQuestion();
    }

    public boolean checkSelectedAnswer(String selectedAnswer, int indexOfSelectedAnswer) {
        return useCases.checkSelectedAnswer(selectedAnswer, indexOfSelectedAnswer);
    }

    public boolean getEventBoolean(EnumEvent enumEvent) {
        switch (enumEvent) {
            case CHECK_ANSWER_IS_RESOLVED:
                return useCases.checkAnswerIsResolved();
            case CHECK_ANSWER_IS_TRUE:
                return useCases.answerIsTrue();
            case BEFORE_QUESTION:
                mutableLiveData.setValue(useCases.getQuestion(enumEvent, 0));
                return useCases.isFirstQuestion();
            default:
                return false;
        }
    }


    public int getIndexOfResolvedAnswer() {
        return useCases.getIndexOfAnswer();
    }

    public void startNewTest(EnumEvent enumEvent) {
        useCases.getQuestion(enumEvent, 0);
        if (mutableLiveData != null) {
            mutableLiveData = null;
        }
    }

    public int[] getAmountTrueAnswers() {
        return useCases.getAmountTrueAnswers();
    }

    public List<Question> getListQuestions() {
        return useCases.getListQuestions();
    }

    public String getPercentTrueAnswers() {
        return useCases.getPercentCorrectAnswers();
    }

    public int getIndexCorrectAnswer() {
        return useCases.getIndexCorrectAnswer();
    }

}
