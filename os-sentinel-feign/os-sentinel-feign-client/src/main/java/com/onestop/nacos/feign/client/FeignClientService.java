package com.onestop.nacos.feign.client;

import com.onestop.common.core.util.Res;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Clark
 * @version 2021/6/10
 */
@FeignClient(value = "os-sentinel-feign-server")
@Component
public interface FeignClientService {
    @GetMapping("server/api/timeout")
    Res timeout();

    @GetMapping("server/api/flow")
    Res flow();

    @GetMapping("server/api/degrade")
    Res degrade();
}
