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
        LiveData<Question> data = viewModel.loadData();
        data.observe(getViewLifecycleOwner(), new Observer<Question>() {
            @Override
            public void onChanged(Question question) {
                setDataToView(question);
                if (viewModel.checkAnswerIsResolved()) {
                    setAnswerIsResolved();
                }
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

    private void setAnswerIsResolved() {
        int ID_COLOR_ANSWER;
        int indexOfTrueAnswer;
        if (viewModel.answerIsTrue()) {
            ID_COLOR_ANSWER = ID_COLOR_ANSWER_TRUE;
        } else {
            indexOfTrueAnswer = viewModel.getIndexTrueAnswer();
            ID_COLOR_ANSWER = ID_COLOR_ANSWER_FALSE;
            setColorToView(indexOfTrueAnswer, ID_COLOR_CORRECT_ANSWER);

        }
        setColorToView(viewModel.getResolvedAnswer(),ID_COLOR_ANSWER);
    }

    private void setColorToView(int idView, int colorAnswer){
        switch (idView) {
            case 0:
                tvAnswerFirst.setBackgroundColor(colorAnswer);
                break;
            case 1:
                tvAnswerSecond.setBackgroundColor(colorAnswer);
                break;
            case 2:
                tvAnswerThird.setBackgroundColor(colorAnswer);
                break;
            case 3:
                tvAnswerFourth.setBackgroundColor(colorAnswer);
                break;
        }
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
}
