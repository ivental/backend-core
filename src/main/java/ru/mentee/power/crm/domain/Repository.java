package ru.mentee.power.crm.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Repository<Type> {
    public void add(Type type);

    public void remove(UUID uuid);

    public Optional<Type> findById(UUID id);

    public List<Type> findAll();

}
