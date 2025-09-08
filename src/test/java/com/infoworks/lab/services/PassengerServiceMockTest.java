package com.infoworks.lab.services;

import com.infoworks.lab.domain.entities.Passenger;
import com.infoworks.lab.domain.models.Gender;
import com.infoworks.lab.domain.repositories.PassengerRepository;
import com.infoworks.lab.services.iServices.PassengerService;
import com.infoworks.lab.services.impl.PassengerServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class PassengerServiceMockTest {

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Mock
    PassengerRepository repository;

    @InjectMocks
    PassengerService service = new PassengerServiceImpl(repository);

    @Test
    public void mockPassengerTest() {
        //Defining Mock Object:
        Passenger aPassenger = new Passenger("Gregory", Gender.MALE, 36);
        when(repository.save(any(Passenger.class))).thenReturn(aPassenger);

        //Call controller to make the save:
        Passenger nPassenger = service.add(aPassenger);

        //Verify:
        assertNotNull(nPassenger);
        assertNotNull(nPassenger.getId());
        assertEquals("Gregory", nPassenger.getName());
        System.out.println(nPassenger.marshallingToMap(true));
    }
}
