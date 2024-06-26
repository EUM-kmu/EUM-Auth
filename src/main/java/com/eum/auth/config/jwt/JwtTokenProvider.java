package com.eum.auth.config.jwt;

import com.eum.auth.controller.DTO.response.ProfileResponseDTO;
import com.eum.auth.controller.DTO.response.UserResponse;
import com.eum.auth.domain.CustomUserInfoDto;
import com.eum.auth.domain.user.Role;
import com.eum.auth.domain.user.User;
import com.eum.auth.domain.user.UserRepository;
import com.eum.auth.exception.TokenException;
import com.eum.auth.service.CustomUserDetailsService;
import com.eum.auth.service.ProfileService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

import static com.eum.auth.domain.user.Role.ROLE_USER;

@Slf4j
@Component
@RequiredArgsConstructor
@AllArgsConstructor
public class JwtTokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private static final String USER_ID = "userId";
    private static final String UID = "uid";
    private static final String ROLE = "role";
    private static final String BEARER_TYPE = "Bearer";
    private static final String PREVIOUS_USERID = "previousUserId";
    private static final String DELETED = "deleted";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 12 * 60 * 60 * 1000L;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;    // 7일
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    private final Key key;
    @Autowired
    private UserRepository usersRepository;
    @Autowired
    private  ProfileService profileService;

    public JwtTokenProvider(){
        byte[] keyBytes = Decoders.BASE64.decode("VlwEyVBsYt9V7zq57TejMnVUyzblYcfPQye08f7MGVA9XkHN");
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public UserResponse.TokenInfo generateToken(Authentication authentication) {
        // 권한 가져오기
        User user = usersRepository.findById(Long.valueOf(authentication.getName())).orElseThrow(() -> new IllegalArgumentException("Invalid userId"));

        Claims claims = Jwts.claims();
        claims.put(USER_ID, user.getUserId());
        claims.put(UID, user.getUid());
        claims.put(ROLE, user.getRole());
        claims.put(PREVIOUS_USERID, user.getPreviousUserId());
//        claims.put(DELETED, user.isDeleted());

        UserResponse.TokenInfo tokenInfo = generateToken(user.getUserId(),user.getRole(), claims);
        if(user.getRole().equals(ROLE_USER)) {
            ProfileResponseDTO.ProfileResponse profileResponse = profileService.getProfile(String.valueOf(tokenInfo.getUserId()));
            tokenInfo.setNickName(profileResponse.getNickName());
        }
        return tokenInfo;
    }
    public UserResponse.TokenInfo generateToken(CustomUserInfoDto user,Role role) {
        log.info(String.valueOf(user.getPreviousUserId()));
        Claims claims = Jwts.claims();
        claims.put(USER_ID, user.getUserId());
        claims.put(UID, user.getUid());
        claims.put(ROLE, user.getRole());
        claims.put(PREVIOUS_USERID, user.getPreviousUserId());
//        claims.put(DELETED, user.());
//        log.info(claims.get("userId",Long.class).toString());
        // Check if the user has the TEST role
        if (user.getUserId() == 2L || user.getUserId() == 3L || (user.getUserId()> 14L && user.getUserId() < 29L)) {
            // Set the access token expiration time to infinity for TEST users
            log.info(user.getRole().toString());
            long now = (new Date()).getTime();
            Date accessTokenExpiresIn = new Date(Long.MAX_VALUE);

            // Generate the access token
            String accessToken = Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(Date.from(accessTokenExpiresIn.toInstant()))
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();

            // Generate the refresh token
            String refreshToken = Jwts.builder()
                    .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();

            return UserResponse.TokenInfo.builder()
                    .userId(user.getUserId())
                    .grantType(BEARER_TYPE)
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .refreshTokenExpirationTime(REFRESH_TOKEN_EXPIRE_TIME)
                    .role(role)
                    .build();
        } else {
            // Generate the access and refresh tokens normally for non-TEST users
            UserResponse.TokenInfo tokenInfo = generateToken(user.getUserId(),role, claims);
//            if(user.getRole().equals(ROLE_USER)) {
//                ProfileResponseDTO.ProfileResponse profileResponse = profileService.getProfile(String.valueOf(tokenInfo.getUserId()));
//                tokenInfo.setNickName(profileResponse.getNickName());
//            }
            return tokenInfo;
        }
    }

    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(claims.get(USER_ID,Long.class).toString());
//        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
        log.info(userDetails.getUsername());
        return new UsernamePasswordAuthenticationToken(userDetails, accessToken, userDetails.getAuthorities());
    }


    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) throws SecurityException, ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
            // 401 Unauthorized
            throw e;
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
            // 401 Expired
            throw e;
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
            // 400 Bad Request
            throw e;
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
            // 400 Bad Request
            throw e;
        }
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }catch (SignatureException e){
            throw new TokenException("Invalid JWT Token");
        }
    }

    public Long getExpiration(String accessToken) {
        // accessToken 남은 유효시간
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody().getExpiration();
        // 현재 시간
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }
    private UserResponse.TokenInfo generateToken(Long userId, Role role,Claims claims){
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime accessTokenExpiresIn = now.plusSeconds( ACCESS_TOKEN_EXPIRE_TIME);
        ZonedDateTime refreshTokenExpiresIn = now.plusSeconds( REFRESH_TOKEN_EXPIRE_TIME);

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(accessTokenExpiresIn.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Generate the refresh token
        String refreshToken = Jwts.builder()
                .setExpiration(Date.from(refreshTokenExpiresIn.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        return UserResponse.toTokenInfo(BEARER_TYPE, userId, accessToken, refreshToken, REFRESH_TOKEN_EXPIRE_TIME, role);

    }

}


