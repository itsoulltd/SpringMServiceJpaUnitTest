package com.infoworks.lab.domain.repositories;

import com.infoworks.lab.domain.entities.Passenger;

import java.util.List;

public interface PassengerRepositoryExtension {
    List<Passenger> findAllByContainNameLike(String like, int page, int size);
}
