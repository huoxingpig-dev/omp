package com.gis.omp.account.client;

import com.gis.omp.account.AccountConstant;
import com.gis.omp.account.dto.CreateAccountRequest;
import com.gis.omp.account.dto.GenericAccountResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = AccountConstant.SERVICE_NAME, path = "/v1/account", url = "${omp.account-service-endpoint}")
public interface AccountClient {

    @PostMapping(path = "/create")
    GenericAccountResponse createAccount(@RequestBody CreateAccountRequest request);
}
