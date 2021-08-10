package com.example.miniapptest.screens.interfaces;

import androidx.lifecycle.LiveData;

public interface IViewModel {
    public LiveData<String[]> loadData();
    public boolean checkAnswerToCorrect (String s);
    public String[] getNextQuestion();
    public void setAnswerIsSelected();
    public boolean getAnswerIsSelected();
    public void increaseQuestionNumber();
    public void decreaseQuestionNumber();
    public int getIndexSelectedAnswer();
    public void saveIndexOfSelectedAnswer(int index);
    public void saveSelectedAnswer(boolean answer);
    public boolean getSavedSelectedAnswer();
    public int getNumberOfQuestion();
    public String getPercentTrueAnswers();
    public boolean notTheLastQuestion();

    public int[] getAmountTrueAnswers();

}
