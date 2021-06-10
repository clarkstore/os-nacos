package com.onestop.nacos.feign.client;

/**
 * @author Clark
 * @version 2021/6/10
 */

import com.onestop.common.core.util.Res;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "nacos-feign-server")
@Service
public interface FeignClientService {
    @GetMapping("server/api/test")
    Res test();
}
