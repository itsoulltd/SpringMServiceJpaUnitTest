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

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

    @Test
    public void insert() {
        Passenger passenger = new Passenger("Sayed The Coder", Gender.MALE, 24);
        //Call controller to make the save:
        Passenger nPassenger = service.add(passenger);
        //Verify:
        assertNotNull(nPassenger);
        assertNotNull(nPassenger.getId());
        assertEquals("Sayed The Coder", nPassenger.getName());
        System.out.println(nPassenger.marshallingToMap(true));
    }

    @Test
    public void update() {}

    @Test
    public void delete() {}

    @Test
    public void count() {
        Passenger passenger = new Passenger("Sayed The Coder", Gender.MALE, 24);
        service.add(passenger);
        //
        long count = service.totalCount();
        Assert.assertTrue(count == 1);
    }

    @Test
    public void fetch() {
        service.add(new Passenger("Sayed The Coder", Gender.MALE, 24));
        service.add(new Passenger("Evan The Pankha Coder", Gender.MALE, 24));
        service.add(new Passenger("Razib The Pagla", Gender.MALE, 26));
        //
        List<Passenger> paged = service.findAll(0, 10);
        Assert.assertTrue(!paged.isEmpty());
        paged.forEach(passenger -> System.out.println(passenger.getName()));
    }

    @Test
    public void testSearchByName() {
        service.add(new Passenger("Sayed The Coder", Gender.MALE, 24));
        service.add(new Passenger("Evan The Pankha Coder", Gender.MALE, 24));
        service.add(new Passenger("Razib The Pagla", Gender.MALE, 26));
        //
        List<Passenger> paged = service.searchByName("Pankha", 0, 10);
        Assert.assertTrue(!paged.isEmpty());
        paged.forEach(passenger -> System.out.println(passenger.getName()));
        //
        List<Passenger> notFound = service.searchByName("qwe", 0, 10);
        Assert.assertTrue(notFound.isEmpty());
    }
}
