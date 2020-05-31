package com.team.app.backend.rest;

import com.team.app.backend.persistance.model.Announcement;
import com.team.app.backend.persistance.model.UserInvite;
import com.team.app.backend.service.SecurityService;
import com.team.app.backend.service.UserInviteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("api/user/invite")
public class UserInviteController {
    private final UserInviteService userInviteService;
    private final MessageSource messageSource;
    private final SecurityService securityService;

    @Autowired
    public UserInviteController(UserInviteService userInviteService, MessageSource messageSource, SecurityService securityService) {
        this.userInviteService = userInviteService;
        this.messageSource = messageSource;
        this.securityService = securityService;
    }

    @PostMapping("/send")
    public ResponseEntity sendUserInvite(@RequestBody UserInvite userInvite) {
        Map<String, String> response = new HashMap<>();
        userInviteService.sendUserInvite(userInvite);
        response.put("message", messageSource.getMessage("invite.create", null, LocaleContextHolder.getLocale()));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get")
    public ResponseEntity<List<UserInvite>> getUserInvite() {
        Long id = securityService.getCurrentUser().getId();
        return ResponseEntity.ok().body(userInviteService.getUserInvite(id));
    }

    @PutMapping("/{invite_id}")
    public ResponseEntity acceptUserInvite(@PathVariable("invite_id") Long invite_id) {
        Map<String, String> model = new HashMap<>();
        userInviteService.acceptUserInvite(invite_id);
        model.put("message", messageSource.getMessage("invite.updated", null, LocaleContextHolder.getLocale()));
        return ResponseEntity.ok(model);
    }

    @DeleteMapping("/{invite_id}")
    public ResponseEntity declineUserInvite(@PathVariable("invite_id") Long invite_id) {
        Map<String, String> model = new HashMap<>();
        userInviteService.declineUserInvite(invite_id);
        model.put("message", messageSource.getMessage("invite.decline", null, LocaleContextHolder.getLocale()));
        return ResponseEntity.ok(model);
    }

    @GetMapping("/friends/")
    public ResponseEntity<List<UserInvite>> getFriends() {
        Long id = securityService.getCurrentUser().getId();
        return ResponseEntity.ok().body(userInviteService.getFriendsList(id));
    }

    // TODO: refactor this
    @DeleteMapping("/friends/{delete_id}")
    public ResponseEntity deleteUserFromList(@PathVariable("delete_id") Long deleteId) {
        Long id = securityService.getCurrentUser().getId();
        Map<String, String> model = new HashMap<>();
        userInviteService.deleteUserFromList(id, deleteId);
        model.put("message", messageSource.getMessage("announcement.deleted", null, LocaleContextHolder.getLocale()));
        return ResponseEntity.ok(model);
    }
}
