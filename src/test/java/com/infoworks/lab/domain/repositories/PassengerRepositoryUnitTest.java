package com.infoworks.lab.domain.repositories;

import com.infoworks.lab.domain.models.Gender;
import com.infoworks.lab.domain.entities.Passenger;
import com.infoworks.lab.webapp.config.TestJPAH2Config;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestJPAH2Config.class})
@Transactional
@TestPropertySource(locations = {"classpath:application-h2db.properties"})
public class PassengerRepositoryUnitTest {

    @Autowired
    PassengerRepository repository;

    @Test
    public void insert(){
        Passenger passenger = new Passenger("Sayed The Coder", Gender.MALE, 24);
        repository.save(passenger);

        List<Passenger> list = repository.findByName("Sayed The Coder");
        Assert.assertTrue(Objects.nonNull(list));

        if (list != null && list.size() > 0){
            Passenger passenger2 = list.get(0);
            Assert.assertTrue(Objects.equals(passenger.getName(), passenger2.getName()));
        }
    }

    @Test
    public void update(){
        //TODO
    }

    @Test
    public void delete(){
        //TODO
    }

    @Test
    public void count(){
        //
        Passenger passenger = new Passenger("Sayed The Coder", Gender.MALE, 24);
        repository.save(passenger);
        //
        long count = repository.count();
        Assert.assertTrue(count == 1);
    }

    @Test
    public void fetch(){
        //
        repository.save(new Passenger("Sayed The Coder", Gender.MALE, 24));
        repository.save(new Passenger("Evan The Pankha Coder", Gender.MALE, 24));
        repository.save(new Passenger("Razib The Pagla", Gender.MALE, 26));
        //
        Page<Passenger> paged = repository.findAll(PageRequest.of(0
                , 10
                , Sort.by(Sort.Order.asc("name"))));
        Assert.assertTrue(!paged.getContent().isEmpty());
        paged.get().forEach(passenger -> System.out.println(passenger.getName()));
    }

    @Test
    public void testNameContainsLike(){
        //
        repository.save(new Passenger("Sayed The Coder", Gender.MALE, 24));
        repository.save(new Passenger("Evan The Pankha Coder", Gender.MALE, 24));
        repository.save(new Passenger("Razib The Pagla", Gender.MALE, 26));
        //
        List<Passenger> paged = repository.findAllByContainNameLike("Pankha", 0, 10);
        Assert.assertTrue(!paged.isEmpty());
        paged.forEach(passenger -> System.out.println(passenger.getName()));
        //
        List<Passenger> notFound = repository.findAllByContainNameLike("qwe", 0, 10);
        Assert.assertTrue(notFound.isEmpty());
    }
}