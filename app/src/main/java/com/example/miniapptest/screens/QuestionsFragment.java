package com.example.miniapptest.screens;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
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

import com.example.miniapptest.screens.question.Question;
import com.example.miniapptest.R;
import com.example.miniapptest.screens.interfaces.IFragmentQuestion;
import com.example.miniapptest.screens.viewmodel.ViewModel;
import com.example.miniapptest.support.EnumEvent;

import org.w3c.dom.ls.LSOutput;

public class QuestionsFragment extends Fragment {
    private TextView tvShowQuestions;
    private TextView tvAnswerFirst;
    private TextView tvAnswerSecond;
    private TextView tvAnswerThird;
    private TextView tvAnswerFourth;
    private ViewModel viewModel;
    private Button buttonNextQuestion;
    private Button buttonBack;
    private Button buttonStartTestAgain;
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
        setClickListenerNextQuestion();
        setClickListenerPreviousQuestion();
        setClickListenerStartNewTest();

        LiveData<Question> data = viewModel.loadData(EnumEvent.START_FRAGMENT, 0);
        data.observe(getViewLifecycleOwner(), new Observer<Question>() {
            @Override
            public void onChanged(Question question) {
                setDataToView(question);
                if (viewModel.getEventBoolean(EnumEvent.CHECK_ANSWER_IS_RESOLVED)) {
                    setColorAnswerIsResolved();
                }
            }
        });
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
        String selectedAnswer = view.getText().toString();
        if (!viewModel.getEventBoolean(EnumEvent.CHECK_ANSWER_IS_RESOLVED)) {
            if (viewModel.checkSelectedAnswer(selectedAnswer, indexSelectedAnswer))
                view.setBackgroundColor(ID_COLOR_ANSWER_TRUE);
            else view.setBackgroundColor(ID_COLOR_ANSWER_FALSE);
        }
    }

    private void setClickListenerNextQuestion() {
        buttonNextQuestion.setOnClickListener(v -> {
            switch (viewModel.getEventForClickNextQuestion()) {
                case NEXT_QUESTION:
                    iFragmentQuestion.nextQuestion();
                    break;
                case FINISH_TEST:
                    iFragmentQuestion.finishTest();
                    break;
                case QUESTION_IS_NOT_RESOLVED:
                    Toast.makeText(getContext(), "Нужно выбрать ответ", Toast.LENGTH_SHORT).show();
                    break;
            }
        });

    }

    private void setColorAnswerIsResolved() {
        int ID_COLOR_ANSWER;
        if (viewModel.getEventBoolean(EnumEvent.CHECK_ANSWER_IS_TRUE)) {
            ID_COLOR_ANSWER = ID_COLOR_ANSWER_TRUE;
        } else ID_COLOR_ANSWER = ID_COLOR_ANSWER_FALSE;

        switch (viewModel.getIndexOfResolvedAnswer()) {
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

    private void setClickListenerPreviousQuestion() {
        buttonBack.setOnClickListener(v -> {
            if (!viewModel.getEventBoolean(EnumEvent.BEFORE_QUESTION)) iFragmentQuestion.backButton();
            else Toast.makeText(getContext(), "Это первый вопрос", Toast.LENGTH_SHORT).show();
        });
    }

    private void initializationViews(View view) {
        tvShowQuestions = view.findViewById(R.id.tvShowQuestions);
        tvAnswerFirst = view.findViewById(R.id.tvAnswerFirst);
        tvAnswerSecond = view.findViewById(R.id.tvAnswerSecond);
        tvAnswerThird = view.findViewById(R.id.tvAnswerThird);
        tvAnswerFourth = view.findViewById(R.id.tvAnswerFourth);
        buttonNextQuestion = view.findViewById(R.id.buttonNext);
        buttonBack = view.findViewById(R.id.buttonBack);
        buttonStartTestAgain = view.findViewById(R.id.buttonStartTestAgain);
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

    private void setClickListenerStartNewTest() {
        buttonStartTestAgain.setOnClickListener(v -> {
            viewModel.startNewTest(EnumEvent.START_NEW_TEST);
            iFragmentQuestion.startNewTest();
        });
    }

    //При изменении конфигурации заново подгружаем данные
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        viewModel.loadData(EnumEvent.CONFIGURATION_CHANGED);
//        super.onConfigurationChanged(newConfig);
//        Log.i("CONFIGURATION", "Configuration Changed");
//    }


}
