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
import androidx.arch.core.util.Function;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miniapptest.R;
import com.example.miniapptest.screens.adapter.FinishFragmentAdapter;
import com.example.miniapptest.screens.interfaces.IFinishFragmentListener;
import com.example.miniapptest.screens.interfaces.IFragmentQuestion;
import com.example.miniapptest.screens.viewmodel.ViewModel;
import com.example.miniapptest.support.EnumEvent;

public class FinishFragment extends Fragment {
    private ViewModel viewModel;
    private int[] amountTrueAndFalseAnswers;
    private RecyclerView recyclerView;
    private FinishFragmentAdapter adapter;
    private IFinishFragmentListener iFinishFragmentListener;
    private Button buttonStartTestAgain;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof IFinishFragmentListener) {
            iFinishFragmentListener = (IFinishFragmentListener) context;
        } else throw new ClassCastException("context can`t be casted to IFinishFragmentListener");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ViewModel.class);
        amountTrueAndFalseAnswers = viewModel.getAmountTrueAnswers();
        adapter = new FinishFragmentAdapter(viewModel.getListQuestions(), new Function<Integer, Void>() {
            @Override
            public Void apply(Integer input) {
                iFinishFragmentListener.onNumberOnClick(input);
                return null;
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.finish_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        TextView textViewPercent = view.findViewById(R.id.textViewFinishPercent);
        TextView textViewAmountTrueAnswers = view.findViewById(R.id.textViewFinishInfoTrueAnswers);
        TextView textViewAmountFalseAnswers = view.findViewById(R.id.textViewFinishInfoFalseAnswers);
        buttonStartTestAgain = view.findViewById(R.id.buttonFinishFragmentStartNewTest);
        buttonStartTestAgain.setOnClickListener(v -> {
            viewModel.startNewTest(EnumEvent.START_NEW_TEST);
            iFinishFragmentListener.startNewTestFromFinishFragment();
        });
        textViewPercent.setText(viewModel.getPercentTrueAnswers());
        textViewAmountTrueAnswers.setText(String.valueOf(amountTrueAndFalseAnswers[0]));
        textViewAmountFalseAnswers.setText(String.valueOf(amountTrueAndFalseAnswers[1]));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 5));
    }
}
