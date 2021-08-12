package com.example.miniapptest.screens;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.miniapptest.UseCases;

public class ModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final UseCases useCases;

    public ModelFactory(UseCases useCases) {
        super();
        this.useCases = useCases;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull  Class<T> modelClass) {
        if (modelClass == com.example.miniapptest.screens.ViewModel.class) {
            return (T) new com.example.miniapptest.screens.ViewModel(useCases);
        }

        return null;
    }
}
