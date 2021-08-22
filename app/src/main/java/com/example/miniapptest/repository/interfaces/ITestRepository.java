package com.example.miniapptest.repository.interfaces;

import com.example.miniapptest.screens.question.Question;

import java.util.List;

public interface ITestRepository {

    List<Question> getDataFromJSON();
}
