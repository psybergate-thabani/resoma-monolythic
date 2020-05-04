package com.psybergate.time.repository;

import com.psybergate.time.entity.StatusHistory;
import com.psybergate.time.entity.TimeEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StatusHistoryRepository extends CrudRepository<StatusHistory, UUID> {
    void deleteByTimeEntry(TimeEntry timeEntry);
}
