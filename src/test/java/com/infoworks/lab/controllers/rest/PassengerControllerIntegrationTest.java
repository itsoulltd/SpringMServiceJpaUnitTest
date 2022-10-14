package com.infoworks.lab.controllers.rest;

import com.infoworks.lab.domain.entities.Passenger;
import com.infoworks.lab.domain.models.Gender;
import com.infoworks.lab.rest.models.ItemCount;
import com.infoworks.lab.rest.models.Message;
import com.infoworks.lab.services.ServiceExecutionLogger;
import com.infoworks.lab.services.impl.PassengerServiceImpl;
import com.infoworks.lab.webapp.WebApplicationTest;
import com.infoworks.lab.webapp.config.BeanConfig;
import com.infoworks.lab.webapp.config.TestJPAH2Config;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
import org.junit.runner.RunWith;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WebApplicationTest.class
        , PassengerController.class, PassengerServiceImpl.class
        , BeanConfig.class, TestJPAH2Config.class
        , ServiceExecutionLogger.class, AnnotationAwareAspectJAutoProxyCreator.class})
//@TestPropertySource(locations = {"classpath:application-h2db.properties"})
public class PassengerControllerIntegrationTest {

    @Value("${app.db.name}")
    private String dbName;

    @Rule
    public final EnvironmentVariables env = new EnvironmentVariables();

    @Before
    public void before() {
        env.set("my.system.env", "my-env");
        env.set("app.db.name", dbName);
    }

    @Test
    public void envTest(){
        Assert.assertTrue(System.getenv("my.system.env").equalsIgnoreCase("my-env"));
    }

    @Autowired
    private PassengerController controller;

    @Test
    public void initiateTest(){
        System.out.println("Integration Tests");
    }

    @Test
    public void count() throws IOException {
        //
        controller.insert(new Passenger("Sayed The Coder", Gender.MALE, 24));
        //
        ResponseEntity<String> res = controller.getRowCount();
        ItemCount count = Message.unmarshal(ItemCount.class, res.getBody());
        System.out.println(count.getCount());
    }

    @Test
    public void query() throws IOException {
        //
        controller.insert(new Passenger("Sayed The Coder", Gender.MALE, 24));
        controller.insert(new Passenger("Evan The Pankha Coder", Gender.MALE, 24));
        controller.insert(new Passenger("Razib The Pagla", Gender.MALE, 26));
        //
        ResponseEntity<String> res = controller.getRowCount();
        ItemCount count = Message.unmarshal(ItemCount.class, res.getBody());
        int size = Long.valueOf(count.getCount()).intValue();
        List<Passenger> items = controller.query(size, 0);
        items.stream().forEach(passenger -> System.out.println(passenger.getName()));
    }

}