package com.team.app.backend.rest;

import com.team.app.backend.dto.UserImageDto;
import com.team.app.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Slf4j
@RestController
@RequestMapping("/api/image")
public class ImageUploadController {

    @Autowired
    UserService userService;

    @PostMapping("/upload")
    public ResponseEntity uploadImage(
            @RequestParam("image_file") MultipartFile multipartFile,
            @RequestParam("user_id") Long id) {
        Map response = new HashMap<>();
        try {
            userService.uploadImageForUser(id, compressBytes(multipartFile.getBytes()));
            response.put("message", "Image successfully uploaded");
        } catch (IOException e) {
            e.printStackTrace();
            response.put("message", "IOException occurred");
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/get")
    @ResponseBody
    public UserImageDto getImage(
            @RequestParam("user_id") Long userId
    ) {
        UserImageDto userImageDto = new UserImageDto();
        userImageDto.setUserId(userId);
        userImageDto.setImageBytes(userService.getUserImage(userId));
        return userImageDto;
    }

    private byte[] compressBytes(byte[] bytes) {
        log.debug("Original image byte size - {}", bytes.length);
        Deflater deflater = new Deflater();
        deflater.setInput(bytes);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(bytes.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.debug("Compressed image byte size - {}", outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    private byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }

}
