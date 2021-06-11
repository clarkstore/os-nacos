package com.onestop.nacos.feign.client;

/**
 * @author Clark
 * @version 2021/6/10
 */

import com.onestop.common.core.util.Res;
import com.onestop.nacos.feign.fallback.FeignClientServiceFallbackImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "nacos-feign-server", fallback = FeignClientServiceFallbackImpl.class)
@Component
public interface FeignClientService {
    @GetMapping("server/api/test")
    Res test();
}
