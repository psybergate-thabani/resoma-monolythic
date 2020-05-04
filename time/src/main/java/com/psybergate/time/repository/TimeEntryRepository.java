package com.psybergate.time.repository;

import com.psybergate.time.entity.TimeEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TimeEntryRepository extends JpaRepository<TimeEntry, UUID> {
    TimeEntry findByIdAndDeleted(UUID entryId, boolean deleted);

    List<TimeEntry> findAllByDeleted(boolean deleted);

    List<TimeEntry> findAllByTaskCodeAndDeleted(String taskCode, boolean deleted);

    List<TimeEntry> findAllByEmployeeCodeAndDeleted(String employeeCode, boolean deleted);
}
