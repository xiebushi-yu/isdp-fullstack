package edu.scau.mis.web.controller;

import edu.scau.mis.common.web.domain.ApiResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    @GetMapping("/")
    public ApiResult<String> index() {
        return ApiResult.success("Hello SpringBoot");
    }
}