package com.lu.user.authentication;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderUtil {

    public static void main(String[] args) {
        String encoded=new BCryptPasswordEncoder().encode("test1234");
        System.out.println("PASSWORD: " + encoded);
    }
}
