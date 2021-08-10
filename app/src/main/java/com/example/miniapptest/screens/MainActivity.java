package com.example.miniapptest.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.miniapptest.screens.interfaces.IFragmentStart;
import com.example.miniapptest.R;
import com.example.miniapptest.UseCases;
import com.example.miniapptest.screens.interfaces.IFragmentQuestion;

public class MainActivity extends AppCompatActivity implements IFragmentStart, IFragmentQuestion {
    private FragmentManager fragmentManager;
private ViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.containerFrameLayout, new StartFragment())
                    .commit();
        }
        UseCases useCases = new UseCases();
       viewModel  = new ViewModelProvider(this, new ModelFactory(useCases)).get(ViewModel.class);
//        ViewModel viewModel = new ViewModelProvider(this).get(ViewModel.class);
    }

    @Override
    public void startTest() {
        fragmentManager.beginTransaction()
                .replace(R.id.containerFrameLayout, new QuestionsFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void nextQuestion() {
        fragmentManager.beginTransaction()
                .replace(R.id.containerFrameLayout, new QuestionsFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void finishTest() {
        fragmentManager.beginTransaction()
                .replace(R.id.containerFrameLayout, new FinishFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void backButton() {
        viewModel.decreaseQuestionNumber();
        fragmentManager.popBackStack();
    }

    @Override
    public void onBackPressed() {
        viewModel.decreaseQuestionNumber();
        super.onBackPressed();
    }
}