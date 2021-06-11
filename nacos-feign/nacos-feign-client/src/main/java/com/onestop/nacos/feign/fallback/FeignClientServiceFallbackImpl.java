package com.onestop.nacos.feign.fallback;

import com.onestop.common.core.util.Res;
import com.onestop.nacos.feign.client.FeignClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Clark
 * @version 2021/6/11
 */
@Slf4j
@Component
public class FeignClientServiceFallbackImpl implements FeignClientService {
    @Override
    public Res test() {
        log.error("===============服务异常、降级处理===============");
        return Res.failed("服务异常、降级处理");
    }
}
