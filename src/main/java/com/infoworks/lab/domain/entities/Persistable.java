package com.infoworks.lab.domain.entities;

import com.it.soul.lab.sql.entity.Entity;

import javax.persistence.*;

@MappedSuperclass
public class Persistable<ID,VERSION> extends Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private ID id;

    @Version
    private VERSION version;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public VERSION getVersion() {
        return version;
    }

    public void setVersion(VERSION version) {
        this.version = version;
    }
}
