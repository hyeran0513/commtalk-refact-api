package com.commtalk.config.security;

import java.security.SecureRandom;
import java.util.Date;

import com.commtalk.domain.auth.PrincipalDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

@Component
public class JwtTokenProvider {

    private final Key key;
    private final UserDetailsService userDetailsService;

    @Value("${spring.jwt.expiration}")
    private long expire_time;

    public JwtTokenProvider(@Value("${spring.jwt.secret}") String secretKey, UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 적절한 설정을 통해 토큰을 생성하여 반환
     * @param authentication 인증 객체
     * @return 토큰
     */
    public String generateToken(Authentication authentication) {
        PrincipalDetails userDetails = (PrincipalDetails) authentication.getPrincipal();

        Date now = new Date();
        Date expiresIn = new Date(now.getTime() + expire_time);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("memberId", userDetails.getMemberId())
                .setExpiration(expiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 토큰으로부터 클레임을 만들고, 이를 통해 UserDetails객체를 생성하여 Authentication객체를 반환
     * @param token 토큰
     * @return 인증 객체
     */
    public Authentication getAuthentication(String token) {
        String username = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    /**
     * http 헤더로부터 bearer 토큰을 가져옴.
     * @param req 요청 객체
     * @return 토큰
     */
    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * 토큰을 검증
     * @param token 토큰
     * @return 유효 여부
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    /**
     * 토큰으로 memberId를 찾아 반환
     * @param req 요청 객체
     * @return memberId
     */
    public Long getMemberId(HttpServletRequest req) {
        String token = resolveToken(req);
        if (token == null || token.equals("null") || !validateToken(token)) {
            return null;
        }
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return ((Integer)claims.get("memberId")).longValue();
    }

    // secret key 랜덤 생성 용도
    public static void main(String[] args) {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(64);

        for (int i = 0; i < 64; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }

        System.out.println(sb);
    }

}
