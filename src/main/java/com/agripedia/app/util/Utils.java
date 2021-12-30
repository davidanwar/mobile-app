package com.agripedia.app.util;

import com.agripedia.app.config.SecurityConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;
import java.util.Date;

@Component
public class Utils {
    public static boolean hasTokenExpired(String token) {
        Claims claims = Jwts.parser() // decrypt
                .setSigningKey(SecurityConstant.getTokenSecret())
                .parseClaimsJws(token).getBody();
        Date tokenExpiredDate = claims.getExpiration();
        Date todayDate = new Date();
        return tokenExpiredDate.before(todayDate);
    }

    public static String generateEmailVerificationToken(String publicUserId) {
        String token = Jwts.builder() // encrypt
                .setSubject(publicUserId)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstant.getTokenSecret())
                .compact();
        return token;
    }
}
