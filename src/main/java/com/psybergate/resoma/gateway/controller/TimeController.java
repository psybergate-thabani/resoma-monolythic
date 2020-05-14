package com.psybergate.resoma.gateway.controller;

import com.psybergate.resoma.gateway.dto.TimeEntryDTO;
import com.psybergate.resoma.people.entity.Employee;
import com.psybergate.resoma.people.service.EmployeeService;
import com.psybergate.resoma.projects.entity.Task;
import com.psybergate.resoma.projects.service.ProjectService;
import com.psybergate.resoma.time.entity.TimeEntry;
import com.psybergate.resoma.time.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping(path = "/api/time")
public class TimeController {

    private TimeService timeService;

    private ProjectService projectService;

    private EmployeeService employeeService;

    @Autowired
    public TimeController(TimeService timeService, ProjectService projectService, EmployeeService employeeService) {
        this.timeService = timeService;
        this.projectService = projectService;
        this.employeeService = employeeService;
    }


    @PostMapping("v1/time-entries")
    public ResponseEntity<TimeEntry> captureTimeEntry(@RequestBody TimeEntryDTO timeEntryDTO) {
        TimeEntry timeEntry = buildTimeEntry(timeEntryDTO);
        TimeEntry retrievedTimeEntry = timeService.captureTime(timeEntry);
        return ResponseEntity.ok(retrievedTimeEntry);
    }

    @PostMapping("v2/time-entries")
    public ResponseEntity<TimeEntry> captureTimeEntry2(@RequestBody TimeEntryDTO timeEntryDTO) {
        TimeEntry retrievedTimeEntry = timeService.captureTime2(timeEntryDTO);
        return ResponseEntity.ok(retrievedTimeEntry);
    }

    @DeleteMapping("v1/time-entries/{timeEntryId}")
    public ResponseEntity<Void> deleteTimeEntry(@PathVariable UUID timeEntryId) {
        timeService.deleteEntry(timeEntryId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("v1/time-entries/{timeEntryId}")
    public ResponseEntity<TimeEntry> retrieveTimeEntry(@PathVariable UUID timeEntryId) {
        return ResponseEntity.ok(timeService.retrieveEntry(timeEntryId));
    }

    @GetMapping(value = "v1/time-entries", params = {"deleted"})
    public ResponseEntity<List<TimeEntry>> retrieveTimeEntries(@RequestParam("deleted") Boolean deleted) {
        return ResponseEntity.ok(timeService.retrieveEntries(deleted));
    }

    @PutMapping("v1/time-entries/{timeEntryId}")
    public ResponseEntity<TimeEntry> updateTimeEntry(@PathVariable UUID timeEntryId, @RequestBody TimeEntryDTO timeEntryDTO) {
        TimeEntry timeEntry = buildTimeEntry(timeEntryDTO);
        if (!timeEntryId.equals(timeEntry.getId()))
            throw new ValidationException("id in url path does not match time entry id in request body");
        TimeEntry retrievedTimeEntry = timeService.updateEntry(timeEntry);
        return ResponseEntity.ok(retrievedTimeEntry);
    }

    private TimeEntry buildTimeEntry(TimeEntryDTO timeEntryDTO) {
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

    @PutMapping(path = "v1/time-entries/{timeEntryId}/submit")
    public ResponseEntity<TimeEntry> submitTimeEntry(@PathVariable UUID timeEntryId,
                                                     @RequestBody TimeEntryDTO timeEntryDTO) {
        TimeEntry timeEntry = buildTimeEntry(timeEntryDTO);
        if (!timeEntryId.equals(timeEntry.getId()))
            throw new ValidationException("id in url path does not match time entry id in request body");
        return ResponseEntity.ok(timeService.submitEntry(timeEntry));
    }

    @PutMapping(path = "v1/time-entries/submit")
    public ResponseEntity<List<TimeEntry>> submitTimeEntries(@RequestBody List<TimeEntry> timeEntries) {
        return ResponseEntity.ok(timeService.submitEntries(timeEntries));
    }

    @PutMapping(path = "v1/time-entries/{timeEntryId}/approve")
    public ResponseEntity<TimeEntry> approveTimeEntry(@PathVariable UUID timeEntryId, @RequestBody TimeEntryDTO timeEntryDTO) {
        TimeEntry timeEntry = buildTimeEntry(timeEntryDTO);
        if (!timeEntryId.equals(timeEntry.getId()))
            throw new ValidationException("id in url path does not match time entry id in request body");
        return ResponseEntity.ok(timeService.approveEntry(timeEntry));
    }

    @PutMapping(path = "v1/time-entries/approve")
    public ResponseEntity<List<TimeEntry>> approveTimeEntries(@RequestBody @Valid List<TimeEntry> timeEntries) {
        return ResponseEntity.ok(timeService.approveEntries(timeEntries));
    }


    @PutMapping(path = "v1/time-entries/{id}/reject")
    public ResponseEntity<TimeEntry> rejectTimeEntry(@PathVariable(name = "id") UUID entryId, @RequestBody TimeEntryDTO timeEntryDTO) {
        TimeEntry timeEntry = buildTimeEntry(timeEntryDTO);
        if (!entryId.equals(timeEntry.getId()))
            throw new ValidationException("id in url path does not match time entry id in request body");
        return ResponseEntity.ok(timeService.rejectEntry(timeEntry));
    }

    @PutMapping(path = "v1/time-entries/reject")
    public ResponseEntity<List<TimeEntry>> rejectTimeEntries(@RequestBody @Valid List<TimeEntry> timeEntries) {
        return ResponseEntity.ok(timeService.rejectEntries(timeEntries));
    }

}

