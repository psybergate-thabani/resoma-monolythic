package com.psybergate.resoma.time.service;

import com.psybergate.resoma.people.entity.Employee;
import com.psybergate.resoma.people.service.EmployeeService;
import com.psybergate.resoma.project.entity.Project;
import com.psybergate.resoma.project.entity.ProjectType;
import com.psybergate.resoma.project.entity.Task;
import com.psybergate.resoma.project.service.ProjectService;
import com.psybergate.resoma.time.dto.TimeEntryDTO;
import com.psybergate.resoma.time.entity.Status;
import com.psybergate.resoma.time.entity.StatusHistory;
import com.psybergate.resoma.time.entity.TimeEntry;
import com.psybergate.resoma.time.repository.StatusHistoryRepository;
import com.psybergate.resoma.time.repository.TimeEntryRepository;
import com.psybergate.resoma.time.service.impl.TimeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimeServiceTest {

    @Mock
    private TimeEntryRepository mockTimeEntryRepository;
    @Mock
    private StatusHistoryRepository mockStatusHistoryRepository;
    @Mock
    private ProjectService mockProjectService;
    @Mock
    private EmployeeService mockEmployeeService;

    private TimeService timeService;
    private TimeEntry testTimeEntry;
    private TimeEntry testTimeEntry2;
    private TimeEntry testTimeEntry3;

    @BeforeEach
    void init() {
        Employee employee = new Employee("emp1", "John", "Doe", "JohnD@resoma.com", "78 Home Address, Johannesburg",
                "79 Postal Address, Johannesburg", LocalDateTime.now(), LocalDate.now(), "Developer", "Active");
        Project project = new Project("proj1", "First Project", "client1", LocalDate.now(), null, ProjectType.BILLABLE);

        Task task = new Task("task1", "Analysis", project, false);
        timeService = new TimeServiceImpl(mockTimeEntryRepository, mockStatusHistoryRepository, mockProjectService, mockEmployeeService);
        testTimeEntry = new TimeEntry(employee, task, "descr1", 100, LocalDate.now(), false);
        testTimeEntry2 = new TimeEntry(employee, task, "descr2", 100, LocalDate.now(), false);
        testTimeEntry3 = new TimeEntry(employee, task, "descr3", 200, LocalDate.now(), false);
    }

    @Test
    void shouldCaptureTimeEntry_whenCaptureTime() {
        //Arrange
        when(mockTimeEntryRepository.save(testTimeEntry)).thenReturn(testTimeEntry);

        //Act
        TimeEntry timeEntry = timeService.captureTime(testTimeEntry);

        //Assert
        assertNotNull(timeEntry);
        assertEquals("emp1", timeEntry.getEmployee().getEmployeeCode());
        assertEquals(Status.NEW, timeEntry.getStatus());
    }

    @Test
    void shouldRetrieveAllTimeEntriesThatAreNotDeleted_whenRetrievingEntries() {
        //Arrange
        UUID uuid = testTimeEntry.getId();
        when(mockTimeEntryRepository.findByIdAndDeleted(uuid, false)).thenReturn(testTimeEntry);

        //Act
        TimeEntry timeEntry = timeService.retrieveEntry(uuid);

        //Assert
        assertNotNull(timeEntry);
        assertEquals("emp1", timeEntry.getEmployee().getEmployeeCode());
    }

    @Test
    void shouldUpdateTimeEntry_whenUpdatingEntry() {
        //Arrange
        when(mockTimeEntryRepository.save(any(TimeEntry.class))).thenReturn(testTimeEntry);

        //Act
        TimeEntry updateEntry = timeService.updateEntry(testTimeEntry);

        //Assert
        verify(mockTimeEntryRepository, times(1)).save(testTimeEntry);
        assertEquals(testTimeEntry, updateEntry);
    }

    @Test
    void shouldThrowValidationException_whenUpdatingApprovedEntry() {
        //Arrange
        testTimeEntry.setStatus(Status.APPROVED);

        //Act
        //Assert
        assertThrows(ValidationException.class, () -> timeService.updateEntry(testTimeEntry));
    }

    @Test
    void shouldDeleteTimeEntry_whenDeletingEntry() {
        //Arrange
        UUID id = testTimeEntry.getId();
        when(mockTimeEntryRepository.findByIdAndDeleted(id, false)).thenReturn(testTimeEntry);

        //Act
        timeService.deleteEntry(id);

        //Assert
        verify(mockTimeEntryRepository).save(testTimeEntry);
    }

    @Test
    void shouldThrowValidationException_whenDeletingApprovedEntry() {
        //Arrange
        UUID id = testTimeEntry.getId();
        when(mockTimeEntryRepository.findByIdAndDeleted(id, false)).thenReturn(testTimeEntry);

        //Act
        testTimeEntry.setStatus(Status.APPROVED);

        //Assert
        assertThrows(ValidationException.class, () -> timeService.deleteEntry(testTimeEntry.getId()));
    }

    @Test
    void shouldSubmitTimeEntry_whenSubmittingEntry() {
        //Arrange
        when(mockTimeEntryRepository.save(testTimeEntry)).thenReturn(testTimeEntry);

        //Act
        testTimeEntry = timeService.submitEntry(testTimeEntry);

        //Assert
        verify(mockTimeEntryRepository).save(testTimeEntry);
        verify(mockStatusHistoryRepository).save(any(StatusHistory.class));
        assertEquals(Status.SUBMITTED, testTimeEntry.getStatus());
    }

    @Test
    void shouldThrowValidationException_whenSubmittingApprovedEntry() {
        //Arrange
        testTimeEntry.setStatus(Status.APPROVED);

        //Act and Assert
        assertThrows(ValidationException.class, () -> timeService.submitEntry(testTimeEntry));
    }

    @Test
    void shouldApproveTimeEntry_whenApprovingEntry() {
        //Arrange
        testTimeEntry.setStatus(Status.SUBMITTED);
        when(mockTimeEntryRepository.save(testTimeEntry)).thenReturn(testTimeEntry);

        //Act
        testTimeEntry = timeService.approveEntry(testTimeEntry);

        //Assert
        verify(mockTimeEntryRepository).save(testTimeEntry);
        verify(mockStatusHistoryRepository).save(any(StatusHistory.class));
        assertEquals(testTimeEntry.getStatus(), Status.APPROVED);
    }

    @Test
    void shouldThrowValidationException_whenApprovingNewEntry() {
        //Arrange
        testTimeEntry.setStatus(Status.NEW);

        //Act and Assert
        assertThrows(ValidationException.class, () -> timeService.approveEntry(testTimeEntry));
    }

    @Test
    void shouldThrowValidationException_whenApprovingApprovedEntry() {
        //Arrange
        testTimeEntry.setStatus(Status.APPROVED);

        //Act and Assert
        assertThrows(ValidationException.class, () -> timeService.approveEntry(testTimeEntry));
    }

    @Test
    void shouldThrowValidationException_whenApprovingRejectedEntry() {
        //Arrange
        testTimeEntry.setStatus(Status.REJECTED);

        //Act and Assert
        assertThrows(ValidationException.class, () -> timeService.approveEntry(testTimeEntry));
    }

    @Test
    void shouldRejectTimeEntry_whenRejectingEntry() {
        //Arrange
        testTimeEntry.setStatus(Status.SUBMITTED);
        testTimeEntry.setStatusReason("Test Reason");
        when(mockTimeEntryRepository.save(testTimeEntry)).thenReturn(testTimeEntry);

        //Act
        testTimeEntry = timeService.rejectEntry(testTimeEntry);

        //Assert
        verify(mockTimeEntryRepository).save(testTimeEntry);
        verify(mockStatusHistoryRepository).save(any(StatusHistory.class));
        assertEquals(testTimeEntry.getStatus(), Status.REJECTED);
    }

    @Test
    void shouldThrowValidationException_whenRejectingNewEntry() {
        //Arrange
        testTimeEntry.setStatus(Status.NEW);

        //Act and Assert
        assertThrows(ValidationException.class, () -> timeService.rejectEntry(testTimeEntry));
    }

    @Test
    void shouldThrowValidationException_whenRejectingApprovedEntry() {
        //Arrange
        testTimeEntry.setStatus(Status.APPROVED);

        //Act and Assert
        assertThrows(ValidationException.class, () -> timeService.rejectEntry(testTimeEntry));
    }

    @Test
    void shouldThrowValidationException_whenRejectingRejectedEntry() {
        //Arrange
        testTimeEntry.setStatus(Status.REJECTED);

        //Act and Assert
        assertThrows(ValidationException.class, () -> timeService.rejectEntry(testTimeEntry));
    }

    @Test
    void shouldRetrieveAllTimeEntriesThatAreNotDeleted_wheRetrievingEntries() {
        // Arrange
        when(mockTimeEntryRepository.findAllByDeleted(false)).thenReturn(Arrays.asList(testTimeEntry, testTimeEntry2, testTimeEntry3));

        // Act
        List<TimeEntry> timeEntries = timeService.retrieveEntries(false);

        // Assert
        assertNotNull(timeEntries);
        assertEquals(3, timeEntries.size());
        verify(mockTimeEntryRepository, times(1)).findAllByDeleted(false);
    }

    @Test
    void shouldSubmitAllTimeEntries_wheSubmittingEntries() {
        //Arrange
        List<TimeEntry> timeEntries = Arrays.asList(testTimeEntry, testTimeEntry2, testTimeEntry3);
        when(mockTimeEntryRepository.save(testTimeEntry)).thenReturn(testTimeEntry);
        when(mockTimeEntryRepository.save(testTimeEntry2)).thenReturn(testTimeEntry2);
        when(mockTimeEntryRepository.save(testTimeEntry3)).thenReturn(testTimeEntry3);
        when(mockStatusHistoryRepository.save(any())).thenReturn(any());

        //Act
        List<TimeEntry> submitEntries = timeService.submitEntries(timeEntries);

        //Assert
        verify(mockTimeEntryRepository, times(3)).save(any());
        verify(mockStatusHistoryRepository, times(3)).save(any(StatusHistory.class));
        submitEntries.forEach((t) -> assertEquals(Status.SUBMITTED, t.getStatus()));
    }

    @Test
    void shouldApproveAllTimeEntries_whenApprovingEntries() {
        //Arrange
        List<TimeEntry> timeEntries = Arrays.asList(testTimeEntry, testTimeEntry2, testTimeEntry3);
        timeEntries.forEach(timeEntry -> timeEntry.setStatus(Status.SUBMITTED));
        when(mockTimeEntryRepository.save(testTimeEntry)).thenReturn(testTimeEntry);
        when(mockTimeEntryRepository.save(testTimeEntry2)).thenReturn(testTimeEntry2);
        when(mockTimeEntryRepository.save(testTimeEntry3)).thenReturn(testTimeEntry3);

        //Act
        List<TimeEntry> approveEntries = timeService.approveEntries(timeEntries);

        //Verify
        verify(mockTimeEntryRepository, times(3)).save(any());
        verify(mockStatusHistoryRepository, times(3)).save(any(StatusHistory.class));
        approveEntries.forEach((t) -> assertEquals(Status.APPROVED, t.getStatus()));
    }

    @Test
    void shouldRejectAllTimeEntries_whenRejectingEntries() {
        //Arrange
        List<TimeEntry> timeEntries = Arrays.asList(testTimeEntry, testTimeEntry2, testTimeEntry3);
        timeEntries.forEach(timeEntry -> timeEntry.setStatus(Status.SUBMITTED));
        when(mockTimeEntryRepository.save(testTimeEntry)).thenReturn(testTimeEntry);
        when(mockTimeEntryRepository.save(testTimeEntry2)).thenReturn(testTimeEntry2);
        when(mockTimeEntryRepository.save(testTimeEntry3)).thenReturn(testTimeEntry3);

        //Act
        List<TimeEntry> submitEntries = timeService.rejectEntries(timeEntries);

        //Assert
        verify(mockTimeEntryRepository, times(3)).save(any());
        verify(mockStatusHistoryRepository, times(3)).save(any(StatusHistory.class));
        submitEntries.forEach((t) -> assertEquals(Status.REJECTED, t.getStatus()));
    }

    @Test
    void shouldCaptureTimeEntry_whenCaptureTimeVersion2() {
        //Arrange
        Task task = new Task();
        task.setId(UUID.randomUUID());
        Employee employee = new Employee();
        employee.setId(UUID.randomUUID());
        TimeEntryDTO timeEntryDTO = new TimeEntryDTO(testTimeEntry, UUID.randomUUID(), UUID.randomUUID());
        when(mockProjectService.retrieveTask(timeEntryDTO.getTaskId())).thenReturn(task);
        when(mockEmployeeService.retrieveEmployee(timeEntryDTO.getEmployeeId())).thenReturn(employee);
        when(mockTimeEntryRepository.save(testTimeEntry)).thenReturn(testTimeEntry);

        //Act
        TimeEntry timeEntry = timeService.captureTime2(timeEntryDTO);

        //Assert
        assertNotNull(timeEntry);
        assertEquals(Status.NEW, timeEntry.getStatus());
    }
}
