package com.team.app.backend.persistance.dao;

import com.team.app.backend.persistance.dao.mappers.QuizCategoryRowMapper;
import com.team.app.backend.persistance.model.QuizCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
public class QuizCategoryDaoImpl implements QuizCategoryDao {



    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private QuizCategoryRowMapper quizCategoryRowMapper;

    public QuizCategoryDaoImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void create(QuizCategory announcement) {

    }

    @Override
    public QuizCategory get(Long id) {
        return null;
    }

    @Override
    public List<QuizCategory> getAll() {
        return jdbcTemplate.query(
                "SELECT id, name, description FROM quiz_category;"
                ,quizCategoryRowMapper);
    }

    @Override
    public void update(QuizCategory announcement) {

    }

    @Override
    public void delete(Long id) {

    }
}
