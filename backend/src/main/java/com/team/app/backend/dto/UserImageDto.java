package com.team.app.backend.dto;

import lombok.Data;

@Data
public class UserImageDto {

    private long userId;
    private byte[] imageBytes;

}
