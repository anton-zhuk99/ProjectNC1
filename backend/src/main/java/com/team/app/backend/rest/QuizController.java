package com.team.app.backend.rest;

import com.team.app.backend.dto.QuizAddDto;
import com.team.app.backend.dto.QuizCategoryDto;
import com.team.app.backend.dto.QuestionDefAddDto;
import com.team.app.backend.dto.QuestionOptAddDto;
import com.team.app.backend.dto.QuestionSeqAddDto;
import com.team.app.backend.persistance.model.Question;
import com.team.app.backend.persistance.model.Quiz;
import com.team.app.backend.service.QuizService;
import com.team.app.backend.service.SecurityService;
import com.team.app.backend.service.UserQuizFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;



@RestController
@RequestMapping("api")
public class QuizController {

    private final QuizService quizService;

    private final UserQuizFavoriteService userQuizFavoriteService;
    private final SecurityService securityService;

    public QuizController(QuizService quizService, UserQuizFavoriteService userQuizFavoriteService, SecurityService securityService) {
        this.quizService = quizService;
        this.userQuizFavoriteService = userQuizFavoriteService;
        this.securityService = securityService;
    }

    @PostMapping("/quiz")
    public HashMap<String,Long> createNewQuiz(
            @RequestBody QuizAddDto quizDto) {
        HashMap<String,Long>result = new HashMap<String,Long>();
        result.put("id",quizService.addQuiz(quizDto));
        return result;
    }

    @DeleteMapping("/quiz/{id}")
    public void deleteQuiz(@PathVariable("id") long id) {
        quizService.deleteQuiz(id);
    }

    @GetMapping("/question/{id}")
    public Question getQuestion(@PathVariable("id") long id) {
        return quizService.getQuestion(id);
    }


    @RequestMapping(value={ "/question/1", "/question/2" },method = { RequestMethod.POST })
    public HashMap<String,Long>  createNewQuestion(@RequestBody QuestionDefAddDto questionDefAddDto) {
        HashMap<String,Long>result = new HashMap<String,Long>();
        result.put("id",quizService.addDefQuestion(questionDefAddDto));
        return result;
    }

    @PostMapping("/question/3")
    public HashMap<String,Long> createNewQuestion(@RequestBody QuestionOptAddDto questionOptAddDto) {
        HashMap<String,Long>result = new HashMap<String,Long>();
        result.put("id",quizService.addOptQuestion(questionOptAddDto));
        return result;
    }

    @PostMapping("/question/4")
    public HashMap<String,Long> createNewQuestion(@RequestBody QuestionSeqAddDto questionSeqAddDto) {
        HashMap<String,Long>result = new HashMap<String,Long>();
        result.put("id",quizService.addSeqOptQuestion(questionSeqAddDto));
        return result;
    }

    @DeleteMapping("/question/{id}")
    public void deleteQuestion(@PathVariable("id") long id) {
        quizService.deleteQuestion(id);
    }

    @GetMapping("/questions/{quiz_id}")
    public List<Question> questions(@PathVariable("quiz_id") long id) {
        return quizService.getQuizQuestion(id);
    }

    @GetMapping("/quiz/{id}")
    public Quiz quiz(@PathVariable("id") long id) {
        return quizService.getQuiz(id);
    }

    @GetMapping("/quiz")
    public List<Quiz> quizzes() {
        return quizService.getAllQuizes();
    }

    @GetMapping("quiz/approved")
    public List<Quiz> approvedQuizzes() {
        return quizService.getApprovedQuizes();
    }

    @GetMapping("quiz/approved/")
    public List<Quiz> getApprovedUserQuizzes() {
        Long user_id = securityService.getCurrentUser().getId();
        return quizService.getApprovedUserQuizes(user_id);
    }
    
    @GetMapping("quiz/favorite/")
    public List<Quiz> getFavoriteQuizzes() {
        Long user_id = securityService.getCurrentUser().getId();
        return quizService.getUserFavoritesQuizes(user_id);
    }
    @GetMapping("quiz/completed/")
    public List<Quiz> getCompleted() {
        Long user_id = securityService.getCurrentUser().getId();
        return quizService.getCompletedQuizes(user_id);
    }

    @GetMapping("quiz/suggestion/")
    public List<Quiz> getSuggestionQuizes() {
        Long user_id = securityService.getCurrentUser().getId();
        return quizService.getSuggestion(user_id);
    }

    @GetMapping("quiz/category/{category}")
    public List<Quiz> categoryQuizes(@PathVariable("category") String category) {
        return quizService.getCategoryQuizes(category);
    }


    @PostMapping("quiz/favorite/{quiz_id}")
    public void addFavorite(@PathVariable("quiz_id") long quiz_id){
        Long user_id = securityService.getCurrentUser().getId();
        userQuizFavoriteService.addFavorite(user_id,quiz_id);
    }

    @DeleteMapping("quiz/favorite/{quiz_id}")
    public void deleteFavorite(@PathVariable("quiz_id") long quiz_id){
        Long user_id = securityService.getCurrentUser().getId();
        userQuizFavoriteService.deleteFavorite(user_id,quiz_id);
    }

    @GetMapping("/quiz/user")
    public List<Quiz> userQuizzes() {
        Long id = securityService.getCurrentUser().getId();
        return quizService.getUserQuizes(id);
    }

    @PostMapping("/quiz/search")
    public List<Quiz> searchQuizzes(@RequestBody QuizCategoryDto quizCategoryDto) {
        return quizService.searchQuizes(quizCategoryDto.getCategories(),quizCategoryDto.getTitle(),quizCategoryDto.getDateFrom(),quizCategoryDto.getDateTo(),quizCategoryDto.getUser());
    }

    @PostMapping("/quiz/approve")
    public ResponseEntity approveQuiz(@RequestBody Quiz quiz) {
            quizService.approveQuiz(quiz);
            return ResponseEntity.ok().build();
    }

    @GetMapping("/quiz/created")
    public ResponseEntity getCreatedQuizs() {
        List<Quiz> quizzes;
        quizzes = quizService.getCreated();
        return ResponseEntity.ok().body(quizzes);
    }
}
