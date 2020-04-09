package com.example.gateway.demo;

import com.ido.util.HttpUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.config.GatewayAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Carl
 * @date 2019/10/23
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = InnerAppRunnerTest.App.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        value = {"spring.application.name=feignclienttest",
                "logging.level.org.springframework.cloud.openfeign.valid=DEBUG",
                "feign.httpclient.enabled=false", "feign.okhttp.enabled=false",
                "feign.hystrix.enabled=true"})
@DirtiesContext
public class InnerAppRunnerTest {

    private String host = "http://localhost:";
    @Value("${local.server.port}")
    private int port;

    @Configuration
    @EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, GatewayAutoConfiguration.class})
    @RestController
    public static class App {
        @RequestMapping("test")
        public String test() {
            return "test";
        }

        public static void main(String[] args) {
            new SpringApplicationBuilder(App.class)
                    .properties("spring.application.name=feignclienttest",
                            "management.contextPath=/admin")
                    .run(args);
        }
    }

    @Test
    public void testSend() {

        String out = new String(HttpUtil.get(host + port + "/test", null));
        System.out.println(out);
    }


}
