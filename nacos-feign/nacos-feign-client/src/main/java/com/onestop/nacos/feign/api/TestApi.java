package com.onestop.nacos.feign.api;

import com.onestop.common.core.util.Res;
import com.onestop.nacos.feign.client.FeignClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Clark
 * @version 2021/6/9
 */
@Slf4j
@RestController
@RequestMapping("feign/client/api")
public class TestApi {
    @Autowired
    private FeignClientService feignClientService;

    @GetMapping("test")
    public Res test() {
        return this.feignClientService.test();
    }
}
