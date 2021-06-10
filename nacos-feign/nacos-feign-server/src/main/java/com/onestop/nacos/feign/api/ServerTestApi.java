package com.onestop.nacos.feign.api;

import com.onestop.common.core.util.Res;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Clark
 * @version 2021/6/10
 */
@Slf4j
@RestController
@RequestMapping("server/api")
public class ServerTestApi {
    @Value("${server.port}")
    private String port;

    @GetMapping("test")
    public Res test() {
        log.error("=========服务端被调用=========" + port);
        return Res.ok("服务端被调用");
    }
}
