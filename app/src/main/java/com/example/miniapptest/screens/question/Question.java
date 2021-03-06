package com.example.miniapptest.screens.question;

import java.io.Serializable;

public class Question implements Serializable {
    private String question;
    private String trueAnswer;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private String nameResolvedAnswer;
    private boolean resolved;
    private boolean solvedCorrectly;
    private int indexOfAnswer;

    public Question(String question, String answer1, String answer2, String answer3, String answer4, String trueAnswer) {
        this.question = question;
        this.trueAnswer = trueAnswer;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
    }

    public boolean isSolvedCorrectly() {
        return solvedCorrectly;
    }

    public void setSolvedCorrectly(boolean solvedCorrectly) {
        this.solvedCorrectly = solvedCorrectly;
    }

    public int getIndexOfResolvedAnswer() {
        return indexOfAnswer;
    }

    public void setIndexOfAnswer(int indexOfAnswer) {
        this.indexOfAnswer = indexOfAnswer;
    }

    public boolean isResolved() {
        return resolved;
    }

    public String getNameResolvedAnswer() {
        return nameResolvedAnswer;
    }

    public void setNameResolvedAnswer(String nameResolvedAnswer) {
        this.nameResolvedAnswer = nameResolvedAnswer;
        if(answerIsTrue()) solvedCorrectly = true;
        else solvedCorrectly = false;
        resolved = true;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public String getQuestion() {
        return question;
    }

    public String getTrueAnswer() {
        return trueAnswer;
    }

    public String getAnswer1() {
        return answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public boolean answerIsTrue() {
        return nameResolvedAnswer.equals(trueAnswer);
    }

    public int getIndexCorrectAnswer() {
        if (answer1.equals(trueAnswer)) return 0;
        else if (answer2.equals(trueAnswer)) return 1;
        else if (answer3.equals(trueAnswer)) return 2;
        else return 3;
    }

}
