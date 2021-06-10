package com.gis.omp.cos.service;

import com.gis.omp.account.client.AccountClient;
import com.gis.omp.account.dto.CreateAccountRequest;
import com.gis.omp.account.dto.GenericAccountResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {
    @Autowired
    AccountClient accountClient;

    public GenericAccountResponse testFunc() {
        CreateAccountRequest c = CreateAccountRequest.builder()
                .name("ttt")
                .phoneNumber("123456789")
                .email("eee@qq.com").build();
        return accountClient.createAccount(c);
    }
}
