package com.nals.tf7.repository;

import com.nals.tf7.domain.PersistentAuditEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * Spring Data JPA repository for the PersistentAuditEvent entity.
 */
@Repository
public interface PersistenceAuditEventRepository
    extends JpaRepository<PersistentAuditEvent, Long> {

    List<PersistentAuditEvent> findByPrincipalAndAuditEventDateAfterAndAuditEventType(String principal,
                                                                                      Instant after,
                                                                                      String type);
}
