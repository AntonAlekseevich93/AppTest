package com.example.miniapptest.screens;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.miniapptest.Question;
import com.example.miniapptest.UseCases;

public class ViewModel extends androidx.lifecycle.ViewModel {
    private UseCases useCases;
    private MutableLiveData<Question> mutableLiveData;

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
            mutableLiveData.setValue(useCases.getData());
        } else {
            mutableLiveData.setValue(useCases.getQuestion());
        }
    }

    public int getResolvedAnswer() {
        return mutableLiveData.getValue().getIndexOfAnswer();
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
}
