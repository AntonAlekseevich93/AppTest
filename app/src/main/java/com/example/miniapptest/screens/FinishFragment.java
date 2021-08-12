package com.example.miniapptest.screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.miniapptest.R;

public class FinishFragment extends Fragment {
    private ViewModel viewModel;
    private int[] amountTrueAndFalseAnswers;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ViewModel.class);
//        amountTrueAndFalseAnswers = viewModel.getAmountTrueAnswers();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.finish_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView textViewPercent = view.findViewById(R.id.textViewFinishPercent);
        TextView textViewAmountTrueAnswers = view.findViewById(R.id.textViewFinishInfoTrueAnswers);
        TextView textViewAmountFalseAnswers = view.findViewById(R.id.textViewFinishInfoFalseAnswers);
//        textViewPercent.setText(viewModel.getPercentTrueAnswers());
//        textViewAmountTrueAnswers.setText(String.valueOf(amountTrueAndFalseAnswers[0]));
//        textViewAmountFalseAnswers.setText(String.valueOf(amountTrueAndFalseAnswers[1]));


    }
}
