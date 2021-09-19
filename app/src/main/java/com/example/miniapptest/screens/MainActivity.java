package com.example.miniapptest.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

import com.example.miniapptest.repository.DataRepository;
import com.example.miniapptest.screens.interfaces.IFinishFragmentListener;
import com.example.miniapptest.screens.interfaces.IFragmentOverview;
import com.example.miniapptest.screens.interfaces.IFragmentStart;
import com.example.miniapptest.R;
import com.example.miniapptest.support.EnumEvent;
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
        DataRepository dataRepository = new DataRepository(this);
        useCases = new UseCases(dataRepository);

        viewModel = new ViewModelProvider(this, new ModelFactory(useCases)).get(ViewModel.class);
        //При запуске приложения проверяется проходился ли раньше тест, если тест проходился
        //то будет открыт тест на первом вопросе с ответами пользователя, если первый запуск, то
        //будет открыт фрагмент для запуска теста
        if (savedInstanceState == null) {
            if (dataRepository.isTestStarted()) {
                viewModel.loadData(EnumEvent.NEW_TEST, 0);
                startNewQuestionFragment();
            } else {
                fragmentManager.beginTransaction()
                        .add(R.id.containerFrameLayout, new StartFragment())
                        .commit();
                dataRepository.testStarted(true);
            }
        }
//        else {
//
////            finishFragmentIsVisible = savedInstanceState.getBoolean(KEY_BUNDLE);
//        }
        getLifecycle().addObserver(dataRepository);
//        getLifecycle().addObserver(viewModel);
    }

    @Override
    public void startTest() {
        startNewQuestionFragment();
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
        fragmentManager.popBackStack();
    }

    @Override
    public void startNewTest() {
        newTest();
    }

    @Override
    public void startNewTestFromFinishFragment() {
        newTest();
    }

    @Override
    public void onBackPressed() {
        if (!viewModel.getEventBoolean(EnumEvent.BEFORE_QUESTION)) super.onBackPressed();
        else Toast.makeText(this, "Это первый вопрос", Toast.LENGTH_SHORT).show();
    }

    private void clearBackStack() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
//            viewModel.clearData();
            FragmentManager.BackStackEntry first = fragmentManager.getBackStackEntryAt(0);
            fragmentManager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    private void startNewQuestionFragment() {
        fragmentManager.beginTransaction()
                .replace(R.id.containerFrameLayout, new QuestionsFragment())
                .addToBackStack(null)
                .commit();
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(KEY_BUNDLE, finishFragmentIsVisible);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onNumberOnClick(int position) {
//        viewModel.setNumberOfQuestionForOverview(position);
        Bundle arguments = new Bundle();
        arguments.putInt("Key", position);
        Fragment fragment = new OverviewResponsesFragment();
        fragment.setArguments(arguments);
        fragmentManager.beginTransaction()
                .replace(R.id.containerFrameLayout, fragment)
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void backToListFromFragmentOverview() {
        fragmentManager.popBackStack();
//        if (UseCases.isOverviewResponse){
//            viewModel.returnNumberOfQuestion();
//            super.onBackPressed();
//        }
    }

    private void newTest() {
        clearBackStack();
        fragmentManager.beginTransaction()
                .add(R.id.containerFrameLayout, new StartFragment())
                .addToBackStack(null)
                .commit();
    }


}