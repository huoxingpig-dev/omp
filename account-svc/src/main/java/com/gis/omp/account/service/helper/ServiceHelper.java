package com.gis.omp.account.service.helper;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ServiceHelper {
    static final ILogger logger = SLoggerFactory.getLogger(ServiceHelper.class);

    public void handleException(ILogger log, Exception ex, String errMsg) {
        log.error(errMsg, ex);
//        if (!envConfig.isDebug()) {
//            sentryClient.sendException(ex);
//        }
    }

     
}
