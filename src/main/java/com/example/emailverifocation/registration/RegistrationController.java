package com.example.emailverifocation.registration;

import com.example.emailverifocation.registration.token.ConfirmationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @PostMapping
    public String register(@RequestBody RegistrationRequest request){
        return registrationService.register(request);
    }

    @GetMapping("/{token}")
    public String confirm(@PathVariable("token")String token){
        return registrationService.confirmToken(token);
    }


}
