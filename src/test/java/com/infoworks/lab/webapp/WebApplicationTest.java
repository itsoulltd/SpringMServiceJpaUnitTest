package com.infoworks.lab.webapp;

import org.junit.Test;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.infoworks.lab.controllers"})
public class WebApplicationTest {
    @Test
    public void contextLoads() {
    }
}