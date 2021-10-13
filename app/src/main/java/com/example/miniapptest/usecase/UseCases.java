package com.example.miniapptest.usecase;

import com.example.miniapptest.repository.DataRepository;
import com.example.miniapptest.screens.question.Question;
import com.example.miniapptest.support.EnumEvent;
import java.text.DecimalFormat;
import java.util.List;
public class UseCases {
    private DataRepository dataRepository;
    private static int questionNumber = -1;
    private Question question;
    private boolean isFirstQuestion;
    private boolean isLastQuestion;

    public UseCases(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public Question getQuestion(EnumEvent enumEvent, int index) {
        switch (enumEvent) {
            case START_NEW_TEST:
                dataRepository.startNewTest();
                questionNumber = 0;
                break;

            case NEW_TEST:
                questionNumber = 0;
                break;

            case BEFORE_QUESTION:
                if (!isLastQuestion) decreaseQuestionNumber();
                else isLastQuestion = false;
                break;

            case OVERVIEW_QUESTION:
                question = dataRepository.getData(enumEvent, index);
                return question;
        }
        question = dataRepository.getData(enumEvent, questionNumber);
        return question;
    }



    /**
     * Вызывается при нажатии на кнопку "Следующий вопрос". Если пользователь выбрал ответ,
     * то будет либо открыт следующий вопрос либо завершен тест. Если ответ не выбран, будет отправлено
     * сообытие о том, что ответ еще не выбран и переход к следующему вопросу не возможен.
     *
     * @return возвращает события: либо следующий вопрос, либо завершить тест, либо необходимо выбрать ответ
     */
    public EnumEvent getEventForClickNextQuestion() {
        if (question.isResolved()) {
            return getEventForNextQuestion();
        } else return EnumEvent.QUESTION_IS_NOT_RESOLVED;
    }

    /**
     * Получает событие которое необходимо выполнить когда пользователь нажимает на кнопку
     * "следующий вопрос". Если в списке еще есть вопросы - откроется следующий вопрос. Если в списке
     * отсутсвуют вопросы, будет открыт фрагмент с результатами теста
     *
     * @return возвращает событие либо открыть следующий вопрос либо завершить тест
     */
    public EnumEvent getEventForNextQuestion() {
        if (sizeOfListIsCorrect()) {
            increaseQuestionNumber();
            return EnumEvent.NEXT_QUESTION;
        } else {
            isLastQuestion = true;
            return EnumEvent.FINISH_TEST;
        }
    }


    /**
     * Проверяет решен ли пользователем данный вопрос
     *
     * @return возвращает true если вопрос решен
     */
    public boolean checkAnswerIsResolved() {
        return question.isResolved();
    }

    /**
     * Проверяет и сохраняет ответ пользователя
     *
     * @param checkAnswer   выбранный ответ пользователем
     * @param indexOfAnswer индекс выбранного ответа
     * @return возвращает true если ответ пользователя правильный
     */
    public boolean checkSelectedAnswer(String checkAnswer, int indexOfAnswer) {
        question.setIndexOfAnswer(indexOfAnswer);
        question.setNameResolvedAnswer(checkAnswer);
        return question.isSolvedCorrectly();
    }


    /**
     * Увеличивает переменную номера вопроса. Если переменная равна 0,
     * то предполагается, что открыт первый вопрос и переменной isFirstQuestion присваивается false
     * Переменная isFirstQuestion позволяет отслеживать первый вопрос, чтобы уведомить пользователя
     * что он находится на первом вопросе и кнопка BACK не срабатывала
     */
    public void increaseQuestionNumber() {
        if (questionNumber == 0) isFirstQuestion = false;
        if (sizeOfListIsCorrect()) {
            questionNumber++;
        }
    }

    /**
     * Уменьшает переменную номера вопроса.
     * Переменная isFirstQuestion позволяет отслеживать первый вопрос, чтобы уведомить пользователя
     * что он находится на первом вопросе и кнопка BACK не срабатывала
     */
    public void decreaseQuestionNumber() {
        if (questionNumber > 0)
            questionNumber--;
        else isFirstQuestion = true;
    }

    /**
     * @return возвращает true если пользователь находится на первом вопросе
     */
    public boolean isFirstQuestion() {
        return isFirstQuestion;
    }

    /**
     * @return возвращает index выбранного ответа
     */
    public int getIndexOfAnswer() {
        return question.getIndexOfResolvedAnswer();
    }

    /**
     * @return возвращает true если вопрос решен правильно
     */
    public boolean answerIsTrue() {
        return question.answerIsTrue();
    }

    /**
     * Проверяет есть ли следующий вопрос в списке
     *
     * @return возвращает true если в списке есть следующий вопрос
     */
    private boolean sizeOfListIsCorrect() {
        return questionNumber + 1 < dataRepository.sizeOfList();
    }

    /**
     * Расчитывает количество правильных ответов
     *
     * @return возвращает массив с количеством правильных и неправильных ответов
     */
    public int[] getAmountTrueAnswers() {
        int[] m = new int[2];
        int i = dataRepository.numberOfCorrectAnswer();
        m[0] = i;
        m[1] = dataRepository.getSizeOfQuestionsList() - i;
        return m;
    }

    /**
     * Высчитывает % правильных ответов и форматирует в формат без запятой
     *
     * @return возвращает String процент правильных ответов
     */
    public String getPercentCorrectAnswers() {
        int numberOfCorrectAnswer = dataRepository.numberOfCorrectAnswer();
        int listSize = dataRepository.getSizeOfQuestionsList();
        float result = (float) numberOfCorrectAnswer / listSize * 100;
        String format = new DecimalFormat("##").format(result);
        return format + "%";
    }

    public List<Question> getListQuestions() {
        return dataRepository.getListQuestions();
    }

    public int getIndexCorrectAnswer(){
        if(question.getAnswer1().equals(question.getTrueAnswer())) return 0;
        else if(question.getAnswer2().equals(question.getTrueAnswer())) return 1;
        else if (question.getAnswer3().equals(question.getTrueAnswer())) return 2;
        else return 3;
    }


}
