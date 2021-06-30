package com.onestop.nacos.gateway.config;

import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * nacos路由数据源
 *
 * @author gourd-hu
 */
@Slf4j
public class NacosRouteDefinitionRepository implements RouteDefinitionRepository {

    private String routerDataId;

    private ApplicationEventPublisher publisher;

    private NacosConfigProperties nacosConfigProperties;

    public NacosRouteDefinitionRepository(String routerDataId, ApplicationEventPublisher publisher, NacosConfigProperties nacosConfigProperties) {
        this.routerDataId = routerDataId;
        this.publisher = publisher;
        this.nacosConfigProperties = nacosConfigProperties;
        addListener();
    }

    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        try {
            Properties properties = new Properties();
            properties.put(PropertyKeyConst.SERVER_ADDR, this.nacosConfigProperties.getServerAddr());
            if (StrUtil.isNotBlank(this.nacosConfigProperties.getNamespace())) {
                properties.put(PropertyKeyConst.NAMESPACE, this.nacosConfigProperties.getNamespace());
            }

            ConfigService config = NacosFactory.createConfigService(properties);
            String content = config.getConfig(this.routerDataId, this.nacosConfigProperties.getGroup(), 5000);
//            String content = nacosConfigProperties.configServiceInstance().getConfig(routerDataId, nacosConfigProperties.getGroup(), 5000);
            log.warn("获取网关当前配置:\r\n{}",content);
            List<RouteDefinition> routeDefinitions = getListByStr(content);
            return Flux.fromIterable(routeDefinitions);
        } catch (NacosException e) {
            log.error("getRouteDefinitions by nacos error", e);
        }
        return Flux.fromIterable(Collections.EMPTY_LIST);
    }

    /**
     * 添加Nacos监听
     */
    private void addListener() {
        try {
            Properties properties = new Properties();
            properties.put(PropertyKeyConst.SERVER_ADDR, this.nacosConfigProperties.getServerAddr());
            if (StrUtil.isNotBlank(this.nacosConfigProperties.getNamespace())) {
                properties.put(PropertyKeyConst.NAMESPACE, this.nacosConfigProperties.getNamespace());
            }

            ConfigService config = NacosFactory.createConfigService(properties);
            config.addListener(routerDataId, nacosConfigProperties.getGroup(), new Listener() {
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
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return null;
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
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
        return new ArrayList<>(0);
    }
}

