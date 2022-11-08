package com.infoworks.lab.domain.repositories.impl;

import com.infoworks.lab.domain.entities.Passenger;
import com.infoworks.lab.domain.repositories.PassengerRepositoryExtension;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class PassengerRepositoryExtensionImpl implements PassengerRepositoryExtension {

    @PersistenceContext
    private EntityManager em;

    @Override @Transactional(readOnly = true)
    public List<Passenger> findAllByContainNameLike(String like, int page, int size) {
        Query query = em.createNativeQuery(
                "SELECT em.* FROM Passenger as em "
                + "WHERE em.name LIKE ?", Passenger.class);
        query.setParameter(1, "%" + like + "%");
        if (page <= 0) page = 1;
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

}
