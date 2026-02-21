package ru.mentee.power.crm.spring.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.mentee.power.crm.spring.model.Lead;
import ru.mentee.power.crm.spring.model.LeadStatusJpa;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LeadRepositoryJpa extends JpaRepository<Lead, UUID> {

    /**
     * Поиск лида по email (точное совпадение).
     * SQL: SELECT * FROM leads WHERE email = ?
     */
    Optional<Lead> findByEmail(String email);

    /**
     * Поиск лидов по статусу.
     * SQL: SELECT * FROM leads WHERE status = ?
     */
    List<Lead> findByStatus(LeadStatusJpa status);

    /**
     * Поиск лидов по названию компании.
     * SQL: SELECT * FROM leads WHERE company = ?
     */
    List<Lead> findByCompany(String company);

    /**
     * Подсчет количества лидов с определенным статусом.
     * SQL: SELECT COUNT(*) FROM leads WHERE status = ?
     */
    long countByStatus(LeadStatusJpa status);

    /**
     * Проверка существования лида с указанным email.
     * SQL: SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM leads WHERE email = ?
     */
    boolean existsByEmail(String email);

    /**
     * Поиск лидов по части email (LIKE запрос).
     * SQL: SELECT * FROM leads WHERE email LIKE '%emailPart%'
     */
    List<Lead> findByEmailContaining(String emailPart);

    /**
     * Поиск лидов по статусу И компании.
     * SQL: SELECT * FROM leads WHERE status = ? AND company = ?
     */
    List<Lead> findByStatusAndCompany(LeadStatusJpa status, String company);

    /**
     * Поиск лидов по статусу с сортировкой по дате создания (от новых к старым).
     * SQL: SELECT * FROM leads WHERE status = ? ORDER BY created_at DESC
     */
    List<Lead> findByStatusOrderByCreatedAtDesc(LeadStatusJpa status);

    @Query("SELECT l FROM Lead l WHERE l.status IN :statuses")
    List<Lead> findByStatusIn(@Param("statuses") List<LeadStatusJpa> statuses);

    @Query("SELECT l FROM Lead l WHERE l.createdAt > :date")
    List<Lead> findCreatedAfter(@Param("date") OffsetDateTime date);

    /**
     * Поиск лидов с фильтрацией и сортировкой (JPQL).
     */

    @Query("SELECT l FROM Lead l WHERE l.company = :company ORDER BY l.createdAt DESC")
    List<Lead> findByCompanyOrderedByDate(@Param("company") String company);

//    Закомментирован до появления связи @ManyToOne с Company
//    @Query("SELECT l FROM Lead l JOIN l.company c WHERE c.name = :companyName")
//    List<Lead> findByCompanyName(@Param("companyName") String name);


    /**
     * Поиск всех лидов с пагинацией (переопределяем из JpaRepository).
     * Клиент: PageRequest.of(0, 20) — первая страница, 20 элементов
     */
    Page<Lead> findAll(Pageable pageable);

    /**
     * Поиск по статусу с пагинацией (derived method).
     */
    Page<Lead> findByStatus(LeadStatusJpa status, Pageable pageable);

    Page<Lead> findByCompany(String company, Pageable pageable);

    /**
     * JPQL запрос с пагинацией.
     */
    @Query("SELECT l FROM Lead l WHERE l.status IN :statuses")
    Page<Lead> findByStatusInPaged(@Param("statuses") List<LeadStatusJpa> statuses, Pageable pageable);

    /**
     * Массовое обновление статуса лидов.
     * ВАЖНО: требует @Transactional на уровне Service!
     *
     * @return количество обновлённых строк
     */
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Lead l SET l.status = :newStatus WHERE l.status = :oldStatus")
    int updateStatusBulk(
            @Param("oldStatus") LeadStatusJpa oldStatus,
            @Param("newStatus") LeadStatusJpa newStatus
    );


     @Modifying
     @Query("DELETE FROM Lead l WHERE l.status = :status")
     int deleteByStatusBulk(@Param("status") LeadStatusJpa status);
}
