package com.example.emailverifocation.appUser;

import com.example.emailverifocation.registration.token.ConfirmationService;
import com.example.emailverifocation.registration.token.ConfirmationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AppService implements UserDetailsService {

    private final static String USER_NOT_FOUND="User with  email %s not found";

    @Autowired
    private AppRepository appRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private ConfirmationService confirmationService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return appRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format(USER_NOT_FOUND,email)));
    }

    public String signAppUser(AppUser appUser){
        boolean userExists= appRepository.findByEmail(appUser.getEmail())
                .isPresent();
        if (userExists){
            throw new IllegalStateException("email already taken");
        }

        String passwordEncode = passwordEncoder.encode(appUser.getPassword());
        appUser.setPassword(passwordEncode);

        appRepository.save(appUser);

        String token= UUID.randomUUID().toString();

        ConfirmationToken confirmationToken=new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(20),
                appUser
        );

        confirmationService.saveConfirmationToken(confirmationToken);
        return token;
    }


    public void enableAppUser(String email) {
        confirmationService.setConfirmated(email);
    }
}
