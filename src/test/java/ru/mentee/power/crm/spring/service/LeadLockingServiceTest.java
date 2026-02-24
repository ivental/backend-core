package ru.mentee.power.crm.spring.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;
import ru.mentee.power.crm.spring.model.Lead;
import ru.mentee.power.crm.spring.model.LeadStatusJpa;
import ru.mentee.power.crm.spring.repository.LeadRepositoryJpa;

import java.util.UUID;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")

class LeadLockingServiceTest {

    @Autowired
    private LeadLockingService leadLockingService;

    @Autowired
    private LeadRepositoryJpa leadRepository;


    @Test
    void shouldPreventLostUpdate_whenPessimisticLockUsed() throws Exception {
        Lead lead = Lead.builder()
                .email("iventalll@gmail.com")
                .phone("+7911")

                .status(LeadStatusJpa.NEW)
                .build();
        lead = leadRepository.save(lead);
        UUID leadId = lead.getId();

        ExecutorService executor = Executors.newFixedThreadPool(2);

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(2);

        Future<LeadStatusJpa> task1 = executor.submit(() -> {
            startLatch.await();
            Lead updated = leadLockingService.convertLeadToDealWithLock(leadId, LeadStatusJpa.CONTACTED);
            doneLatch.countDown();
            return updated.getStatus();
        });

        Future<LeadStatusJpa> task2 = executor.submit(() -> {
            startLatch.await();
            Lead updated = leadLockingService.convertLeadToDealWithLock(leadId, LeadStatusJpa.QUALIFIED);
            doneLatch.countDown();
            return updated.getStatus();
        });

        startLatch.countDown();
        doneLatch.await(10, TimeUnit.SECONDS);

        LeadStatusJpa statusFirst = task1.get();
        LeadStatusJpa statusSecond = task2.get();

        assertThat(statusFirst).isIn(LeadStatusJpa.CONTACTED, LeadStatusJpa.QUALIFIED);
        assertThat(statusSecond).isIn(LeadStatusJpa.CONTACTED, LeadStatusJpa.QUALIFIED);
        assertThat(statusFirst).isNotEqualTo(statusSecond);

        Lead finalLead = leadRepository.findById(leadId).orElseThrow();
        assertThat(finalLead.getStatus()).isIn(LeadStatusJpa.CONTACTED, LeadStatusJpa.QUALIFIED);

        executor.shutdown();
    }


    @Test
    void shouldThrowOptimisticLockException_whenConcurrentUpdateWithoutLock() throws Exception {
        Lead lead = Lead.builder()
                .phone("+7911")
                .email("iv@gmail.com")

                .status(LeadStatusJpa.NEW)
                .build();
        lead = leadRepository.save(lead);
        UUID leadId = lead.getId();

        ExecutorService executor = Executors.newFixedThreadPool(2);

        CountDownLatch startLatch = new CountDownLatch(1);

        Future<?> taskFirst = executor.submit(() -> {
            startLatch.await();
            leadLockingService.updateLeadStatusOptimistic(leadId, LeadStatusJpa.CONTACTED);
            return null;
        });

        Future<?> taskSecond = executor.submit(() -> {
            startLatch.await();
            leadLockingService.updateLeadStatusOptimistic(leadId, LeadStatusJpa.QUALIFIED);
            return null;
        });

        startLatch.countDown();

        boolean exceptionThrown = false;
        try {
            taskFirst.get(5, TimeUnit.SECONDS);
            taskSecond.get(5, TimeUnit.SECONDS);
        } catch (ExecutionException e) {
            assertThat(e.getCause())
                    .isInstanceOfAny(ObjectOptimisticLockingFailureException.class);
            exceptionThrown = true;
        }

        assertThat(exceptionThrown).isTrue();
        executor.shutdown();
    }


    @Test
    void shouldDetectDeadlock_whenLeadsLockedInDifferentOrder() throws Exception {
        Lead leadFirst = Lead.builder()
                .email("ivental@gmail.com")
                .phone("+7911")
                .status(LeadStatusJpa.NEW)
                .build();
        Lead leadSecond = Lead.builder()
                .email("ive@gmail.com")
                .phone("+7912")
                .status(LeadStatusJpa.NEW)
                .build();

        leadFirst = leadRepository.save(leadFirst);
        leadSecond = leadRepository.save(leadSecond);

        UUID id1 = leadFirst.getId();
        UUID id2 = leadSecond.getId();

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch startLatch = new CountDownLatch(1);

        Future<?> threadA = executor.submit(() -> {
            startLatch.await();
            leadLockingService.processTwoLeadsInOrder(id1, id2);
            return null;
        });

        Future<?> threadB = executor.submit(() -> {
            startLatch.await();
            leadLockingService.processTwoLeadsInOrder(id2, id1);
            return null;
        });

        startLatch.countDown();

        boolean deadlockDetected = false;
        try {
            threadA.get(5, TimeUnit.SECONDS);
            threadB.get(5, TimeUnit.SECONDS);
        } catch (ExecutionException _) {
            deadlockDetected = true;
        }

        assertThat(deadlockDetected).isTrue();

        executor.shutdownNow();
    }
}
