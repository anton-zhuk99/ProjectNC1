package com.team.app.backend.persistance.dao;

import com.team.app.backend.persistance.model.User;

import java.util.List;

public interface UserDao {

    List<User> searchByString(String searchstring, int firstRole, int lastRole);

    void save(User user);

    void update(User user);

    void delete(Long id);

    User get(Long id);

    User findByUsername(String username);

    String getUserPasswordByUsername(String username);

    User getUserByToken(String token);

    void activateByToken(String token);

    boolean checkTokenAvailability(String token);

}
