package com.onestop.nacos.gateway.sentinel;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.alibaba.fastjson.JSONObject;
import com.onestop.common.core.util.Res;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

/**
 * TODO  sentinel 限流核心配置
 * Sentinel异常处理
 *
 * @author Clark
 * @version 2021-07-06
 */
@Slf4j
//@Component
public class SentinelExceptionHandler implements BlockRequestHandler {

    @Override
    public Mono<ServerResponse> handleRequest(ServerWebExchange serverWebExchange, Throwable throwable) {
        int status = HttpStatus.OK.value();
        Res res = Res.ok();
        //限流响应
        if (throwable instanceof FlowException) {
            log.error("-------当前访问人数较多，请稍后再试--------");
            res = Res.failed("当前访问人数较多，请稍后再试");
//            res.setCode(MsgCode.FAIL);
//            res.setMsg("当前访问人数较多，请稍后再试");
            status = HttpStatus.TOO_MANY_REQUESTS.value();
        }
        //服务降级响应
        else if (throwable instanceof DegradeException) {
            log.error("-------服务降级，请稍后再试--------");
            res = Res.failed("服务降级，请稍后再试");
//            res.setCode(MsgCode.FAIL);
//            res.setMsg("服务不可用，请稍后再试");
            status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        }
        //热点参数限流响应
        else if (throwable instanceof ParamFlowException) {
            log.error("-------热点参数限流响应，请稍后再试--------");
        }
        //触发系统保护规则响应
        else if (throwable instanceof SystemBlockException) {
            log.error("-------触发系统保护规则响应，请稍后再试--------");
        }
        //授权规则不通过响应
        else if (throwable instanceof AuthorityException) {
            log.error("-------授权规则不通过响应，请稍后再试--------");
        }
        //返回固定响应信息
        ServerHttpResponse response = serverWebExchange.getResponse();
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        return ServerResponse.status(status).contentType(MediaType.APPLICATION_JSON_UTF8).body(fromValue(JSONObject.toJSONString(res)));
    }
}




