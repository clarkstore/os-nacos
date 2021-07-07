package com.onestop.nacos.gateway.sentinel;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

/**
 * Sentinel异常处理
 *
 * @author Clark
 * @version 2021-07-06
 */
@Component
public class SentinelExceptionHandler implements BlockRequestHandler {

    @Override
    public Mono<ServerResponse> handleRequest(ServerWebExchange serverWebExchange, Throwable throwable) {
        Res<String> res = new Res<>();
        int status = HttpStatus.OK.value();
        //限流响应
        if (throwable instanceof FlowException) {
            res.setCode(MsgCode.FAIL);
            res.setMsg("当前访问人数较多，请稍后再试");
            status = HttpStatus.TOO_MANY_REQUESTS.value();
        }
        //服务降级响应
        else if (throwable instanceof DegradeException) {
        }
        //热点参数限流响应
        else if (throwable instanceof ParamFlowException) {
        }
        //触发系统保护规则响应
        else if (throwable instanceof SystemBlockException) {
        }
        //授权规则不通过响应
        else if (throwable instanceof AuthorityException) {
        }
        //返回固定响应信息
        ServerHttpResponse response = serverWebExchange.getResponse();
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        return ServerResponse.status(status).contentType(MediaType.APPLICATION_JSON_UTF8).body(fromValue(JSONObject.toJSONString(res)));
    }

    @Data
    public class Res<T> {
        /**
         * 响应码
         */
        private int code = MsgCode.SUCCESS;
        /**
         * 响应消息
         */
        private String msg;
        /**
         * 响应数据
         */
        private T data;
    }

    public static class MsgCode {
        public static final Integer SUCCESS = 0;
        public static final Integer FAIL = 1;
    }
}




