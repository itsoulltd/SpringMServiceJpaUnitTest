package com.infoworks.lab.domain.repositories;

import com.infoworks.lab.domain.entities.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface PassengerRepository extends JpaRepository<Passenger, Integer>, PassengerRepositoryExtension {

    @Transactional(readOnly = true)
    List<Passenger> findByName(String name);

    @Transactional(readOnly = true)
    long countByName(String name);
}
