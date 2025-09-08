package com.infoworks.lab.domain.repositories;

import com.infoworks.lab.webapp.WebApplicationTest;
import com.infoworks.lab.webapp.config.BeanConfig;
import com.infoworks.lab.webapp.config.TestJPAH2Config;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {WebApplicationTest.class, BeanConfig.class, TestJPAH2Config.class})
@Transactional
@TestPropertySource(locations = {"classpath:application-h2db.properties"})
public class TemplateRepositoryUnitTest {

    @Autowired /*@Qualifier("<bean-name>")*/ /*Or replace JpaRepository with actual @Repository declaration.*/
    JpaRepository repository;

    @Test
    public void initTest() {
        Assert.assertNotNull(repository);
        System.out.println("JpaRepository loaded: " + repository);
    }

    @Test
    public void insert() {}

    @Test
    public void update() {}

    @Test
    public void delete() {}

    @Test
    public void count() {}

    @Test
    public void fetch() {}

}
