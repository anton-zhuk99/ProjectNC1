package com.team.app.backend.rest;

import com.team.app.backend.persistance.model.Quiz;
import com.team.app.backend.persistance.model.Session;
import com.team.app.backend.persistance.model.User;
import com.team.app.backend.persistance.model.UserToSession;
import com.team.app.backend.service.*;
import jdk.jfr.Frequency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/quiz/")
public class PlayQuizController {

    @Autowired
    SessionService sessionService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserToSessionService userToSessionService;

    @RequestMapping("play")
    public ResponseEntity playQuiz(
            @RequestParam("user_id") Long userId,
            @RequestParam("quiz_id") Long quizId) {
        Quiz quiz = quizService.getQuiz(quizId);
        Session session = sessionService.newSessionForQuiz(quiz);
        String accessCode = AccessCodeProvider.createAccessCode(session, quiz);
        session.setAccess_code(accessCode);
        sessionService.updateSession(session);
        User user = userService.getUserById(userId);

        UserToSession userToSession = userToSessionService.createNewUserToSession(user, session);

        return ResponseEntity.ok("");
    }

    @RequestMapping("join")
    public ResponseEntity joinQuiz(
            @RequestParam("user_id") Long userId,
            @RequestParam("access_code") String accessCode
    ) {
        Quiz quiz = quizService.getQuiz(
                AccessCodeProvider.parseAccessCode(accessCode).get("quiz_id"));
        Session session = sessionService.getSessionById(
                AccessCodeProvider.parseAccessCode(accessCode).get("session_id"));
        User user = userService.getUserById(userId);
        UserToSession userToSession = userToSessionService.createNewUserToSession(user, session);
        return ResponseEntity.ok("");
    }

    @RequestMapping("results")
    public ResponseEntity calculateResults(
            @RequestParam("session_id") Long sessionId
    ) {
        List<UserToSession> userToSessionList = sessionService.getAllUsersToSession(sessionId);
        Map<String, Integer> response = new HashMap<>();
        for (UserToSession userToSession: userToSessionList) {
            User user = userService.getUserById(userToSession.getUser_id());
            response.put(user.getUsername(), userToSession.getScore());
        }
        return ResponseEntity.ok().body(response);
    }


}