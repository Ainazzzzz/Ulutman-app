package com.ulutman.security.jwt;

import org.springframework.stereotype.Service;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

@Service
public class JwtKeyGenerator {
    public static void main(String[] args) {

        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // создание безопасного ключа для HS256
        String base64EncodedKey = Encoders.BASE64.encode(key.getEncoded());
        System.out.println("Base64 Encoded Key: " + base64EncodedKey);
    }
}
