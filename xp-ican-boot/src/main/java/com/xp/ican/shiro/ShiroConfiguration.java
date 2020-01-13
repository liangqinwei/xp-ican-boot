package com.xp.ican.shiro;


import net.sf.jsqlparser.expression.operators.relational.RegExpMatchOperatorType;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.shiro.mgt.SecurityManager;
import javax.servlet.Filter;
import java.util.*;

@Configuration
public class ShiroConfiguration {

    @Autowired
    private UserRealm userRealm;

//    @Bean
//    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
//        return new LifecycleBeanPostProcessor();
//    }
//
//    @Bean("shiroFilter")
//    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("securityManager") SecurityManager securityManager,AuthShiroFilter authShiroFilter) {
//        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
//        shiroFilterFactoryBean.setSecurityManager(securityManager);
//        //登录api，不设置默认为web工程目录下的“/login.jsp”页面
//        shiroFilterFactoryBean.setLoginUrl("login");
//
//        // 登录成功后要跳转的链接, 此项目是前后端分离，故此行注释掉，登录成功之后返回用户基本信息及token给前端
//        // shiroFilterFactoryBean.setSuccessUrl("/index");
//
//        // 未授权界面, 对应LoginController中 unauthorized 请求
////        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");
//
//        //自定义拦截器
//        Map<String, Filter> customisedFilter = new HashMap<>();
//        //        自定义过滤器url
////        customisedFilter.put("url", getURLPathMatchingFilter());
//        customisedFilter.put("auth", authShiroFilter);
//        shiroFilterFactoryBean.setFilters(customisedFilter);
//
//        //        1). anon 可以被 匿名访问
//        //        2). authc 必须认证（登录）才能访问
//        //        3). logout 登出
//        //        4). roles 角色过滤器
//        //        过滤器名称	过滤器类	描述
//        //        anon		匿名过滤器
//        //        authc	 	如果继续操作，需要做对应的表单验证否则不能通过
//        //        authcBasic	基本http验证过滤，如果不通过，跳转屋登录页面
//        //        logout	 	登录退出过滤器
//        //        noSessionCreation	 	没有session创建过滤器
//        //        perms	 	权限过滤器
//        //        port	 	端口过滤器，可以设置是否是指定端口如果不是跳转到登录页面
//        //        rest	 	http方法过滤器，可以指定如post不能进行访问等
//        //        roles	 	角色过滤器，判断当前用户是否指定角色
//        //        ssl	 	请求需要通过ssl，如果不是跳转回登录页
//        //        user	 	如果访问一个已知用户，比如记住我功能，走这个过滤器
//        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
//        filterChainDefinitionMap.put("/index", "authc");
//        filterChainDefinitionMap.put("/logout", "anon");
//        filterChainDefinitionMap.put("/reg", "roles[admin]");
//        filterChainDefinitionMap.put("/loginuser", "anon");
//        filterChainDefinitionMap.put("/admin/**", "roles[admin]");
//        filterChainDefinitionMap.put("/edit", "perms[edit]");//具有edit权限的才能访问
//        filterChainDefinitionMap.put("/druid/**", "anon");//开放druid的监控后台
//        // 所有url都必须认证通过才可以访问，anon放行
//        filterChainDefinitionMap.put("/**", "auth");
//
//        // 配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了, 位置放在 anon、authc下面
////        filterChainDefinitionMap.put("/logout", "logout");
//
//        //        filterChainDefinitionMap.put("/**", "url");
//
//
//        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
//
//
//        return shiroFilterFactoryBean;
//
//    }
//
//
////    public URLPathMatchingFilter getURLPathMatchingFilter() {
////        return new URLPathMatchingFilter();
////    }
//
//    @Bean("securityManager")
//    public SecurityManager securityManager(@Qualifier("authRealm") AuthRealm authRealm){
//        DefaultWebSecurityManager manager  = new DefaultWebSecurityManager();
//        manager.setRealm(authRealm);
//        return manager;
//    }
//
//
//    @Bean("authRealm")
//    public AuthRealm authRealm(CredentialMatcher matcher){
//        AuthRealm authRealm = new AuthRealm();
//        // 相关认证缓存到cache中
//        authRealm.setCacheManager(new MemoryConstrainedCacheManager());
//        authRealm.setCredentialsMatcher(matcher);
//        return authRealm;
//    }
//
//
//    // 1 密码采用加密方式进行验证：
//    // 本文使用的是自定义的校验规则，
//    // 另一种方式是 在bean 中 配置相关的加密算法
//
//    @Bean("credentialMatcher")
//    public CredentialMatcher credentialMatcher(){
//        return new CredentialMatcher();
//    }
//
//
//    //Shiro与Spring的关联 开启利用注解配置权限
//    /**
//     * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
//     *
//     * 配置以下两个bean(DefaultAdvisorAutoProxyCreator(可选)和AuthorizationAttributeSourceAdvisor)即可实现此功能
//     */
//    @Bean
//    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") SecurityManager securityManager){
//        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
//        advisor.setSecurityManager(securityManager);
//        return advisor;
//    }
//
//    @Bean
//    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
//        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
//        creator.setProxyTargetClass(true);
//        return creator;
//    }



    /**
     * SHIRO 安全管理
     */
    @Bean
    public DefaultWebSecurityManager securityManager(){
        DefaultWebSecurityManager manager=new DefaultWebSecurityManager();
        // 设置自定义的 SubjectFactory
        manager.setSubjectFactory(subjectFactory());
        // 设置自定义的 sessionManager
        manager.setSessionManager(sessionManager());
        // 禁用 Session
        ((DefaultSessionStorageEvaluator)((DefaultSubjectDAO)manager.getSubjectDAO()).getSessionStorageEvaluator())
                .setSessionStorageEnabled(false);

        // 设置自定义的 Realm
        manager.setRealms(getRealms());
        return manager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean=new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //自定义拦截器 参考 ShiroLoginFilter.java
        Map<String, Filter> filtersMap = new LinkedHashMap<String, Filter>();
        filtersMap.put("shiroLoginFilter", new ShiroLoginFilter());//登录验证拦截器
        shiroFilterFactoryBean.setFilters(filtersMap);

        // 所有请求给这个拦截器处理
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<String,String>();
        filterChainDefinitionMap.put("/**", "shiroLoginFilter");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }


    /**
     * 自定义的 subjectFactory
     * 禁用了 Session
     * @return
     */
    @Bean
    public DefaultWebSubjectFactory subjectFactory(){
        ASubjectFactory mySubjectFactory=new ASubjectFactory();
        return mySubjectFactory;
    }

    /**
     * session管理器
     * 禁用了 Session
     * sessionManager通过sessionValidationSchedulerEnabled禁用掉会话调度器，
     * @return
     */
    @Bean
    public DefaultSessionManager sessionManager(){
        DefaultSessionManager sessionManager=new DefaultSessionManager();
        sessionManager.setSessionValidationSchedulerEnabled(false);
        return sessionManager;
    }

    /**
     * 配置自定义的 Realm
     * @return
     */
    @Bean
    public Collection<Realm> getRealms(){
        Collection<Realm> realms = new ArrayList<>();
        // 配置自定义 UserRealm

        // 由于UserRealm里使用了自动注入，所以这里需要注入Realm而不是new新建
        userRealm.setAuthenticationTokenClass(UserAuthenticationToken.class);
        userRealm.setCredentialsMatcher(new PasswordCredentialsMatcher());//使用自定义的密码匹配器

        realms.add(userRealm);
        return realms;
    }



}
