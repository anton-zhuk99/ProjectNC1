package com.team.app.backend.service.impl;

import com.team.app.backend.persistance.dao.QuizCategoryDao;
import com.team.app.backend.persistance.model.QuizCategory;
import com.team.app.backend.service.QuizCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class QuizCategoryServiceImpl implements QuizCategoryService {

    private final QuizCategoryDao quizCategoryDao;

    @Autowired
    public QuizCategoryServiceImpl(QuizCategoryDao quizCategoryDao) {
        this.quizCategoryDao = quizCategoryDao;
    }


    @Override
    public List<QuizCategory> getAllQuizCategories() {
        return quizCategoryDao.getAll();
    }
}
