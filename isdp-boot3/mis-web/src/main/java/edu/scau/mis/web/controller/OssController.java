package edu.scau.mis.web.controller;

import edu.scau.mis.common.oss.config.OssProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oss")
public class OssController {
    @Autowired
    private OssProperties ossProperties;

    @GetMapping("/info")
    public OssProperties getOssInfo() {
        return ossProperties;
    }
}
