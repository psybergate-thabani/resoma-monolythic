package com.psybergate.resoma.time.service.impl;

import com.psybergate.resoma.gateway.dto.TimeEntryDTO;
import com.psybergate.resoma.people.entity.Employee;
import com.psybergate.resoma.people.service.EmployeeService;
import com.psybergate.resoma.projects.entity.Task;
import com.psybergate.resoma.projects.service.ProjectService;
import com.psybergate.resoma.time.entity.Status;
import com.psybergate.resoma.time.entity.StatusHistory;
import com.psybergate.resoma.time.entity.TimeEntry;
import com.psybergate.resoma.time.repository.StatusHistoryRepository;
import com.psybergate.resoma.time.repository.TimeEntryRepository;
import com.psybergate.resoma.time.service.TimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.*;

@Service
@Slf4j
public class TimeServiceImpl implements TimeService {

    private final TimeEntryRepository timeEntryRepository;

    private StatusHistoryRepository statusHistoryRepository;

    private ProjectService projectService;

    private EmployeeService employeeService;

    @Autowired
    public TimeServiceImpl(TimeEntryRepository timeEntryRepository,
                           StatusHistoryRepository statusHistoryRepository,
                           ProjectService projectService,
                           EmployeeService employeeService) {
        this.timeEntryRepository = timeEntryRepository;
        this.statusHistoryRepository = statusHistoryRepository;
        this.projectService = projectService;
        this.employeeService = employeeService;
    }

    @Override
    @Transactional
    public TimeEntry captureTime(@Valid TimeEntry timeEntry) {
        timeEntry.setStatus(Status.NEW);
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
    public TimeEntry updateEntry(@Valid TimeEntry timeEntry) {
        if (timeEntry.isApproved())
            throw new ValidationException("Status can not be APPROVED");
        timeEntry.setStatus(Status.NEW);
        return timeEntryRepository.save(timeEntry);
    }

    @Override
    @Transactional
    public void deleteEntry(UUID entryId) {
        TimeEntry timeEntry = retrieveEntry(entryId);
        deleteEntry(timeEntry);
    }

    @Override
    @Transactional
    public TimeEntry submitEntry(@Valid TimeEntry timeEntry) {
        if (!EnumSet.of(Status.NEW, Status.SUBMITTED, Status.REJECTED).contains(timeEntry.getStatus()))
            throw new ValidationException("Status can only be NEW, SUBMITTED or REJECTED");
        timeEntry.setStatus(Status.SUBMITTED);
        timeEntry = timeEntryRepository.save(timeEntry);
        saveStatusHistory(timeEntry);
        return timeEntry;
    }


    @Override
    @Transactional
    public TimeEntry approveEntry(@Valid TimeEntry timeEntry) {
        if (!EnumSet.of(Status.SUBMITTED).contains(timeEntry.getStatus()))
            throw new ValidationException("Status can only be SUBMITTED");
        timeEntry.setStatus(Status.APPROVED);
        timeEntry = timeEntryRepository.save(timeEntry);
        saveStatusHistory(timeEntry);
        return timeEntry;
    }

    @Override
    @Transactional
    public TimeEntry rejectEntry(@Valid TimeEntry timeEntry) {
        if (!EnumSet.of(Status.SUBMITTED).contains(timeEntry.getStatus()))
            throw new ValidationException("Status can only be SUBMITTED");
        timeEntry.setStatus(Status.REJECTED);
        timeEntry = timeEntryRepository.save(timeEntry);
        saveStatusHistory(timeEntry);
        return timeEntry;
    }

    @Override
    @Transactional
    public List<TimeEntry> submitEntries(List<@Valid TimeEntry> timeEntries) {
        List<TimeEntry> submittedEntries = new ArrayList<>();
        timeEntries.forEach(timeEntry -> submittedEntries.add(submitEntry(timeEntry)));
        return submittedEntries;
    }

    @Override
    @Transactional
    public List<TimeEntry> approveEntries(List<@Valid TimeEntry> timeEntries) {
        List<TimeEntry> submittedEntries = new ArrayList<>();
        timeEntries.forEach(timeEntry -> submittedEntries.add(approveEntry(timeEntry)));
        return submittedEntries;
    }

    @Override
    @Transactional
    public List<TimeEntry> rejectEntries(List<@Valid TimeEntry> entries) {
        List<TimeEntry> rejectedEntries = new ArrayList<>();
        entries.forEach(timeEntry -> rejectedEntries.add(rejectEntry(timeEntry)));
        return rejectedEntries;
    }

    @Override
    public TimeEntry captureTime2(TimeEntryDTO timeEntryDTO) {
        TimeEntry timeEntry = timeEntryDTO.getTimeEntry();
        Task task = projectService.retrieveTask(timeEntryDTO.getTaskId());
        if (Objects.isNull(task))
            throw new ValidationException("Task does not exist");
        timeEntry.setTask(task);
        Employee employee = employeeService.retrieveEmployee(timeEntryDTO.getEmployeeId());
        if (Objects.isNull(employee))
            throw new ValidationException("Employee does not exist");
        timeEntry.setEmployee(employee);
        return timeEntry;
    }

    private void deleteEntry(TimeEntry timeEntry) {
        if (Status.APPROVED.equals(timeEntry.getStatus()))
            throw new ValidationException("Status can not be APPROVED");
        timeEntry.setDeleted(true);
        timeEntryRepository.save(timeEntry);
    }

    private void saveStatusHistory(TimeEntry timeEntry) {
        statusHistoryRepository.save(new StatusHistory(timeEntry));
    }

}
