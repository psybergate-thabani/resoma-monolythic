package com.psybergate.time.service.impl;

import com.psybergate.time.entity.Status;
import com.psybergate.time.entity.StatusHistory;
import com.psybergate.time.entity.TimeEntry;
import com.psybergate.time.repository.StatusHistoryRepository;
import com.psybergate.time.repository.TimeEntryRepository;
import com.psybergate.time.service.TimeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class TimeServiceImpl implements TimeService {

    private final TimeEntryRepository timeEntryRepository;

    private StatusHistoryRepository statusHistoryRepository;

    @Autowired
    public TimeServiceImpl(TimeEntryRepository timeEntryRepository,
                           StatusHistoryRepository statusHistoryRepository) {
        this.timeEntryRepository = timeEntryRepository;
        this.statusHistoryRepository = statusHistoryRepository;
    }

    @Override
    @Transactional
    public TimeEntry captureTime(TimeEntry timeEntry) {
        validateTimeEntry(timeEntry);
        timeEntry.setStatus(Status.PENDING_VALIDATION);
        timeEntry.setStatusUpdatedBy(timeEntry.getEmployeeCode());
        timeEntry = timeEntryRepository.save(timeEntry);
        saveStatusHistory(timeEntry);
        return timeEntry;
    }

    @Override
    public List<TimeEntry> retrieveEntries(Boolean deleted) {
        return timeEntryRepository.findAllByDeleted(deleted);
    }

    @Override
    public TimeEntry retrieveEntry(UUID entryId) {
        return timeEntryRepository.findByIdAndDeleted(entryId, false);
    }

    @Override
    @Transactional
    public TimeEntry updateEntry(TimeEntry timeEntry) {
        validateTimeEntry(timeEntry);
        if (timeEntry.isApproved()) throw new ValidationException("Status can not be APPROVED");
        timeEntry.setStatus(Status.PENDING_VALIDATION);
        timeEntry = timeEntryRepository.save(timeEntry);
        return timeEntry;
    }

    @Override
    @Transactional
    public void deleteEntry(UUID entryId) {
        TimeEntry timeEntry = retrieveEntry(entryId);
        deleteEntry(timeEntry);
    }

    @Override
    @Transactional
    public TimeEntry submitEntry(TimeEntry timeEntry) {
        if (!EnumSet.of(Status.NEW, Status.SUBMITTED, Status.REJECTED).contains(timeEntry.getStatus()))
            throw new ValidationException("Status can only be NEW, SUBMITTED or REJECTED");
        timeEntry.setStatus(Status.SUBMITTED);
        timeEntry.setStatusUpdatedBy(timeEntry.getEmployeeCode());
        timeEntry = timeEntryRepository.save(timeEntry);
        saveStatusHistory(timeEntry);
        return timeEntry;
    }


    @Override
    @Transactional
    public TimeEntry approveEntry(TimeEntry timeEntry) {
        if (!EnumSet.of(Status.SUBMITTED).contains(timeEntry.getStatus()))
            throw new ValidationException("Status can only be PENDING");

        timeEntry.setStatus(Status.APPROVED);
        timeEntry.setStatusUpdatedBy(timeEntry.getEmployeeCode());
        timeEntry = timeEntryRepository.save(timeEntry);
        saveStatusHistory(timeEntry);

        return timeEntry;
    }

    @Override
    @Transactional
    public TimeEntry rejectEntry(TimeEntry timeEntry) {
        if (!EnumSet.of(Status.SUBMITTED).contains(timeEntry.getStatus()))
            throw new ValidationException("Status can only be PENDING");

        timeEntry.setStatus(Status.REJECTED);
        timeEntry.setStatusUpdatedBy(timeEntry.getEmployeeCode());
        timeEntry = timeEntryRepository.save(timeEntry);
        saveStatusHistory(timeEntry);

        return timeEntry;
    }

    @Override
    @Transactional
    public List<TimeEntry> submitEntries(List<TimeEntry> timeEntries) {
        List<TimeEntry> submittedEntries = new ArrayList<>();
        timeEntries.forEach(timeEntry -> submittedEntries.add(submitEntry(timeEntry)));
        return submittedEntries;
    }

    @Override
    @Transactional
    public List<TimeEntry> approveEntries(List<TimeEntry> timeEntries) {
        List<TimeEntry> submittedEntries = new ArrayList<>();
        timeEntries.forEach(timeEntry -> submittedEntries.add(approveEntry(timeEntry)));
        return submittedEntries;
    }

    @Override
    @Transactional
    public List<TimeEntry> rejectEntries(List<TimeEntry> entries) {
        List<TimeEntry> rejectedEntries = new ArrayList<>();
        entries.forEach(timeEntry -> rejectedEntries.add(rejectEntry(timeEntry)));
        return rejectedEntries;
    }

    private void deleteEntry(TimeEntry timeEntry) {
        if (Status.APPROVED.equals(timeEntry.getStatus())) throw new ValidationException("Status can not be APPROVED");
        timeEntry.setDeleted(true);
    }

    private void saveStatusHistory(TimeEntry timeEntry) {
        statusHistoryRepository.save(new StatusHistory(timeEntry));
    }

    private void validateTimeEntry(TimeEntry timeEntry) {
        List<String> errorMessages = new ArrayList<>();
        if (StringUtils.isBlank(timeEntry.getEmployeeCode())) {
            errorMessages.add("Employee code cannot be blank or null");
        }
        if (StringUtils.isBlank(timeEntry.getTaskCode())) {
            errorMessages.add("Task code cannot be blank or null");
        }
        if (StringUtils.isBlank(timeEntry.getProjectCode())) {
            errorMessages.add("Project code cannot be blank or null");
        }
        if (timeEntry.getPeriod() <= 0) {
            errorMessages.add("Period cannot be null or have a value of 0 or less");
        }
        if (errorMessages.size() > 0) {
            throw new ValidationException(errorMessages.toString());
        }
    }
}
