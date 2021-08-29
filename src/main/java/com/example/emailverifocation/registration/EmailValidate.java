package com.example.emailverifocation.registration;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;
@Service
public class EmailValidate implements Predicate<String> {
    @Override
    public boolean test(String s) {
        return true;
    }
}
