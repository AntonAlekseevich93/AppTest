package com.example.miniapptest;

public interface IUseCase {
    public String[] getData();

    public boolean checkAnswerToCorrect(String s);

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
    public int[] getAmountTrueAnswers();


    public boolean notTheLastQuestion();
}
