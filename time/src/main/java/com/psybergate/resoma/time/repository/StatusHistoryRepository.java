package com.psybergate.resoma.time.repository;

import com.psybergate.resoma.time.entity.StatusHistory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StatusHistoryRepository extends CrudRepository<StatusHistory, UUID> {
}
