package com.infoworks.lab.services.iServices;

import com.infoworks.lab.domain.entities.Passenger;

import java.util.List;

public interface iPassengerService {
    Passenger add(Passenger aPassenger);
    Passenger update(Passenger aPassenger);
    boolean remove(Integer userid);
    Long totalCount();
    Passenger findByUserID(Integer userid);
    List<Passenger> findAllByUserID(List<Integer> userid);
    List<Passenger> findAll(Integer page, Integer size);
}
