package com.spring.security.services;

import com.spring.security.entity.UserEntity;
import com.spring.security.entity.UserSession;
import com.spring.security.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserSessionService {

    private final UserSessionRepository sessionRepository;


    public void createSession(UserEntity user, String jwt, String ip, String device){
        UserSession session = new UserSession();
        session.setUser(user);
        session.setJwtToken(jwt);
        session.setIsActive(true);
        session.setLastActivity(LocalDateTime.now());
        session.setLoginTime(LocalDateTime.now());
        session.setIpAddress(ip);
        session.setDeviceInfo(device);
        sessionRepository.save(session);
    }

    public void updateLastActivity(String jwt){
        sessionRepository.findByJwtToken(jwt).ifPresent(
                userSession -> {
                    userSession.setLastActivity(LocalDateTime.now());
                    sessionRepository.save(userSession);
                });
    }

    public void logout(String jwt){
        sessionRepository.findByJwtToken(jwt).ifPresent(
                userSession -> {
                    userSession.setIsActive(false);
                    sessionRepository.save(userSession);
                }
        );
    }

    public LocalDateTime getExpiryTime(String jwt) {
        return sessionRepository.findByJwtToken(jwt)
                .map(UserSession::getLoginTime)
                .orElse(null);
    }
}
