package com.example.emailverifocation.registration.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConfirmationService {
    @Autowired
    private ConfirmationRepository confirmationRepository;

    public void saveConfirmationToken(ConfirmationToken token){
        confirmationRepository.save(token);

    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationRepository.findByToken(token);
    }

    public void setConfirmated(String token) {
        confirmationRepository.findByToken(token);
    }
}
