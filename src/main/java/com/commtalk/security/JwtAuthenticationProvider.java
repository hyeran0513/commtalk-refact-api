package com.commtalk.security;

import java.security.SecureRandom;
import java.util.Date;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

@Component
public class JwtAuthenticationProvider {

    private final Key key;
    private final UserDetailsService userDetailsService;

    @Value("${jwt.expiration}")
    private long expire_time;

    public JwtAuthenticationProvider(@Value("${jwt.secret}") String secretKey, UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 토큰 생성
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
     * request 객체에서 토큰 추출
     * @param request 요청 객체
     * @return token
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new JwtException("올바르지 않은 토큰입니다.");
        }

        String token = bearerToken.substring(7);
        validateToken(token);
        return token;
    }

    /**
     * 토큰으로부터 Authentication 객체 조회
     * @param token 토큰
     * @return Authentication 객체
     */
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());

        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    /**
     * 토큰 검증
     * @param token 토큰
     * @return 유효 여부
     */
    public void validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (SecurityException e) {
            throw new JwtException("토큰이 유효하지 않습니다.");
        } catch (MalformedJwtException e) {
            throw new JwtException("올바르지 않은 토큰입니다.");
        } catch (ExpiredJwtException e) {
            throw new JwtException("토큰이 만료되었습니다. 다시 로그인해주세요.");
        } catch (UnsupportedJwtException e) {
            throw  new JwtException("지원되지 않는 토큰입니다.");
        } catch (IllegalArgumentException e) {
            throw new JwtException("토큰이 존재하지 않습니다.");
        }
    }

    /**
     * 토큰으로 memberId를 찾아 반환
     * @param request 요청 객체
     * @return memberId
     */
    public Long getMemberId(HttpServletRequest request) {
        String token = resolveToken(request);
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return ((Integer)claims.get("memberId")).longValue();
    }

//    // secret key 랜덤 생성 용도
//    public static void main(String[] args) {
//        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
//
//        SecureRandom random = new SecureRandom();
//        StringBuilder sb = new StringBuilder(64);
//
//        for (int i = 0; i < 64; i++) {
//            int randomIndex = random.nextInt(CHARACTERS.length());
//            char randomChar = CHARACTERS.charAt(randomIndex);
//            sb.append(randomChar);
//        }
//
//        System.out.println(sb);
//    }

}
