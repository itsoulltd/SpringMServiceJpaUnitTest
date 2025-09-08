package com.infoworks.lab.services;

import com.infoworks.lab.domain.entities.Passenger;
import com.infoworks.lab.domain.models.Gender;
import com.infoworks.lab.services.iServices.PassengerService;
import com.infoworks.lab.services.impl.PassengerServiceImpl;
import com.infoworks.lab.webapp.WebApplicationTest;
import com.infoworks.lab.webapp.config.BeanConfig;
import com.infoworks.lab.webapp.config.TestJPAH2Config;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WebApplicationTest.class, ServiceExecutionLogger.class, AnnotationAwareAspectJAutoProxyCreator.class
        , BeanConfig.class, TestJPAH2Config.class
        , PassengerService.class, PassengerServiceImpl.class})
@Transactional
@TestPropertySource(locations = {"classpath:application-h2db.properties"})
public class PassengerServiceUnitTest {

    @Before
    public void setup(){
        /**/
    }

    @Autowired
    private PassengerService service;

    @Test
    public void initTest() {
        Assert.assertNotNull(service);
        System.out.println("PassengerService injected: Yes");
    }
}
