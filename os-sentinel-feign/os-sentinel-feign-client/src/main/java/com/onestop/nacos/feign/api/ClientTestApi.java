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
@RequestMapping("client/api")
public class ClientTestApi {
    @Autowired
    private FeignClientService feignClientService;

    @GetMapping("timeout")
    public Res timeout() {
        log.info("=========客户端发起调用 timeout=========");
        return this.feignClientService.timeout();
    }

    @GetMapping("flow")
    public Res flow() {
        log.info("=========客户端发起调用 flow=========");
        return this.feignClientService.flow();
    }

    @GetMapping("degrade")
    public Res degrade() {
        log.info("=========客户端发起调用 degrade=========");
        return this.feignClientService.degrade();
    }
}
