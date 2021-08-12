package com.example.miniapptest.screens;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.miniapptest.Question;
import com.example.miniapptest.R;
import com.example.miniapptest.screens.interfaces.IFragmentQuestion;

public class QuestionsFragment extends Fragment {

    private TextView tvShowQuestions;
    private TextView tvAnswerFirst;
    private TextView tvAnswerSecond;
    private TextView tvAnswerThird;
    private TextView tvAnswerFourth;
    private ViewModel viewModel;
    private Button buttonNextQuestion;
    private Button buttonBack;
    private IFragmentQuestion iFragmentQuestion;

    private int ID_COLOR_ANSWER_TRUE;
    private int ID_COLOR_ANSWER_FALSE;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IFragmentQuestion) {
            iFragmentQuestion = (IFragmentQuestion) context;
        } else
            throw new RuntimeException(context.toString() + "must implement IFragmentDataListener");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.questions_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializationViews(view);

        ID_COLOR_ANSWER_TRUE = view.getResources().getColor(R.color.true_answer);
        ID_COLOR_ANSWER_FALSE = view.getResources().getColor(R.color.false_answer);
        setOnClickListenerToTextView();

        nextQuestion();
        previousQuestion();


        LiveData<Question> data = viewModel.loadData();
        data.observe(getViewLifecycleOwner(), new Observer<Question>() {
            @Override
            public void onChanged(Question question) {
                setDataToView(question);
            }
        });

        if(viewModel.checkAnswerIsResolved()){
            setAnswerIsResolved();
        }


//

    }

    @Override
    public void onStart() {
//        if (viewModel.getAnswerIsSelected())
//            setSelectedAnswer(viewModel.getIndexSelectedAnswer(), viewModel.getSavedSelectedAnswer());
        super.onStart();
    }


    private void setOnClickListenerToTextView() {
        tvAnswerFirst.setOnClickListener(v -> {
            selectAnswer(tvAnswerFirst, 0);
        });

        tvAnswerSecond.setOnClickListener(v -> {
            selectAnswer(tvAnswerSecond, 1);
        });

        tvAnswerThird.setOnClickListener(v -> {
            selectAnswer(tvAnswerThird, 2);
        });

        tvAnswerFourth.setOnClickListener(v -> {
            selectAnswer(tvAnswerFourth, 3);
        });
    }

    private void selectAnswer(TextView view, int indexSelectedAnswer) {
        if (!viewModel.checkAnswerIsResolved()) {
            boolean answerIsTrue = viewModel.checkSelectedAnswer(view.getText().toString(), indexSelectedAnswer);
            if (answerIsTrue) view.setBackgroundColor(ID_COLOR_ANSWER_TRUE);
            else view.setBackgroundColor(ID_COLOR_ANSWER_FALSE);
        }
    }

    private void setAnswerIsResolved(){
        int ID_COLOR_ANSWER;
        if (viewModel.answerIsTrue()) {
            ID_COLOR_ANSWER = ID_COLOR_ANSWER_TRUE;
        } else ID_COLOR_ANSWER = ID_COLOR_ANSWER_FALSE;

        switch (viewModel.getResolvedAnswer()){
            case 0:
                tvAnswerFirst.setBackgroundColor(ID_COLOR_ANSWER);
                break;
            case 1:
                tvAnswerSecond.setBackgroundColor(ID_COLOR_ANSWER);
                break;
            case 2:
                tvAnswerThird.setBackgroundColor(ID_COLOR_ANSWER);
                break;
            case 3:
                tvAnswerFourth.setBackgroundColor(ID_COLOR_ANSWER);
                break;
        }
    }

    private void nextQuestion() {
        buttonNextQuestion.setOnClickListener(v -> {
            if (!viewModel.isLastQuestion()) {
                if (viewModel.checkAnswerIsResolved()) {
                    viewModel.increaseQuestionNumber();
                    iFragmentQuestion.nextQuestion();
                } else
                    Toast.makeText(getContext(), "Нужно выбрать ответ", Toast.LENGTH_SHORT).show();
            } else iFragmentQuestion.finishTest();
        });
    }

    private void previousQuestion() {

        buttonBack.setOnClickListener(v -> {
            if (!viewModel.isFirstQuestion()) iFragmentQuestion.backButton();
            else Toast.makeText(getContext(), "Это первый вопрос", Toast.LENGTH_SHORT).show();
        });
//        viewModel.decreaseQuestionNumber();
    }

    private void setSelectedAnswer(int indexOfAnswer, boolean selectedAnswer) {
        int ID_COLOR_ANSWER;
        if (selectedAnswer) {
            ID_COLOR_ANSWER = ID_COLOR_ANSWER_TRUE;
        } else ID_COLOR_ANSWER = ID_COLOR_ANSWER_FALSE;

        switch (indexOfAnswer) {
            case 0:
                tvAnswerFirst.setBackgroundColor(ID_COLOR_ANSWER);
                break;
            case 1:
                tvAnswerSecond.setBackgroundColor(ID_COLOR_ANSWER);
                break;
            case 2:
                tvAnswerThird.setBackgroundColor(ID_COLOR_ANSWER);
                break;
            case 3:
                tvAnswerFourth.setBackgroundColor(ID_COLOR_ANSWER);
                break;
        }
    }


    private void initializationViews(View view) {
        tvShowQuestions = view.findViewById(R.id.tvShowQuestions);
        tvAnswerFirst = view.findViewById(R.id.tvAnswerFirst);
        tvAnswerSecond = view.findViewById(R.id.tvAnswerSecond);
        tvAnswerThird = view.findViewById(R.id.tvAnswerThird);
        tvAnswerFourth = view.findViewById(R.id.tvAnswerFourth);
        buttonNextQuestion = view.findViewById(R.id.buttonNext);
        buttonBack = view.findViewById(R.id.buttonBack);
    }

    private void setDataToView(Question question) {
        if (question != null) {
            tvShowQuestions.setText(question.getQuestion());
            tvAnswerFirst.setText(question.getAnswer1());
            tvAnswerSecond.setText(question.getAnswer2());
            tvAnswerThird.setText(question.getAnswer3());
            tvAnswerFourth.setText(question.getAnswer4());
        }
    }

    @Override
    public void onDestroyView() {
        System.out.println("onDestroyView");
        super.onDestroyView();
    }
}
