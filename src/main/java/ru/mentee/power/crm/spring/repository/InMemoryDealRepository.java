package ru.mentee.power.crm.spring.repository;

import org.springframework.stereotype.Repository;
import ru.mentee.power.crm.domain.Deal;
import ru.mentee.power.crm.domain.DealStatus;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryDealRepository implements DealRepository {
    private final Map<UUID, Deal> storage = new ConcurrentHashMap<>();

    @Override
    public void save(Deal deal) {
        if (deal.getId() == null) {
            throw new IllegalArgumentException("Deal must not be null");
        }
        storage.put(deal.getId(), deal);
    }

    @Override
    public Optional<Deal> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Deal> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Deal> findByStatus(DealStatus status) {
        return storage.values().stream()
                .filter(deal -> deal.getStatus() == status)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        storage.remove(id);
    }
}
