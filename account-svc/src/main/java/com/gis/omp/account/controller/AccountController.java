package com.gis.omp.account.controller;

import com.gis.omp.account.dto.AccountDto;
import com.gis.omp.account.dto.CreateAccountRequest;
import com.gis.omp.account.dto.GenericAccountResponse;
import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/account")
@Validated
public class AccountController {
    static final ILogger logger = SLoggerFactory.getLogger(AccountController.class);

    @PostMapping(path = "/create")
    public GenericAccountResponse createAccount(@RequestBody CreateAccountRequest request) {
        GenericAccountResponse response = new GenericAccountResponse();
        AccountDto c = new AccountDto();
        c.setName(request.getName());
        response.setAccount(c);
        //response.setCode(ResultCode.SUCCESS);

        return response;
    }
}
