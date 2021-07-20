package com.onestop.nacos.feign.api;

import com.onestop.common.core.util.Res;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

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

    @GetMapping("/timeout")
    public Res timeout() {
        log.error("=========服务端被调用 timeout=========" + port);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Res.ok("=========服务端被调用 timeout=========");
    }

    @GetMapping("/flow")
    public Res flow() {
        log.error("=========服务端被调用 flow=========" + port);
        return Res.ok("=========服务端被调用 flow=========");
    }

    @GetMapping("/degrade")
    public Res degrade() {
        log.error("=========服务端被调用 degrade========");
        throw new RuntimeException("发生异常");
//        return Res.ok(result);
    }
}
