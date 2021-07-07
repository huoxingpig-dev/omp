package com.gis.omp.common.auth;

import java.lang.annotation.*;

/**
 *  based on role， 基于角色
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Authorize {
    // allowed users
    String[] value();
}
