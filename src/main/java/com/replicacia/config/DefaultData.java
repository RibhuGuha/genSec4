package com.replicacia.config;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.replicacia.model.AppUser;
import com.replicacia.rest.security.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DefaultData {

    @Autowired
    private UserService userService;

    @Autowired
    protected ObjectMapper objectMapper;

    @EventListener
    public void appReady(ApplicationReadyEvent event) {
        List<AppUser> userList;
        try {
            userList = objectMapper.readValue(new ClassPathResource(
                    "user.json").getInputStream(), new TypeReference<List<AppUser>>() {
                    });
            for (AppUser user : userList) {

                if (userService.findAllByUserName(user.getUsername()) == null) {
                    userService.save(user);
                }
            }
        } catch (IOException e) {
           log.error("Unable to load default data",e);
        }

    }
}