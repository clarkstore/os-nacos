package com.onestop.nacos.gateway.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * nacos动态路由数据
 *
 * @author Clark
 * @version 2021-07-06
 */
@Slf4j
@Configuration
public class NacosRouteDefinitionRepository implements RouteDefinitionRepository {
//    @Autowired
//    private RouteDefinitionWriter routeDefinitionWriter;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private NacosConfigProperties nacosConfigProperties;
    @Value("${os.nacos.gateway.route-data-id:os-gateway-router}")
    private String routerDataId;

    private ConfigService config;

    /**
     * 添加Nacos监听
     */
    @PostConstruct
    public void dynamicRouteByNacosListener() {
        try {
            Properties properties = new Properties();
            properties.put(PropertyKeyConst.SERVER_ADDR, this.nacosConfigProperties.getServerAddr());
            if (StrUtil.isNotBlank(this.nacosConfigProperties.getNamespace())) {
                properties.put(PropertyKeyConst.NAMESPACE, this.nacosConfigProperties.getNamespace());
            }

            this.config = NacosFactory.createConfigService(properties);
            this.config.addListener(routerDataId, nacosConfigProperties.getGroup(), new Listener() {
                @Override
                public Executor getExecutor() {
                    return null;
                }

                @Override
                public void receiveConfigInfo(String configInfo) {
                    publisher.publishEvent(new RefreshRoutesEvent(this));
                }
            });
        } catch (NacosException e) {
            log.error("nacos-addListener-error", e);
        }
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        try {
            String content = this.config.getConfig(this.routerDataId, this.nacosConfigProperties.getGroup(), 5000);
            log.warn("获取网关当前配置:\r\n{}",content);
            List<RouteDefinition> routeDefinitions = this.getListByStr(content);
            return Flux.fromIterable(routeDefinitions);
        } catch (NacosException e) {
            log.error("getRouteDefinitions by nacos error", e);
        }
        return Flux.fromIterable(Collections.EMPTY_LIST);
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
//        try {
//            this.routeDefinitionWriter.save(route).subscribe();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return null;
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
//        this.routeDefinitionWriter.delete(routeId).subscribe();
        return null;
    }

    /**
     * 解析路由
     *
     * @param content
     * @return
     */
    private List<RouteDefinition> getListByStr(String content) {
        if (StrUtil.isNotEmpty(content)) {
            return JSONObject.parseArray(content, RouteDefinition.class);
        }
        return CollUtil.newArrayList();
    }
}

