package com.example.miniapptest;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UseCases {
    private TestRepository testRepository = new TestRepository();
    private List<Question> listOfQuestions = new ArrayList<>();
    private int questionNumber = -1;

    public Question getData() {

        listOfQuestions.clear();

        listOfQuestions.addAll(testRepository.getDataFromJSON());
        questionNumber++;
        return getQuestion();
    }

    public Question getQuestion() {
        return listOfQuestions.get(questionNumber);
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
        return listOfQuestions.get(questionNumber).isResolved();
    }

    public boolean isResolved() {
        return listOfQuestions.get(questionNumber).isResolved();
    }

    public boolean isLastQuestion() {
        return questionNumber + 1 == listOfQuestions.size();
    }

    public boolean isFirstLaunchApp() {
         return questionNumber == -1;
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

    public double percentCorrectAnswers() {
        return (double) numberOfCorrectAnswer() / listOfQuestions.size() * 100;
    }

    public void increaseQuestionNumber() {
        questionNumber++;
    }

    public void decreaseQuestionNumber() {
        if (questionNumber != 0) questionNumber--;
    }

    public boolean answerIsTrue() {
        Question q = listOfQuestions.get(questionNumber);
        return q.getNameResolvedAnswer().equals(q.getTrueAnswer());
    }
}
