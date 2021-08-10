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
    private String[] questionsList;
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
        initializationView(view);
        ID_COLOR_ANSWER_TRUE = view.getResources().getColor(R.color.true_answer);
        ID_COLOR_ANSWER_FALSE = view.getResources().getColor(R.color.false_answer);
        buttonNextQuestion.setOnClickListener(v -> {
            if(viewModel.notTheLastQuestion()) {
                if (viewModel.getAnswerIsSelected()) {
                    viewModel.setAnswerIsSelected();
                    viewModel.increaseQuestionNumber();
                    iFragmentQuestion.nextQuestion();
                    iFragmentQuestion.nextQuestion();
                } else
                    Toast.makeText(getContext(), "Нужно выбрать ответ", Toast.LENGTH_SHORT).show();
            }else {
                iFragmentQuestion.finishTest();
            }
        });

        LiveData<String[]> data = viewModel.loadData();
        data.observe(getViewLifecycleOwner(), new Observer<String[]>() {
            @Override
            public void onChanged(String[] strings) {
                setDataToView(strings);
            }
        });

        setOnClickListenerToTextView();
        buttonBack.setOnClickListener(v -> {
            if (viewModel.getNumberOfQuestion() != 0) iFragmentQuestion.backButton();
            else Toast.makeText(getContext(), "Это первый вопрос", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onStart() {
        if (viewModel.getAnswerIsSelected())
            setSelectedAnswer(viewModel.getIndexSelectedAnswer(), viewModel.getSavedSelectedAnswer());
        super.onStart();
    }

    private void setDataToView(String[] strings) {
        questionsList = strings;
        if (questionsList != null) {
            tvShowQuestions.setText(strings[0]);
            tvAnswerFirst.setText(strings[1]);
            tvAnswerSecond.setText(strings[2]);
            tvAnswerThird.setText(strings[3]);
            tvAnswerFourth.setText(strings[4]);
        }
    }

    private void setOnClickListenerToTextView() {
        tvAnswerFirst.setOnClickListener(v -> {
            checkSelectedAnswer(tvAnswerFirst, 0);
        });

        tvAnswerSecond.setOnClickListener(v -> {
            checkSelectedAnswer(tvAnswerSecond, 1);
        });

        tvAnswerThird.setOnClickListener(v -> {
            checkSelectedAnswer(tvAnswerThird, 2);
        });

        tvAnswerFourth.setOnClickListener(v -> {
            checkSelectedAnswer(tvAnswerFourth, 3);
        });
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

    private void checkSelectedAnswer(TextView view, int indexSelectedAnswer) {
        if (!viewModel.getAnswerIsSelected()) {
            boolean checkAnswer = viewModel.checkAnswerToCorrect(view.getText().toString());
            viewModel.saveSelectedAnswer(checkAnswer);
            if (checkAnswer) {
                view.setBackgroundColor(ID_COLOR_ANSWER_TRUE);
            } else
                view.setBackgroundColor(ID_COLOR_ANSWER_FALSE);
            viewModel.setAnswerIsSelected();
            viewModel.saveIndexOfSelectedAnswer(indexSelectedAnswer);
        }
    }

    private void initializationView(View view) {
        tvShowQuestions = view.findViewById(R.id.tvShowQuestions);
        tvAnswerFirst = view.findViewById(R.id.tvAnswerFirst);
        tvAnswerSecond = view.findViewById(R.id.tvAnswerSecond);
        tvAnswerThird = view.findViewById(R.id.tvAnswerThird);
        tvAnswerFourth = view.findViewById(R.id.tvAnswerFourth);
        buttonNextQuestion = view.findViewById(R.id.buttonNext);
        buttonBack = view.findViewById(R.id.buttonBack);
    }

}
