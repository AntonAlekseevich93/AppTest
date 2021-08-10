package com.example.miniapptest.screens;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.miniapptest.IUseCase;
import com.example.miniapptest.UseCases;
import com.example.miniapptest.screens.interfaces.IViewModel;

public class ViewModel extends androidx.lifecycle.ViewModel implements IViewModel {
    private IUseCase iUseCase;
    private MutableLiveData<String[]> mutableLiveData;
    private MutableLiveData<Boolean> mutableLiveDataBoolean;

    public ViewModel(UseCases useCases) {
        this.iUseCase = useCases;
    }

    @Override
    public LiveData<String[]> loadData() {
        if (mutableLiveData == null) {
            mutableLiveData = new MutableLiveData<>();
        }
        getData();
        return mutableLiveData;
    }

    @Override
    public boolean checkAnswerToCorrect(String s) {
        return iUseCase.checkAnswerToCorrect(s);
    }

    @Override
    public String[] getNextQuestion() {
        return iUseCase.getNextQuestion();
    }

    @Override
    public void setAnswerIsSelected() {
        iUseCase.setAnswerIsSelected();
    }

    @Override
    public boolean getAnswerIsSelected() {
        return iUseCase.getAnswerIsSelected();
    }

    @Override
    public void increaseQuestionNumber() {
        iUseCase.increaseQuestionNumber();
    }

    @Override
    public void decreaseQuestionNumber() {
        iUseCase.decreaseQuestionNumber();
    }

    @Override
    public int getIndexSelectedAnswer() {
        return iUseCase.getIndexSelectedAnswer();
    }

    @Override
    public void saveIndexOfSelectedAnswer(int index) {
        iUseCase.saveIndexOfSelectedAnswer(index);
    }

    @Override
    public void saveSelectedAnswer(boolean answer) {
        iUseCase.saveSelectedAnswer(answer);
    }

    @Override
    public boolean getSavedSelectedAnswer() {
        return iUseCase.getSavedSelectedAnswer();
    }

    @Override
    public int getNumberOfQuestion() {
        return iUseCase.getNumberOfQuestion();
    }

    @Override
    public String getPercentTrueAnswers() {
        return iUseCase.getPercentTrueAnswers();
    }

    @Override
    public boolean notTheLastQuestion() {

        return iUseCase.notTheLastQuestion();
    }


    @Override
    public int[] getAmountTrueAnswers() {
        return iUseCase.getAmountTrueAnswers();
    }


    private void getData() {
        if (UseCases.questionNumber == -1) {
            mutableLiveData.setValue(iUseCase.getData());
        } else {
            mutableLiveData.setValue(iUseCase.getNextQuestion());
        }
    }





}
