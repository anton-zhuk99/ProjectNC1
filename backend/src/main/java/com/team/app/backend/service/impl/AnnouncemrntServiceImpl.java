package com.team.app.backend.service.impl;

import com.team.app.backend.persistance.dao.AnnouncementDao;
import com.team.app.backend.persistance.dao.NotificationDao;
import com.team.app.backend.persistance.model.Announcement;
import com.team.app.backend.persistance.model.Notification;
import com.team.app.backend.service.AnnouncementService;
import com.team.app.backend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import java.util.Date;
import java.util.List;


@Service

public class AnnouncemrntServiceImpl implements AnnouncementService {

    @Autowired
    private AnnouncementDao announcementDao;

    @Autowired
    private NotificationService notificationService;

    @Transactional
    public void createAnnouncement(Announcement announcement) {
        announcement.setCategoryId(1L);
        long millis=System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);
        announcement.setDate(date);
        announcementDao.create(announcement);
    }

    @Override
    public List<Announcement> getCreated() {
        return announcementDao.getCreated();
    }


    @Override
    public List<Announcement> getAll() {
        return announcementDao.getAll();
    }

    @Override
    public void approve(Announcement announcement) {
        Notification notification = new Notification();
        notification.setCategoryId(1L);
        notification.setUserId(announcement.getUserId());
        if(announcement.getStatusId() == 2) {
            announcementDao.approve(announcement.getId());
            notification.setText(String.format("Announcement '%s' approved!)",announcement.getTitle()));
        } else {
            notification.setText(String.format("Announcement '%s' not approved!",announcement.getTitle()));
            announcementDao.delete(announcement.getId());
        }
        notificationService.create(notification);
    }

    public Announcement  getAnnouncement(Long id) {
       return new Announcement();
    }
    @Transactional
    public void updateAnnouncement(Announcement announcement) {
        announcementDao.update(announcement);
    }
    @Transactional
    public void deleteAnnouncement(Long id) {
        announcementDao.delete(id);
    }

}
