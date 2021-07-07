package com.gis.omp.common.auth;

import com.gis.omp.common.auth.constant.AuthConstant;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 *  interceptor http request，统一Http请求拦截
 */
public class AuthorizeInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        ///////////////////////// 可以在此鉴权 （在网关中？）
        /*String userId = AuthContext.getUserId();
        String toekn = Sessions.getToken(request);
        String token = Sessions.getToken(request);
        if (token == null) return true;
        try {
            DecodedJWT decodedJWT = Sign.verifySessionToken(token, "123");
            String userId1 = decodedJWT.getClaim(Sign.CLAIM_USER_ID).asString();
            boolean support = decodedJWT.getClaim(Sign.CLAIM_SUPPORT).asBoolean();
            String[] list;
            list = decodedJWT.getClaim(Sign.CLAIM_AUTHORITY).asArray(String.class);
            int n = 0;
            System.out.println(list);
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }*/


        // annotation， 基于权限
        HandlerMethod handlerMethod = (HandlerMethod)handler;
        Authorize authorize = handlerMethod.getMethod().getAnnotation(Authorize.class);
        if (authorize == null) {
            return true;  // no need to authorize
        }

        String[] allowedHeaders = authorize.value();
        String authHeader = request.getHeader(AuthConstant.AUTHORIZATION_HEADER);

        if ( StringUtils.isEmpty(authHeader)) {
            throw new PermissionDeniedException(AuthConstant.ERROR_MSG_MISSING_AUTH_HEADER);
        }

        if (!Arrays.asList(allowedHeaders).contains(authHeader)) {
            throw new PermissionDeniedException(AuthConstant.ERROR_MSG_DO_NOT_HAVE_ACCESS);
        }

        return true;
    }
}
