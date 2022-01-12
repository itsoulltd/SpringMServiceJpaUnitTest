package com.infoworks.lab.services.impl;

import com.infoworks.lab.domain.entities.Passenger;
import com.infoworks.lab.domain.repositories.PassengerRepository;
import com.infoworks.lab.services.iServices.PassengerService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PassengerServiceImpl implements PassengerService {

    private PassengerRepository repository;

    public PassengerServiceImpl(PassengerRepository repository) {
        this.repository = repository;
    }

    @Override
    public void setPassengerRepository(PassengerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Passenger add(Passenger aPassenger) {
        return repository.save(aPassenger);
    }

    @Override
    public Passenger update(Passenger aPassenger) {
        return repository.save(aPassenger);
    }

    @Override
    public boolean remove(Integer userid) {
        if (repository.existsById(userid)){
            repository.deleteById(userid);
            return true;
        }
        return false;
    }

    @Override
    public Long totalCount() {
        return repository.count();
    }

    @Override
    public Passenger findByUserID(Integer userid) {
        Optional<Passenger> isFound = repository.findById(userid);
        if (isFound.isPresent()) return isFound.get();
        else return null;
    }

    @Override
    public List<Passenger> findAllByUserID(List<Integer> userid) {
        return repository.findAllById(userid);
    }

    @Override
    public List<Passenger> findAll(Integer page, Integer size) {
        return repository.findAll(PageRequest.of(page, size)).getContent();
    }
}
