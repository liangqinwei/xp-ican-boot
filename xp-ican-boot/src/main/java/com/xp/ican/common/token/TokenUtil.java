package com.xp.ican.common.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class TokenUtil {

    private static Long EXPIRE_TIME = 21600000l;
    private static String SECRET = "!QWERT";

    /**
     * 生成token
     *
     * @param
     * @return
     */
    public static String generateToken(String userName) {
        Date expirationDate = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        return Jwts.builder().claim("userName", userName).setSubject(userName).setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, SECRET).compact();
    }

    /**
     * 从令牌中获取用户名 *
     * * @param token 令牌
     * * @return 用户名
     */
    public static String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

//    /**
//     * 从令牌中获取用户名 *
//     * * @param token 令牌
//     * * @return 用户名
//     */
//    public static String getUidFromToken(String token) {
//        String uid = getClaimsFromToken(token).get("uid", String.class);
//        return uid;
//    }

    /**
     * 验证令牌   *
     * * @param token
     * * @param username
     * * @return
     */
    public static Boolean validateToken(String token, String username) {
        String userName = getUsernameFromToken(token);
        return (userName.equals(username) && !isTokenExpired(token));
    }

    /*
     * Token 是否过期验证
     */
    public static boolean isTokenExpired(String token) {

        return getClaimsFromToken(token).getExpiration().before(new Date());
    }

    /**
     * 从令牌中获取数据声明*
     * * @param token 令牌
     * * @return 数据声明
     */
    private static Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }
}
