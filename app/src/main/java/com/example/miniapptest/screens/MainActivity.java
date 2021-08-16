package com.example.miniapptest.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.miniapptest.screens.interfaces.IFinishFragmentListener;
import com.example.miniapptest.screens.interfaces.IFragmentStart;
import com.example.miniapptest.R;
import com.example.miniapptest.UseCases;
import com.example.miniapptest.screens.interfaces.IFragmentQuestion;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements IFragmentStart, IFragmentQuestion, IFinishFragmentListener {
    private FragmentManager fragmentManager;
    private ViewModel viewModel;
    private boolean finishFragmentIsVisible = false;
    private final String KEY_BUNDLE = "KEY_BUNDLE";
    public static int nameFragmentToBackStack = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        UseCases useCases = new UseCases(this);
        viewModel = new ViewModelProvider(this, new ModelFactory(useCases)).get(ViewModel.class);
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.containerFrameLayout, new StartFragment())
                    .commit();
        } else {
            finishFragmentIsVisible = savedInstanceState.getBoolean(KEY_BUNDLE);
        }

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
        System.out.println(nameFragmentToBackStack + " нект квестион");
        fragmentManager.beginTransaction()
                .replace(R.id.containerFrameLayout, new QuestionsFragment())
                .addToBackStack(String.valueOf(nameFragmentToBackStack))
                .commit();
    }

    @Override
    public void finishTest() {
        fragmentManager.beginTransaction()
                .replace(R.id.containerFrameLayout, new FinishFragment())
                .addToBackStack("Finish Fragment")
                .commit();
        finishFragmentIsVisible = true;
    }

    @Override
    public void backButton() {
        decreaseQuestionNumber();
        nameFragmentToBackStack--;
        System.out.println(nameFragmentToBackStack + " бэк буттон");
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
        decreaseQuestionNumber();
        nameFragmentToBackStack--;
        super.onBackPressed();
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
        System.out.println(position);
    }
}