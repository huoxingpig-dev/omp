package com.gis.omp.common.auth;

import com.gis.omp.common.auth.constant.AuthConstant;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 *  A context holder class for holding the current userId and auth info
 */
public class AuthContext {

    private static String getRequestHeader(String headerName) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
            String value = request.getHeader(headerName);
            return value;
        }
        return null;
    }

    public static String getUserId() {
        return getRequestHeader(AuthConstant.CURRENT_USER_HEADER);
    }

    public static String getAuth() {
        return getRequestHeader(AuthConstant.AUTHORIZATION_HEADER);
    }
}
