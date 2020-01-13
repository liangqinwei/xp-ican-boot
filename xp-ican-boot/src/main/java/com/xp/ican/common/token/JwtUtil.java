package com.xp.ican.common.token;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xp.ican.common.CommonUtil;
import com.xp.ican.common.constants.ResponseCodeNum;
import com.xp.ican.exception.IcanBusinessException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.compression.DefaultCompressionCodecResolver;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.appKey}")
    private String appKey;//app key，用于加密

    @Value("${jwt.period}")
    private Long period;//token有效时间

    @Value(("${jwt.issuer}"))
    private String issuer;//jwt token 签发人

    public static final long DEFAULT_PERIOD = 60*60*1000;//token默认有效时间，1小时
    public static final String DEFAULT_APPKEY = "defaultAppKey";//默认appkey，配置文件里读不到appKey时用此值
    public static final String DEFAULT_ISSUER = "Server-System-2333";//默认签发人


    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static CompressionCodecResolver codecResolver = new DefaultCompressionCodecResolver();

    /**
     * 签发 JWT Token Token
     * @param id 令牌ID
     * @param subject subject 用户ID
     * @param issuer 签发人，自定义
     * @param roles 角色
     * @param permissions 权限集合，建议传入权限集合的json字符串
     * @param period 有效时间(ms)
     *               1. 在 当前时间-签发时间>有效时间 时携带token访问接口，会重新刷新token
     *                  在 当前时间-签发时间>有效时间*2 时，则需要重新登录。
     *               2. 这样可以分离长时间不活跃的用户和活跃用户
     *                  活跃用户感受不到token的刷新
     *                  不活跃用户需要登录才可以重新获取token
     * @param algorithm 加密算法
     * @return
     */
    public String issueJWT(String id,
                           String subject,
                           String issuer,
                           String roles,
                           String permissions,
                           Long period,
                           SignatureAlgorithm algorithm) {
        // 需要读取appKey
        if(appKey == null || appKey.equals("")){
            appKey = DEFAULT_APPKEY;
        }

        byte[] secreKeyBytes = DatatypeConverter.parseBase64Binary(appKey);// 秘钥
        JwtBuilder jwtBuilder = Jwts.builder();
        if (!StringUtils.isEmpty(id)) {
            jwtBuilder.setId(id);
        }
        if (!StringUtils.isEmpty(subject)) {
            jwtBuilder.setSubject(subject);
        }
        if (!StringUtils.isEmpty(issuer)) {
            jwtBuilder.setIssuer(issuer);
        }
        // 设置签发时间
        Date now = new Date();
        jwtBuilder.setIssuedAt(now);
        // 设置到期时间
        if (null != period) {
            jwtBuilder.setExpiration(
                    new Date(now.getTime() + period + period)//签发时间+有效期*2
            );
        }
        if (!StringUtils.isEmpty(roles)) {
            jwtBuilder.claim("roles",roles);
        }
        if (!StringUtils.isEmpty(permissions)) {
            jwtBuilder.claim("perms",permissions);
        }
        // 压缩，可选GZIP
        jwtBuilder.compressWith(CompressionCodecs.DEFLATE);
        // 加密设置
        jwtBuilder.signWith(algorithm,secreKeyBytes);

        return jwtBuilder.compact();
    }

    /**
     * 验签JWT
     *
     * @param jwt json web token
     * @return 如果验证通过，且刷新了token，则设置 JwtToken.isFlushed 为true
     */
    public JwtToken parseJwt(String jwt) throws IcanBusinessException {
        if(appKey == null || appKey.equals("")){
            appKey = DEFAULT_APPKEY;
        }

        // 检查 jwt token 合法性
        Claims claims;
        try{
            claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(appKey))
                    .parseClaimsJws(jwt)
                    .getBody();
        }catch (ExpiredJwtException ex){//token过期异常 token已经失效需要重新登录
            throw new IcanBusinessException(ResponseCodeNum.REQUEST_ERROR,"token过期或失效");
        }catch (SignatureException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e){//不支持的token
            throw new IcanBusinessException(ResponseCodeNum.REQUEST_ERROR,"token验证失败");
        }catch (Exception e){
            throw new IcanBusinessException(ResponseCodeNum.REQUEST_ERROR,"未知异常");
        }

        JwtToken jwtToken = new JwtToken();

        // 检查是否需要刷新 jwt token
        long time = claims.getIssuedAt().getTime();//token签发时间
        long now = new Date().getTime();//当前时间
        period = (period == null ? JwtUtil.DEFAULT_PERIOD : period);
        if(time + period >= now){//还在有效期内，不需要刷新token
//            log.info("不需要刷新token");
            jwtToken.setToken(jwt);
            jwtToken.setIsFlushed(false);
        }else if(time + period < now &&//超过有效期，但未超过2倍有效期，此时应该刷新token
                time + period + period >= now){
//            log.info("刷新token");
            jwtToken.setToken(issueJWT(// 制作JWT Token
                    CommonUtil.getRandomString(20),//令牌id
                    claims.getSubject(),//用户id
                    (issuer == null ? DEFAULT_ISSUER : issuer),//签发人
                    claims.get("roles", String.class),//访问角色,设置为null，不使用
                    claims.get("perms", String.class),//权限集合字符串，json
                    period,//token有效时间*2
                    SignatureAlgorithm.HS512
            ));
            jwtToken.setIsFlushed(true);
        }else{
            throw new IcanBusinessException(ResponseCodeNum.REQUEST_ERROR,"token验证失败");
        }

        // 设置其他字段
        jwtToken.setName(claims.getSubject());//用户id
        jwtToken.setPermissions(
                JSONObject.parseObject(
                        claims.get("perms", String.class),
                        List.class
                )
        );//用户权限集合,json转为list集合

        return jwtToken;
    }


    /* *
     * @Description
     * @Param [val] 从json数据中读取格式化map
     * @Return java.util.Map<java.lang.String,java.lang.Object>
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> readValue(String val) {
        try {
            return MAPPER.readValue(val, Map.class);
        } catch (IOException e) {
            throw new MalformedJwtException("Unable to read JSON value: " + val, e);
        }
    }
}
