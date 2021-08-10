package com.example.miniapptest;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class UseCases implements IUseCase {
    private ITestRepository ItestRepository = new TestRepository();
    public static int questionNumber = -1;
    private static boolean[] answerIsSelected;
    private static boolean[] selectedAnswer;
    private int[] indexOfSelectedAnswer;
    private List<Question> listOfQuestion = new ArrayList<>();
    private List<String[]> randomSortingQuestionsList = new ArrayList<>();
    private int[] scoreCounter;
    private final int TRUE_QUESTION = 1;
    private final int FALSE_QUESTION = 0;


    private void randomSortingList(List<Question> list) {
        Collections.shuffle(list);
        listOfQuestion.clear();
        listOfQuestion.addAll(list);
        generateRandomQuestions(list);
        scoreCounter = new int[randomSortingQuestionsList.size()];
        answerIsSelected = new boolean[randomSortingQuestionsList.size()];
        indexOfSelectedAnswer = new int[randomSortingQuestionsList.size()];
        selectedAnswer = new boolean[randomSortingQuestionsList.size()];
    }

    public boolean checkAnswerToCorrect(String selectedAnswer) {
        String trueAnswer = listOfQuestion.get(questionNumber).getTrueAnswer();
        if (selectedAnswer.equals(trueAnswer)) {
            scoreCounter[questionNumber] = TRUE_QUESTION;
            return true;
        } else {
            scoreCounter[questionNumber] = FALSE_QUESTION;
            return false;
        }
    }

    @Override
    public String[] getNextQuestion() {
        if (questionNumber < randomSortingQuestionsList.size()) {
            return randomSortingQuestionsList.get(questionNumber);
        } else {
            return null;
        }

    }

    @Override
    public void setAnswerIsSelected() {
        answerIsSelected[questionNumber] = true;
    }

    @Override
    public boolean getAnswerIsSelected() {
        return answerIsSelected[questionNumber];
    }

    @Override
    public void increaseQuestionNumber() {
        if (questionNumber < listOfQuestion.size() - 1) {
            questionNumber++;
        }
    }

    @Override
    public void decreaseQuestionNumber() {
        questionNumber--;
    }

    @Override
    public int getIndexSelectedAnswer() {
        return indexOfSelectedAnswer[questionNumber];
    }


    @Override
    public String[] getData() {
        randomSortingList(loadData());
        return randomSortingQuestionsList.get(questionNumber);
    }

    private List<Question> loadData() {
        questionNumber = 0;
        return ItestRepository.getDataFromJSON();
    }

    public void saveIndexOfSelectedAnswer(int index) {
        indexOfSelectedAnswer[questionNumber] = index;
    }

    @Override
    public void saveSelectedAnswer(boolean answer) {
        selectedAnswer[questionNumber] = answer;
    }

    @Override
    public boolean getSavedSelectedAnswer() {
        return selectedAnswer[questionNumber];
    }

    @Override
    public int getNumberOfQuestion() {
        return questionNumber;
    }

    @Override
    public String getPercentTrueAnswers() {
        int count = 0;
        int amountQuestion = listOfQuestion.size();
        for (int i : scoreCounter) {
            count = count + i;
        }
        double i = (double) count / amountQuestion * 100;
        String result = String.valueOf(i + "%");
        return result;
    }

    @Override
    public int[] getAmountTrueAnswers() {
        int countTrue = 0;
        int countFalse = 0;
        for (int i : scoreCounter) {
            if (i == 0) {
                countFalse++;
            } else countTrue++;
        }
        int i[] = new int[]{countTrue, countFalse};

        return i;
    }

    @Override
    public boolean notTheLastQuestion() {
        int i = questionNumber;
        return i + 1 < listOfQuestion.size();
    }


    private void generateRandomQuestions(List<Question> list) {
        randomSortingQuestionsList.clear();
        for (int i = 0; i < list.size(); i++) {
            Question q = list.get(i);
            String question = q.getQuestion();
            String answer1 = q.getTrueAnswer();
            String answer2 = q.getFalseAnswer1();
            String answer3 = q.getFalseAnswer2();
            String answer4 = q.getFalseAnswer3();
            String[] randomArray = new String[5];
            int count = 0;
            randomArray[count] = question;
            count++;
            List<String> getRandomItem = Arrays.asList(answer1, answer2, answer3, answer4);
            Collections.shuffle(getRandomItem);
            for (String s : getRandomItem) {
                randomArray[count] = s;
                count++;
            }
            randomSortingQuestionsList.add(randomArray);

        }
    }

    private double amountTrueAnswer() {
        int count = 0;
        int amountQuestion = listOfQuestion.size();
        for (int i : scoreCounter) {
            count = count + i;
        }
        return (double) (count / amountQuestion) * 100;
    }


}
