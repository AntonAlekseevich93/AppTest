package com.example.miniapptest.screens;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.miniapptest.screens.question.Question;
import com.example.miniapptest.R;
import com.example.miniapptest.screens.interfaces.IFragmentOverview;
import com.example.miniapptest.screens.viewmodel.ViewModel;
import com.example.miniapptest.support.EnumEvent;

public class OverviewResponsesFragment extends Fragment {
    private TextView tvShowQuestions;
    private TextView tvAnswerFirst;
    private TextView tvAnswerSecond;
    private TextView tvAnswerThird;
    private TextView tvAnswerFourth;
    private ViewModel viewModel;
    private Button buttonBackToList;
    private int ID_COLOR_ANSWER_TRUE;
    private int ID_COLOR_ANSWER_FALSE;
    private int ID_COLOR_CORRECT_ANSWER;
    private IFragmentOverview iFragmentOverview;
    private int position;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IFragmentOverview) {
            iFragmentOverview = (IFragmentOverview) context;
        } else
            throw new RuntimeException(context.toString() + "must implement IFragmentDataListener");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ViewModel.class);
        position = getArguments().getInt("Key");
        System.out.println("ПОЗИЦИЯ:" + position);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.overview_responses_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializationViews(view);

        LiveData<Question> data = viewModel.loadData(EnumEvent.OVERVIEW_QUESTION, position);
        data.observe(getViewLifecycleOwner(), new Observer<Question>() {
            @Override
            public void onChanged(Question question) {
                setDataToView(question);
//                if (viewModel.getEventBoolean(EnumEvent.CHECK_ANSWER_IS_RESOLVED)) {
                setColorAnswerIsResolved();
//                }
            }
        });
        buttonBackToList.setOnClickListener(v -> {
            iFragmentOverview.backToListFromFragmentOverview();
        });

    }

    private void initializationViews(View view) {
        tvShowQuestions = view.findViewById(R.id.tvShowQuestionsOverview);
        tvAnswerFirst = view.findViewById(R.id.tvAnswerFirstOverview);
        tvAnswerSecond = view.findViewById(R.id.tvAnswerSecondOverview);
        tvAnswerThird = view.findViewById(R.id.tvAnswerThirdOverView);
        tvAnswerFourth = view.findViewById(R.id.tvAnswerFourthOverView);
        buttonBackToList = view.findViewById(R.id.buttonBackToList);
        ID_COLOR_ANSWER_TRUE = view.getResources().getColor(R.color.true_answer);
        ID_COLOR_ANSWER_FALSE = view.getResources().getColor(R.color.false_answer);
        ID_COLOR_CORRECT_ANSWER = view.getResources().getColor(R.color.correct_answer);
    }

    private void setColorAnswerIsResolved() {
        int ID_COLOR_ANSWER;
        boolean incorrectAnswer = false;
        if (viewModel.getEventBoolean(EnumEvent.CHECK_ANSWER_IS_TRUE)) {
            ID_COLOR_ANSWER = ID_COLOR_ANSWER_TRUE;
        } else {
            incorrectAnswer = true;
            ID_COLOR_ANSWER = ID_COLOR_ANSWER_FALSE;
        }
        //Устанавливает в TextView цвет правильного ответа, если пользователь ответил не правильно
        if (incorrectAnswer) {
            int indexCorrectAnswer = viewModel.getIndexCorrectAnswer();
            setBackgroundColorToTextView(indexCorrectAnswer, ID_COLOR_CORRECT_ANSWER);
        }
        //Устанавливает в TextView цвет выбранного ответа пользователя
        setBackgroundColorToTextView(viewModel.getIndexOfResolvedAnswer(), ID_COLOR_ANSWER);

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

    private void setBackgroundColorToTextView(int index, int ID_COLOR_ANSWER){
        switch (index) {
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
}
