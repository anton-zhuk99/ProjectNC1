package com.team.app.backend.service.impl;

import com.team.app.backend.persistance.dao.NotificationDao;
import com.team.app.backend.persistance.dao.UserInviteDao;
import com.team.app.backend.persistance.model.Notification;
import com.team.app.backend.persistance.model.UserInvite;
import com.team.app.backend.service.NotificationService;
import com.team.app.backend.service.UserInviteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class UserInviteServiceImpl implements UserInviteService {
    @Autowired
    private UserInviteDao userInviteDao;

    @Autowired
    private NotificationDao notificationDao;

    @Override
    public void sendUserInvite(UserInvite userInvite) {
        Notification notification = new Notification();
        notification.setCategoryId(2L);
        notification.setUserId(userInvite.getUserIdTo());
        notification.setText(userInvite.getUsernameFrom()+" invites you to friends");
        userInvite.setActivated(false);
        long millis = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);
        userInvite.setInviteDate(date);
        userInviteDao.send(userInvite);
        notificationDao.create(notification);
    }

    @Override
    public void acceptUserInvite(Long id) {
        userInviteDao.accept(id);
    }

    @Override
    public List<UserInvite> getUserInvite(Long userId) {
        return userInviteDao.getUserInvite(userId);
    }


    @Override
    public void declineUserInvite(Long id) {
        userInviteDao.decline(id);
    }

    @Override
    public List<UserInvite> getFriendsList(Long userId) {
        return userInviteDao.getFriendsList(userId);
    }

    @Override
    public void deleteUserFromList(Long userId, Long deleteId) {
        userInviteDao.deleteFriendFromList(userId, deleteId);
    }
}
