package com.gis.omp.cos.controller;

import com.gis.omp.account.dto.GenericAccountResponse;
import com.gis.omp.cos.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/test")
@Validated
public class TestController {
    @Autowired
    TestService testService;

    @PostMapping(path = "/func")
    public GenericAccountResponse func() {

        return testService.testFunc();
    }
}
