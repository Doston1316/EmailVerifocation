package com.example.emailverifocation.registration;

import com.example.emailverifocation.appUser.AppService;
import com.example.emailverifocation.appUser.AppUser;
import com.example.emailverifocation.appUser.AppUserRole;
import com.example.emailverifocation.email.EmailSender;
import com.example.emailverifocation.registration.token.ConfirmationService;
import com.example.emailverifocation.registration.token.ConfirmationToken;
import javassist.tools.rmi.AppletServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class RegistrationService {

    @Autowired
    private EmailValidate emailValidate;
    @Autowired
    private AppService appService;
    @Autowired
    private ConfirmationService confirmationService;
    @Autowired
    private EmailSender emailSender;

    public String register(RegistrationRequest request) {
        boolean isValidateEmail=emailValidate.test(request.getEmail());
        if (!isValidateEmail){
            throw new IllegalStateException("Email not valid//");
        }
        String token= appService.signAppUser(
                new AppUser(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword(),
                        AppUserRole.USER
                )
        );
        String link="http://localhost:8081/api/"+token;

        emailSender.send(request.getEmail(),buildEmail(request.getFirstName(),link));
        return token;
    }

    @Transactional
    public String confirmToken(String token){
        ConfirmationToken confirmationToken=confirmationService
                .getToken(token)
                .orElseThrow(() ->
                        new  IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() !=null){
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime dateTime=confirmationToken.getExpiredAt();
        if (dateTime.isBefore(LocalDateTime.now())){
           throw  new IllegalStateException("expired token");
        }

        confirmationService.setConfirmated(token);
        appService.enableAppUser(
                confirmationToken.getAppUser().getEmail()
        );

        return "confirmed";

    }

    private String buildEmail(String name,String link){
        return " </div>\n" +
                "  <button type=\"submit\" class=\"btn btn-primary\">Submit</button>\n" +
                "</form>";
    }



}
