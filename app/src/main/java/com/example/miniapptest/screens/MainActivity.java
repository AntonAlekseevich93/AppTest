package com.example.miniapptest.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Toast;

import com.example.miniapptest.screens.interfaces.IFinishFragmentListener;
import com.example.miniapptest.screens.interfaces.IFragmentOverview;
import com.example.miniapptest.screens.interfaces.IFragmentStart;
import com.example.miniapptest.R;
import com.example.miniapptest.usecase.UseCases;
import com.example.miniapptest.screens.interfaces.IFragmentQuestion;
import com.example.miniapptest.screens.viewmodel.ViewModel;

public class MainActivity extends AppCompatActivity implements IFragmentStart, IFragmentQuestion, IFinishFragmentListener, IFragmentOverview {
    private FragmentManager fragmentManager;
    private ViewModel viewModel;
    private boolean finishFragmentIsVisible = false;
    private final String KEY_BUNDLE = "KEY_BUNDLE";
    private UseCases useCases;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        useCases = new UseCases(this);
        viewModel = new ViewModelProvider(this, new ModelFactory(useCases)).get(ViewModel.class);
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.containerFrameLayout, new StartFragment())
                    .commit();
        } else {
            finishFragmentIsVisible = savedInstanceState.getBoolean(KEY_BUNDLE);
        }
        getLifecycle().addObserver(useCases);
        getLifecycle().addObserver(viewModel);
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
        finishFragmentIsVisible = true;
    }

    @Override
    public void backButton() {
        decreaseQuestionNumber();
        fragmentManager.popBackStack();
    }

    @Override
    public void startNewTest() {
        clearBackStack();
        fragmentManager.beginTransaction()
                .add(R.id.containerFrameLayout, new StartFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (!useCases.isFirstQuestion() && !UseCases.isOverviewResponse) {
            decreaseQuestionNumber();
            super.onBackPressed();
        } else if(UseCases.isOverviewResponse){
            viewModel.returnNumberOfQuestion();
            super.onBackPressed();
        }
        else Toast.makeText(this, "Это первый вопрос", Toast.LENGTH_SHORT).show();

    }

    private void clearBackStack() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            viewModel.clearData();
            FragmentManager.BackStackEntry first = fragmentManager.getBackStackEntryAt(0);
            fragmentManager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    private void decreaseQuestionNumber() {
        if (!finishFragmentIsVisible) {
            viewModel.decreaseQuestionNumber();
        } else finishFragmentIsVisible = false;

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(KEY_BUNDLE, finishFragmentIsVisible);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onNumberOnClick(int position) {
        viewModel.setNumberOfQuestionForOverview(position);
        fragmentManager.beginTransaction()
                .replace(R.id.containerFrameLayout, new OverviewResponsesFragment())
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void backToListFromFragmentOverview() {
        if (UseCases.isOverviewResponse){
            viewModel.returnNumberOfQuestion();
            super.onBackPressed();
        }
    }
}