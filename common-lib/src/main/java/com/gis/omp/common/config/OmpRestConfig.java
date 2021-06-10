package com.gis.omp.common.config;

import com.gis.omp.common.error.GlobalExceptionTranslator;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = {OmpConfig.class, GlobalExceptionTranslator.class})
public class OmpRestConfig {
}
