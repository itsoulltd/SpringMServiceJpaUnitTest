package com.infoworks.lab.services.iServices;

import com.infoworks.lab.domain.entities.Passenger;
import com.infoworks.lab.domain.repositories.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface PassengerService {
    void setPassengerRepository(@Autowired PassengerRepository repository);
    Passenger add(Passenger aPassenger);
    Passenger update(Passenger aPassenger);
    boolean remove(Integer userid);
    Long totalCount();
    Passenger findByUserID(Integer userid);
    List<Passenger> findAllByUserID(List<Integer> userid);
    List<Passenger> findAll(Integer page, Integer size);
}
